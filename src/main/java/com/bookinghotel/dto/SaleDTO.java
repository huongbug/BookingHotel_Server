package com.bookinghotel.dto;

import com.bookinghotel.dto.common.CreatedByDTO;
import com.bookinghotel.dto.common.DateAuditingDTO;
import com.bookinghotel.dto.common.LastModifiedByDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleDTO extends DateAuditingDTO {

  private Long id;

  private LocalDateTime dayStart;

  private LocalDateTime dayEnd;

  private Integer salePercent;

  private CreatedByDTO createdBy;

  private LastModifiedByDTO lastModifiedBy;

}
