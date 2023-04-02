package com.bookinghotel.service.impl;

import com.bookinghotel.constant.CommonConstant;
import com.bookinghotel.constant.CommonMessage;
import com.bookinghotel.constant.ErrorMessage;
import com.bookinghotel.constant.SortByDataConstant;
import com.bookinghotel.dto.PostCreateDTO;
import com.bookinghotel.dto.PostDTO;
import com.bookinghotel.dto.PostUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSortRequestDTO;
import com.bookinghotel.dto.pagination.PagingMeta;
import com.bookinghotel.entity.Media;
import com.bookinghotel.entity.Post;
import com.bookinghotel.entity.User;
import com.bookinghotel.exception.NotFoundException;
import com.bookinghotel.mapper.MediaMapper;
import com.bookinghotel.mapper.PostMapper;
import com.bookinghotel.projection.PostProjection;
import com.bookinghotel.repository.PostRepository;
import com.bookinghotel.repository.UserRepository;
import com.bookinghotel.security.UserPrincipal;
import com.bookinghotel.service.MediaService;
import com.bookinghotel.service.PostService;
import com.bookinghotel.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  private final UserRepository userRepository;

  private final MediaService mediaService;

  private final PostMapper postMapper;

  private final MediaMapper mediaMapper;

  @Override
  public PostDTO getPost(Long postId) {
    PostProjection postProjection = postRepository.findPostById(postId);
    checkNotFoundPostById(postProjection, postId);
    return toPostDTO(postProjection);
  }

  @Override
  public PaginationResponseDTO<PostDTO> getPosts(PaginationSortRequestDTO requestDTO) {
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.POST);
    Page<PostProjection> posts = postRepository.findAllPost(pageable);
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.POST, posts);
    return new PaginationResponseDTO<PostDTO>(meta, toPostDTOs(posts));
  }

  @Override
  @Transactional
  public PostDTO createPost(PostCreateDTO createDTO, UserPrincipal currentUser) {
    User creator = userRepository.getUser(currentUser);
    Post post = postMapper.createDtoToPost(createDTO);
    post.setUser(creator);
    Post postCreate = postRepository.save(post);
    if (CollectionUtils.isNotEmpty(createDTO.getFiles())) {
      Set<Media> medias = mediaService.createMediaForPost(post, createDTO.getFiles());
      postCreate.setMedias(medias);
    }
    return postMapper.toPostDTO(postCreate, creator, creator);
  }

  @Override
  @Transactional
  public PostDTO updatePost(Long postId, PostUpdateDTO updateDTO, UserPrincipal currentUser) {
    Optional<Post> currentPost = postRepository.findById(postId);
    checkNotFoundPostById(currentPost, postId);
    postMapper.updatePostFromDTO(updateDTO, currentPost.get());
    User updater = userRepository.getUser(currentUser);
    //Delete media if not found in mediaDTO
    Post postUpdate = mediaService.deleteMediaFromPostUpdate(currentPost.get(), updateDTO);
    //add file if exist
    if (updateDTO.getFiles() != null) {
      Set<Media> medias = mediaService.createMediaForPost(currentPost.get(), updateDTO.getFiles());
      postUpdate.getMedias().addAll(medias);
      postRepository.save(postUpdate);
    }
    User creator = userRepository.findById(postUpdate.getCreatedBy()).get();
    return postMapper.toPostDTO(postUpdate, creator, updater);
  }

  @Override
  @Transactional
  public CommonResponseDTO deletePost(Long postId) {
    Optional<Post> post = postRepository.findById(postId);
    checkNotFoundPostById(post, postId);
    post.get().setDeleteFlag(CommonConstant.TRUE);
    //set deleteFlag Media
    Set<Media> mediaDeleteFlag = post.get().getMedias();
    mediaService.deleteMediaFlagFalse(mediaDeleteFlag);
    postRepository.save(post.get());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
  }

  private List<PostDTO> toPostDTOs(Page<PostProjection> postProjections) {
    List<PostDTO> postDTOs = new LinkedList<>();
    for(PostProjection postProjection : postProjections) {
      postDTOs.add(toPostDTO(postProjection));
    }
    return postDTOs;
  }

  private PostDTO toPostDTO(PostProjection roomProjection) {
    PostDTO postDTO = postMapper.postProjectionToPostDTO(roomProjection);
    List<Media> medias = mediaService.getMediaByPost(postDTO.getId());
    postDTO.setMedias(mediaMapper.toMediaDTOs(medias));
    return postDTO;
  }

  @Override
  @Transactional
  public void deletePostByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords) {
    postRepository.deleteByDeleteFlag(isDeleteFlag, daysToDeleteRecords);
  }

  private void checkNotFoundPostById(Optional<Post> post, Long postId) {
    if (post.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.Post.ERR_NOT_FOUND_ID, postId));
    }
  }

  private void checkNotFoundPostById(PostProjection projection, Long postId) {
    if (ObjectUtils.isEmpty(projection)) {
      throw new NotFoundException(String.format(ErrorMessage.Post.ERR_NOT_FOUND_ID, postId));
    }
  }

}
