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
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestApiV1
public class UserController {

  private final UserService userService;

  @Operation(summary = "API get user")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @GetMapping(UrlConstant.User.GET_USER)
  public ResponseEntity<?> getUserById(@PathVariable String userId) {
    return VsResponseUtil.ok(userService.getUserById(userId));
  }

  @Operation(summary = "API get all user")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @GetMapping(UrlConstant.User.GET_USERS)
  public ResponseEntity<?> getUsers(@Valid @ParameterObject PaginationSearchSortRequestDTO requestDTO) {
    return VsResponseUtil.ok(userService.getUsers(requestDTO));
  }

  @Operation(summary = "API update user by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @PatchMapping(UrlConstant.User.UPDATE_USER)
  public ResponseEntity<?> updateUserById(@PathVariable String userId, @Valid @RequestBody UserUpdateDTO userUpdateDTO,
                                          @Parameter(name = "principal", hidden = true) @CurrentUserLogin UserPrincipal principal) {
    return VsResponseUtil.ok(userService.updateUser(userUpdateDTO, userId, principal));
  }

}
