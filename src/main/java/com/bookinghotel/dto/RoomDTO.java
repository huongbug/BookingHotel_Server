package com.bookinghotel.dto;

import com.bookinghotel.constant.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomDTO {

  private Long id;

  private String title;

  private Long price;

  private RoomType type;

  private Integer maxNum;

  private Integer floor;

  private String description;

  private List<MediaDTO> medias;

}
