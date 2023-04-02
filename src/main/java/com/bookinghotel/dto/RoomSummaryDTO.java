package com.bookinghotel.dto;

import com.bookinghotel.constant.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomSummaryDTO {

  private Long id;

  private String title;

  private Long price;

  private RoomType type;

  private Integer maxNum;

  private Integer floor;

  private String description;

}
