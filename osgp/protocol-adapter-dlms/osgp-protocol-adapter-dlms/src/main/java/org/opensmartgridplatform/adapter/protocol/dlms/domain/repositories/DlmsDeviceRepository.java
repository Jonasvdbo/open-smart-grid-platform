/*
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.dlms.domain.repositories;

import java.time.Instant;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DlmsDeviceRepository extends JpaRepository<DlmsDevice, Long> {

  DlmsDevice findByDeviceIdentification(String deviceIdentification);

  DlmsDevice findByMbusIdentificationNumberAndMbusManufacturerIdentification(
      String mbusIdentificationNumber, String mbusManufacturerIdentification);

  @Modifying
  @Query(
      value =
          "UPDATE DlmsDevice"
              + "   SET keyProcessingStartTime = CURRENT_TIMESTAMP"
              + " WHERE deviceIdentification = :deviceIdentification"
              + "   AND (keyProcessingStartTime IS NULL OR"
              + "        keyProcessingStartTime < :oldestStartTimeNotConsiderTimedOut)")
  int setProcessingStartTime(
      @Param("deviceIdentification") String deviceIdentification,
      @Param("oldestStartTimeNotConsiderTimedOut") Instant oldestStartTimeNotConsiderTimedOut);
}
