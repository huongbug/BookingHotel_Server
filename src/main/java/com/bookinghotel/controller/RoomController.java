package com.bookinghotel.controller;

import com.bookinghotel.base.RestApiV1;
import com.bookinghotel.base.VsResponseUtil;
import com.bookinghotel.constant.RoleConstant;
import com.bookinghotel.constant.RoomType;
import com.bookinghotel.constant.UrlConstant;
import com.bookinghotel.dto.RoomCreateDTO;
import com.bookinghotel.dto.RoomUpdateDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;
import com.bookinghotel.security.AuthorizationInfo;
import com.bookinghotel.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestApiV1
public class RoomController {

  private final RoomService roomService;

  @Operation(summary = "API get room by id")
  @GetMapping(UrlConstant.Room.GET_ROOM)
  public ResponseEntity<?> getRoomById(@PathVariable Long roomId) {
    return VsResponseUtil.ok(roomService.getRoom(roomId));
  }

  @Tag(name = "room-controller-admin")
  @Operation(summary = "API get all room")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @GetMapping(UrlConstant.Room.GET_ROOMS)
  public ResponseEntity<?> getRooms(@Valid @ParameterObject PaginationSearchSortRequestDTO pagination,
                                    @RequestParam(required = false) RoomType filter) {
    return VsResponseUtil.ok(roomService.getRooms(pagination, filter == null ? null : filter.getValue()));
  }

  @Tag(name = "room-controller-admin")
  @Operation(summary = "API create room")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @PostMapping(value = UrlConstant.Room.CREATE_ROOM, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createRoom(@Valid @ModelAttribute RoomCreateDTO roomCreateDTO) {
    return VsResponseUtil.ok(roomService.createRoom(roomCreateDTO));
  }

  @Tag(name = "room-controller-admin")
  @Operation(summary = "API update room by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @PutMapping(value = UrlConstant.Room.UPDATE_ROOM, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updateRoomById(@PathVariable Long roomId, @Valid @ModelAttribute RoomUpdateDTO roomUpdateDTO) {
    return VsResponseUtil.ok(roomService.updateRoom(roomId, roomUpdateDTO));
  }

  @Tag(name = "room-controller-admin")
  @Operation(summary = "API delete room by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @DeleteMapping(UrlConstant.Room.DELETE_ROOM)
  public ResponseEntity<?> deleteRoomById(@PathVariable Long roomId) {
    return VsResponseUtil.ok(roomService.deleteRoom(roomId));
  }

}
