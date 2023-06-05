//  SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//  SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dlms.interfaceclass.method;

import org.opensmartgridplatform.dlms.interfaceclass.InterfaceClass;

/** This class contains the methods defined for IC ImageTransfer. */
public enum ImageTransferMethod implements MethodClass {
  IMAGE_TRANSFER_INITIATE(1, true),
  IMAGE_BLOCK_TRANSFER(2, true),
  IMAGE_VERIFY(3, true),
  IMAGE_ACTIVATE(4, true);

  static final InterfaceClass INTERFACE_CLASS = InterfaceClass.IMAGE_TRANSFER;

  private final int methodId;
  private final boolean mandatory;

  private ImageTransferMethod(final int methodId, final boolean mandatory) {
    this.methodId = methodId;
    this.mandatory = mandatory;
  }

  @Override
  public int getMethodId() {
    return this.methodId;
  }

  @Override
  public InterfaceClass getInterfaceClass() {
    return INTERFACE_CLASS;
  }

  @Override
  public boolean isMandatory() {
    return this.mandatory;
  }

  @Override
  public String getMethodName() {
    return this.name();
  }
}
