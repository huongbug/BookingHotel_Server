package com.bookinghotel.service.impl;

import com.bookinghotel.constant.CommonConstant;
import com.bookinghotel.constant.ErrorMessage;
import com.bookinghotel.dto.RoomUpdateDTO;
import com.bookinghotel.entity.Media;
import com.bookinghotel.entity.Room;
import com.bookinghotel.exception.InvalidException;
import com.bookinghotel.mapper.MediaMapper;
import com.bookinghotel.repository.MediaRepository;
import com.bookinghotel.service.MediaService;
import com.bookinghotel.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

  private final MediaRepository mediaRepository;

  private final MediaMapper mediaMapper;

  private final UploadFileUtil uploadFile;

  @Override
  public void saveMedia(Media media) {
    mediaRepository.save(media);
  }

  @Override
  public Set<Media> getMediaByRoom(Long roomId) {
    return mediaRepository.findByRoomToSet(roomId);
  }

  @Override
  public Set<Media> createMediaForRoom(Room room, List<MultipartFile> files) {
    Set<Media> medias = new HashSet<>();
    for(MultipartFile file : files) {
      Media media = new Media();
      media.setUrl(uploadFile.getUrlFromFile(file));
      media.setRoom(room);
      mediaRepository.save(media);
      medias.add(media);
    }
    return medias;
  }

  @Override
  public void deleteMediaFromRoomUpdate(Long roomId, RoomUpdateDTO roomUpdateDTO) {
    if(CollectionUtils.isNotEmpty(roomUpdateDTO.getMedias())) {
      List<Media> medias = mediaMapper.toMedias(roomUpdateDTO.getMedias());
      List<Media> mediaDeleteFlag = mediaRepository.findByRoomIdAndNotInMedia(roomId, medias);
      if (CollectionUtils.isNotEmpty(mediaDeleteFlag)) {
        mediaDeleteFlag.forEach(item -> {
          item.setDeleteFlag(CommonConstant.TRUE);
          mediaRepository.save(item);
        });
      }
    } else {
      if(roomUpdateDTO.getFiles() == null) {
        throw new InvalidException(ErrorMessage.Room.ERR_NO_PHOTO);
      }
    }
  }


}
