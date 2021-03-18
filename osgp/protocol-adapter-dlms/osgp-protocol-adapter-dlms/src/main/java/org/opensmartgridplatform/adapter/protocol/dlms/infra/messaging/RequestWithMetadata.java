/**
 * Copyright 2021 Alliander N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.dlms.infra.messaging;

import java.io.Serializable;

import lombok.Getter;
import org.opensmartgridplatform.shared.infra.jms.MessageMetadata;

@Getter
public class RequestWithMetadata<S extends Serializable> {
    private final MessageMetadata metadata;
    private final S requestObject;

    RequestWithMetadata(final MessageMetadata metadata, final S requestObject) {
        this.metadata = metadata;
        this.requestObject = requestObject;
    }
}
