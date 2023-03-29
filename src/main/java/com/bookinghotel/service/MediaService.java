package com.bookinghotel.service;

import com.bookinghotel.dto.RoomUpdateDTO;
import com.bookinghotel.entity.Media;
import com.bookinghotel.entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface MediaService {

  void saveMedia(Media media);

  Set<Media> getMediaByRoom(Long roomId);

  Set<Media> createMediaForRoom(Room room, List<MultipartFile> files);

  //Delete media if not found MediaDTO in RoomUpdateDTO
  void deleteMediaFromRoomUpdate(Long roomId, RoomUpdateDTO roomUpdateDTO);

}
