/*
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.monitoring;

import static org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.utils.DlmsDateTimeConverter.toDateTime;
import static org.opensmartgridplatform.dlms.objectconfig.DlmsObjectType.DEFINABLE_LOAD_PROFILE;
import static org.opensmartgridplatform.dlms.objectconfig.DlmsObjectType.POWER_QUALITY_PROFILE_1;
import static org.opensmartgridplatform.dlms.objectconfig.DlmsObjectType.POWER_QUALITY_PROFILE_2;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import org.joda.time.DateTime;
import org.openmuc.jdlms.AttributeAddress;
import org.openmuc.jdlms.GetResult;
import org.openmuc.jdlms.ObisCode;
import org.openmuc.jdlms.SelectiveAccessDescription;
import org.openmuc.jdlms.datatypes.DataObject;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.utils.DlmsHelper;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.factories.DlmsConnectionManager;
import org.opensmartgridplatform.adapter.protocol.dlms.exceptions.ProtocolAdapterException;
import org.opensmartgridplatform.dlms.exceptions.ObjectConfigException;
import org.opensmartgridplatform.dlms.interfaceclass.InterfaceClass;
import org.opensmartgridplatform.dlms.interfaceclass.attribute.ExtendedRegisterAttribute;
import org.opensmartgridplatform.dlms.interfaceclass.attribute.ProfileGenericAttribute;
import org.opensmartgridplatform.dlms.interfaceclass.attribute.RegisterAttribute;
import org.opensmartgridplatform.dlms.objectconfig.CosemObject;
import org.opensmartgridplatform.dlms.objectconfig.DlmsObjectType;
import org.opensmartgridplatform.dlms.objectconfig.ObjectProperty;
import org.opensmartgridplatform.dlms.objectconfig.PowerQualityRequest;
import org.opensmartgridplatform.dlms.services.ObjectConfigService;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.CaptureObjectDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.CosemDateTimeDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.CosemObjectDefinitionDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.DlmsMeterValueDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.DlmsUnitTypeDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.GetPowerQualityProfileRequestDataDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.GetPowerQualityProfileResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ObisCodeValuesDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PowerQualityProfileDataDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ProfileEntryDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ProfileEntryValueDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGetPowerQualityProfileHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(AbstractGetPowerQualityProfileHandler.class);

  private static final String CAPTURE_OBJECT = "capture-object";

  private static final int ACCESS_SELECTOR_RANGE_DESCRIPTOR = 1;

  private static final int INTERVAL_DEFINABLE_LOAD_PROFILE = 15;
  private static final int INTERVAL_PROFILE_1 = 15;
  private static final int INTERVAL_PROFILE_2 = 10;
  private static final String PUBLIC = "PUBLIC";
  private static final String PRIVATE = "PRIVATE";

  protected final DlmsHelper dlmsHelper;
  private final ObjectConfigService objectConfigService;

  protected AbstractGetPowerQualityProfileHandler(
      final DlmsHelper dlmsHelper, final ObjectConfigService objectConfigService) {
    this.dlmsHelper = dlmsHelper;
    this.objectConfigService = objectConfigService;
  }

  protected abstract DataObject convertSelectableCaptureObjects(
      final List<SelectableObject> selectableCaptureObjects);

  protected abstract List<ProfileEntryValueDto> createProfileEntryValueDto(
      final DataObject profileEntryDataObject,
      ProfileEntryDto previousProfileEntryDto,
      final Map<Integer, SelectableObject> selectableCaptureObjects,
      int timeInterval);

  protected GetPowerQualityProfileResponseDto handle(
      final DlmsConnectionManager conn,
      final DlmsDevice device,
      final GetPowerQualityProfileRequestDataDto request)
      throws ProtocolAdapterException {

    final String privateOrPublic = request.getProfileType();
    final List<CosemObject> profilesToRead = this.getProfilesToRead(privateOrPublic, device);

    final GetPowerQualityProfileResponseDto response = new GetPowerQualityProfileResponseDto();
    final List<PowerQualityProfileDataDto> responseDatas = new ArrayList<>();

    // TODO: Get objects from property SELECTABLE_OBJECTS in profileToRead
    final List<CosemObject> cosemConfigObjects = this.getCosemObjects(device, privateOrPublic);

    for (final CosemObject profile : profilesToRead) {

      final ObisCode obisCode = new ObisCode(profile.getObis());
      final DateTime beginDateTime = toDateTime(request.getBeginDate(), device.getTimezone());
      final DateTime endDateTime = toDateTime(request.getEndDate(), device.getTimezone());

      // All value types that can be selected based on the info in the meter
      final List<GetResult> captureObjects = this.retrieveCaptureObjects(conn, device, obisCode);

      // The values that are allowed to be retrieved from the meter, used as filter either
      // before or after data retrieval, depending on selective access supported or not
      final Map<Integer, SelectableObject> selectableObjects =
          this.createSelectableObjects(captureObjects, cosemConfigObjects);

      // Get the values from the buffer in the meter
      final List<GetResult> bufferList =
          this.retrieveBuffer(
              conn,
              device,
              obisCode,
              beginDateTime,
              endDateTime,
              new ArrayList<>(selectableObjects.values()));

      // Convert the retrieved values (e.g. add timestamps and add unit)
      final PowerQualityProfileDataDto responseDataDto =
          this.processData(profile, captureObjects, selectableObjects, bufferList);

      responseDatas.add(responseDataDto);
    }

    response.setPowerQualityProfileDatas(responseDatas);

    return response;
  }

  private List<CosemObject> getCosemObjects(final DlmsDevice device, final String profileType)
      throws ProtocolAdapterException {
    final List<CosemObject> cosemConfigObjects = new ArrayList<>();

    try {
      final CosemObject clockObject =
          this.objectConfigService.getCosemObject(
              device.getProtocolName(), device.getProtocolVersion(), DlmsObjectType.CLOCK);
      cosemConfigObjects.add(clockObject);

      final EnumMap<ObjectProperty, List<Object>> pqProperties =
          new EnumMap<>(ObjectProperty.class);
      pqProperties.put(ObjectProperty.PQ_PROFILE, Collections.singletonList(profileType));
      pqProperties.put(
          ObjectProperty.PQ_REQUEST,
          Arrays.asList(PowerQualityRequest.ONDEMAND.name(), PowerQualityRequest.BOTH.name()));

      final List<CosemObject> cosemObjectsWithProperties =
          this.objectConfigService.getCosemObjectsWithProperties(
              device.getProtocolName(), device.getProtocolVersion(), pqProperties);

      cosemConfigObjects.addAll(cosemObjectsWithProperties);

      return cosemConfigObjects;
    } catch (final ObjectConfigException e) {
      throw new ProtocolAdapterException("Error in object config", e);
    }
  }

  private List<CosemObject> getProfilesToRead(final String privateOrPublic, final DlmsDevice device)
      throws ProtocolAdapterException {
    final String protocol = device.getProtocolName();
    final String version = device.getProtocolVersion();

    try {
      switch (privateOrPublic) {
        case PUBLIC:
          return Arrays.asList(
              this.objectConfigService.getCosemObject(protocol, version, DEFINABLE_LOAD_PROFILE),
              this.objectConfigService.getCosemObject(protocol, version, POWER_QUALITY_PROFILE_2));
        case PRIVATE:
          return Arrays.asList(
              this.objectConfigService.getCosemObject(protocol, version, POWER_QUALITY_PROFILE_1),
              this.objectConfigService.getCosemObject(protocol, version, POWER_QUALITY_PROFILE_2));
        default:
          throw new IllegalArgumentException(
              "GetPowerQualityProfile: an unknown profileType was requested: " + privateOrPublic);

          // TODO: DSMR4 only has DEFINABLE_LOAD_PROFILE, with public and private.
      }
    } catch (final ObjectConfigException e) {
      throw new ProtocolAdapterException(
          privateOrPublic + " profiles not found for " + device.getDeviceIdentification(), e);
    }
  }

  private List<GetResult> retrieveCaptureObjects(
      final DlmsConnectionManager conn, final DlmsDevice device, final ObisCode obisCode)
      throws ProtocolAdapterException {
    final AttributeAddress captureObjectsAttributeAddress =
        new AttributeAddress(
            InterfaceClass.PROFILE_GENERIC.id(),
            obisCode,
            ProfileGenericAttribute.CAPTURE_OBJECTS.attributeId());

    return this.dlmsHelper.getAndCheck(
        conn, device, "retrieve profile generic capture objects", captureObjectsAttributeAddress);
  }

  private List<GetResult> retrieveBuffer(
      final DlmsConnectionManager conn,
      final DlmsDevice device,
      final ObisCode obisCode,
      final DateTime beginDateTime,
      final DateTime endDateTime,
      final List<SelectableObject> selectableObjects)
      throws ProtocolAdapterException {

    final DataObject selectableValues = this.convertSelectableCaptureObjects(selectableObjects);

    final SelectiveAccessDescription selectiveAccessDescription =
        this.getSelectiveAccessDescription(beginDateTime, endDateTime, selectableValues);
    final AttributeAddress bufferAttributeAddress =
        new AttributeAddress(
            InterfaceClass.PROFILE_GENERIC.id(),
            obisCode,
            ProfileGenericAttribute.BUFFER.attributeId(),
            selectiveAccessDescription);

    return this.dlmsHelper.getAndCheck(
        conn, device, "retrieve profile generic buffer", bufferAttributeAddress);
  }

  private PowerQualityProfileDataDto processData(
      final CosemObject profile,
      final List<GetResult> captureObjects,
      final Map<Integer, SelectableObject> selectableCaptureObjects,
      final List<GetResult> bufferList)
      throws ProtocolAdapterException {

    final List<CaptureObjectDto> captureObjectDtos =
        this.createSelectableCaptureObjects(
            captureObjects, new ArrayList<>(selectableCaptureObjects.values()));

    final List<ProfileEntryDto> profileEntryDtos =
        this.createProfileEntries(
            bufferList, selectableCaptureObjects, this.getIntervalInMinutes(profile));
    return new PowerQualityProfileDataDto(
        new ObisCodeValuesDto(profile.getObis()), captureObjectDtos, profileEntryDtos);
  }

  private List<ProfileEntryDto> createProfileEntries(
      final List<GetResult> bufferList,
      final Map<Integer, SelectableObject> selectableCaptureObjects,
      final int timeInterval) {

    final List<ProfileEntryDto> profileEntryDtos = new ArrayList<>();

    // there is always only one GetResult, which is an array of array data
    for (final GetResult buffer : bufferList) {
      final DataObject dataObject = buffer.getResultData();

      final List<DataObject> dataObjectValue = dataObject.getValue();
      ProfileEntryDto previousProfileEntryDto = null;

      for (final DataObject profileEntryDataObject : dataObjectValue) {

        final ProfileEntryDto profileEntryDto =
            new ProfileEntryDto(
                this.createProfileEntryValueDto(
                    profileEntryDataObject,
                    previousProfileEntryDto,
                    selectableCaptureObjects,
                    timeInterval));

        profileEntryDtos.add(profileEntryDto);

        previousProfileEntryDto = profileEntryDto;
      }
    }
    return profileEntryDtos;
  }

  // the available CaptureObjects are filtered with the ones that can be
  // selected
  private List<CaptureObjectDto> createSelectableCaptureObjects(
      final List<GetResult> captureObjects, final List<SelectableObject> selectableCaptureObjects)
      throws ProtocolAdapterException {

    final List<CaptureObjectDto> captureObjectDtos = new ArrayList<>();
    for (final GetResult captureObjectResult : captureObjects) {
      final DataObject dataObject = captureObjectResult.getResultData();
      final List<DataObject> captureObjectList = dataObject.getValue();
      for (final DataObject object : captureObjectList) {
        final Optional<SelectableObject> selectableObject =
            this.matchSelectableObject(object, selectableCaptureObjects);

        if (selectableObject.isPresent()) {
          captureObjectDtos.add(
              this.makeCaptureObjectDto(object, selectableObject.get().getScalerUnit()));
        }
      }
    }
    return captureObjectDtos;
  }

  private Optional<SelectableObject> matchSelectableObject(
      final DataObject dataObject, final List<SelectableObject> selectableCaptureObjects)
      throws ProtocolAdapterException {
    final CosemObjectDefinitionDto cosemObjectDefinitionDto =
        this.dlmsHelper.readObjectDefinition(dataObject, CAPTURE_OBJECT);

    return selectableCaptureObjects.stream()
        .filter(
            selectableObject ->
                this.isDefinitionOfSameObject(cosemObjectDefinitionDto, selectableObject))
        .findFirst();
  }

  private boolean isDefinitionOfSameObject(
      final CosemObjectDefinitionDto cosemObjectDefinitionDto,
      final SelectableObject captureObjectDefinition) {

    final int classIdCosemObjectDefinition = cosemObjectDefinitionDto.getClassId();
    final byte[] obisBytesCosemObjectDefinition =
        cosemObjectDefinitionDto.getLogicalName().toByteArray();
    final byte attributeIndexCosemObjectDefinition =
        (byte) cosemObjectDefinitionDto.getAttributeIndex();
    final int dataIndexCosemObjectDefinition = cosemObjectDefinitionDto.getDataIndex();
    final int classIdCaptureObjectDefinition = captureObjectDefinition.getClassId();
    final byte[] obisBytesCaptureObjectDefinition = captureObjectDefinition.getObisAsBytes();
    final byte attributeIndexCaptureObjectDefinition = captureObjectDefinition.getAttributeIndex();
    final int dataIndexCaptureObjectDefinition =
        captureObjectDefinition.getDataIndex() == null ? 0 : captureObjectDefinition.getDataIndex();

    return classIdCaptureObjectDefinition == classIdCosemObjectDefinition
        && Arrays.equals(obisBytesCaptureObjectDefinition, obisBytesCosemObjectDefinition)
        && attributeIndexCaptureObjectDefinition == attributeIndexCosemObjectDefinition
        && dataIndexCaptureObjectDefinition == dataIndexCosemObjectDefinition;
  }

  private SelectiveAccessDescription getSelectiveAccessDescription(
      final DateTime beginDateTime,
      final DateTime endDateTime,
      final DataObject selectableCaptureObjects) {

    /*
     * Define the clock object {8,0-0:1.0.0.255,2,0} to be used as
     * restricting object in a range descriptor with a from value and to
     * value to determine which elements from the buffered array should be
     * retrieved.
     */
    final DataObject clockDefinition = this.dlmsHelper.getClockDefinition();
    final DataObject fromValue = this.dlmsHelper.asDataObject(beginDateTime);
    final DataObject toValue = this.dlmsHelper.asDataObject(endDateTime);

    final DataObject accessParameter =
        DataObject.newStructureData(
            Arrays.asList(clockDefinition, fromValue, toValue, selectableCaptureObjects));

    return new SelectiveAccessDescription(ACCESS_SELECTOR_RANGE_DESCRIPTOR, accessParameter);
  }

  private CaptureObjectDto makeCaptureObjectDto(
      final DataObject captureObjectDataObject, final String scalerUnit)
      throws ProtocolAdapterException {

    final CosemObjectDefinitionDto cosemObjectDefinitionDto =
        this.dlmsHelper.readObjectDefinition(captureObjectDataObject, CAPTURE_OBJECT);

    return new CaptureObjectDto(
        cosemObjectDefinitionDto.getClassId(),
        cosemObjectDefinitionDto.getLogicalName().toString(),
        cosemObjectDefinitionDto.getAttributeIndex(),
        cosemObjectDefinitionDto.getDataIndex(),
        this.getUnit(scalerUnit));
  }

  private String getUnit(final String scalerUnit) {
    final DlmsUnitTypeDto unitType = this.getUnitType(scalerUnit);
    return unitType.getUnit();
  }

  private DlmsUnitTypeDto getUnitType(final String scalerUnit) {
    if (scalerUnit != null) {
      final String[] scalerUnitParts = scalerUnit.split(",");
      final DlmsUnitTypeDto unitType = DlmsUnitTypeDto.getUnitType(scalerUnitParts[1].trim());
      if (unitType != null) {
        return unitType;
      }
    }
    return DlmsUnitTypeDto.UNDEFINED;
  }

  protected ProfileEntryValueDto makeProfileEntryValueDto(
      final DataObject dataObject,
      final SelectableObject selectableObject,
      final ProfileEntryDto previousProfileEntryDto,
      final int timeInterval) {
    if (InterfaceClass.CLOCK.id() == selectableObject.getClassId()) {
      return this.makeDateProfileEntryValueDto(dataObject, previousProfileEntryDto, timeInterval);
    } else if (dataObject.isNumber()) {
      return this.createNumericProfileEntryValueDto(dataObject, selectableObject);
    } else if (dataObject.isNull()) {
      return new ProfileEntryValueDto(null);
    } else {
      final String dbgInfo = this.dlmsHelper.getDebugInfo(dataObject);
      LOGGER.debug("creating ProfileEntryDto from {} {} ", dbgInfo, selectableObject);
      return new ProfileEntryValueDto(dbgInfo);
    }
  }

  private ProfileEntryValueDto makeDateProfileEntryValueDto(
      final DataObject dataObject,
      final ProfileEntryDto previousProfileEntryDto,
      final int timeInterval) {

    final CosemDateTimeDto cosemDateTime = this.dlmsHelper.convertDataObjectToDateTime(dataObject);

    if (cosemDateTime == null) {
      // in case of null date, we calculate the date based on the always
      // existing previous value plus interval
      final Date previousDate =
          (Date) previousProfileEntryDto.getProfileEntryValues().get(0).getValue();
      final LocalDateTime newLocalDateTime =
          Instant.ofEpochMilli(previousDate.getTime())
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime()
              .plusMinutes(timeInterval);

      return new ProfileEntryValueDto(
          Date.from(newLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    } else {
      return new ProfileEntryValueDto(cosemDateTime.asDateTime().toDate());
    }
  }

  private ProfileEntryValueDto createNumericProfileEntryValueDto(
      final DataObject dataObject, final SelectableObject selectableObject) {
    try {
      if (selectableObject.getScalerUnit() != null) {
        final DlmsMeterValueDto meterValue =
            this.dlmsHelper.getScaledMeterValueWithScalerUnit(
                dataObject, selectableObject.getScalerUnit(), "getScaledMeterValue");
        if (DlmsUnitTypeDto.COUNT.equals(this.getUnitType(selectableObject.getScalerUnit()))) {

          return new ProfileEntryValueDto(meterValue.getValue().longValue());
        } else {
          return new ProfileEntryValueDto(meterValue.getValue());
        }
      } else {
        final long value = this.dlmsHelper.readLong(dataObject, "read long");
        return new ProfileEntryValueDto(value);
      }
    } catch (final ProtocolAdapterException e) {
      LOGGER.error("Error creating ProfileEntryDto from {}", dataObject, e);
      final String debugInfo = this.dlmsHelper.getDebugInfo(dataObject);
      return new ProfileEntryValueDto(debugInfo);
    }
  }

  private Map<Integer, SelectableObject> createSelectableObjects(
      final List<GetResult> captureObjects, final List<CosemObject> cosemConfigObjects)
      throws ProtocolAdapterException {

    final Map<Integer, SelectableObject> selectableObjects = new HashMap<>();

    // there is always only one GetResult
    for (final GetResult captureObjectResult : captureObjects) {

      final List<DataObject> dataObjects = captureObjectResult.getResultData().getValue();

      for (int positionInDataObjectsList = 0;
          positionInDataObjectsList < dataObjects.size();
          positionInDataObjectsList++) {

        final DataObject dataObject = dataObjects.get(positionInDataObjectsList);

        final CosemObjectDefinitionDto objectDefinition =
            this.dlmsHelper.readObjectDefinition(dataObject, CAPTURE_OBJECT);

        final Optional<CosemObject> matchedCosemObject =
            cosemConfigObjects.stream()
                .filter(obj -> objectDefinition.getLogicalName().toString().equals(obj.getObis()))
                .findFirst();

        if (matchedCosemObject.isPresent()) {
          final CosemObject cosemObject = matchedCosemObject.get();

          selectableObjects.put(
              positionInDataObjectsList,
              new SelectableObject(
                  objectDefinition.getClassId(),
                  cosemObject.getObis(),
                  (byte) objectDefinition.getAttributeIndex(),
                  objectDefinition.getDataIndex(),
                  this.getScalerUnit(cosemObject)));
        }
      }
    }

    return selectableObjects;
  }

  private String getScalerUnit(final CosemObject object) throws ProtocolAdapterException {
    if (object.getClassId() == InterfaceClass.REGISTER.id()) {
      return object.getAttribute(RegisterAttribute.SCALER_UNIT.attributeId()).getValue();
    } else if (object.getClassId() == InterfaceClass.EXTENDED_REGISTER.id()) {
      return object.getAttribute(ExtendedRegisterAttribute.SCALER_UNIT.attributeId()).getValue();
    } else if (object.getClassId() == InterfaceClass.CLOCK.id()) {
      return null;
    } else if (object.getClassId() == InterfaceClass.DATA.id()) {
      return null;
    } else {
      throw new ProtocolAdapterException(
          "Unexpected class id for getScalerUnit: " + object.getClassId());
    }
  }

  private int getIntervalInMinutes(final CosemObject object) throws ProtocolAdapterException {
    // TODO: Get interval from object (attribute 4, capture period in sec)
    switch (DlmsObjectType.valueOf(object.getTag())) {
      case DEFINABLE_LOAD_PROFILE:
        return INTERVAL_DEFINABLE_LOAD_PROFILE;
      case POWER_QUALITY_PROFILE_1:
        return INTERVAL_PROFILE_1;
      case POWER_QUALITY_PROFILE_2:
        return INTERVAL_PROFILE_2;
      default:
        throw new ProtocolAdapterException("Unknown profile generic " + object.getTag());
    }
  }

  @Getter
  protected static class SelectableObject {
    private final int classId;
    private final String logicalName;
    private final byte attributeIndex;
    private final Integer dataIndex;
    private final String scalerUnit;

    public SelectableObject(
        final int classId,
        final String logicalName,
        final byte attributeIndex,
        final Integer dataIndex,
        final String scalerUnit) {
      this.classId = classId;
      this.logicalName = logicalName;
      this.attributeIndex = attributeIndex;
      this.dataIndex = dataIndex;
      this.scalerUnit = scalerUnit;
    }

    public byte[] getObisAsBytes() {
      final ObisCodeValuesDto obisCodeValuesDto = new ObisCodeValuesDto(this.logicalName);
      return obisCodeValuesDto.toByteArray();
    }
  }
}
