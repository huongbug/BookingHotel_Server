package com.bookinghotel.service;

import com.bookinghotel.dto.UserCreateDTO;
import com.bookinghotel.dto.UserDTO;
import com.bookinghotel.dto.UserUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;
import com.bookinghotel.entity.User;
import com.bookinghotel.security.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDTO getUserById(String userId);

  UserDTO getCurrentUser(UserPrincipal principal);

  PaginationResponseDTO<UserDTO> getUsers(PaginationSearchSortRequestDTO requestDTO);

  User createUser(UserCreateDTO userCreateDTO);

  User createAdmin(UserCreateDTO userCreateDTO);

  UserDTO updateUser(UserUpdateDTO userUpdateDTO, String userId, UserPrincipal principal);

  CommonResponseDTO lockUser(String userId);

  CommonResponseDTO deleteUser(String userId);

  CommonResponseDTO changeAvatar(MultipartFile avatar, UserPrincipal principal);

}
