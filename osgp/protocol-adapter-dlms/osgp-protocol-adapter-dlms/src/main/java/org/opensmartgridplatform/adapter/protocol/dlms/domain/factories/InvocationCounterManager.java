/*
 * Copyright 2019 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.dlms.domain.factories;

import java.io.IOException;
import org.openmuc.jdlms.AttributeAddress;
import org.openmuc.jdlms.ObisCode;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.utils.DlmsHelper;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.repositories.DlmsDeviceRepository;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalException;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Object that manages initializing and resetting the value of the invocationCounter property of a
 * device.
 */
@Component
public class InvocationCounterManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(InvocationCounterManager.class);
  private static final AttributeAddress ATTRIBUTE_ADDRESS_INVOCATION_COUNTER_VALUE =
      new AttributeAddress(1, new ObisCode(new byte[] {0, 0, 43, 1, 0, -1}), 2);

  private final DlmsConnectionFactory connectionFactory;
  private final DlmsHelper dlmsHelper;
  private final DlmsDeviceRepository deviceRepository;

  @Autowired
  public InvocationCounterManager(
      final DlmsConnectionFactory connectionFactory,
      final DlmsHelper dlmsHelper,
      final DlmsDeviceRepository deviceRepository) {
    this.connectionFactory = connectionFactory;
    this.dlmsHelper = dlmsHelper;
    this.deviceRepository = deviceRepository;
  }

  /**
   * Updates the device instance with the invocation counter value on the actual device. Should only
   * be called for a device that actually has an invocation counter stored on the device itself.
   */
  public void initializeInvocationCounter(final DlmsDevice device) throws OsgpException {
    this.initializeWithInvocationCounterStoredOnDevice(device);
    this.deviceRepository.save(device);
  }

  private void initializeWithInvocationCounterStoredOnDevice(final DlmsDevice device)
      throws OsgpException {
    try (final DlmsConnectionManager connectionManager =
        this.connectionFactory.getPublicClientConnection(device, null)) {
      device.setInvocationCounter(this.getInvocationCounter(connectionManager));
      LOGGER.info(
          "Property invocationCounter of device {} initialized to the value of the invocation counter "
              + "stored on the device: {}",
          device.getDeviceIdentification(),
          device.getInvocationCounter());
      try {
        /*
         * Call disconnect on the connectionManager instead of depending on the try-with-resources
         * handling calling close. Calling disconnect terminates the connection more gracefully
         * allowing connections to be set up on the management client after the invocation counter
         * has been retrieved utilizing the public client.
         */
        connectionManager.disconnect();
      } catch (final IOException e) {
        LOGGER.warn(
            "Failure disconnecting from the public client connection to {}",
            device.getDeviceIdentification(),
            e);
      }
    }
  }

  private long getInvocationCounter(final DlmsConnectionManager connectionManager)
      throws FunctionalException {
    final Number invocationCounter =
        this.dlmsHelper
            .getAttributeValue(connectionManager, ATTRIBUTE_ADDRESS_INVOCATION_COUNTER_VALUE)
            .getValue();
    return invocationCounter.longValue();
  }

  public void resetInvocationCounter(final DlmsDevice device) {
    device.setInvocationCounter(null);
    this.deviceRepository.save(device);
  }
}
