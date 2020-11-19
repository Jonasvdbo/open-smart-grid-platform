/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.da.application.config;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.opensmartgridplatform.adapter.domain.da.application.tasks.CommunicationMonitoringTask;
import org.opensmartgridplatform.shared.application.config.AbstractConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

/**
 * An application context Java configuration class.
 */
@Configuration
@EnableScheduling
@PropertySource("classpath:osgp-adapter-domain-distributionautomation.properties")
@PropertySource(value = "file:${osgp/Global/config}", ignoreResourceNotFound = true)
@PropertySource(value = "file:${osgp/AdapterDomainDistributionAutomation/config}", ignoreResourceNotFound = true)
public class CommunicationMonitoringConfig extends AbstractConfig {

    private static final Integer DEFAULT_AWAIT_TERMINATION_SECONDS = 10;

    private static final Boolean DEFAULT_COMMUNICATION_MONITORING_ENABLED = true;
    private static final String DEFAULT_CRON_EXPRESSION = "0 */5 * * * *";
    // Default last communication update interval in seconds
    private static final Integer DEFAULT_LAST_COMMUNICATION_UPDATE_INTERVAL = 30;
    // Default maximum time without communication in minutes
    private static final Integer DEFAULT_MAXIMUM_TIME_WITHOUT_COMMUNICATION = 15;
    private static final Integer DEFAULT_MINIMUM_TIME_BETWEEN_RUNS = 2;
    private static final Integer DEFAULT_POOL_SIZE = 1;
    private static final String DEFAULT_THREAD_NAME_PREFIX = "da-communication-monitoring-";

