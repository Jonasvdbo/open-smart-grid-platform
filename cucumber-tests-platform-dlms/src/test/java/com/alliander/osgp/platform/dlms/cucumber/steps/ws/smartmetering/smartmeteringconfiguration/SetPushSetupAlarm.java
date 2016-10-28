/**
 * Copyright 2016 Smart Society Services B.V.
 */
package com.alliander.osgp.platform.dlms.cucumber.steps.ws.smartmetering.smartmeteringconfiguration;

import static com.alliander.osgp.platform.cucumber.core.Helpers.getString;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.alliander.osgp.logging.domain.entities.DeviceLogItem;
import com.alliander.osgp.logging.domain.repositories.DeviceLogItemRepository;
import com.alliander.osgp.platform.cucumber.core.ScenarioContext;
import com.alliander.osgp.platform.cucumber.hooks.SimulatePushedAlarmsHooks;
import com.alliander.osgp.platform.cucumber.steps.Defaults;
import com.alliander.osgp.platform.cucumber.steps.Keys;
import com.alliander.osgp.platform.cucumber.steps.common.ResponseSteps;
import com.alliander.osgp.platform.dlms.cucumber.steps.ws.smartmetering.SmartMeteringStepsBase;
import com.eviware.soapui.model.testsuite.TestStepResult.TestStepStatus;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SetPushSetupAlarm extends SmartMeteringStepsBase {
    private static final String PATH_RESULT = "/Envelope/Body/SetPushSetupAlarmResponse/Result/text()";

    private static final String XPATH_MATCHER_PUSH_NOTIFICATION = "DlmsPushNotification \\[device = \\w*, trigger type = Push alarm monitor, alarms=\\[(\\w*(, )?)+\\]\\]";

    private static final String TEST_SUITE_XML = "SmartmeterConfiguration";
    private static final String TEST_CASE_XML = "125 Receive Alarm Notifications";
    private static final String TEST_CASE_NAME_REQUEST = "SetPushSetupAlarm - Request 1";
    private static final String TEST_CASE_NAME_GETRESPONSE_REQUEST = "GetSetPushSetupAlarmResponse - Request 1";

    private static final Logger LOGGER = LoggerFactory.getLogger(SetPushSetupAlarm.class);

    private static final String KnownDevice = "E9998000014123414";
    private static final String UnknownDevice = "Z9876543210123456";

    @Autowired
    private DeviceLogItemRepository deviceLogItemRepository;

    @Autowired
    private ResponseSteps responseSteps;

    @When("^an alarm notification is received from a known device$")
    public void anAlarmNotificationIsReceivedFromAKnownDevice(final Map<String, String> settings) throws Throwable {
        try {
            SimulatePushedAlarmsHooks.simulateAlarm(KnownDevice, new byte[] { 0x2C, 0x00, 0x00, 0x01, 0x02 });
            SimulatePushedAlarmsHooks.simulateAlarm(KnownDevice, new byte[] { 0x2C, 0x04, 0x20, 0x00, 0x00 });
        } catch (final Exception e) {
            LOGGER.error("Error occured simulateAlarm: ", e);
        }

        PROPERTIES_MAP.put(DEVICE_IDENTIFICATION_LABEL, getString(settings, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));
        PROPERTIES_MAP.put(ORGANISATION_IDENTIFICATION_LABEL, getString(settings, Keys.KEY_ORGANIZATION_IDENTIFICATION, Defaults.DEFAULT_ORGANIZATION_IDENTIFICATION));

        this.requestRunner(TestStepStatus.OK, PROPERTIES_MAP, TEST_CASE_NAME_REQUEST, TEST_CASE_XML, TEST_SUITE_XML);
    }

    @Then("^the alarm should be pushed to OSGP$")
    public void theAlarmShouldBePushedToOSGP(final Map<String, String> settings) throws Throwable {
        PROPERTIES_MAP.put(DEVICE_IDENTIFICATION_LABEL, getString(settings, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));
        PROPERTIES_MAP.put(CORRELATION_UID_LABEL, ScenarioContext.Current().get("CorrelationUid").toString());

        this.requestRunner(TestStepStatus.OK, PROPERTIES_MAP, TEST_CASE_NAME_GETRESPONSE_REQUEST, TEST_CASE_XML, TEST_SUITE_XML);

        assertTrue(this.runXpathResult.assertXpath(this.response, PATH_RESULT, Defaults.EXPECTED_RESULT_OK));
    }

    @And("^the alarm should be pushed to the osgp_logging database device_log_item table$")
    public void theAlarmShouldBePushedToTheOsgpLoggingDatabaseTable() throws Throwable {
        final Pattern responsePattern = Pattern.compile(XPATH_MATCHER_PUSH_NOTIFICATION);

        final List<DeviceLogItem> deviceLogItems = this.deviceLogItemRepository
                .findByDeviceIdentificationInOrderByCreationTimeDesc(Arrays.asList(KnownDevice, UnknownDevice),
                        new PageRequest(0, 2)).getContent();
        for (int i = 0; i < 2; i++) {
            final DeviceLogItem item = deviceLogItems.get(i);
            LOGGER.info("CreationTime: {}", item.getCreationTime().toString());
            LOGGER.info("DecodedMessage: {}", item.getDecodedMessage());

            // Assert a matching DlmsPushNotification is logged.
            final Matcher responseMatcher = responsePattern.matcher(item.getDecodedMessage());
            assertTrue(responseMatcher.find());
        }
    }

    @When("^an alarm notification is received from an unknown device$")
    public void anAlarmNotificationIsReceivedFromAnUnknownDevice(final Map<String, String> settings) throws Throwable {
        try {
            SimulatePushedAlarmsHooks.simulateAlarm(UnknownDevice, new byte[] { 0x2C, 0x00, 0x00, 0x01, 0x02 });
            SimulatePushedAlarmsHooks.simulateAlarm(UnknownDevice, new byte[] { 0x2C, 0x04, 0x20, 0x00, 0x00 });
        } catch (final Exception e) {
            LOGGER.error("Error occured simulateAlarm: ", e);
        }

        PROPERTIES_MAP.put(DEVICE_IDENTIFICATION_LABEL, getString(settings, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));
        PROPERTIES_MAP.put(ORGANISATION_IDENTIFICATION_LABEL, getString(settings, Keys.KEY_ORGANIZATION_IDENTIFICATION, Defaults.DEFAULT_ORGANIZATION_IDENTIFICATION));

        this.requestRunner(TestStepStatus.FAILED, PROPERTIES_MAP, TEST_CASE_NAME_REQUEST, TEST_CASE_XML, TEST_SUITE_XML);
    }

    /**
     * Verify that the response contains the fault with the given expectedResult parameters.
     * @param expectedResult
     * @throws Throwable
     */
    @Then("^the response contains$")
    public void the_response_contains(final Map<String, String> expectedResult) throws Throwable {
        this.responseSteps.VerifyFaultResponse(this.runXpathResult, this.response, expectedResult);
    }

}
