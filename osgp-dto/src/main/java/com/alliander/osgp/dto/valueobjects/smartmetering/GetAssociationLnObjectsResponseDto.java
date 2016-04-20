/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.dto.valueobjects.smartmetering;

public class GetAssociationLnObjectsResponseDto extends ActionResponseDto {
    private static final long serialVersionUID = 1164423597435802735L;

    private AssociationLnListTypeDto objectListType;

    public GetAssociationLnObjectsResponseDto(final AssociationLnListTypeDto objectListType) {
        this.objectListType = objectListType;
    }

    public AssociationLnListTypeDto getObjectListType() {
        return this.objectListType;
    }
}
