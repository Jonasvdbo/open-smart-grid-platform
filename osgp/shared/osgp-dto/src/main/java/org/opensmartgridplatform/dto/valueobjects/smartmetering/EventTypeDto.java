/*
 * Copyright 2021 Alliander N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.dto.valueobjects.smartmetering;

public enum EventTypeDto {
  EVENTLOG_CLEARED,
  POWER_FAILURE,
  POWER_FAILURE_G,
  POWER_FAILURE_W,
  POWER_RETURNED,
  CLOCK_UPDATE,
  CLOCK_ADJUSTED_OLD_TIME,
  CLOCK_ADJUSTED_NEW_TIME,
  CLOCK_INVALID,
  REPLACE_BATTERY,
  BATTERY_VOLTAGE_LOW,
  TARIFF_ACTIVATED,
  ERROR_REGISTER_CLEARED,
  ALARM_REGISTER_CLEARED,
  HARDWARE_ERROR_PROGRAM_MEMORY,
  HARDWARE_ERROR_RAM,
  HARDWARE_ERROR_NV_MEMORY,
  WATCHDOG_ERROR,
  HARDWARE_ERROR_MEASUREMENT_SYSTEM,
  FIRMWARE_READY_FOR_ACTIVATION,
  FIRMWARE_ACTIVATED,
  PASSIVE_TARIFF_UPDATED,
  SUCCESSFUL_SELFCHECK_AFTER_FIRMWARE_UPDATE,
  COMMUNICATION_MODULE_REMOVED,
  COMMUNICATION_MODULE_INSERTED,
  TERMINAL_COVER_REMOVED,
  TERMINAL_COVER_CLOSED,
  STRONG_DC_FIELD_DETECTED,
  NO_STRONG_DC_FIELD_ANYMORE,
  METER_COVER_REMOVED,
  METER_COVER_CLOSED,
  FAILED_LOGIN_ATTEMPT,
  CONFIGURATION_CHANGE,
  MODULE_COVER_OPENED,
  MODULE_COVER_CLOSED,
  METROLOGICAL_MAINTENANCE,
  TECHNICAL_MAINTENANCE,
  RETRIEVE_METER_READINGS_E,
  RETRIEVE_METER_READINGS_G,
  RETRIEVE_INTERVAL_DATA_E,
  RETRIEVE_INTERVAL_DATA_G,
  UNDER_VOLTAGE_L1,
  UNDER_VOLTAGE_L2,
  UNDER_VOLTAGE_L3,
  PV_VOLTAGE_SAG_L1,
  PV_VOLTAGE_SAG_L2,
  PV_VOLTAGE_SAG_L3,
  PV_VOLTAGE_SWELL_L1,
  PV_VOLTAGE_SWELL_L2,
  PV_VOLTAGE_SWELL_L3,
  OVER_VOLTAGE_L1,
  OVER_VOLTAGE_L2,
  OVER_VOLTAGE_L3,
  VOLTAGE_L1_NORMAL,
  VOLTAGE_L2_NORMAL,
  VOLTAGE_L3_NORMAL,
  PHASE_OUTAGE_L1,
  PHASE_OUTAGE_L2,
  PHASE_OUTAGE_L3,
  PHASE_OUTAGE_TEST,
  PHASE_RETURNED_L1,
  PHASE_RETURNED_L2,
  PHASE_RETURNED_L3,
  COMMUNICATION_ERROR_M_BUS_CHANNEL_1,
  COMMUNICATION_OK_M_BUS_CHANNEL_1,
  REPLACE_BATTERY_M_BUS_CHANNEL_1,
  FRAUD_ATTEMPT_M_BUS_CHANNEL_1,
  CLOCK_ADJUSTED_M_BUS_CHANNEL_1,
  NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_1,
  PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_1,
  COMMUNICATION_ERROR_M_BUS_CHANNEL_2,
  COMMUNICATION_OK_M_BUS_CHANNEL_2,
  REPLACE_BATTERY_M_BUS_CHANNEL_2,
  FRAUD_ATTEMPT_M_BUS_CHANNEL_2,
  CLOCK_ADJUSTED_M_BUS_CHANNEL_2,
  NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_2,
  PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_2,
  COMMUNICATION_ERROR_M_BUS_CHANNEL_3,
  COMMUNICATION_OK_M_BUS_CHANNEL_3,
  REPLACE_BATTERY_M_BUS_CHANNEL_3,
  FRAUD_ATTEMPT_M_BUS_CHANNEL_3,
  CLOCK_ADJUSTED_M_BUS_CHANNEL_3,
  NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_3,
  PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_3,
  COMMUNICATION_ERROR_M_BUS_CHANNEL_4,
  COMMUNICATION_OK_M_BUS_CHANNEL_4,
  REPLACE_BATTERY_M_BUS_CHANNEL_4,
  FRAUD_ATTEMPT_M_BUS_CHANNEL_4,
  CLOCK_ADJUSTED_M_BUS_CHANNEL_4,
  NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_4,
  PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_4,
  MANUFACTURER_SPECIFIC_231,
  MANUFACTURER_SPECIFIC_232,
  MANUFACTURER_SPECIFIC_233,
  MANUFACTURER_SPECIFIC_234,
  MANUFACTURER_SPECIFIC_235,
  MANUFACTURER_SPECIFIC_236,
  MANUFACTURER_SPECIFIC_237,
  MANUFACTURER_SPECIFIC_238,
  MANUFACTURER_SPECIFIC_239,
  MANUFACTURER_SPECIFIC_240,
  MANUFACTURER_SPECIFIC_241,
  MANUFACTURER_SPECIFIC_242,
  MANUFACTURER_SPECIFIC_243,
  MANUFACTURER_SPECIFIC_244,
  MANUFACTURER_SPECIFIC_245,
  MANUFACTURER_SPECIFIC_246,
  MANUFACTURER_SPECIFIC_247,
  MANUFACTURER_SPECIFIC_248,
  MANUFACTURER_SPECIFIC_249,
  FATAL_ERROR_ISKR,
  BILLING_RESET_ISKR,
  POWER_DOWN_PHASE_L1_ISKR,
  POWER_DOWN_PHASE_L2_ISKR,
  POWER_DOWN_PHASE_L3_ISKR,
  POWER_RESTORED_PHASE_L1_ISKR,
  POWER_RESTORED_PHASE_L2_ISKR,
  POWER_RESTORED_PHASE_L3_ISKR,
  MODULE_COVER_OPENED_ISKR,
  MODULE_COVER_CLOSED_ISKR,

  /* Auxiliary logs */
  AUXILIARY_EVENTLOG_CLEARED,
  MBUS_FW_UPGRADE_SUCCESSFUL_CHANNEL_1,
  MBUS_FW_UPGRADE_BLOCK_SIZE_NOT_SUPPORTED_CHANNEL_1,
  MBUS_FW_UPGRADE_IMAGE_SIZE_TOO_BIG_CHANNEL_1,
  MBUS_FW_UPGRADE_INVALID_BLOCK_NUMBER_CHANNEL_1,
  MBUS_FW_UPGRADE_DATA_RECEIVE_ERROR_CHANNEL_1,
  MBUS_FW_UPGRADE_IMAGE_NOT_COMPLETE_ERROR_CHANNEL_1,
  MBUS_FW_UPGRADE_INVALID_SECURITY_ERROR_CHANNEL_1,
  MBUS_FW_UPGRADE_INVALID_FIRMWARE_FOR_THIS_DEVICE_CHANNEL_1,
  MBUS_FW_UPGRADE_SUCCESSFUL_CHANNEL_2,
  MBUS_FW_UPGRADE_BLOCK_SIZE_NOT_SUPPORTED_CHANNEL_2,
  MBUS_FW_UPGRADE_IMAGE_SIZE_TOO_BIG_CHANNEL_2,
  MBUS_FW_UPGRADE_INVALID_BLOCK_NUMBER_CHANNEL_2,
  MBUS_FW_UPGRADE_DATA_RECEIVE_ERROR_CHANNEL_2,
  MBUS_FW_UPGRADE_IMAGE_NOT_COMPLETE_ERROR_CHANNEL_2,
  MBUS_FW_UPGRADE_INVALID_SECURITY_ERROR_CHANNEL_2,
  MBUS_FW_UPGRADE_INVALID_FIRMWARE_FOR_THIS_DEVICE_CHANNEL_2,
  MBUS_FW_UPGRADE_SUCCESSFUL_CHANNEL_3,
  MBUS_FW_UPGRADE_BLOCK_SIZE_NOT_SUPPORTED_CHANNEL_3,
  MBUS_FW_UPGRADE_IMAGE_SIZE_TOO_BIG_CHANNEL_3,
  MBUS_FW_UPGRADE_INVALID_BLOCK_NUMBER_CHANNEL_3,
  MBUS_FW_UPGRADE_DATA_RECEIVE_ERROR_CHANNEL_3,
  MBUS_FW_UPGRADE_IMAGE_NOT_COMPLETE_ERROR_CHANNEL_3,
  MBUS_FW_UPGRADE_INVALID_SECURITY_ERROR_CHANNEL_3,
  MBUS_FW_UPGRADE_INVALID_FIRMWARE_FOR_THIS_DEVICE_CHANNEL_3,
  MBUS_FW_UPGRADE_SUCCESSFUL_CHANNEL_4,
  MBUS_FW_UPGRADE_BLOCK_SIZE_NOT_SUPPORTED_CHANNEL_4,
  MBUS_FW_UPGRADE_IMAGE_SIZE_TOO_BIG_CHANNEL_4,
  MBUS_FW_UPGRADE_INVALID_BLOCK_NUMBER_CHANNEL_4,
  MBUS_FW_UPGRADE_DATA_RECEIVE_ERROR_CHANNEL_4,
  MBUS_FW_UPGRADE_IMAGE_NOT_COMPLETE_ERROR_CHANNEL_4,
  MBUS_FW_UPGRADE_INVALID_SECURITY_ERROR_CHANNEL_4,
  MBUS_FW_UPGRADE_INVALID_FIRMWARE_FOR_THIS_DEVICE_CHANNEL_4,
  MBUS_STATUS_BIT_0_BATTERY_LOW_CHANNEL_1,
  MBUS_STATUS_BIT_1_BATTERY_CONSUMPTION_TOO_HIGH_CHANNEL_1,
  MBUS_STATUS_BIT_2_REVERSE_FLOW_CHANNEL_1,
  MBUS_STATUS_BIT_3_TAMPER_P2_CHANNEL_1,
  MBUS_STATUS_BIT_4_TAMPER_P0_CHANNEL_1,
  MBUS_STATUS_BIT_5_TAMPER_CASE_CHANNEL_1,
  MBUS_STATUS_BIT_6_TAMPER_MAGNETIC_CHANNEL_1,
  MBUS_STATUS_BIT_7_TEMP_OUT_OF_RANGE_CHANNEL_1,
  MBUS_STATUS_BIT_8_CLOCK_SYNC_ERROR_CHANNEL_1,
  MBUS_STATUS_BIT_9_SW_ERROR_CHANNEL_1,
  MBUS_STATUS_BIT_10_WATCHDOG_ERROR_CHANNEL_1,
  MBUS_STATUS_BIT_11_SYSTEM_HW_ERROR_CHANNEL_1,
  MBUS_STATUS_BIT_12_CFG_CALIBRATION_ERROR_CHANNEL_1,
  MBUS_STATUS_BIT_13_HIGH_FLOW_GREATER_THAN_QMAX_CHANNEL_1,
  MBUS_STATUS_BIT_14_TEMP_SENSOR_ERROR_CHANNEL_1,
  MBUS_STATUS_BIT_15_RESERVED_CHANNEL_1,
  MBUS_STATUS_BIT_16_P0_ENABLED_CHANNEL_1,
  MBUS_STATUS_BIT_17_NEW_KEY_ACCEPTED_CHANNEL_1,
  MBUS_STATUS_BIT_18_NEW_KEY_REJECTED_CHANNEL_1,
  MBUS_STATUS_BIT_18_RESERVED_CHANNEL_1,
  MBUS_STATUS_BIT_20_MANUFACTURER_SPECIFIC_CHANNEL_1,
  MBUS_STATUS_BIT_21_MANUFACTURER_SPECIFIC_CHANNEL_1,
  MBUS_STATUS_BIT_22_MANUFACTURER_SPECIFIC_CHANNEL_1,
  MBUS_STATUS_BIT_23_MANUFACTURER_SPECIFIC_CHANNEL_1,
  MBUS_STATUS_BIT_24_MANUFACTURER_SPECIFIC_CHANNEL_1,
  MBUS_STATUS_BIT_25_MANUFACTURER_SPECIFIC_CHANNEL_1,
  MBUS_STATUS_BIT_26_MANUFACTURER_SPECIFIC_CHANNEL_1,
  MBUS_STATUS_BIT_27_MANUFACTURER_SPECIFIC_CHANNEL_1,
  MBUS_STATUS_BIT_28_MANUFACTURER_SPECIFIC_CHANNEL_1,
  MBUS_STATUS_BIT_29_MANUFACTURER_SPECIFIC_CHANNEL_1,
  MBUS_STATUS_BIT_30_MANUFACTURER_SPECIFIC_CHANNEL_1,
  MBUS_STATUS_BIT_31_MANUFACTURER_SPECIFIC_CHANNEL_1,
  KEY_SENT_TO_MBUS_DEVICE_ON_CHANNEL_1,
  KEY_ACKNOWLEDGED_BY_MBUS_DEVICE_ON_CHANNEL_1,
  MBUS_STATUS_BIT_0_BATTERY_LOW_CHANNEL_2,
  MBUS_STATUS_BIT_1_BATTERY_CONSUMPTION_TOO_HIGH_CHANNEL_2,
  MBUS_STATUS_BIT_2_REVERSE_FLOW_CHANNEL_2,
  MBUS_STATUS_BIT_3_TAMPER_P2_CHANNEL_2,
  MBUS_STATUS_BIT_4_TAMPER_P0_CHANNEL_2,
  MBUS_STATUS_BIT_5_TAMPER_CASE_CHANNEL_2,
  MBUS_STATUS_BIT_6_TAMPER_MAGNETIC_CHANNEL_2,
  MBUS_STATUS_BIT_7_TEMP_OUT_OF_RANGE_CHANNEL_2,
  MBUS_STATUS_BIT_8_CLOCK_SYNC_ERROR_CHANNEL_2,
  MBUS_STATUS_BIT_9_SW_ERROR_CHANNEL_2,
  MBUS_STATUS_BIT_10_WATCHDOG_ERROR_CHANNEL_2,
  MBUS_STATUS_BIT_11_SYSTEM_HW_ERROR_CHANNEL_2,
  MBUS_STATUS_BIT_12_CFG_CALIBRATION_ERROR_CHANNEL_2,
  MBUS_STATUS_BIT_13_HIGH_FLOW_GREATER_THAN_QMAX_CHANNEL_2,
  MBUS_STATUS_BIT_14_TEMP_SENSOR_ERROR_CHANNEL_2,
  MBUS_STATUS_BIT_15_RESERVED_CHANNEL_2,
  MBUS_STATUS_BIT_16_P0_ENABLED_CHANNEL_2,
  MBUS_STATUS_BIT_17_NEW_KEY_ACCEPTED_CHANNEL_2,
  MBUS_STATUS_BIT_18_NEW_KEY_REJECTED_CHANNEL_2,
  MBUS_STATUS_BIT_18_RESERVED_CHANNEL_2,
  MBUS_STATUS_BIT_20_MANUFACTURER_SPECIFIC_CHANNEL_2,
  MBUS_STATUS_BIT_21_MANUFACTURER_SPECIFIC_CHANNEL_2,
  MBUS_STATUS_BIT_22_MANUFACTURER_SPECIFIC_CHANNEL_2,
  MBUS_STATUS_BIT_23_MANUFACTURER_SPECIFIC_CHANNEL_2,
  MBUS_STATUS_BIT_24_MANUFACTURER_SPECIFIC_CHANNEL_2,
  MBUS_STATUS_BIT_25_MANUFACTURER_SPECIFIC_CHANNEL_2,
  MBUS_STATUS_BIT_26_MANUFACTURER_SPECIFIC_CHANNEL_2,
  MBUS_STATUS_BIT_27_MANUFACTURER_SPECIFIC_CHANNEL_2,
  MBUS_STATUS_BIT_28_MANUFACTURER_SPECIFIC_CHANNEL_2,
  MBUS_STATUS_BIT_29_MANUFACTURER_SPECIFIC_CHANNEL_2,
  MBUS_STATUS_BIT_30_MANUFACTURER_SPECIFIC_CHANNEL_2,
  MBUS_STATUS_BIT_31_MANUFACTURER_SPECIFIC_CHANNEL_2,
  KEY_SENT_TO_MBUS_DEVICE_ON_CHANNEL_2,
  KEY_ACKNOWLEDGED_BY_MBUS_DEVICE_ON_CHANNEL_2,
  MBUS_STATUS_BIT_0_BATTERY_LOW_CHANNEL_3,
  MBUS_STATUS_BIT_1_BATTERY_CONSUMPTION_TOO_HIGH_CHANNEL_3,
  MBUS_STATUS_BIT_2_REVERSE_FLOW_CHANNEL_3,
  MBUS_STATUS_BIT_3_TAMPER_P2_CHANNEL_3,
  MBUS_STATUS_BIT_4_TAMPER_P0_CHANNEL_3,
  MBUS_STATUS_BIT_5_TAMPER_CASE_CHANNEL_3,
  MBUS_STATUS_BIT_6_TAMPER_MAGNETIC_CHANNEL_3,
  MBUS_STATUS_BIT_7_TEMP_OUT_OF_RANGE_CHANNEL_3,
  MBUS_STATUS_BIT_8_CLOCK_SYNC_ERROR_CHANNEL_3,
  MBUS_STATUS_BIT_9_SW_ERROR_CHANNEL_3,
  MBUS_STATUS_BIT_10_WATCHDOG_ERROR_CHANNEL_3,
  MBUS_STATUS_BIT_11_SYSTEM_HW_ERROR_CHANNEL_3,
  MBUS_STATUS_BIT_12_CFG_CALIBRATION_ERROR_CHANNEL_3,
  MBUS_STATUS_BIT_13_HIGH_FLOW_GREATER_THAN_QMAX_CHANNEL_3,
  MBUS_STATUS_BIT_14_TEMP_SENSOR_ERROR_CHANNEL_3,
  MBUS_STATUS_BIT_15_RESERVED_CHANNEL_3,
  MBUS_STATUS_BIT_16_P0_ENABLED_CHANNEL_3,
  MBUS_STATUS_BIT_17_NEW_KEY_ACCEPTED_CHANNEL_3,
  MBUS_STATUS_BIT_18_NEW_KEY_REJECTED_CHANNEL_3,
  MBUS_STATUS_BIT_18_RESERVED_CHANNEL_3,
  MBUS_STATUS_BIT_20_MANUFACTURER_SPECIFIC_CHANNEL_3,
  MBUS_STATUS_BIT_21_MANUFACTURER_SPECIFIC_CHANNEL_3,
  MBUS_STATUS_BIT_22_MANUFACTURER_SPECIFIC_CHANNEL_3,
  MBUS_STATUS_BIT_23_MANUFACTURER_SPECIFIC_CHANNEL_3,
  MBUS_STATUS_BIT_24_MANUFACTURER_SPECIFIC_CHANNEL_3,
  MBUS_STATUS_BIT_25_MANUFACTURER_SPECIFIC_CHANNEL_3,
  MBUS_STATUS_BIT_26_MANUFACTURER_SPECIFIC_CHANNEL_3,
  MBUS_STATUS_BIT_27_MANUFACTURER_SPECIFIC_CHANNEL_3,
  MBUS_STATUS_BIT_28_MANUFACTURER_SPECIFIC_CHANNEL_3,
  MBUS_STATUS_BIT_29_MANUFACTURER_SPECIFIC_CHANNEL_3,
  MBUS_STATUS_BIT_30_MANUFACTURER_SPECIFIC_CHANNEL_3,
  MBUS_STATUS_BIT_31_MANUFACTURER_SPECIFIC_CHANNEL_3,
  KEY_SENT_TO_MBUS_DEVICE_ON_CHANNEL_3,
  KEY_ACKNOWLEDGED_BY_MBUS_DEVICE_ON_CHANNEL_3,
  MBUS_STATUS_BIT_0_BATTERY_LOW_CHANNEL_4,
  MBUS_STATUS_BIT_1_BATTERY_CONSUMPTION_TOO_HIGH_CHANNEL_4,
  MBUS_STATUS_BIT_2_REVERSE_FLOW_CHANNEL_4,
  MBUS_STATUS_BIT_3_TAMPER_P2_CHANNEL_4,
  MBUS_STATUS_BIT_4_TAMPER_P0_CHANNEL_4,
  MBUS_STATUS_BIT_5_TAMPER_CASE_CHANNEL_4,
  MBUS_STATUS_BIT_6_TAMPER_MAGNETIC_CHANNEL_4,
  MBUS_STATUS_BIT_7_TEMP_OUT_OF_RANGE_CHANNEL_4,
  MBUS_STATUS_BIT_8_CLOCK_SYNC_ERROR_CHANNEL_4,
  MBUS_STATUS_BIT_9_SW_ERROR_CHANNEL_4,
  MBUS_STATUS_BIT_10_WATCHDOG_ERROR_CHANNEL_4,
  MBUS_STATUS_BIT_11_SYSTEM_HW_ERROR_CHANNEL_4,
  MBUS_STATUS_BIT_12_CFG_CALIBRATION_ERROR_CHANNEL_4,
  MBUS_STATUS_BIT_13_HIGH_FLOW_GREATER_THAN_QMAX_CHANNEL_4,
  MBUS_STATUS_BIT_14_TEMP_SENSOR_ERROR_CHANNEL_4,
  MBUS_STATUS_BIT_15_RESERVED_CHANNEL_4,
  MBUS_STATUS_BIT_16_P0_ENABLED_CHANNEL_4,
  MBUS_STATUS_BIT_17_NEW_KEY_ACCEPTED_CHANNEL_4,
  MBUS_STATUS_BIT_18_NEW_KEY_REJECTED_CHANNEL_4,
  MBUS_STATUS_BIT_18_RESERVED_CHANNEL_4,
  MBUS_STATUS_BIT_20_MANUFACTURER_SPECIFIC_CHANNEL_4,
  MBUS_STATUS_BIT_21_MANUFACTURER_SPECIFIC_CHANNEL_4,
  MBUS_STATUS_BIT_22_MANUFACTURER_SPECIFIC_CHANNEL_4,
  MBUS_STATUS_BIT_23_MANUFACTURER_SPECIFIC_CHANNEL_4,
  MBUS_STATUS_BIT_24_MANUFACTURER_SPECIFIC_CHANNEL_4,
  MBUS_STATUS_BIT_25_MANUFACTURER_SPECIFIC_CHANNEL_4,
  MBUS_STATUS_BIT_26_MANUFACTURER_SPECIFIC_CHANNEL_4,
  MBUS_STATUS_BIT_27_MANUFACTURER_SPECIFIC_CHANNEL_4,
  MBUS_STATUS_BIT_28_MANUFACTURER_SPECIFIC_CHANNEL_4,
  MBUS_STATUS_BIT_29_MANUFACTURER_SPECIFIC_CHANNEL_4,
  MBUS_STATUS_BIT_30_MANUFACTURER_SPECIFIC_CHANNEL_4,
  MBUS_STATUS_BIT_31_MANUFACTURER_SPECIFIC_CHANNEL_4,
  KEY_SENT_TO_MBUS_DEVICE_ON_CHANNEL_4,
  KEY_ACKNOWLEDGED_BY_MBUS_DEVICE_ON_CHANNEL_4,

  /* Auxiliary logs - Manufacturer specific events channel 1 */
  MBUS_STATUS_BIT_20_POWERFAIL_CHANNEL_1_FLO,
  MBUS_STATUS_BIT_20_BATTERY_CHANGED_CHANNEL_1_LAGY,
  MBUS_STATUS_BIT_21_MAX_FLOW_ERROR_CHANNEL_1_FLO,
  MBUS_STATUS_BIT_21_CLOCK_INVALID_CHANNEL_1_LAGY,
  MBUS_STATUS_BIT_21_BOTTOM_TAMPER_CHANNEL_1_GWI,
  MBUS_STATUS_BIT_22_MIN_TEMPERATURE_ALARM_CHANNEL_1_FLO,
  MBUS_STATUS_BIT_22_MEASUREMENT_DATA_CORRUPTION_ERROR_CHANNEL_1_LAGY,
  MBUS_STATUS_BIT_22_MID_TAMPER_CHANNEL_1_GWI,
  MBUS_STATUS_BIT_23_HIGH_TEMPERATUR_ALARM_CHANNEL_1_FLO,
  MBUS_STATUS_BIT_23_SENSOR_DEGRADATION_ERROR_CHANNEL_1_LAGY,
  MBUS_STATUS_BIT_23_BATTERY_COMPLETELY_EMPTY_ALARM_CHANNEL_1_GWI,
  MBUS_STATUS_BIT_24_PULSE_ERROR_CHANNEL_1_FLO,
  MBUS_STATUS_BIT_24_DISPLAY_ERROR_CHANNEL_1_LAGY,
  MBUS_STATUS_BIT_24_BATTERY_REMOVED_ALARM_CHANNEL_1_GWI,
  MBUS_STATUS_BIT_25_CONSUMPTION_ERROR_CHANNEL_1_FLO,
  MBUS_STATUS_BIT_25_USAGE_P0_CHANNEL_1_LAGY,
  MBUS_STATUS_BIT_25_INTERNAL_INDEX_MBUS_COMMUNICATION_ERROR_CHANNEL_1_GWI,
  MBUS_STATUS_BIT_26_USAGE_TESTMODE_CHANNEL_1_LAGY,
  MBUS_STATUS_BIT_26_RESET_REQUESTED_CHANNEL_1_GWI,
  MBUS_STATUS_BIT_27_SETTING_WAS_RESET_TO_DEFAULT_CHANNEL_1_GWI,
  MBUS_STATUS_BIT_30_MEASUREMENT_DATA_CORRUPTION_ERROR_CHANNEL_1_FLO,

  /* Auxiliary logs - Manufacturer specific events channel 2 */
  MBUS_STATUS_BIT_20_POWERFAIL_CHANNEL_2_FLO,
  MBUS_STATUS_BIT_20_BATTERY_CHANGED_CHANNEL_2_LAGY,
  MBUS_STATUS_BIT_21_MAX_FLOW_ERROR_CHANNEL_2_FLO,
  MBUS_STATUS_BIT_21_CLOCK_INVALID_CHANNEL_2_LAGY,
  MBUS_STATUS_BIT_21_BOTTOM_TAMPER_CHANNEL_2_GWI,
  MBUS_STATUS_BIT_22_MIN_TEMPERATURE_ALARM_CHANNEL_2_FLO,
  MBUS_STATUS_BIT_22_MEASUREMENT_DATA_CORRUPTION_ERROR_CHANNEL_2_LAGY,
  MBUS_STATUS_BIT_22_MID_TAMPER_CHANNEL_2_GWI,
  MBUS_STATUS_BIT_23_HIGH_TEMPERATUR_ALARM_CHANNEL_2_FLO,
  MBUS_STATUS_BIT_23_SENSOR_DEGRADATION_ERROR_CHANNEL_2_LAGY,
  MBUS_STATUS_BIT_23_BATTERY_COMPLETELY_EMPTY_ALARM_CHANNEL_2_GWI,
  MBUS_STATUS_BIT_24_PULSE_ERROR_CHANNEL_2_FLO,
  MBUS_STATUS_BIT_24_DISPLAY_ERROR_CHANNEL_2_LAGY,
  MBUS_STATUS_BIT_24_BATTERY_REMOVED_ALARM_CHANNEL_2_GWI,
  MBUS_STATUS_BIT_25_CONSUMPTION_ERROR_CHANNEL_2_FLO,
  MBUS_STATUS_BIT_25_USAGE_P0_CHANNEL_2_LAGY,
  MBUS_STATUS_BIT_25_INTERNAL_INDEX_MBUS_COMMUNICATION_ERROR_CHANNEL_2_GWI,
  MBUS_STATUS_BIT_26_USAGE_TESTMODE_CHANNEL_2_LAGY,
  MBUS_STATUS_BIT_26_RESET_REQUESTED_CHANNEL_2_GWI,
  MBUS_STATUS_BIT_27_SETTING_WAS_RESET_TO_DEFAULT_CHANNEL_2_GWI,
  MBUS_STATUS_BIT_30_MEASUREMENT_DATA_CORRUPTION_ERROR_CHANNEL_2_FLO,

  /* Auxiliary logs - Manufacturer specific events channel 3 */
  MBUS_STATUS_BIT_20_POWERFAIL_CHANNEL_3_FLO,
  MBUS_STATUS_BIT_20_BATTERY_CHANGED_CHANNEL_3_LAGY,
  MBUS_STATUS_BIT_21_MAX_FLOW_ERROR_CHANNEL_3_FLO,
  MBUS_STATUS_BIT_21_CLOCK_INVALID_CHANNEL_3_LAGY,
  MBUS_STATUS_BIT_21_BOTTOM_TAMPER_CHANNEL_3_GWI,
  MBUS_STATUS_BIT_22_MIN_TEMPERATURE_ALARM_CHANNEL_3_FLO,
  MBUS_STATUS_BIT_22_MEASUREMENT_DATA_CORRUPTION_ERROR_CHANNEL_3_LAGY,
  MBUS_STATUS_BIT_22_MID_TAMPER_CHANNEL_3_GWI,
  MBUS_STATUS_BIT_23_HIGH_TEMPERATUR_ALARM_CHANNEL_3_FLO,
  MBUS_STATUS_BIT_23_SENSOR_DEGRADATION_ERROR_CHANNEL_3_LAGY,
  MBUS_STATUS_BIT_23_BATTERY_COMPLETELY_EMPTY_ALARM_CHANNEL_3_GWI,
  MBUS_STATUS_BIT_24_PULSE_ERROR_CHANNEL_3_FLO,
  MBUS_STATUS_BIT_24_DISPLAY_ERROR_CHANNEL_3_LAGY,
  MBUS_STATUS_BIT_24_BATTERY_REMOVED_ALARM_CHANNEL_3_GWI,
  MBUS_STATUS_BIT_25_CONSUMPTION_ERROR_CHANNEL_3_FLO,
  MBUS_STATUS_BIT_25_USAGE_P0_CHANNEL_3_LAGY,
  MBUS_STATUS_BIT_25_INTERNAL_INDEX_MBUS_COMMUNICATION_ERROR_CHANNEL_3_GWI,
  MBUS_STATUS_BIT_26_USAGE_TESTMODE_CHANNEL_3_LAGY,
  MBUS_STATUS_BIT_26_RESET_REQUESTED_CHANNEL_3_GWI,
  MBUS_STATUS_BIT_27_SETTING_WAS_RESET_TO_DEFAULT_CHANNEL_3_GWI,
  MBUS_STATUS_BIT_30_MEASUREMENT_DATA_CORRUPTION_ERROR_CHANNEL_3_FLO,

  /* Auxiliary logs - Manufacturer specific events channel 4 */
  MBUS_STATUS_BIT_20_POWERFAIL_CHANNEL_4_FLO,
  MBUS_STATUS_BIT_20_BATTERY_CHANGED_CHANNEL_4_LAGY,
  MBUS_STATUS_BIT_21_MAX_FLOW_ERROR_CHANNEL_4_FLO,
  MBUS_STATUS_BIT_21_CLOCK_INVALID_CHANNEL_4_LAGY,
  MBUS_STATUS_BIT_21_BOTTOM_TAMPER_CHANNEL_4_GWI,
  MBUS_STATUS_BIT_22_MIN_TEMPERATURE_ALARM_CHANNEL_4_FLO,
  MBUS_STATUS_BIT_22_MEASUREMENT_DATA_CORRUPTION_ERROR_CHANNEL_4_LAGY,
  MBUS_STATUS_BIT_22_MID_TAMPER_CHANNEL_4_GWI,
  MBUS_STATUS_BIT_23_HIGH_TEMPERATUR_ALARM_CHANNEL_4_FLO,
  MBUS_STATUS_BIT_23_SENSOR_DEGRADATION_ERROR_CHANNEL_4_LAGY,
  MBUS_STATUS_BIT_23_BATTERY_COMPLETELY_EMPTY_ALARM_CHANNEL_4_GWI,
  MBUS_STATUS_BIT_24_PULSE_ERROR_CHANNEL_4_FLO,
  MBUS_STATUS_BIT_24_DISPLAY_ERROR_CHANNEL_4_LAGY,
  MBUS_STATUS_BIT_24_BATTERY_REMOVED_ALARM_CHANNEL_4_GWI,
  MBUS_STATUS_BIT_25_CONSUMPTION_ERROR_CHANNEL_4_FLO,
  MBUS_STATUS_BIT_25_USAGE_P0_CHANNEL_4_LAGY,
  MBUS_STATUS_BIT_25_INTERNAL_INDEX_MBUS_COMMUNICATION_ERROR_CHANNEL_4_GWI,
  MBUS_STATUS_BIT_26_USAGE_TESTMODE_CHANNEL_4_LAGY,
  MBUS_STATUS_BIT_26_RESET_REQUESTED_CHANNEL_4_GWI,
  MBUS_STATUS_BIT_27_SETTING_WAS_RESET_TO_DEFAULT_CHANNEL_4_GWI,
  MBUS_STATUS_BIT_30_MEASUREMENT_DATA_CORRUPTION_ERROR_CHANNEL_4_FLO
}
