/*
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.simulator.protocol.dlms.cosem;

import org.openmuc.jdlms.CosemAttribute;
import org.openmuc.jdlms.CosemClass;
import org.openmuc.jdlms.CosemInterfaceObject;
import org.openmuc.jdlms.datatypes.DataObject;
import org.openmuc.jdlms.datatypes.DataObject.Type;
import org.opensmartgridplatform.dlms.interfaceclass.attribute.DataAttribute;
import org.opensmartgridplatform.simulator.protocol.dlms.util.DynamicValues;
import org.springframework.beans.factory.annotation.Autowired;

@CosemClass(id = 1)
public class AlarmObject extends CosemInterfaceObject {
  public static final String ALARM_OBJECT_1 = "0.0.97.98.0.255";
  public static final String ALARM_OBJECT_2 = "0.0.97.98.1.255";

  private static final int ATTRIBUTE_ID_VALUE = DataAttribute.VALUE.attributeId();

  @Autowired private DynamicValues dynamicValues;

  @CosemAttribute(id = 2, type = Type.DOUBLE_LONG_UNSIGNED)
  private DataObject value;

  public AlarmObject(final String instanceId) {
    super(instanceId);
  }

  public DataObject getValue() {
    return this.dynamicValues.getDlmsAttributeValue(this, ATTRIBUTE_ID_VALUE);
  }

  public void setValue(final DataObject attributeValue) {
    this.dynamicValues.setDlmsAttributeValue(this, ATTRIBUTE_ID_VALUE, attributeValue);
  }
}
