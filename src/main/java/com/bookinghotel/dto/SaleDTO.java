package com.bookinghotel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleDTO {

  private Long id;

  private LocalDateTime dayStart;

  private LocalDateTime dayEnd;

  private Integer salePercent;

}
