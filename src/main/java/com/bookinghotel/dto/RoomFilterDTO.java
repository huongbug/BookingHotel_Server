package com.bookinghotel.dto;

import com.bookinghotel.constant.CommonConstant;
import com.bookinghotel.constant.RoomType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RoomFilterDTO {

  @Parameter(description = "fromDate format yyyy-MM-dd")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE)
  @DateTimeFormat(pattern = CommonConstant.PATTERN_DATE)
  private LocalDate checkin = LocalDate.now();

  @Parameter(description = "toDate format yyyy-MM-dd")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE)
  @DateTimeFormat(pattern = CommonConstant.PATTERN_DATE)
  private LocalDate checkout = LocalDate.now().plusDays(1);

  private RoomType roomType;

  private Integer maxNum;

}
