package com.bookinghotel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RevenueDTO {

  private Integer year;
  private Integer totalBooking;
  private Long totalRevenue;
  private List<RevenueMonthDTO> revenueMonths;

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class RevenueMonthDTO {
    private String month;
    private Integer totalBooking;
    private Long totalRevenue;
  }

}
