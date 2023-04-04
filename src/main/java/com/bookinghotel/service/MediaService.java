package com.bookinghotel.service;

import com.bookinghotel.dto.PostUpdateDTO;
import com.bookinghotel.dto.RoomUpdateDTO;
import com.bookinghotel.entity.Media;
import com.bookinghotel.entity.Post;
import com.bookinghotel.entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface MediaService {

  void saveMedia(Media media);

  List<Media> getMediaByRoom(Long roomId);

  List<Media> getMediaByRoomAndIsDeleteFlag(Long roomId);

  Set<Media> createMediaForRoom(Room room, List<MultipartFile> files);

  List<Media> getMediaByPost(Long postId);

  Set<Media> createMediaForPost(Post post, List<MultipartFile> files);

  //Delete media if not found MediaDTO in RoomUpdateDTO
  Room deleteMediaFromRoomUpdate(Room room, RoomUpdateDTO roomUpdateDTO);

  //Delete media if not found MediaDTO in PostUpdateDTO
  Post deleteMediaFromPostUpdate(Post post, PostUpdateDTO postUpdateDTO);

  void deleteMediaFlagFalse(Set<Media> mediaDeleteFlag);

}
