-- SPDX-FileCopyrightText: Contributors to the GXF project
--
-- SPDX-License-Identifier: Apache-2.0

DO
$$
BEGIN

IF NOT EXISTS (SELECT 1 FROM device_function_mapping WHERE "function" = 'SET_DEVICE_LIFECYCLE_STATUS_BY_CHANNEL' AND function_group = 'OWNER') THEN
	INSERT INTO device_function_mapping (function_group, "function") VALUES ('OWNER', 'SET_DEVICE_LIFECYCLE_STATUS_BY_CHANNEL');
END IF;

END;
$$
