package com.bookinghotel.service;

import com.bookinghotel.dto.RoomCreateDTO;
import com.bookinghotel.dto.RoomDTO;
import com.bookinghotel.dto.RoomFilterDTO;
import com.bookinghotel.dto.RoomUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;
import com.bookinghotel.security.UserPrincipal;

public interface RoomService {

  RoomDTO getRoom(Long roomId);

  PaginationResponseDTO<RoomDTO> getRooms(PaginationSearchSortRequestDTO requestDTO, String roomType, Boolean deleteFlag);

  PaginationResponseDTO<RoomDTO> getRoomsAvailable(PaginationSearchSortRequestDTO requestDTO, RoomFilterDTO roomFilterDTO);

  RoomDTO createRoom(RoomCreateDTO roomCreateDTO, UserPrincipal currentUser);

  RoomDTO updateRoom(Long roomId, RoomUpdateDTO roomUpdateDTO, UserPrincipal currentUser);

  CommonResponseDTO deleteRoom(Long roomId);

  void deleteRoomByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords);

}
