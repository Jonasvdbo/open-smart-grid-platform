@DistributionAutomation @Platform @MediumVoltageMessage @Skip
Feature: DistributionAutomation Medium voltage message processing

  Scenario: Process a medium voltage message from MQTT device
    Given a location
      | substation identification | sub-1        |
      | substation name           | substation-1 |
    And a feeder
      | substation identification | sub-1   |
      | feeder number             |     200 |
      | feeder name               | fdr-200 |
    When MQTT device "TST-01" sends a measurement report
      | MqttTopic | TST-01/measurement                                                                                                                                                                                                                                                                                         |
      | payload   | [{"gisnr":"sub-1", "versie":"2", "feeder":"200", "D": "02/10/2020 16:03:38", "uts":"1601647418", "data": [20000,20000,20000,0.4,0.5,0.6,0.7,0.8,0.9,1.0,1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,2.0,2.1,2.2,2.3,2.4,2.5,2.6,2.7,2.8,2.9,3.0,3.1,3.2,3.3,3.4,3.5,3.6,3.7,3.8,3.9,4.0,4.1,5,6,7,8,9,0,1,2,3,4]}] |
    Then a MEDIUM_VOLTAGE message is published to Kafka
      | substation identification    | sub-1        |
      | version                      |            2 |
      | substation name              | substation-1 |
      | bay position                 |          200 |
      | bay identification           | fdr-200      |
      | numberOfElements             |           51 |
      | measurement1_description     | U-L1-E       |
      | measurement1_unitSymbol      | V            |
      | measurement1_value           |        20000 |
      | measurement2_description     | U-L2-E       |
      | measurement2_unitSymbol      | V            |
      | measurement2_value           |        20000 |
      | measurement3_description     | U-L3-E       |
      | measurement3_unitSymbol      | V            |
      | measurement3_value           |        20000 |
      | measurement4_description     | I-L1         |
      | measurement4_unitSymbol      | A            |
      | measurement4_value           |          0.4 |
      | measurement5_description     | I-L2         |
      | measurement5_unitSymbol      | A            |
      | measurement5_value           |          0.5 |
      | measurement6_description     | I-L3         |
      | measurement6_unitSymbol      | A            |
      | measurement6_value           |          0.6 |
      | measurement7_description     | SomP         |
      | measurement7_unitSymbol      | W            |
      | measurement7_unitMultiplier  | k            |
      | measurement7_value           |          0.7 |
      | measurement8_description     | SomQ         |
      | measurement8_unitSymbol      | VAr          |
      | measurement8_unitMultiplier  | k            |
      | measurement8_value           |          0.8 |
      | measurement9_description     | P-L1         |
      | measurement9_unitSymbol      | W            |
      | measurement9_unitMultiplier  | k            |
      | measurement9_value           |          0.9 |
      | measurement10_description    | P-L2         |
      | measurement10_unitSymbol     | W            |
      | measurement10_unitMultiplier | k            |
      | measurement10_value          |          1.0 |
      | measurement11_description    | P-L3         |
      | measurement11_unitSymbol     | W            |
      | measurement11_unitMultiplier | k            |
      | measurement11_value          |          1.1 |
      | measurement12_description    | Q-L1         |
      | measurement12_unitSymbol     | VAr          |
      | measurement12_unitMultiplier | k            |
      | measurement12_value          |          1.2 |
      | measurement13_description    | Q-L2         |
      | measurement13_unitSymbol     | VAr          |
      | measurement13_unitMultiplier | k            |
      | measurement13_value          |          1.3 |
      | measurement14_description    | Q-L3         |
      | measurement14_unitSymbol     | VAr          |
      | measurement14_unitMultiplier | k            |
      | measurement14_value          |          1.4 |
      | measurement15_description    | PF-L1        |
      | measurement15_unitSymbol     | none         |
      | measurement15_value          |          1.5 |
      | measurement16_description    | PF-L2        |
      | measurement16_unitSymbol     | none         |
      | measurement16_value          |          1.6 |
      | measurement17_description    | PF-L3        |
      | measurement17_unitSymbol     | none         |
      | measurement17_value          |          1.7 |
      | measurement18_description    | THDi-L1      |
      | measurement18_unitSymbol     | PerCent      |
      | measurement18_value          |          1.8 |
      | measurement19_description    | THDi-L2      |
      | measurement19_unitSymbol     | PerCent      |
      | measurement19_value          |          1.9 |
      | measurement20_description    | THDi-L3      |
      | measurement20_unitSymbol     | PerCent      |
      | measurement20_value          |          2.0 |
      | measurement21_description    | I1-H3        |
      | measurement21_unitSymbol     | A            |
      | measurement21_value          |          2.1 |
      | measurement22_description    | I2-H3        |
      | measurement22_unitSymbol     | A            |
      | measurement22_value          |          2.2 |
      | measurement23_description    | I3-H3        |
      | measurement23_unitSymbol     | A            |
      | measurement23_value          |          2.3 |
      | measurement24_description    | I1-H5        |
      | measurement24_unitSymbol     | A            |
      | measurement24_value          |          2.4 |
      | measurement25_description    | I2-H5        |
      | measurement25_unitSymbol     | A            |
      | measurement25_value          |          2.5 |
      | measurement26_description    | I3-H5        |
      | measurement26_unitSymbol     | A            |
      | measurement26_value          |          2.6 |
      | measurement27_description    | I1-H7        |
      | measurement27_unitSymbol     | A            |
      | measurement27_value          |          2.7 |
      | measurement28_description    | I2-H7        |
      | measurement28_unitSymbol     | A            |
      | measurement28_value          |          2.8 |
      | measurement29_description    | I3-H7        |
      | measurement29_unitSymbol     | A            |
      | measurement29_value          |          2.9 |
      | measurement30_description    | I1-H9        |
      | measurement30_unitSymbol     | A            |
      | measurement30_value          |          3.0 |
      | measurement31_description    | I2-H9        |
      | measurement31_unitSymbol     | A            |
      | measurement31_value          |          3.1 |
      | measurement32_description    | I3-H9        |
      | measurement32_unitSymbol     | A            |
      | measurement32_value          |          3.2 |
      | measurement33_description    | I1-H11       |
      | measurement33_unitSymbol     | A            |
      | measurement33_value          |          3.3 |
      | measurement34_description    | I2-H11       |
      | measurement34_unitSymbol     | A            |
      | measurement34_value          |          3.4 |
      | measurement35_description    | I3-H11       |
      | measurement35_unitSymbol     | A            |
      | measurement35_value          |          3.5 |
      | measurement36_description    | I1-H13       |
      | measurement36_unitSymbol     | A            |
      | measurement36_value          |          3.6 |
      | measurement37_description    | I2-H13       |
      | measurement37_unitSymbol     | A            |
      | measurement37_value          |          3.7 |
      | measurement38_description    | I3-H13       |
      | measurement38_unitSymbol     | A            |
      | measurement38_value          |          3.8 |
      | measurement39_description    | I1-H15       |
      | measurement39_unitSymbol     | A            |
      | measurement39_value          |          3.9 |
      | measurement40_description    | I2-H15       |
      | measurement40_unitSymbol     | A            |
      | measurement40_value          |          4.0 |
      | measurement41_description    | I3-H15       |
      | measurement41_unitSymbol     | A            |
      | measurement41_value          |          4.1 |
      | measurement42_description    | INULL        |
      | measurement42_unitSymbol     | A            |
      | measurement42_value          |            5 |
      | measurement43_description    | Pplus        |
      | measurement43_unitSymbol     | none         |
      | measurement43_value          |            6 |
      | measurement44_description    | Pmin         |
      | measurement44_unitSymbol     | none         |
      | measurement44_value          |            7 |
      | measurement45_description    | Qplus        |
      | measurement45_unitSymbol     | none         |
      | measurement45_value          |            8 |
      | measurement46_description    | Qmin         |
      | measurement46_unitSymbol     | none         |
      | measurement46_value          |            9 |
      | measurement47_description    | U-L1-E       |
      | measurement47_unitSymbol     | V            |
      | measurement47_value          |            0 |
      | measurement48_description    | U-L2-E       |
      | measurement48_unitSymbol     | V            |
      | measurement48_value          |            1 |
      | measurement49_description    | U-L3-E       |
      | measurement49_unitSymbol     | V            |
      | measurement49_value          |            2 |
      | measurement50_description    | T            |
      | measurement50_unitSymbol     | C            |
      | measurement50_value          |            3 |
      | measurement51_description    | F            |
      | measurement51_unitSymbol     | Hz           |
      | measurement51_value          |            4 |
