package com.bookinghotel.projection;

import java.time.LocalDateTime;

public interface StatisticRoomBookedProjection {

  Long getId();

  String getTitle();

  Long getPrice();

  String getType();

  Integer getMaxNum();

  Integer getFloor();

  String getDescription();

  LocalDateTime getCreatedDate();

  LocalDateTime getLastModifiedDate();

  Integer getValue();

}
