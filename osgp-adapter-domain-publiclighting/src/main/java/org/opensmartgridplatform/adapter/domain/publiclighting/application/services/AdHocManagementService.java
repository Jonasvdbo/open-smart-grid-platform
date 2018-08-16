/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.publiclighting.application.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.opensmartgridplatform.adapter.domain.publiclighting.application.valueobjects.CdmaRun;
import org.opensmartgridplatform.adapter.domain.shared.FilterLightAndTariffValuesHelper;
import org.opensmartgridplatform.adapter.domain.shared.GetStatusResponse;
import org.opensmartgridplatform.domain.core.entities.Device;
import org.opensmartgridplatform.domain.core.entities.DeviceOutputSetting;
import org.opensmartgridplatform.domain.core.entities.Event;
import org.opensmartgridplatform.domain.core.entities.LightMeasurementDevice;
import org.opensmartgridplatform.domain.core.entities.RelayStatus;
import org.opensmartgridplatform.domain.core.entities.Ssld;
import org.opensmartgridplatform.domain.core.exceptions.ValidationException;
import org.opensmartgridplatform.domain.core.repositories.EventRepository;
import org.opensmartgridplatform.domain.core.valueobjects.CdmaDevice;
import org.opensmartgridplatform.domain.core.valueobjects.DeviceFunction;
import org.opensmartgridplatform.domain.core.valueobjects.DeviceLifecycleStatus;
import org.opensmartgridplatform.domain.core.valueobjects.DeviceStatus;
import org.opensmartgridplatform.domain.core.valueobjects.DeviceStatusMapped;
import org.opensmartgridplatform.domain.core.valueobjects.DomainType;
import org.opensmartgridplatform.domain.core.valueobjects.EventMessageDataContainer;
import org.opensmartgridplatform.domain.core.valueobjects.EventType;
import org.opensmartgridplatform.domain.core.valueobjects.LightValue;
import org.opensmartgridplatform.domain.core.valueobjects.TransitionType;
import org.opensmartgridplatform.dto.valueobjects.LightValueMessageDataContainerDto;
import org.opensmartgridplatform.dto.valueobjects.ResumeScheduleMessageDataContainerDto;
import org.opensmartgridplatform.dto.valueobjects.TransitionMessageDataContainerDto;
import org.opensmartgridplatform.shared.exceptionhandling.ComponentType;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalException;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalExceptionType;
import org.opensmartgridplatform.shared.exceptionhandling.NoDeviceResponseException;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.opensmartgridplatform.shared.exceptionhandling.TechnicalException;
import org.opensmartgridplatform.shared.infra.jms.RequestMessage;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessage;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "domainPublicLightingAdHocManagementService")
@Transactional(value = "transactionManager")
public class AdHocManagementService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdHocManagementService.class);

    @Autowired
    private EventRepository eventRepository;

    /**
     * Constructor
     */
    public AdHocManagementService() {
        // Parameterless constructor required for transactions...
    }

    // === SET LIGHT ===

    public void setLight(final String organisationIdentification, final String deviceIdentification,
            final String correlationUid, final List<LightValue> lightValues, final String messageType,
            final int messagePriority) throws FunctionalException {

        LOGGER.debug("setLight called for device {} with organisation {}", deviceIdentification,
                organisationIdentification);

        this.findOrganisation(organisationIdentification);
        final Device device = this.findActiveDevice(deviceIdentification);

        final List<org.opensmartgridplatform.dto.valueobjects.LightValueDto> lightValuesDto = this.domainCoreMapper
                .mapAsList(lightValues, org.opensmartgridplatform.dto.valueobjects.LightValueDto.class);
        final LightValueMessageDataContainerDto lightValueMessageDataContainer = new LightValueMessageDataContainerDto(
                lightValuesDto);

        this.osgpCoreRequestMessageSender.send(new RequestMessage(correlationUid, organisationIdentification,
                deviceIdentification, lightValueMessageDataContainer), messageType, messagePriority,
                device.getIpAddress());
    }

    // === GET STATUS ===

    /**
     * Retrieve status of device and provide a mapped response (PublicLighting
     * or TariffSwitching)
     *
     * @param organisationIdentification
     *            identification of organization
     * @param deviceIdentification
     *            identification of device
     * @param correlationUid
     *            the correlation UID of the message
     * @param allowedDomainType
     *            domain type performing requesting the status
     * @param messageType
     *            the message type
     * @param messagePriority
     *            the priority of the message
     *
     * @throws FunctionalException
     *             in case the organization is not authorized for this action or
     *             the device is not active.
     */
    public void getStatus(final String organisationIdentification, final String deviceIdentification,
            final String correlationUid, final DomainType allowedDomainType, final String messageType,
            final int messagePriority) throws FunctionalException {

        this.findOrganisation(organisationIdentification);
        final Device device = this.findActiveDevice(deviceIdentification);

        final org.opensmartgridplatform.dto.valueobjects.DomainTypeDto allowedDomainTypeDto = this.domainCoreMapper
                .map(allowedDomainType, org.opensmartgridplatform.dto.valueobjects.DomainTypeDto.class);

        final String actualMessageType = LightMeasurementDevice.LMD_TYPE.equals(device.getDeviceType())
                ? DeviceFunction.GET_LIGHT_SENSOR_STATUS.name() : messageType;

        this.osgpCoreRequestMessageSender.send(new RequestMessage(correlationUid, organisationIdentification,
                deviceIdentification, allowedDomainTypeDto), actualMessageType, messagePriority, device.getIpAddress());
    }

    public void handleGetStatusResponse(
            final org.opensmartgridplatform.dto.valueobjects.DeviceStatusDto deviceStatusDto,
            final DomainType allowedDomainType, final String deviceIdentification,
            final String organisationIdentification, final String correlationUid, final String messageType,
            final int messagePriority, final ResponseMessageResultType deviceResult, final OsgpException exception) {

        LOGGER.info("handleResponse for MessageType: {}", messageType);
        final GetStatusResponse response = new GetStatusResponse();
        response.setOsgpException(exception);
        response.setResult(deviceResult);

        if (deviceResult == ResponseMessageResultType.NOT_OK || exception != null) {
            LOGGER.error("Device Response not ok.", exception);
        } else {
            final DeviceStatus status = this.domainCoreMapper.map(deviceStatusDto, DeviceStatus.class);
            try {
                final Device dev = this.deviceDomainService.searchDevice(deviceIdentification);
                if (LightMeasurementDevice.LMD_TYPE.equals(dev.getDeviceType())) {
                    this.handleLmd(status, response);
                } else {
                    this.handleSsld(deviceIdentification, status, allowedDomainType, response);
                }
            } catch (final FunctionalException e) {
                LOGGER.error("Caught FunctionalException", e);
            }
        }

        final ResponseMessage responseMessage = ResponseMessage.newResponseMessageBuilder()
                .withCorrelationUid(correlationUid).withOrganisationIdentification(organisationIdentification)
                .withDeviceIdentification(deviceIdentification).withResult(response.getResult())
                .withOsgpException(response.getOsgpException()).withDataObject(response.getDeviceStatusMapped())
                .withMessagePriority(messagePriority).build();
        this.webServiceResponseMessageSender.send(responseMessage);
    }

    private void handleLmd(final DeviceStatus status, final GetStatusResponse response) {
        if (status != null) {
            final DeviceStatusMapped deviceStatusMapped = new DeviceStatusMapped(null, status.getLightValues(),
                    status.getPreferredLinkType(), status.getActualLinkType(), status.getLightType(),
                    status.getEventNotificationsMask());
            // Return mapped status using GetStatusResponse instance.
            response.setDeviceStatusMapped(deviceStatusMapped);
        } else {
            // No status received, create bad response.
            response.setDeviceStatusMapped(null);
            response.setOsgpException(new TechnicalException(ComponentType.DOMAIN_PUBLIC_LIGHTING,
                    "Light measurement device was not able to report light sensor status",
                    new NoDeviceResponseException()));
            response.setResult(ResponseMessageResultType.NOT_OK);
        }
    }

    private void handleSsld(final String deviceIdentification, final DeviceStatus status,
            final DomainType allowedDomainType, final GetStatusResponse response) {

        // Find device and output settings.
        final Ssld ssld = this.ssldRepository.findByDeviceIdentification(deviceIdentification);
        final List<DeviceOutputSetting> deviceOutputSettings = ssld.getOutputSettings();

        // Create map with external relay number as key set.
        final Map<Integer, DeviceOutputSetting> dosMap = new HashMap<>();
        for (final DeviceOutputSetting dos : deviceOutputSettings) {
            dosMap.put(dos.getExternalId(), dos);
        }

        if (status != null) {
            // Map the DeviceStatus for SSLD.
            final DeviceStatusMapped deviceStatusMapped = new DeviceStatusMapped(
                    FilterLightAndTariffValuesHelper.filterTariffValues(status.getLightValues(), dosMap,
                            allowedDomainType),
                    FilterLightAndTariffValuesHelper.filterLightValues(status.getLightValues(), dosMap,
                            allowedDomainType),
                    status.getPreferredLinkType(), status.getActualLinkType(), status.getLightType(),
                    status.getEventNotificationsMask());

            // Update the relay overview with the relay information.
            this.updateDeviceRelayOverview(ssld, deviceStatusMapped);

            // Return mapped status using GetStatusResponse instance.
            response.setDeviceStatusMapped(deviceStatusMapped);
        } else {
            // No status received, create bad response.
            response.setDeviceStatusMapped(null);
            response.setOsgpException(new TechnicalException(ComponentType.DOMAIN_PUBLIC_LIGHTING,
                    "SSLD was not able to report relay status", new NoDeviceResponseException()));
            response.setResult(ResponseMessageResultType.NOT_OK);
        }
    }

    // === RESUME SCHEDULE ===

    public void resumeSchedule(final String organisationIdentification, final String deviceIdentification,
            final String correlationUid, final Integer index, final boolean isImmediate, final String messageType,
            final int messagePriority) throws FunctionalException {

        this.findOrganisation(organisationIdentification);
        final Device device = this.findActiveDevice(deviceIdentification);
        final Ssld ssld = this.findSsldForDevice(device);

        if (!ssld.getHasSchedule()) {
            throw new FunctionalException(FunctionalExceptionType.UNSCHEDULED_DEVICE,
                    ComponentType.DOMAIN_PUBLIC_LIGHTING, new ValidationException(
                            String.format("Device %1$s does not have a schedule.", deviceIdentification)));
        }

        final ResumeScheduleMessageDataContainerDto resumeScheduleMessageDataContainerDto = new ResumeScheduleMessageDataContainerDto(
                index, isImmediate);

        this.osgpCoreRequestMessageSender.send(new RequestMessage(correlationUid, organisationIdentification,
                deviceIdentification, resumeScheduleMessageDataContainerDto), messageType, messagePriority,
                device.getIpAddress());
    }

    // === SET TRANSITION ===

    public void setTransition(final String organisationIdentification, final String deviceIdentification,
            final String correlationUid, @NotNull final TransitionType transitionType, final DateTime transitionTime,
            final String messageType, final int messagePriority) throws FunctionalException {

        LOGGER.debug("Public setTransition called for device {} with organisation {}", deviceIdentification,
                organisationIdentification);

        this.findOrganisation(organisationIdentification);
        final Device device = this.findActiveDevice(deviceIdentification);

        this.setTransition(organisationIdentification, device, correlationUid, transitionType, transitionTime,
                messageType, messagePriority);
    }

    private void setTransition(final String organisationIdentification, final Device device,
            final String correlationUid, final TransitionType transitionType, final DateTime transitionTime,
            final String messageType, final int messagePriority) {

        LOGGER.debug("Private setTransition called for device {} with organisation {}",
                device.getDeviceIdentification(), organisationIdentification);

        final TransitionMessageDataContainerDto transitionMessageDataContainerDto = new TransitionMessageDataContainerDto(
                this.domainCoreMapper.map(transitionType,
                        org.opensmartgridplatform.dto.valueobjects.TransitionTypeDto.class),
                transitionTime);

        this.osgpCoreRequestMessageSender.send(new RequestMessage(correlationUid, organisationIdentification,
                device.getDeviceIdentification(), transitionMessageDataContainerDto), messageType, messagePriority,
                device.getIpAddress());
    }

    // === TRANSITION MESSAGE FROM LIGHT MEASUREMENT DEVICE ===

    /**
     * Send transition message to SSLD's based on light measurement device
     * trigger.
     *
     * @param organisationIdentification
     *            Organization issuing the request.
     * @param deviceIdentification
     *            Light measurement device identification.
     * @param correlationUid
     *            The generated correlation UID.
     * @param eventMessageDataContainer
     *            List of {@link Event}s contained by
     *            {@link EventMessageDataContainer}.
     */
    public void handleLightMeasurementDeviceTransition(final String organisationIdentification,
            final String deviceIdentification, final String correlationUid,
            final EventMessageDataContainer eventMessageDataContainer) {

        // Check the event and the LMD.
        final Event event = eventMessageDataContainer.getEvents().get(0);
        if (event == null) {
            LOGGER.error("No event received for light measurement device: {}", deviceIdentification);
            return;
        }
        LightMeasurementDevice lmd = this.lightMeasurementDeviceRepository
                .findByDeviceIdentification(deviceIdentification);
        if (lmd == null) {
            LOGGER.error("No light measurement device found: {}", deviceIdentification);
            return;
        }

        // Update last communication time for the LMD.
        lmd = this.updateLmdLastCommunicationTime(lmd);

        // Determine if the event is a duplicate. If so, quit.
        if (this.isDuplicateEvent(event, lmd)) {
            LOGGER.info("Duplicate event detected for light measurement device: {}. Event[id:{} {} {} {} {}]",
                    lmd.getDeviceIdentification(), event.getId(), event.getDateTime(), event.getDescription(),
                    event.getEventType(), event.getIndex());
            return;
        }

        // Determine the transition type based on the event of the LMD.
        final TransitionType transitionType = this.determineTransitionTypeForEvent(event);

        // Find all SSLDs which need to receive a SET_TRANSITION message.
        // final List<CdmaDevice> devicesForLmd =
        // this.ssldRepository.findCdmaBatchDevicesInUseForLmd(lmd,
        // DeviceLifecycleStatus.IN_USE);

        final CdmaRun cdmaRun = this.getCdmaDevicesPerMastSegment(lmd);

        // Send SET_TRANSITION messages to the SSLDs.
        this.transitionDevices(cdmaRun, organisationIdentification, correlationUid, transitionType);
    }

    private CdmaRun getCdmaDevicesPerMastSegment(final LightMeasurementDevice lmd) {

        // Find all SSLDs which need to receive a SET_TRANSITION message.
        final List<CdmaDevice> devicesForLmd = this.ssldRepository.findCdmaBatchDevicesInUseForLmd(lmd,
                DeviceLifecycleStatus.IN_USE);

        // final Stream<CdmaDevice> nonEmptyDevices =
        // devicesForLmd.stream().map(CdmaDevice::mapEmptyFields);
        return devicesForLmd.stream().collect(CdmaRun::new, CdmaRun::add, CdmaRun::combine);
    }

    /*
     * Returns a collector which can create a map by dividing a stream of CDMA
     * mast segments over items having the default CDMA mast segment and items
     * having another CDMA mast segment.
     *
     * Example: (Boolean.TRUE, [(DEVICE-WITHOUT-MAST-SEGMENT, [(1, [cd6,
     * cd1])])], Boolean.FALSE, [(2500/1, [(1, [cd6, cd1]), (2, [cd24, cd21])]),
     * ....])
     */
    // private Collector<CdmaDevice, ?, Map<Boolean, TreeMap<String,
    // TreeMap<Short, List<CdmaDevice>>>>> partitionMastSegmentCollector() {
    // private Collector<CdmaDevice, ?, Map<Boolean, List<CdmaDevice>>>
    // partitionMastSegmentCollector_old() {
    // return Collectors.partitioningBy(CdmaDevice::hasDefaultMastSegment);
    // }

    private LightMeasurementDevice updateLmdLastCommunicationTime(final LightMeasurementDevice lmd) {
        final DateTime now = DateTime.now();
        LOGGER.info("Trying to update lastCommunicationTime for light measurement device: {} with dateTime: {}",
                lmd.getDeviceIdentification(), now);
        lmd.setLastCommunicationTime(now.toDate());
        return this.lightMeasurementDeviceRepository.save(lmd);
    }

    private boolean isDuplicateEvent(final Event event, final LightMeasurementDevice lmd) {
        final List<Event> events = this.eventRepository.findTop2ByDeviceOrderByDateTimeDesc(lmd);
        for (final Event e : events) {
            if (event.getDateTime().equals(e.getDateTime())) {
                // Exact match found, skip this event of the result set.
                continue;
            } else if (event.getEventType().equals(e.getEventType())) {
                // If second event has same type, duplicate event has been
                // detected.
                return true;
            }
        }
        return false;
    }

    // private List<Ssld> getSsldsToTransitionForLmd(final
    // LightMeasurementDevice lmd) {
    // LOGGER.info("Find SSLDs for light measurement device: {}",
    // lmd.getDeviceIdentification());
    // final List<Ssld> ssldsToTransition = this.ssldRepository
    // .findByLightMeasurementDeviceAndIsActivatedTrueAndInMaintenanceFalseAndProtocolInfoNotNullAndNetworkAddressNotNullAndTechnicalInstallationDateNotNullAndDeviceLifecycleStatus(
    // lmd, DeviceLifecycleStatus.IN_USE);
    // LOGGER.info("For light measurement device: {}, {} SSLDs were found",
    // lmd.getDeviceIdentification(),
    // ssldsToTransition.size());
    // return ssldsToTransition;
    // }

    private TransitionType determineTransitionTypeForEvent(final Event event) {
        final String transitionTypeFromLightMeasurementDevice = event.getEventType().name();
        TransitionType transitionType;
        if (EventType.LIGHT_SENSOR_REPORTS_DARK.name().equals(transitionTypeFromLightMeasurementDevice)) {
            transitionType = TransitionType.DAY_NIGHT;
        } else {
            transitionType = TransitionType.NIGHT_DAY;
        }
        return transitionType;
    }

    private void transitionDevices(final CdmaRun cdmaRun, final String organisationIdentification,
            final String correlationUid, final TransitionType transitionType) {

        /*
         * devicesToTransition.values().stream().forEach(x ->
         * LOGGER.info(x.toString()));
         *
         * for (final Map.Entry<String, List<CdmaDevice>> mastSegmentDevices :
         * devicesToTransition.entrySet()) { final TreeMap<Short,
         * List<CdmaDevice>> deviceBatchesForMastSegment =
         * mastSegmentDevices.getValue().stream()
         * .collect(Collectors.groupingBy(CdmaDevice::getBatchNumber,
         * TreeMap<Short, List<CdmaDevice>>::new, Collectors.toList()));
         * System.out.println(deviceBatchesForMastSegment.size()); }
         */

        // Don't send time stamp to switch device.
        // final DateTime transitionTime = null;
        // for (final Ssld ssld : ssldsToTransition) {
        // try {
        // this.setTransition(organisationIdentification, ssld, correlationUid,
        // transitionType, transitionTime,
        // DeviceFunction.SET_TRANSITION.name(),
        // MessagePriorityEnum.LOW.getPriority());
        // } catch (final Exception e) {
        // LOGGER.error("Caught unexpected Exception", e);
        // }
        // }
    }

    private void transitionDevices_v1(final Map<String, List<CdmaDevice>> devicesToTransition,
            final String organisationIdentification, final String correlationUid, final TransitionType transitionType) {
        devicesToTransition.values().stream().forEach(x -> LOGGER.info(x.toString()));

        for (final Map.Entry<String, List<CdmaDevice>> mastSegmentDevices : devicesToTransition.entrySet()) {
            final TreeMap<Short, List<CdmaDevice>> deviceBatchesForMastSegment = mastSegmentDevices.getValue().stream()
                    .collect(Collectors.groupingBy(CdmaDevice::getBatchNumber, TreeMap<Short, List<CdmaDevice>>::new,
                            Collectors.toList()));
            System.out.println(deviceBatchesForMastSegment.size());
        }

        // Don't send time stamp to switch device.
        // final DateTime transitionTime = null;
        // for (final Ssld ssld : ssldsToTransition) {
        // try {
        // this.setTransition(organisationIdentification, ssld, correlationUid,
        // transitionType, transitionTime,
        // DeviceFunction.SET_TRANSITION.name(),
        // MessagePriorityEnum.LOW.getPriority());
        // } catch (final Exception e) {
        // LOGGER.error("Caught unexpected Exception", e);
        // }
        // }
    }

    // === SET LIGHT MEASUREMENT DEVICE ===

    /**
     * Couple an SSLD with a light measurement device.
     *
     * @param organisationIdentification
     *            Organization issuing the request.
     * @param deviceIdentification
     *            The SSLD.
     * @param correlationUid
     *            The generated correlation UID.
     * @param lightMeasurementDeviceIdentification
     *            The light measurement device.
     * @param messageType
     *            SET_LIGHTMEASUREMENT_DEVICE
     *
     * @throws FunctionalException
     *             In case the organisation can not be found.
     */
    public void coupleLightMeasurementDeviceForSsld(final String organisationIdentification,
            final String deviceIdentification, final String correlationUid,
            final String lightMeasurementDeviceIdentification, final String messageType) throws FunctionalException {

        LOGGER.debug(
                "setLightMeasurementDevice called for device {} with organisation {} and light measurement device, message type: {}, correlationUid: {}",
                deviceIdentification, organisationIdentification, lightMeasurementDeviceIdentification, messageType,
                correlationUid);

        this.findOrganisation(organisationIdentification);
        final Ssld ssld = this.ssldRepository.findByDeviceIdentification(deviceIdentification);
        if (ssld == null) {
            LOGGER.error("Unable to find ssld: {}", deviceIdentification);
            return;
        }

        final LightMeasurementDevice lightMeasurementDevice = this.lightMeasurementDeviceRepository
                .findByDeviceIdentification(lightMeasurementDeviceIdentification);
        if (lightMeasurementDevice == null) {
            LOGGER.error("Unable to find light measurement device: {}", lightMeasurementDeviceIdentification);
            return;
        }

        ssld.setLightMeasurementDevice(lightMeasurementDevice);
        this.ssldRepository.save(ssld);

        LOGGER.info("Set light measurement device: {} for ssld: {}", lightMeasurementDeviceIdentification,
                deviceIdentification);
    }

    /**
     * Updates the relay overview from a device based on the given device
     * status.
     *
     * @param device
     *            The device to update.
     * @param deviceStatus
     *            The device status to update the relay overview with.
     */
    private void updateDeviceRelayOverview(final Ssld device, final DeviceStatusMapped deviceStatusMapped) {
        final List<RelayStatus> relayStatuses = device.getRelayStatusses();

        for (final LightValue lightValue : deviceStatusMapped.getLightValues()) {
            boolean updated = false;
            for (final RelayStatus relayStatus : relayStatuses) {
                if (relayStatus.getIndex() == lightValue.getIndex()) {
                    relayStatus.setLastKnownState(lightValue.isOn());
                    relayStatus.setLastKnowSwitchingTime(DateTime.now().toDate());
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                final RelayStatus newRelayStatus = new RelayStatus(device, lightValue.getIndex(), lightValue.isOn(),
                        DateTime.now().toDate());
                relayStatuses.add(newRelayStatus);
            }
        }

        this.ssldRepository.save(device);
    }

    /**
     * Logs the response of SET_TRANSITION calls.
     */
    public void handleSetTransitionResponse(final String deviceIdentification, final String organisationIdentification,
            final String correlationUid, final String messageType, final int messagePriority,
            final ResponseMessageResultType responseMessageResultType, final OsgpException osgpException) {

        if (osgpException == null) {
            LOGGER.info(
                    "Received response: {} for messageType: {}, messagePriority: {}, deviceIdentification: {}, organisationIdentification: {}, correlationUid: {}",
                    responseMessageResultType.getValue(), messageType, messagePriority, deviceIdentification,
                    organisationIdentification, correlationUid);
        } else {
            LOGGER.error(
                    "Exception: {} for response: {} for messageType: {}, messagePriority: {}, deviceIdentification: {}, organisationIdentification: {}, correlationUid: {}",
                    osgpException.getMessage(), responseMessageResultType.getValue(), messageType, messagePriority,
                    deviceIdentification, organisationIdentification, correlationUid);
        }

    }
}
