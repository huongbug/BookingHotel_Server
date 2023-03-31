package com.bookinghotel.mapper;

import com.bookinghotel.dto.MediaDTO;
import com.bookinghotel.dto.RoomCreateDTO;
import com.bookinghotel.dto.RoomDTO;
import com.bookinghotel.dto.RoomUpdateDTO;
import com.bookinghotel.entity.BookingRoomDetail;
import com.bookinghotel.entity.Media;
import com.bookinghotel.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

  @Mappings({
      @Mapping(target = "sale.dayStart", source = "room.sale.dayStart"),
      @Mapping(target = "sale.dayEnd", source = "room.sale.dayEnd"),
      @Mapping(target = "sale.salePercent", source = "room.sale.salePercent")
  })
  RoomDTO toRoomDTO(Room room);

  List<RoomDTO> toRoomDTOs(List<Room> rooms);

  @Mappings({
      @Mapping(target = "id", source = "bookingRoomDetail.room.id"),
      @Mapping(target = "title", source = "bookingRoomDetail.room.title"),
      @Mapping(target = "price", source = "bookingRoomDetail.price"),
      @Mapping(target = "type", source = "bookingRoomDetail.room.type"),
      @Mapping(target = "maxNum", source = "bookingRoomDetail.room.maxNum"),
      @Mapping(target = "floor", source = "bookingRoomDetail.room.floor"),
      @Mapping(target = "description", source = "bookingRoomDetail.room.description"),
      @Mapping(target = "medias", source = "medias")
  })
  RoomDTO toRoomDTO(BookingRoomDetail bookingRoomDetail, List<Media> medias);

  List<MediaDTO> toMediaDTO(List<Media> medias);

  MediaDTO toMediaDTO(Media media);

  Room createDtoToRoom(RoomCreateDTO createDTO);

  @Mapping(target = "medias", ignore = true)
  void updateRoomFromDTO(RoomUpdateDTO updateDTO, @MappingTarget Room room);

}
