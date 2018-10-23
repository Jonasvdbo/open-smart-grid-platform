/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.cucumber.platform.microgrids.glue.steps.housekeeping;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.opensmartgridplatform.cucumber.core.RetryableAssert;
import org.opensmartgridplatform.cucumber.platform.microgrids.glue.steps.database.ws.ResponseDataSteps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ResponseDataCleanupJobSteps {

    private static final TimeUnit TIME_UNIT_RESPONSE_CLEANUP_DELAY = TimeUnit.MILLISECONDS;

    @Autowired
    private ResponseDataSteps responseDataSteps;

    @Value("${iec61850.rtu.response.cleanup.wait.delay:1000}")
    private long delay;

    @Value("${iec61850.rtu.response.cleanup.wait.retries:65}")
    private int retries;

    @When("^the response data cleanup job runs$")
    public void theResponseDataCleanupJobRuns() {
        // Do nothing - Scheduled task runs automatically
    }

    @Then("^the cleanup job should have removed the response data with correlation uid \"(.*)\"$")
    public void theCleanupJobShouldHaveRemovedTheResponseData(final String correlationUid) {

        this.waitForResponseDataToBeRemoved(correlationUid, this.delay, this.retries);
    }

    @Then("^the cleanup job should not have removed the response data with correlation uid \"(.*)\"$")
    public void theCleanupJobShouldNotHaveRemovedTheResponseData(final String correlationUid) {

        this.waitToMakeSureResponseDataIsNotRemoved(correlationUid, this.delay, this.retries);
    }

    private void waitForResponseDataToBeRemoved(final String correlationUid, final long delay, final int retries) {
        try {
            RetryableAssert.assertWithRetries(
                    () -> this.responseDataSteps.theResponseDataRecordShouldBeDeleted(correlationUid), retries, delay,
                    TIME_UNIT_RESPONSE_CLEANUP_DELAY);
        } catch (final AssertionError e) {
            fail("Cleanup job should have removed response data with correlation uid " + correlationUid + " within "
                    + RetryableAssert.describeMaxDuration(retries, delay, TIME_UNIT_RESPONSE_CLEANUP_DELAY));
        }
    }

    private void waitToMakeSureResponseDataIsNotRemoved(final String correlationUid, final long delay,
            final int retries) {

        try {
            RetryableAssert.assertDelayedWithRetries(
                    () -> this.responseDataSteps.theResponseDataRecordShouldNotBeDeleted(correlationUid), 0,
                    retries * delay, TIME_UNIT_RESPONSE_CLEANUP_DELAY);
        } catch (final AssertionError e) {
            fail("Cleanup job should not have removed response data with correlation uid " + correlationUid + ".");
        }
    }
}
