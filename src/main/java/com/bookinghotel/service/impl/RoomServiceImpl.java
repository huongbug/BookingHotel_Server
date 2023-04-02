package com.bookinghotel.service.impl;

import com.bookinghotel.constant.CommonConstant;
import com.bookinghotel.constant.CommonMessage;
import com.bookinghotel.constant.ErrorMessage;
import com.bookinghotel.constant.SortByDataConstant;
import com.bookinghotel.dto.RoomCreateDTO;
import com.bookinghotel.dto.RoomDTO;
import com.bookinghotel.dto.RoomFilterDTO;
import com.bookinghotel.dto.RoomUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;
import com.bookinghotel.dto.pagination.PagingMeta;
import com.bookinghotel.entity.Media;
import com.bookinghotel.entity.Room;
import com.bookinghotel.entity.User;
import com.bookinghotel.exception.NotFoundException;
import com.bookinghotel.mapper.MediaMapper;
import com.bookinghotel.mapper.RoomMapper;
import com.bookinghotel.projection.RoomProjection;
import com.bookinghotel.repository.RoomRepository;
import com.bookinghotel.repository.UserRepository;
import com.bookinghotel.security.UserPrincipal;
import com.bookinghotel.service.MediaService;
import com.bookinghotel.service.RoomService;
import com.bookinghotel.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

  private final RoomRepository roomRepository;

  private final MediaService mediaService;

  private final UserRepository userRepository;

  private final RoomMapper roomMapper;

  private final MediaMapper mediaMapper;

  @Override
  public RoomDTO getRoom(Long roomId) {
    RoomProjection room = roomRepository.findRoomById(roomId);
    checkNotFoundRoomById(room, roomId);
    return toRoomDTO(room);
  }

  @Override
  public PaginationResponseDTO<RoomDTO> getRooms(PaginationSearchSortRequestDTO requestDTO, String filter) {
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.ROOM);
    Page<RoomProjection> rooms = roomRepository.findAllByKey(requestDTO.getKeyword(), filter, pageable);
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.ROOM, rooms);
    return new PaginationResponseDTO<RoomDTO>(meta, toRoomDTOs(rooms));
  }

  @Override
  public PaginationResponseDTO<RoomDTO> getRoomsAvailable(PaginationSearchSortRequestDTO requestDTO, RoomFilterDTO roomFilter) {
    String roomType = roomFilter.getRoomType() == null ? null : roomFilter.getRoomType().getValue();
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.ROOM);
    Page<RoomProjection> rooms = roomRepository.findAllAvailable(requestDTO.getKeyword(), roomFilter, roomType, pageable);
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.ROOM, rooms);
    return new PaginationResponseDTO<RoomDTO>(meta, toRoomDTOs(rooms));
  }

  @Override
  @Transactional
  public RoomDTO createRoom(RoomCreateDTO roomCreateDTO, UserPrincipal currentUser) {
    User creator = userRepository.getUser(currentUser);
    Room room = roomMapper.createDtoToRoom(roomCreateDTO);
    roomRepository.save(room);
    Set<Media> medias = mediaService.createMediaForRoom(room, roomCreateDTO.getFiles());
    room.setMedias(medias);
    return roomMapper.toRoomDTO(roomRepository.save(room), creator, creator);
  }

  @Override
  @Transactional
  public RoomDTO updateRoom(Long roomId, RoomUpdateDTO roomUpdateDTO, UserPrincipal currentUser) {
    Optional<Room> currentRoom = roomRepository.findById(roomId);
    checkNotFoundRoomById(currentRoom, roomId);
    roomMapper.updateRoomFromDTO(roomUpdateDTO, currentRoom.get());
    User updater = userRepository.getUser(currentUser);
    //Delete media if not found in mediaDTO
    Room roomUpdate = mediaService.deleteMediaFromRoomUpdate(currentRoom.get(), roomUpdateDTO);
    //add file if exist
    if(roomUpdateDTO.getFiles() != null) {
      Set<Media> medias = mediaService.createMediaForRoom(currentRoom.get(), roomUpdateDTO.getFiles());
      roomUpdate.getMedias().addAll(medias);
      roomRepository.save(roomUpdate);
    }
    User creator = userRepository.findById(roomUpdate.getCreatedBy()).get();
    return roomMapper.toRoomDTO(roomUpdate, creator, updater);
  }

  @Override
  @Transactional
  public CommonResponseDTO deleteRoom(Long roomId) {
    Optional<Room> currentRoom = roomRepository.findById(roomId);
    checkNotFoundRoomById(currentRoom, roomId);
    currentRoom.get().setDeleteFlag(CommonConstant.TRUE);
    //set deleteFlag Media
    Set<Media> mediaDeleteFlag = currentRoom.get().getMedias();
    mediaService.deleteMediaFlagFalse(mediaDeleteFlag);
    roomRepository.save(currentRoom.get());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
  }

  @Override
  @Transactional
  public void deleteRoomByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords) {
    roomRepository.deleteByDeleteFlag(isDeleteFlag, daysToDeleteRecords);
  }

  private List<RoomDTO> toRoomDTOs(Page<RoomProjection> roomProjections) {
    List<RoomDTO> roomDTOs = new LinkedList<>();
    for(RoomProjection roomProjection : roomProjections) {
      roomDTOs.add(toRoomDTO(roomProjection));
    }
    return roomDTOs;
  }

  private RoomDTO toRoomDTO(RoomProjection roomProjection) {
    RoomDTO roomDTO = roomMapper.roomProjectionToRoomDTO(roomProjection);
    List<Media> medias = mediaService.getMediaByRoom(roomDTO.getId());
    roomDTO.setMedias(mediaMapper.toMediaDTOs(medias));
    return roomDTO;
  }

  private void checkNotFoundRoomById(Optional<Room> room, Long roomId) {
    if (room.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.Room.ERR_NOT_FOUND_ID, roomId));
    }
  }

  private void checkNotFoundRoomById(RoomProjection room, Long roomId) {
    if (ObjectUtils.isEmpty(room)) {
      throw new NotFoundException(String.format(ErrorMessage.Room.ERR_NOT_FOUND_ID, roomId));
    }
  }

}
