package com.bookinghotel.service.impl;

import com.bookinghotel.constant.CommonConstant;
import com.bookinghotel.constant.CommonMessage;
import com.bookinghotel.constant.ErrorMessage;
import com.bookinghotel.constant.SortByDataConstant;
import com.bookinghotel.dto.RoomCreateDTO;
import com.bookinghotel.dto.RoomDTO;
import com.bookinghotel.dto.RoomUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;
import com.bookinghotel.dto.pagination.PagingMeta;
import com.bookinghotel.entity.Media;
import com.bookinghotel.entity.Room;
import com.bookinghotel.exception.NotFoundException;
import com.bookinghotel.mapper.RoomMapper;
import com.bookinghotel.repository.RoomRepository;
import com.bookinghotel.service.MediaService;
import com.bookinghotel.service.RoomService;
import com.bookinghotel.util.PaginationUtil;
import com.bookinghotel.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

  private final RoomRepository roomRepository;

  private final MediaService mediaService;

  private final RoomMapper roomMapper;

  private final UploadFileUtil uploadFile;

  @Override
  public RoomDTO getRoom(Long roomId) {
    Optional<Room> room = roomRepository.findById(roomId);
    checkNotFoundRoomById(room, roomId);
    room.get().setMedias(mediaService.getMediaByRoom(roomId));
    return roomMapper.toRoomDTO(room.get());
  }

  @Override
  public PaginationResponseDTO<RoomDTO> getRooms(PaginationSearchSortRequestDTO requestDTO, String filter) {
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.ROOM);
    Page<Room> rooms = roomRepository.findAllByKey(requestDTO.getKeyword(), filter, pageable);
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.ROOM, rooms);
    List<RoomDTO> roomDTOs = roomMapper.toRoomDTOs(rooms.getContent());
    return new PaginationResponseDTO<RoomDTO>(meta, roomDTOs);
  }

  @Override
  @Transactional
  public RoomDTO createRoom(RoomCreateDTO roomCreateDTO) {
    Room room = roomMapper.createDtoToRoom(roomCreateDTO);
    roomRepository.save(room);
    Set<Media> medias = mediaService.createMediaForRoom(room, roomCreateDTO.getFiles());
    room.setMedias(medias);
    return roomMapper.toRoomDTO(roomRepository.save(room));
  }

  @Override
  @Transactional
  public RoomDTO updateRoom(Long roomId, RoomUpdateDTO roomUpdateDTO) {
    Optional<Room> currentRoom = roomRepository.findById(roomId);
    checkNotFoundRoomById(currentRoom, roomId);
    //Delete media if not found in mediaDTO
    mediaService.deleteMediaFromRoomUpdate(roomId, roomUpdateDTO);
    //add file if exist
    if(roomUpdateDTO.getFiles() != null) {
      Set<Media> medias = mediaService.createMediaForRoom(currentRoom.get(), roomUpdateDTO.getFiles());
      currentRoom.get().getMedias().addAll(medias);
    }
    roomMapper.updateRoomFromDTO(roomUpdateDTO, currentRoom.get());
    currentRoom.get().setMedias(mediaService.getMediaByRoom(roomId));
    return roomMapper.toRoomDTO(roomRepository.save(currentRoom.get()));
  }

  @Override
  @Transactional
  public CommonResponseDTO deleteRoom(Long roomId) {
    Optional<Room> currentRoom = roomRepository.findById(roomId);
    checkNotFoundRoomById(currentRoom, roomId);
    currentRoom.get().setDeleteFlag(CommonConstant.TRUE);
    //set deleteFlag Media
    Set<Media> mediaDeleteFlag = mediaService.getMediaByRoom(roomId);
    if (CollectionUtils.isNotEmpty(mediaDeleteFlag)) {
      mediaDeleteFlag.forEach(item -> {
        item.setDeleteFlag(CommonConstant.TRUE);
        mediaService.saveMedia(item);
      });
    }
    roomRepository.save(currentRoom.get());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
  }

  @Override
  @Transactional
  public void deleteRoomByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords) {
    roomRepository.deleteByDeleteFlag(isDeleteFlag, daysToDeleteRecords);
  }

  private void checkNotFoundRoomById(Optional<Room> room, Long roomId) {
    if (room.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.Room.ERR_NOT_FOUND_ID, roomId));
    }
  }

}
