/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.entities;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.alliander.osgp.domain.core.valueobjects.RelayType;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Ssld extends Device {

    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "id")
    // protected Long id;

    //
    // @Column(nullable = false)
    // private Date creationTime = new Date();
    //
    // @Column(nullable = false)
    // private Date modificationTime = new Date();
    //
    // @Version
    // private Long version = -1L;
    //
    // public final Long getId() {
    // return this.id;
    // }
    //
    // public final Date getCreationTime() {
    // return (Date) this.creationTime.clone();
    // }
    //
    // public final Date getModificationTime() {
    // return (Date) this.modificationTime.clone();
    // }
    //
    // public final Long getVersion() {
    // return this.version;
    // }
    //
    // public void setVersion(final Long newVersion) {
    // this.version = newVersion;
    // }
    //
    // /**
    // * Method for actions to be taken before inserting.
    // */
    // @PrePersist
    // private void prePersist() {
    // final Date now = new Date();
    // this.creationTime = now;
    // this.modificationTime = now;
    // }
    //
    // /**
    // * Method for actions to be taken before updating.
    // */
    // @PreUpdate
    // private void preUpdate() {
    // this.modificationTime = new Date();
    // }

    @Column()
    private boolean hasPublicKey;

    private boolean hasSchedule;

    @OneToMany(mappedBy = "device", targetEntity = Ean.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    private final List<Ean> eans = new ArrayList<Ean>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection()
    @CollectionTable(name = "device_output_setting", joinColumns = @JoinColumn(name = "device_id"))
    private List<DeviceOutputSetting> outputSettings = new ArrayList<>();

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<RelayStatus> relayStatusses;

    public Ssld(final String deviceIdentification, final String deviceType, final InetAddress networkAddress,
            final boolean activated, final boolean hasSchedule) {
        this.deviceIdentification = deviceIdentification;
        this.deviceType = deviceType;
        this.networkAddress = networkAddress;
        this.isActivated = activated;
        this.hasSchedule = hasSchedule;
    }

    public Ssld(final String deviceIdentification, final String alias, final String containerCity,
            final String containerPostalCode, final String containerStreet, final String containerNumber,
            final String containerMunicipality, final Float latitude, final Float longitude) {
        this.deviceIdentification = deviceIdentification;
        this.alias = alias;
        this.containerCity = containerCity;
        this.containerPostalCode = containerPostalCode;
        this.containerStreet = containerStreet;
        this.containerNumber = containerNumber;
        this.containerMunicipality = containerMunicipality;
        this.gpsLatitude = latitude;
        this.gpsLongitude = longitude;
    }

    public boolean isPublicKeyPresent() {
        return this.hasPublicKey;
    }

    public void setPublicKeyPresent(final boolean isPublicKeyPresent) {
        this.hasPublicKey = isPublicKeyPresent;
    }

    public void setHasSchedule(final boolean hasSchedule) {
        this.hasSchedule = hasSchedule;
    }

    public boolean getHasSchedule() {
        return this.hasSchedule;
    }

    /**
     * Get the Ean codes for this device.
     *
     * @return List of Ean codes for this device.
     */
    public List<Ean> getEans() {
        return this.eans;
    }

    public List<DeviceOutputSetting> getOutputSettings() {
        if (this.outputSettings == null || this.outputSettings.isEmpty()) {
            return Collections.unmodifiableList(this.createDefaultConfiguration());
        }

        return Collections.unmodifiableList(this.outputSettings);
    }

    public void updateOutputSettings(final List<DeviceOutputSetting> outputSettings) {
        this.outputSettings = outputSettings;
    }

    public List<DeviceOutputSetting> receiveOutputSettings() {
        return this.outputSettings;
    }

    public List<RelayStatus> getRelayStatusses() {
        return this.relayStatusses;
    }

    /**
     * Returns the {@link RelayStatus} for the given index, or null if it
     * doesn't exist.
     */
    public RelayStatus getRelayStatusByIndex(final int index) {
        if (this.relayStatusses != null) {
            for (final RelayStatus r : this.relayStatusses) {
                if (r.getIndex() == index) {
                    return r;
                }
            }
        }
        return null;
    }

    /**
     * Updates the {@link RelayStatus} for the given index if it exists.
     */
    public void updateRelayStatusByIndex(final int index, final RelayStatus relayStatus) {

        boolean found = false;
        if (this.relayStatusses != null) {
            for (final RelayStatus r : this.relayStatusses) {
                if (r.getIndex() == index) {
                    r.updateStatus(relayStatus.isLastKnownState(), relayStatus.getLastKnowSwitchingTime());
                    found = true;
                    break;
                }
            }

            if (!found) {
                this.relayStatusses.add(relayStatus);
            }
        }
    }

    /**
     * Create default configuration for a device (based on type).
     *
     * @return default configuration
     */
    private List<DeviceOutputSetting> createDefaultConfiguration() {
        final List<DeviceOutputSetting> defaultConfiguration = new ArrayList<>();

        if (this.deviceType == null) {
            return defaultConfiguration;
        }

        if (this.deviceType.equalsIgnoreCase(SSLD_TYPE)) {
            defaultConfiguration.add(new DeviceOutputSetting(1, 1, RelayType.LIGHT, ""));
            defaultConfiguration.add(new DeviceOutputSetting(2, 2, RelayType.LIGHT, ""));
            defaultConfiguration.add(new DeviceOutputSetting(3, 3, RelayType.TARIFF, ""));

            return defaultConfiguration;
        }

        if (this.deviceType.equalsIgnoreCase(PSLD_TYPE)) {
            defaultConfiguration.add(new DeviceOutputSetting(1, 1, RelayType.LIGHT, ""));
            return defaultConfiguration;
        }

        return defaultConfiguration;
    }
}
