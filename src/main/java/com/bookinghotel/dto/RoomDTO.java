package com.bookinghotel.dto;

import com.bookinghotel.constant.RoomType;
import com.bookinghotel.dto.common.CreatedByDTO;
import com.bookinghotel.dto.common.DateAuditingDTO;
import com.bookinghotel.dto.common.LastModifiedByDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomDTO extends DateAuditingDTO {

  private Long id;

  private String title;

  private Long price;

  private RoomType type;

  private Integer maxNum;

  private Integer floor;

  private String description;

  private SaleSummaryDTO sale;

  private Boolean isAvailable;

  private CreatedByDTO createdBy;

  private LastModifiedByDTO lastModifiedBy;

  private List<MediaDTO> medias;

}
