/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.steps.database.core;

import static com.alliander.osgp.platform.cucumber.core.Helpers.getBoolean;
import static com.alliander.osgp.platform.cucumber.core.Helpers.getDate;
import static com.alliander.osgp.platform.cucumber.core.Helpers.getInteger;
import static com.alliander.osgp.platform.cucumber.core.Helpers.getEnum;
import static com.alliander.osgp.platform.cucumber.core.Helpers.getFloat;
import static com.alliander.osgp.platform.cucumber.core.Helpers.getLong;
import static com.alliander.osgp.platform.cucumber.core.Helpers.getString;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.DeviceAuthorization;
import com.alliander.osgp.domain.core.entities.DeviceModel;
import com.alliander.osgp.domain.core.entities.DeviceOutputSetting;
import com.alliander.osgp.domain.core.entities.Organisation;
import com.alliander.osgp.domain.core.entities.Ssld;
import com.alliander.osgp.domain.core.repositories.DeviceAuthorizationRepository;
import com.alliander.osgp.domain.core.repositories.DeviceModelRepository;
import com.alliander.osgp.domain.core.repositories.DeviceRepository;
import com.alliander.osgp.domain.core.repositories.OrganisationRepository;
import com.alliander.osgp.domain.core.repositories.ProtocolInfoRepository;
import com.alliander.osgp.domain.core.repositories.SsldRepository;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunctionGroup;
import com.alliander.osgp.domain.core.valueobjects.RelayType;
import com.alliander.osgp.platform.cucumber.config.CorePersistenceConfig;
import com.alliander.osgp.platform.cucumber.core.ScenarioContext;
import com.alliander.osgp.platform.cucumber.steps.Defaults;
import com.alliander.osgp.platform.cucumber.steps.Keys;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

