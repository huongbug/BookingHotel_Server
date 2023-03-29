package com.bookinghotel.service;

import com.bookinghotel.dto.RoomCreateDTO;
import com.bookinghotel.dto.RoomDTO;
import com.bookinghotel.dto.RoomFilterDTO;
import com.bookinghotel.dto.RoomUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;

public interface RoomService {

  RoomDTO getRoom(Long roomId);

  PaginationResponseDTO<RoomDTO> getRooms(PaginationSearchSortRequestDTO requestDTO, String filter);

  PaginationResponseDTO<RoomDTO> getRoomsAvailable(PaginationSearchSortRequestDTO requestDTO, RoomFilterDTO roomFilterDTO);

  RoomDTO createRoom(RoomCreateDTO roomCreateDTO);

  RoomDTO updateRoom(Long roomId, RoomUpdateDTO roomUpdateDTO);

  CommonResponseDTO deleteRoom(Long roomId);

  void deleteRoomByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords);

}
