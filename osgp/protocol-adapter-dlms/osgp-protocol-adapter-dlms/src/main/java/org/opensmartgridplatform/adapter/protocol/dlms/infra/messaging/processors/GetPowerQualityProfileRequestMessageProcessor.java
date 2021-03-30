/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.dlms.infra.messaging.processors;

import java.io.Serializable;

import org.opensmartgridplatform.adapter.protocol.dlms.application.services.MonitoringService;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.factories.DlmsConnectionManager;
import org.opensmartgridplatform.adapter.protocol.dlms.infra.messaging.DeviceRequestMessageProcessor;
import org.opensmartgridplatform.adapter.protocol.dlms.infra.messaging.RequestWithMetadata;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.GetPowerQualityProfileRequestDataDto;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.opensmartgridplatform.shared.infra.jms.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class for processing Power Quality Profile request messages.
 */
@Component
public class GetPowerQualityProfileRequestMessageProcessor
        extends DeviceRequestMessageProcessor<GetPowerQualityProfileRequestDataDto> {

    @Autowired
    private MonitoringService monitoringService;

    public GetPowerQualityProfileRequestMessageProcessor() {
        super(MessageType.GET_PROFILE_GENERIC_DATA);
    }

    @Override
    protected Serializable handleMessage(final DlmsConnectionManager conn, final DlmsDevice device,
            final RequestWithMetadata<GetPowerQualityProfileRequestDataDto> request) throws OsgpException {
        return this.monitoringService.requestPowerQualityProfile(conn, device, request.getRequestObject());
    }
}