    private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationMonitoringConfig.class);
    private static final String PROPERTY_NAME_COMMUNICATION_MONITORING_ENABLED = "communication.monitoring.enabled";
    private static final String PROPERTY_NAME_CRON_EXPRESSION = "communication.monitoring.cron.expression";
    private static final String PROPERTY_NAME_LAST_COMMUNICATION_UPDATE_INTERVAL = "communication.monitoring.last.communication.update.interval";
    private static final String PROPERTY_NAME_MAXIMUM_TIME_WITHOUT_COMMUNICATION = "communication.monitoring.maximum.time.without.communication";

    private static final String PROPERTY_NAME_MINIMUM_TIME_BETWEEN_RUNS = "communication.monitoring.minumum.time.between.runs";

    private static final String PROPERTY_NAME_SCHEDULER_POOL_SIZE = "communication.monitoring.scheduler.pool.size";

    private static final String PROPERTY_NAME_SCHEDULER_THREAD_NAME_PREFIX = "communication.monitoring.scheduler.thread.name.prefix";

    @Resource
    private Environment environment;

    private boolean communicationMonitoringEnabled() {
        final String value = this.environment.getProperty(PROPERTY_NAME_COMMUNICATION_MONITORING_ENABLED);
        if (StringUtils.isNotBlank(value)) {
            LOGGER.info("Using value {} for communication monitoring enabled.", value);
            return Boolean.parseBoolean(value);
        } else {
            LOGGER.info("Using default value {} for communication monitoring enabled.",
                    DEFAULT_COMMUNICATION_MONITORING_ENABLED);
            return DEFAULT_COMMUNICATION_MONITORING_ENABLED;
        }
    }

    @Bean
    public CronTrigger communicationMonitoringTaskCronTrigger() {
        LOGGER.info("Initializing Cron Trigger bean with cron expression {}.", this.cronExpression());
        return new CronTrigger(this.cronExpression());
    }

    @Autowired
    @Bean(destroyMethod = "shutdown")
    public TaskScheduler communicationMonitoringTaskScheduler(
            final CommunicationMonitoringTask communicationMonitoringTask) {
        LOGGER.info("Initializing Communication Monitoring Task Scheduler bean");
        final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

        if (this.communicationMonitoringEnabled()) {
            LOGGER.info("Communicaton monitoring enabled, initializing task scheduler.");
            taskScheduler.setPoolSize(this.schedulerPoolSize());
            taskScheduler.setThreadNamePrefix(this.schedulerThreadNamePrefix());
            taskScheduler.setWaitForTasksToCompleteOnShutdown(false);
            taskScheduler.setAwaitTerminationSeconds(DEFAULT_AWAIT_TERMINATION_SECONDS);
            taskScheduler.initialize();
            taskScheduler.schedule(communicationMonitoringTask, this.communicationMonitoringTaskCronTrigger());
        } else {
            LOGGER.info("Communication Monitoring not enabled, skipping task scheduler initialization.");
        }
        return taskScheduler;
    }

    private String cronExpression() {
        final String value = this.environment.getProperty(PROPERTY_NAME_CRON_EXPRESSION);
        if (StringUtils.isNotBlank(value)) {
            LOGGER.info("Using value {} for cron expression.", value);
            return value;
        } else {
            LOGGER.info("Using default value {} for cron expression.", DEFAULT_CRON_EXPRESSION);
            return DEFAULT_CRON_EXPRESSION;
        }
    }

    @Bean
    public Integer lastCommunicationUpdateInterval() {
        LOGGER.info("Initializing Last Communication Update Interval bean.");
        final String value = this.environment.getProperty(PROPERTY_NAME_LAST_COMMUNICATION_UPDATE_INTERVAL);
        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
            LOGGER.info("Using value {} for last communication update interval.", value);
            return Integer.parseInt(value);
        } else {
            LOGGER.info("Using default value {} for last communication update interval.",
                    DEFAULT_LAST_COMMUNICATION_UPDATE_INTERVAL);
            return DEFAULT_LAST_COMMUNICATION_UPDATE_INTERVAL;
        }
    }

    @Bean
    public Integer maximumTimeWithoutCommunication() {
        LOGGER.info("Initializing Maximum Time Without Communication bean.");
        final String value = this.environment.getProperty(PROPERTY_NAME_MAXIMUM_TIME_WITHOUT_COMMUNICATION);
        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
            LOGGER.info("Using value {} for maximum time without communication.", value);
            return Integer.parseInt(value);
        } else {
            LOGGER.info("Using default value {} for maximum time without communication.",
                    DEFAULT_MAXIMUM_TIME_WITHOUT_COMMUNICATION);
            return DEFAULT_MAXIMUM_TIME_WITHOUT_COMMUNICATION;
        }
    }

    @Bean
    public Integer minimumTimeBetweenRuns() {
        LOGGER.info("Initializing Minimum Time Between Runs bean.");
        final String value = this.environment.getProperty(PROPERTY_NAME_MINIMUM_TIME_BETWEEN_RUNS);
        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
            LOGGER.info("Using value {} for minimum time between runs.", value);
            return Integer.parseInt(value);
        } else {
            LOGGER.info("Using default value {} for minimum time between runs.", DEFAULT_MINIMUM_TIME_BETWEEN_RUNS);
            return DEFAULT_MINIMUM_TIME_BETWEEN_RUNS;
        }
    }

    private Integer schedulerPoolSize() {
        final String value = this.environment.getProperty(PROPERTY_NAME_SCHEDULER_POOL_SIZE);
        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
            LOGGER.info("Using value {} for scheduler pool size.", value);
            return Integer.parseInt(value);
        } else {
            LOGGER.info("Using default value {} for scheduler pool size.", DEFAULT_POOL_SIZE);
            return DEFAULT_POOL_SIZE;
        }
    }

    private String schedulerThreadNamePrefix() {
        final String value = this.environment.getProperty(PROPERTY_NAME_SCHEDULER_THREAD_NAME_PREFIX);
        if (StringUtils.isNotBlank(value)) {
            LOGGER.info("Using value {} for scheduler thread name prefix.", value);
            return value;
        } else {
            LOGGER.info("Using default value {} for scheduler thread name prefix.", DEFAULT_THREAD_NAME_PREFIX);
            return DEFAULT_THREAD_NAME_PREFIX;
        }
    }

}
