// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0

package org.opensmartgridplatform.simulator.protocol.iec60870.domain.lightmeasurementdevice;

import org.openmuc.j60870.ASdu;
import org.opensmartgridplatform.simulator.protocol.iec60870.domain.Iec60870AsduGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("light_measurement_device")
public class LightMeasurementEventAsduGenerator implements Iec60870AsduGenerator {

  @Autowired private LightMeasurementDeviceAsduFactory factory;

  @Override
  public ASdu getNextAsdu() {
    return this.factory.createLightMeasurementEvent();
  }
}
