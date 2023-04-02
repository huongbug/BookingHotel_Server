package com.bookinghotel.mapper;

import com.bookinghotel.dto.RoomCreateDTO;
import com.bookinghotel.dto.RoomDTO;
import com.bookinghotel.dto.RoomUpdateDTO;
import com.bookinghotel.entity.Room;
import com.bookinghotel.entity.User;
import com.bookinghotel.projection.RoomProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RoomMapper {

  @Mappings({
      @Mapping(target = "id", source = "room.id"),
      @Mapping(target = "createdDate", source = "room.createdDate"),
      @Mapping(target = "lastModifiedDate", source = "room.lastModifiedDate"),
      @Mapping(target = "createdBy.id", source = "createdBy.id"),
      @Mapping(target = "createdBy.firstName", source = "createdBy.firstName"),
      @Mapping(target = "createdBy.lastName", source = "createdBy.lastName"),
      @Mapping(target = "createdBy.avatar", source = "createdBy.avatar"),
      @Mapping(target = "lastModifiedBy.id", source = "lastModifiedBy.id"),
      @Mapping(target = "lastModifiedBy.firstName", source = "lastModifiedBy.firstName"),
      @Mapping(target = "lastModifiedBy.lastName", source = "lastModifiedBy.lastName"),
      @Mapping(target = "lastModifiedBy.avatar", source = "lastModifiedBy.avatar"),
  })
  RoomDTO toRoomDTO(Room room, User createdBy, User lastModifiedBy);

  Room createDtoToRoom(RoomCreateDTO createDTO);

  @Mapping(target = "medias", ignore = true)
  void updateRoomFromDTO(RoomUpdateDTO updateDTO, @MappingTarget Room room);

  RoomDTO roomProjectionToRoomDTO(RoomProjection projection);

}
