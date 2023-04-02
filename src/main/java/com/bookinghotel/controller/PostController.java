package com.bookinghotel.controller;

import com.bookinghotel.base.RestApiV1;
import com.bookinghotel.base.VsResponseUtil;
import com.bookinghotel.constant.RoleConstant;
import com.bookinghotel.constant.UrlConstant;
import com.bookinghotel.dto.PostCreateDTO;
import com.bookinghotel.dto.PostUpdateDTO;
import com.bookinghotel.dto.pagination.PaginationSortRequestDTO;
import com.bookinghotel.security.AuthorizationInfo;
import com.bookinghotel.security.CurrentUserLogin;
import com.bookinghotel.security.UserPrincipal;
import com.bookinghotel.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestApiV1
public class PostController {

  private final PostService postService;

  @Operation(summary = "API get post by id")
  @GetMapping(UrlConstant.Post.GET_POST)
  public ResponseEntity<?> getPostById(@PathVariable Long postId) {
    return VsResponseUtil.ok(postService.getPost(postId));
  }

  @Operation(summary = "API get all post")
  @GetMapping(UrlConstant.Post.GET_POSTS)
  public ResponseEntity<?> getPosts(@Valid @ParameterObject PaginationSortRequestDTO requestDTO) {
    return VsResponseUtil.ok(postService.getPosts(requestDTO));
  }

  @Tag(name = "post-controller-admin")
  @Operation(summary = "API create post")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @PostMapping(value = UrlConstant.Post.CREATE_POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createPostById(@Valid @ModelAttribute PostCreateDTO postCreateDTO,
                                          @Parameter(name = "principal", hidden = true)
                                          @CurrentUserLogin UserPrincipal principal) {
    return VsResponseUtil.ok(postService.createPost(postCreateDTO, principal));
  }

  @Tag(name = "post-controller-admin")
  @Operation(summary = "API update post by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @PutMapping(value = UrlConstant.Post.UPDATE_POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updatePostById(@PathVariable Long postId,
                                          @Valid @ModelAttribute PostUpdateDTO postUpdateDTO,
                                          @Parameter(name = "principal", hidden = true)
                                          @CurrentUserLogin UserPrincipal principal) {
    return VsResponseUtil.ok(postService.updatePost(postId, postUpdateDTO, principal));
  }

  @Tag(name = "post-controller-admin")
  @Operation(summary = "API delete post by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @DeleteMapping(UrlConstant.Post.DELETE_POST)
  public ResponseEntity<?> deletePostById(@PathVariable Long postId) {
    return VsResponseUtil.ok(postService.deletePost(postId));
  }

}
