/*
 * Copyright 2019 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.smartmetering.application.config.messaging;

import javax.jms.ConnectionFactory;
import javax.net.ssl.SSLException;
import org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.core.JmsMessageSender;
import org.opensmartgridplatform.shared.application.config.messaging.DefaultJmsConfiguration;
import org.opensmartgridplatform.shared.application.config.messaging.JmsConfigurationFactory;
import org.opensmartgridplatform.shared.application.config.messaging.JmsConfigurationNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/** Configuration class for outbound requests to OSGP Core. */
@Configuration
public class OutboundOsgpCoreRequestsMessagingConfig {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(OutboundOsgpCoreRequestsMessagingConfig.class);

  private final JmsConfigurationFactory jmsConfigurationFactory;

  public OutboundOsgpCoreRequestsMessagingConfig(
      final Environment environment, final DefaultJmsConfiguration defaultJmsConfiguration)
      throws SSLException {
    this.jmsConfigurationFactory =
        new JmsConfigurationFactory(
            environment,
            defaultJmsConfiguration,
            JmsConfigurationNames.JMS_OUTGOING_OSGP_CORE_REQUESTS);
  }

  @Bean(
      destroyMethod = "stop",
      name = "domainSmartMeteringOutboundOsgpCoreRequestsConnectionFactory")
  public ConnectionFactory connectionFactory() {
    LOGGER.info("Initializing domainSmartMeteringOutboundOsgpCoreRequestsConnectionFactory bean.");
    return this.jmsConfigurationFactory.getPooledConnectionFactory();
  }

  @Bean(name = "domainSmartMeteringOutboundOsgpCoreRequestsMessageSender")
  public JmsMessageSender domainSmartMeteringOutboundOsgpCoreRequestsMessageSender() {
    LOGGER.info("Initializing domainSmartMeteringOutboundOsgpCoreRequestsMessageSender bean.");
    return new JmsMessageSender(this.jmsConfigurationFactory.initJmsTemplate());
  }
}
