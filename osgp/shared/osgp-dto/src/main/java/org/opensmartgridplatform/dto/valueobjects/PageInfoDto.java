/*
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.dto.valueobjects;

import java.io.Serializable;

public class PageInfoDto implements Serializable {

  /** Serial Version UID. */
  private static final long serialVersionUID = 796943881244400914L;

  private final Integer currentPage;

  private final Integer pageSize;

  private final Integer totalPages;

  private final Integer itemCount;

  public PageInfoDto(final Integer currentPage, final Integer pageSize, final Integer totalPages) {
    this.currentPage = currentPage;
    this.pageSize = pageSize;
    this.totalPages = totalPages;
    this.itemCount = -1;
  }

  public PageInfoDto(
      final Integer currentPage,
      final Integer pageSize,
      final Integer totalPages,
      final Integer itemCount) {
    this.currentPage = currentPage;
    this.pageSize = pageSize;
    this.totalPages = totalPages;
    this.itemCount = itemCount;
  }

  public Integer getCurrentPage() {
    return this.currentPage;
  }

  public Integer getPageSize() {
    return this.pageSize;
  }

  public Integer getTotalPages() {
    return this.totalPages;
  }

  public Integer getItemCount() {
    return this.itemCount;
  }
}
