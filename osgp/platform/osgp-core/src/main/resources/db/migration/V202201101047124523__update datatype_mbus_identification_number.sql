-- SPDX-FileCopyrightText: Contributors to the GXF project
--
-- SPDX-License-Identifier: Apache-2.0

DO
$$
BEGIN
    ALTER TABLE smart_meter ALTER COLUMN mbus_identification_number TYPE character varying(10);
END;
$$
