/**
 * Copyright 2017 Smart Society Services B.V.
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

import com.alliander.osgp.adapter.ws.shared.services.ResponseDataCleanupJob;
import com.alliander.osgp.shared.application.config.AbstractSchedulingConfig;

@EnableScheduling
@Configuration
@PropertySource("classpath:osgp-adapter-ws-distributionautomation.properties")
@PropertySource(value = "file:${osgp/Global/config}", ignoreResourceNotFound = true)
@PropertySource(value = "file:${osgp/AdapterWsDistributionAutomation/config}", ignoreResourceNotFound = true)
public class SchedulingConfig extends AbstractSchedulingConfig {

    private static final String KEY_CLEANUP_JOB_CRON_EXPRESSION = "scheduling.job.cleanup.cron.expression";
    private static final String KEY_CLEANUP_JOB_THREAD_COUNT = "scheduling.job.cleanup.thread.count";

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

    @Value("${scheduling.cleanupjob.retention.time.in.days}")
    private int cleanupJobRetentionTimeInDays;

    @Bean
    public int cleanupJobRetentionTimeInDays() {
        return this.cleanupJobRetentionTimeInDays;
    }

    @Bean(destroyMethod = "shutdown")
    public Scheduler cleanupJobScheduler() throws SchedulerException {
        return this.constructScheduler(ResponseDataCleanupJob.class, KEY_CLEANUP_JOB_THREAD_COUNT,
                KEY_CLEANUP_JOB_CRON_EXPRESSION, this.getDatabaseUrl(), this.databaseUsername, this.databasePassword,
                this.databaseDriver);
    }

    private String getDatabaseUrl() {
        return this.databaseProtocol + this.databaseHost + ":" + this.databasePort + "/" + this.databaseName;
    }
}
