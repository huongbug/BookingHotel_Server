package com.bookinghotel.dto.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class FlagUserDateAuditingDTO extends UserDateAuditingDTO {

  private Boolean deleteFlag;

}
