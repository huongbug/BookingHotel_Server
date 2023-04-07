package com.bookinghotel.projection;

import com.bookinghotel.constant.RoomType;

import java.time.LocalDateTime;

public interface StatisticRoomBookedProjection {

  Long getId();

  String getTitle();

  Long getPrice();

  RoomType getType();

  Integer getMaxNum();

  Integer getFloor();

  String getDescription();

  LocalDateTime getCreatedDate();

  LocalDateTime getLastModifiedDate();

  Integer getValue();

}
