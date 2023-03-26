package com.bookinghotel.dto.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class UserDateAuditingDTO extends DateAuditingDTO {

  private String createdBy;

  private String lastModifiedBy;

}
