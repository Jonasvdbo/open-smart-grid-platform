-- SPDX-FileCopyrightText: Contributors to the GXF project
--
-- SPDX-License-Identifier: Apache-2.0

ALTER TABLE device ADD COLUMN has_public_key boolean;

UPDATE device SET has_public_key=FALSE;