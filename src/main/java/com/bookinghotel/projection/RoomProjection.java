package com.bookinghotel.projection;

import com.bookinghotel.constant.RoomType;
import com.bookinghotel.dto.SaleSummaryDTO;
import com.bookinghotel.dto.common.CreatedByDTO;
import com.bookinghotel.dto.common.LastModifiedByDTO;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface RoomProjection {

  Long getId();

  String getTitle();

  Long getPrice();

  RoomType getType();

  Integer getMaxNum();

  Integer getFloor();

  String getDescription();

  LocalDateTime getCreatedDate();

  LocalDateTime getLastModifiedDate();

  @Value("#{new com.bookinghotel.dto.SaleSummaryDTO(target.saleId, target.saleDayStart, target.saleDayEnd, target.saleSalePercent)}")
  SaleSummaryDTO getSale();

  @Value("#{new com.bookinghotel.dto.common.CreatedByDTO(target.createdById, target.createdByFirstName, target.createdByLastName, target.createdByAvatar)}")
  CreatedByDTO getCreatedBy();

  @Value("#{new com.bookinghotel.dto.common.LastModifiedByDTO(target.lastModifiedById, target.lastModifiedByFirstName, target.lastModifiedByLastName, target.lastModifiedByAvatar)}")
  LastModifiedByDTO getLastModifiedBy();

}
