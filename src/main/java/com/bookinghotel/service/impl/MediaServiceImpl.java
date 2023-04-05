package com.bookinghotel.service.impl;

import com.bookinghotel.constant.CommonConstant;
import com.bookinghotel.constant.ErrorMessage;
import com.bookinghotel.dto.PostUpdateDTO;
import com.bookinghotel.dto.RoomUpdateDTO;
import com.bookinghotel.entity.Media;
import com.bookinghotel.entity.Post;
import com.bookinghotel.entity.Room;
import com.bookinghotel.exception.InvalidException;
import com.bookinghotel.mapper.MediaMapper;
import com.bookinghotel.repository.MediaRepository;
import com.bookinghotel.repository.PostRepository;
import com.bookinghotel.repository.RoomRepository;
import com.bookinghotel.service.MediaService;
import com.bookinghotel.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

  private final MediaRepository mediaRepository;

  private final RoomRepository roomRepository;

  private final PostRepository postRepository;

  private final MediaMapper mediaMapper;

  private final UploadFileUtil uploadFile;

  @Override
  public void saveMedia(Media media) {
    mediaRepository.save(media);
  }

  @Override
  public List<Media> getMediaByRoom(Long roomId) {
    return mediaRepository.findByRoomId(roomId);
  }

  @Override
  public List<Media> getMediaByRoomAndIsDeleteFlag(Long roomId) {
    return mediaRepository.findByRoomIdAndIsDeleteFlag(roomId);
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
  public List<Media> getMediaByPost(Long postId) {
    return mediaRepository.findByPostId(postId);
  }

  @Override
  public List<Media> getMediaByPostAndIsDeleteFlag(Long postId) {
    return mediaRepository.findByPostIdAndIsDeleteFlag(postId);
  }

  @Override
  public Set<Media> createMediaForPost(Post post, List<MultipartFile> files) {
    Set<Media> medias = new HashSet<>();
    for (MultipartFile file : files) {
      Media media = new Media();
      media.setUrl(uploadFile.getUrlFromFile(file));
      media.setPost(post);
      mediaRepository.save(media);
      medias.add(media);
    }
    return medias;
  }

  @Override
  public Room deleteMediaFromRoomUpdate(Room room, RoomUpdateDTO roomUpdateDTO) {
    if(CollectionUtils.isNotEmpty(roomUpdateDTO.getMediaIds())) {
      List<Media> mediaDeleteFlag = mediaRepository.findByRoomIdAndNotInMedia(room.getId(), roomUpdateDTO.getMediaIds());
      if (CollectionUtils.isNotEmpty(mediaDeleteFlag)) {
        mediaDeleteFlag.forEach(item -> {
          item.setDeleteFlag(CommonConstant.TRUE);
          mediaRepository.save(item);
        });
        roomRepository.save(room);
        room.setMedias(room.getMedias().stream().filter(item -> item.getDeleteFlag() == Boolean.FALSE).collect(Collectors.toSet()));
      }
    } else {
      if(roomUpdateDTO.getFiles() == null) {
        throw new InvalidException(ErrorMessage.Room.ERR_NO_PHOTO);
      }
    }
    return room;
  }

  @Override
  public Post deleteMediaFromPostUpdate(Post post, PostUpdateDTO postUpdateDTO) {
    if (CollectionUtils.isNotEmpty(postUpdateDTO.getMediaIds())) {
      List<Media> mediaDeleteFlag = mediaRepository.findByPostIdAndNotInMedia(post.getId(), postUpdateDTO.getMediaIds());
      if (CollectionUtils.isNotEmpty(mediaDeleteFlag)) {
        mediaDeleteFlag.forEach(item -> {
          item.setDeleteFlag(CommonConstant.TRUE);
          mediaRepository.save(item);
        });
        postRepository.save(post);
        post.setMedias(post.getMedias().stream().filter(item -> item.getDeleteFlag() == Boolean.FALSE).collect(Collectors.toSet()));
      }
    } else {
      List<Media> mediaDeleteFlag = mediaRepository.findByPostId(post.getId());
      if (CollectionUtils.isNotEmpty(mediaDeleteFlag)) {
        mediaDeleteFlag.forEach(item -> {
          item.setDeleteFlag(CommonConstant.TRUE);
          mediaRepository.save(item);
        });
        postRepository.save(post);
        post.setMedias(new HashSet<>());
      }
    }
    return post;
  }

  @Override
  public void deleteMediaFlagFalse(Set<Media> mediaDeleteFlag) {
    if (CollectionUtils.isNotEmpty(mediaDeleteFlag)) {
      mediaDeleteFlag.forEach(item -> {
        item.setDeleteFlag(CommonConstant.TRUE);
        mediaRepository.save(item);
      });
    }
  }

}
