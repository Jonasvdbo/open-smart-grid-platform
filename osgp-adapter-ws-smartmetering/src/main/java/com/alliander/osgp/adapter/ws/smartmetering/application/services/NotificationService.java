/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.smartmetering.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alliander.osgp.adapter.ws.schema.smartmetering.notification.Notification;
import com.alliander.osgp.adapter.ws.schema.smartmetering.notification.NotificationType;
import com.alliander.osgp.adapter.ws.smartmetering.infra.ws.SendNotificationServiceClient;
import com.alliander.osgp.domain.core.entities.Organisation;
import com.alliander.osgp.domain.core.repositories.OrganisationRepository;
import com.alliander.osgp.domain.core.validation.Identification;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;

@Service(value = "wsSmartMeteringNotificationService")
@Transactional(value = "transactionManager")
@Validated
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private SendNotificationServiceClient sendNotificationServiceClient;

    @Autowired
    private OrganisationRepository organisationRepository;

    public NotificationService() {
        // Parameterless constructor required for transactions
    }

    public void sendNotification(@Identification final String organisationIdentification,
            final String deviceIdentification, final String result, final String correlationUid, final String message,
            final NotificationType notificationType) throws FunctionalException {

        LOGGER.info("sendNotification called with organisation {}", organisationIdentification);

        final Notification notification = new Notification();
        // TODO message is null, unless an error occurred
        notification.setMessage(message);
        notification.setResult(result);
        notification.setDeviceIdentification(deviceIdentification);
        notification.setCorrelationUid(correlationUid);
        notification.setNotificationType(notificationType);

        final Organisation organisation = this.organisationRepository
                .findByOrganisationIdentification(organisationIdentification);
        final String notificationURL = organisation.getNotificationURL();

        try {
            this.sendNotificationServiceClient.sendNotification(organisationIdentification, notification,
                    notificationURL);
        } catch (final Exception e) {
            LOGGER.error("Notification exception", e);
        }
    }
}