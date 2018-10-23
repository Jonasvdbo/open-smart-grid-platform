/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.cucumber.platform.smartmetering.support.ws.smartmetering.monitoring;

import static org.opensmartgridplatform.cucumber.core.ReadSettingsHelper.getInteger;

import java.util.List;
import java.util.Map;

import org.opensmartgridplatform.adapter.ws.schema.smartmetering.common.CaptureObjectDefinition;
import org.opensmartgridplatform.adapter.ws.schema.smartmetering.common.CaptureObjectDefinitions;
import org.opensmartgridplatform.adapter.ws.schema.smartmetering.common.ObisCodeValues;
import org.opensmartgridplatform.cucumber.platform.helpers.SettingsHelper;

public class CaptureObjectDefinitionsFactory {

    private final static String NUMBER_OF_SELECTED_VALUES = "NumberOfSelectedValues";
    private final static String CLASS_ID = "SelectedValue_ClassId";
    private final static String LOGICAL_NAME = "SelectedValue_LogicalName";
    private final static String ATTRIBUTE_INDEX = "SelectedValue_AttributeIndex";
    private final static String DATA_INDEX = "SelectedValue_DataIndex";

    public static CaptureObjectDefinitions fromParameterMap(final Map<String, String> requestParameters) {

        final CaptureObjectDefinitions captureObjectDefinitions = new CaptureObjectDefinitions();
        final List<CaptureObjectDefinition> selectedValues = captureObjectDefinitions.getCaptureObject();

        final int numberOfSelectedValues = getInteger(requestParameters, NUMBER_OF_SELECTED_VALUES, 0);
        for (int i = 1; i <= numberOfSelectedValues; i++) {
            final CaptureObjectDefinition captureObjectDefinition = new CaptureObjectDefinition();
            captureObjectDefinition.setClassId(SettingsHelper.getIntegerValue(requestParameters, CLASS_ID, i));
            captureObjectDefinition.setLogicalName(logicalNameFromParemeterMap(requestParameters, i));
            captureObjectDefinition
                    .setAttributeIndex(SettingsHelper.getByteValue(requestParameters, ATTRIBUTE_INDEX, i));
            captureObjectDefinition.setDataIndex(SettingsHelper.getIntegerValue(requestParameters, DATA_INDEX, i));
            selectedValues.add(captureObjectDefinition);
        }
        return captureObjectDefinitions;
    }

    private static ObisCodeValues logicalNameFromParemeterMap(final Map<String, String> requestParameters,
            final int i) {
        final String logicalName = SettingsHelper.getStringValue(requestParameters, LOGICAL_NAME, i);
        final String[] obisBytes = logicalName.split("\\.");
        final ObisCodeValues obisCodeValues = new ObisCodeValues();
        obisCodeValues.setA(Short.parseShort(obisBytes[0]));
        obisCodeValues.setB(Short.parseShort(obisBytes[1]));
        obisCodeValues.setC(Short.parseShort(obisBytes[2]));
        obisCodeValues.setD(Short.parseShort(obisBytes[3]));
        obisCodeValues.setE(Short.parseShort(obisBytes[4]));
        obisCodeValues.setF(Short.parseShort(obisBytes[5]));
        return obisCodeValues;
    }

}
