@Common @Platform @CoreDeviceInstallation
Feature: CoreDeviceInstallation Device Updating
  As a ...
  I want to be able to perform DeviceInstallation operations on a device
  In order to ...

  Scenario: Updating a device
    Given a device
      | DeviceIdentification | TEST1024000000001 |
      | Alias                | BeforeTest        |
    When receiving an update device request
      | DeviceIdentification      | TEST1024000000001       |
      | Alias                     | AfterTest               |
      | NetworkAddress            | 127.0.0.1               |
      | internalId                |                       1 |
      | externalId                |                       2 |
      | relayType                 | LIGHT                   |
      | code                      |      100000000000000000 |
      | Index                     |                       1 |
      | LastKnownState            | false                   |
      | LastKnowSwitchingTime     | 2016-12-07T09:10:33.684 |
      | InMaintenance             | false                   |
      | TechnicalInstallationDate | 2016-12-07T09:10:33.684 |
      | UsePrefix                 | false                   |
      | Metered                   | false                   |
    Then the update device response is successful
    And the device exists
      | DeviceIdentification | TEST1024000000001 |
      | Alias                | AfterTest         |

  Scenario: Updating device data does not change GPS coordinates ( FLEX-4503 )
    Given a device
      | DeviceIdentification | TEST1024000000001 |
      | Alias                | Alias             |
      # Default values for the GPS coordinates are null.
    When receiving an update device request
      | DeviceIdentification      | TEST1024000000001       |
      | Alias                     | Alias                   |
      | NetworkAddress            | 127.0.0.1               |
      | internalId                |                       1 |
      | externalId                |                       2 |
      | relayType                 | TARIFF                  |
      | code                      |      100000000000000001 |
      | Index                     |                       1 |
      | LastKnownState            | false                   |
      | LastKnowSwitchingTime     | 2016-12-07T09:10:33.684 |
      | InMaintenance             | false                   |
      | TechnicalInstallationDate | 2016-12-07T09:10:33.684 |
      | UsePrefix                 | false                   |
      | Metered                   | false                   |
    Then the update device response is successful
    And the device exists
      | DeviceIdentification | TEST1024000000001 |
      | Alias                | Alias             |
    And the default values for the GPS coordinates remain
      | DeviceIdentification | TEST1024000000001 |

  Scenario: Updating a non existing device
    When receiving an update device request
      | DeviceIdentification      | TEST1024000000001       |
      | Alias                     | AfterTest               |
      | NetworkAddress            | 127.0.0.1               |
      | internalId                |                       1 |
      | externalId                |                       2 |
      | relayType                 | LIGHT                   |
      | code                      |      100000000000000000 |
      | Index                     |                       1 |
      | LastKnownState            | false                   |
      | LastKnowSwitchingTime     | 2016-12-07T09:10:33.684 |
      | InMaintenance             | false                   |
      | TechnicalInstallationDate | 2016-12-07T09:10:33.684 |
      | UsePrefix                 | false                   |
      | Metered                   | false                   |
    Then the update device response contains soap fault
      | Message | UNKNOWN_DEVICE |
