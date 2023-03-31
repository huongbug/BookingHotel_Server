package com.bookinghotel.controller;

import com.bookinghotel.base.RestApiV1;
import com.bookinghotel.base.VsResponseUtil;
import com.bookinghotel.constant.RoleConstant;
import com.bookinghotel.constant.UrlConstant;
import com.bookinghotel.dto.UserUpdateDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;
import com.bookinghotel.security.AuthorizationInfo;
import com.bookinghotel.security.CurrentUserLogin;
import com.bookinghotel.security.UserPrincipal;
import com.bookinghotel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestApiV1
public class UserController {

  private final UserService userService;

  @Tag(name = "user-controller-admin")
  @Operation(summary = "API get user")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @GetMapping(UrlConstant.User.GET_USER)
  public ResponseEntity<?> getUserById(@PathVariable String userId) {
    return VsResponseUtil.ok(userService.getUserById(userId));
  }

  @Tags({@Tag(name = "user-controller-admin"), @Tag(name = "user-controller")})
  @Operation(summary = "API get current user login")
  @AuthorizationInfo(role = { RoleConstant.ADMIN, RoleConstant.USER })
  @GetMapping(UrlConstant.User.GET_CURRENT_USER)
  public ResponseEntity<?> getCurrentUser(@Parameter(name = "principal", hidden = true) @CurrentUserLogin UserPrincipal principal) {
    return VsResponseUtil.ok(userService.getCurrentUser(principal));
  }

  @Tag(name = "user-controller-admin")
  @Operation(summary = "API get all user")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @GetMapping(UrlConstant.User.GET_USERS)
  public ResponseEntity<?> getUsers(@Valid @ParameterObject PaginationSearchSortRequestDTO requestDTO) {
    return VsResponseUtil.ok(userService.getUsers(requestDTO));
  }

  @Tags({@Tag(name = "user-controller-admin"), @Tag(name = "user-controller")})
  @Operation(summary = "API update user by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN, RoleConstant.USER })
  @PatchMapping(UrlConstant.User.UPDATE_USER)
  public ResponseEntity<?> updateUserById(@PathVariable String userId, @Valid @RequestBody UserUpdateDTO userUpdateDTO,
                                          @Parameter(name = "principal", hidden = true) @CurrentUserLogin UserPrincipal principal) {
    return VsResponseUtil.ok(userService.updateUser(userUpdateDTO, userId, principal));
  }

  @Tag(name = "user-controller-admin")
  @Operation(summary = "API delete user by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @DeleteMapping(UrlConstant.User.DELETE_USER)
  public ResponseEntity<?> deleteUserById(@PathVariable String userId) {
    return VsResponseUtil.ok(userService.deleteUser(userId));
  }

  @Tags({@Tag(name = "user-controller-admin"), @Tag(name = "user-controller")})
  @Operation(summary = "API change avatar user current login")
  @AuthorizationInfo(role = { RoleConstant.ADMIN, RoleConstant.USER })
  @PatchMapping(UrlConstant.User.CHANGE_AVT_USER)
  public ResponseEntity<?> changeAvatarUser(@ModelAttribute MultipartFile avatar,
                                          @Parameter(name = "principal", hidden = true) @CurrentUserLogin UserPrincipal principal) {
    return VsResponseUtil.ok(userService.changeAvatar(avatar, principal));
  }

}