@Transactional("txMgrCore")
public class DeviceSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceSteps.class);

    public static String DEFAULT_DEVICE_IDENTIFICATION = "test-device";
    public static String DEFAULT_DEVICE_TYPE = "OSLP";
    public static String DEFAULT_PROTOCOL = "OSLP";
    public static String DEFAULT_PROTOCOL_VERSION = "1.0";

    @SuppressWarnings("unused")
    private final Long DEFAULT_DEVICE_ID = new java.util.Random().nextLong();

    @Autowired
    private CorePersistenceConfig configuration;

    @Autowired
    private DeviceModelRepository deviceModelRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private OrganisationRepository organizationRepository;

    @Autowired
    private DeviceAuthorizationRepository deviceAuthorizationRepository;

    @Autowired
    private ProtocolInfoRepository protocolInfoRepository;

    @Autowired
    private SsldRepository ssldRepository;

    /**
     * Generic method which adds a device using the settings.
     *
     * @param settings
     *            The settings for the device to be used.
     * @throws Throwable
     */
    @Given("^a device$")
    public void aDevice(final Map<String, String> settings) throws Throwable {

        // Set the required stuff
        final String deviceIdentification = settings.get("DeviceIdentification");
        final Ssld ssld = new Ssld(deviceIdentification);

        ssld.setPublicKeyPresent(getBoolean(settings, Keys.KEY_PUBLICKEYPRESENT, Defaults.DEFAULT_PUBLICKEYPRESENT));
        ssld.setHasSchedule(getBoolean(settings, Keys.KEY_HAS_SCHEDULE, Defaults.DEFAULT_HASSCHEDULE));

        if (settings.containsKey(Keys.KEY_INTERNALID) || settings.containsKey(Keys.KEY_EXTERNALID)
                || settings.containsKey(Keys.KEY_RELAY_TYPE)) {
            List<DeviceOutputSetting> dosList = new ArrayList<>();
            int internalId = getInteger(settings, Keys.KEY_INTERNALID, Defaults.DEFAULT_INTERNALID),
                    externalId = getInteger(settings, Keys.KEY_EXTERNALID, Defaults.DEFAULT_EXTERNALID);
            RelayType relayType = getEnum(settings, Keys.KEY_RELAY_TYPE, RelayType.class, RelayType.LIGHT);

            if (relayType != null) {
                dosList.add(new DeviceOutputSetting(internalId, externalId, relayType));

                ssld.updateOutputSettings(dosList);
            }
        }

        this.ssldRepository.save(ssld);

        final Device device = this.deviceRepository.findByDeviceIdentification(deviceIdentification);
        this.updateDevice(device, settings);
    }

    /**
     * Update a device entity given its deviceidentification.
     * 
     * @param deviceIdentification
     *            The deviceIdentification.
     * @param settings
     *            The settings.
     */
    public void updateDevice(String deviceIdentification, Map<String, String> settings) {
        final Device device = this.deviceRepository.findByDeviceIdentification(deviceIdentification);
        this.updateDevice(device, settings);
    }

    /**
     * Update an existing device with the given settings.
     * 
     * @param device
     * @param settings
     */
    private void updateDevice(Device device, final Map<String, String> settings) {

        // Now set the optional stuff
        device.setActivated(getBoolean(settings, "IsActivated", Defaults.DEFAULT_IS_ACTIVATED));
        device.setTechnicalInstallationDate(getDate(settings, "TechnicalInstallationDate").toDate());

        final DeviceModel deviceModel = this.deviceModelRepository
                .findByModelCode(getString(settings, "DeviceModel", Defaults.DEFAULT_DEVICE_MODEL_MODEL_CODE));
        device.setDeviceModel(deviceModel);

        device.updateProtocol(this.protocolInfoRepository.findByProtocolAndProtocolVersion(
                getString(settings, Keys.KEY_PROTOCOL, DeviceSteps.DEFAULT_PROTOCOL),
                getString(settings, Keys.KEY_PROTOCOL_VERSION, DeviceSteps.DEFAULT_PROTOCOL_VERSION)));

        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(configuration.deviceNetworkaddress);
        } catch (UnknownHostException e) {
            inetAddress = InetAddress.getLoopbackAddress();
        }
        device.updateRegistrationData(inetAddress, getString(settings, "DeviceType", DeviceSteps.DEFAULT_DEVICE_TYPE));

        device.setVersion(getLong(settings, "Version"));
        device.setActive(getBoolean(settings, "Active", Defaults.DEFAULT_ACTIVE));
        if (getString(settings, "OrganizationIdentification", Defaults.DEFAULT_ORGANIZATION_IDENTIFICATION)
                .toLowerCase() != "null") {
            device.addOrganisation(
                    getString(settings, "OrganizationIdentification", Defaults.DEFAULT_ORGANIZATION_IDENTIFICATION));
        }
        device.updateMetaData(getString(settings, "Alias", Defaults.DEFAULT_ALIAS),
                getString(settings, "containerCity", Defaults.DEFAULT_CONTAINER_CITY),
                getString(settings, "containerPostalCode", Defaults.DEFAULT_CONTAINER_POSTALCODE),
                getString(settings, "containerStreet", Defaults.DEFAULT_CONTAINER_STREET),
                getString(settings, "containerNumber", Defaults.DEFAULT_CONTAINER_NUMBER),
                getString(settings, "containerMunicipality", Defaults.DEFAULT_CONTAINER_MUNICIPALITY),
                getFloat(settings, "gpsLatitude", Defaults.DEFAULT_LATITUDE),
                getFloat(settings, "gpsLongitude", Defaults.DEFAULT_LONGITUDE));

        device = this.deviceRepository.save(device);

        final Organisation organization = this.organizationRepository.findByOrganisationIdentification(
                getString(settings, "OrganizationIdentification", Defaults.DEFAULT_ORGANIZATION_IDENTIFICATION));

        if (getString(settings, "OrganizationIdentification", Defaults.DEFAULT_ORGANIZATION_IDENTIFICATION)
                .toLowerCase() != "null") {
            final DeviceFunctionGroup functionGroup = getEnum(settings, "DeviceFunctionGroup",
                    DeviceFunctionGroup.class, DeviceFunctionGroup.OWNER);
            final DeviceAuthorization authorization = device.addAuthorization(organization, functionGroup);
            final Device savedDevice = this.deviceRepository.save(device);
            this.deviceAuthorizationRepository.save(authorization);
            ScenarioContext.Current().put("DeviceIdentification", savedDevice.getDeviceIdentification());
        }
    }

    @Then("^the device with device identification \"([^\"]*)\" should be active$")
    public void theDeviceWithDeviceIdentificationShouldBeActive(final String deviceIdentification) throws Throwable {

        boolean success = false;
        int count = 0;
        while (!success) {
            try {
                if (count > configuration.defaultTimeout) {
                    Assert.fail("Failed");
                }

                // Wait for next try to retrieve a response
                count++;
                Thread.sleep(1000);

                final Device device = this.deviceRepository.findByDeviceIdentification(deviceIdentification);

                Assert.assertTrue(device.isActive());

                success = true;
            } catch (final Exception e) {
                // Do nothing
            }
        }
    }

    /**
     *
     * @param deviceIdentification
     * @throws Throwable
     */
    @Then("^the device with device identification \"([^\"]*)\" should be inactive$")
    public void theDeviceWithDeviceIdentificationShouldBeInActive(final String deviceIdentification) throws Throwable {
        boolean success = false;
        int count = 0;
        while (!success) {
            try {
                if (count > configuration.defaultTimeout) {
                    Assert.fail("Failed");
                }

                // Wait for next try to retrieve a response
                count++;
                Thread.sleep(1000);

                final Device device = this.deviceRepository.findByDeviceIdentification(deviceIdentification);
                Assert.assertFalse(device.isActive());

                success = true;
            } catch (final Exception e) {
                // Do nothing
            }
        }
    }

    /**
     * Verify that the device exists in the database.
     *
     * @return
     */
    @And("^the device exists")
    public void theDeviceExists(final Map<String, String> settings) throws Throwable {
        Device device = null; 
    	
        boolean success = false;
        int count = 0;
        while (!success) {
            if (count > configuration.defaultTimeout) {
                Assert.fail("Failed");
            }

            count++;
            Thread.sleep(1000);

            try {
                // Wait for next try to retrieve a response
                device = this.deviceRepository
                        .findByDeviceIdentification(settings.get(Keys.KEY_DEVICE_IDENTIFICATION));
                Assert.assertNotNull(device);


                success = true;
            } catch (final Exception | AssertionError e) {
                LOGGER.info("Waiting for device entity. [{}]", e.getMessage());
            }
        }
        
        if (settings.containsKey("Alias")) {
            Assert.assertEquals(settings.get("Alias"), device.getAlias());
        }
        if (settings.containsKey("OrganizationIdentification")) {
            Assert.assertEquals(settings.get("OrganizationIdentification"),
                    device.getOwner().getOrganisationIdentification());
        }
        if (settings.containsKey("ContainerPostalCode")) {
            Assert.assertEquals(settings.get("ContainerPostalCode"), device.getContainerPostalCode());
        }
        if (settings.containsKey("ContainerCity")) {
            Assert.assertEquals(settings.get("ContainerCity"), device.getContainerCity());
        }
        if (settings.containsKey("ContainerStreet")) {
            Assert.assertEquals(settings.get("ContainerStreet"), device.getContainerStreet());
        }
        if (settings.containsKey("ContainerNumber")) {
            Assert.assertEquals(settings.get("ContainerNumber"), device.getContainerNumber());
        }
        if (settings.containsKey("ContainerMunicipality")) {
            Assert.assertEquals(settings.get("ContainerMunicipality"), device.getContainerMunicipality());
        }
        if (settings.containsKey("GpsLatitude")) {
            Assert.assertTrue(Float.parseFloat(settings.get("GpsLatitude")) == device.getGpsLatitude());
        }
        if (settings.containsKey("GpsLongitude")) {
            Assert.assertTrue(Float.parseFloat(settings.get("GpsLongitude")) == device.getGpsLongitude());
        }
        if (settings.containsKey("Activated")) {
            Assert.assertTrue(Boolean.parseBoolean(settings.get("Activated")) == device.isActivated());
        }
        if (settings.containsKey("HasSchedule") || settings.containsKey("PublicKeyPresent")) {
            final Ssld ssld = this.ssldRepository
                    .findByDeviceIdentification(settings.get("DeviceIdentification"));

            if (settings.containsKey("HasSchedule")) {
                Assert.assertTrue(Boolean.parseBoolean(settings.get("HasSchedule")) == ssld.getHasSchedule());
            }
            if (settings.containsKey("PublicKeyPresent")) {
                Assert.assertTrue(
                        Boolean.parseBoolean(settings.get("PublicKeyPresent")) == ssld.isPublicKeyPresent());
            }
        }
        if (settings.containsKey("DeviceModel")) {
            Assert.assertEquals(settings.get("DeviceModel"), device.getDeviceModel().getModelCode());
        }
    }

    /**
     * Checks whether the device exists in the database..
     * 
     * @param deviceIdentification
     * @return
     */
    @Then("^the device with id \"([^\"]*)\" exists$")
    public void theDeviceWithIdExists(final String deviceIdentification) throws Throwable {
        final Device device = this.deviceRepository.findByDeviceIdentification(deviceIdentification);
        final List<DeviceAuthorization> devAuths = this.deviceAuthorizationRepository.findByDevice(device);

        Assert.assertNotNull(device);
        Assert.assertTrue(devAuths.size() > 0);
    }

    /**
     * Checks whether the device does not exist in the database.
     * 
     * @param deviceIdentification
     * @throws Throwable
     */
    @Then("^the device with id \"([^\"]*)\" does not exists$")
    public void theDeviceShouldBeRemoved(final String deviceIdentification) throws Throwable {
        final Device device = this.deviceRepository.findByDeviceIdentification(deviceIdentification);
        Assert.assertNull(device);

        final List<DeviceAuthorization> devAuths = this.deviceAuthorizationRepository.findByDevice(device);
        Assert.assertTrue(devAuths.size() == 0);
    }
}
