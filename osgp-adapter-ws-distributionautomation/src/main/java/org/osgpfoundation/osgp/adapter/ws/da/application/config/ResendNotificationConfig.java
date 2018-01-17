/**
 * Copyright 2018 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.adapter.ws.da.application.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alliander.osgp.adapter.ws.shared.services.ResendNotificationJob;
import com.alliander.osgp.shared.application.config.AbstractSchedulingConfig;

@EnableScheduling
@Configuration
@PropertySource("classpath:osgp-adapter-ws-distributionautomation.properties")
@PropertySource(value = "file:${osgp/Global/config}", ignoreResourceNotFound = true)
@PropertySource(value = "file:${osgp/AdapterWsDistributionAutomation/config}", ignoreResourceNotFound = true)
public class ResendNotificationConfig extends AbstractSchedulingConfig {

    private static final String KEY_RESEND_NOTIFICATION_CRON_EXPRESSION = "distributionautomation.scheduling.job.resend.notification.cron.expression";
    private static final String KEY_RESEND_NOTIFICATION_THREAD_COUNT = "distributionautomation.scheduling.job.resend.notification.thread.count";

    @Value("${db.driver}")
    private String databaseDriver;

    @Value("${db.password}")
    private String databasePassword;

    @Value("${db.protocol}")
    private String databaseProtocol;

    @Value("${db.host}")
    private String databaseHost;

    @Value("${db.port}")
    private String databasePort;

    @Value("${db.name}")
    private String databaseName;

    @Value("${db.username}")
    private String databaseUsername;

    @Value("${distributionautomation.scheduling.job.resend.notification.maximum}")
    private short resendNotificationMaximum;

    @Value("${distributionautomation.scheduling.job.resend.notification.multiplier}")
    private int resendNotificationMultiplier;

    @Value("${distributionautomation.scheduling.job.resend.notification.resend.threshold.in.minutes}")
    private int resendThresholdInHours;

    @Bean
    public short resendNotificationMaximum() {
        return this.resendNotificationMaximum;
    }

    @Bean
    public int resendNotificationMultiplier() {
        return this.resendNotificationMultiplier;
    }

    @Bean
    public int resendThresholdInHours() {
        return this.resendThresholdInHours;
    }

    @Bean(destroyMethod = "shutdown")
    public Scheduler resendNotificationScheduler() throws SchedulerException {
        return this.constructScheduler(ResendNotificationJob.class, KEY_RESEND_NOTIFICATION_THREAD_COUNT,
                KEY_RESEND_NOTIFICATION_CRON_EXPRESSION, this.getDatabaseUrl(), this.databaseUsername,
                this.databasePassword, this.databaseDriver);
    }

    private String getDatabaseUrl() {
        return this.databaseProtocol + this.databaseHost + ":" + this.databasePort + "/" + this.databaseName;
    }
}
