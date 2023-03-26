package com.bookinghotel.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RoomType {

  Normal("Normal"),
  VIP("VIP");

  private String value;

}
