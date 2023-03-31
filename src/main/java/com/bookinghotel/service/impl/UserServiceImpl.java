package com.bookinghotel.service.impl;

import com.bookinghotel.constant.*;
import com.bookinghotel.dto.UserCreateDTO;
import com.bookinghotel.dto.UserDTO;
import com.bookinghotel.dto.UserUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;
import com.bookinghotel.dto.pagination.PagingMeta;
import com.bookinghotel.entity.User;
import com.bookinghotel.exception.ForbiddenException;
import com.bookinghotel.exception.NotFoundException;
import com.bookinghotel.mapper.UserMapper;
import com.bookinghotel.repository.RoleRepository;
import com.bookinghotel.repository.UserRepository;
import com.bookinghotel.security.UserPrincipal;
import com.bookinghotel.service.UserService;
import com.bookinghotel.util.PaginationUtil;
import com.bookinghotel.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final UserMapper userMapper;

  private final PasswordEncoder passwordEncoder;

  private final UploadFileUtil uploadFile;

  @Override
  public UserDTO getUserById(String userId) {
    Optional<User> user = userRepository.findById(userId);
    checkNotFoundUserById(user, userId);
    return userMapper.toUserDTO(user.get());
  }

  @Override
  public UserDTO getCurrentUser(UserPrincipal principal) {
    User user = userRepository.getUser(principal);
    return userMapper.toUserDTO(user);
  }

  @Override
  public PaginationResponseDTO<UserDTO> getUsers(PaginationSearchSortRequestDTO requestDTO) {
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.USER);
    Page<User> users = userRepository.findAllByKey(requestDTO.getKeyword(), pageable);
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.USER, users);
    List<UserDTO> userDTOs = userMapper.toUserDTOs(users.getContent());
    return new PaginationResponseDTO<UserDTO>(meta, userDTOs);
  }

  @Override
  public User createUser(UserCreateDTO userCreateDTO) {
    User user = userMapper.toUser(userCreateDTO);
    user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
    user.setRole(roleRepository.findByRoleName(RoleConstant.USER));
    if(userCreateDTO.getAvatarFile() != null) {
      user.setAvatar(uploadFile.getUrlFromFile(userCreateDTO.getAvatarFile()));
    }
    return userRepository.save(user);
  }

  @Override
  public User createAdmin(UserCreateDTO userCreateDTO) {
    User user = userMapper.toUser(userCreateDTO);
    user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
    user.setRole(roleRepository.findByRoleName(RoleConstant.ADMIN));
    if(userCreateDTO.getAvatarFile() != null) {
      user.setAvatar(uploadFile.getUrlFromFile(userCreateDTO.getAvatarFile()));
    }
    return userRepository.save(user);
  }

  @Override
  public UserDTO updateUser(UserUpdateDTO userUpdateDTO, String userId, UserPrincipal principal) {
    Optional<User> user = userRepository.findById(userId);
    checkNotFoundUserById(user, userId);

    if(!principal.getId().equals(userId)) {
      boolean isValid = false;
      for(GrantedAuthority authority : principal.getAuthorities()) {
        if(authority.getAuthority().equals(RoleConstant.ADMIN)) {
          isValid = true;
          break;
        }
      }
      if(!isValid) {
        throw new ForbiddenException(ErrorMessage.FORBIDDEN_UD);
      }
    }
    userMapper.updateUserFromDTO(userUpdateDTO, user.get());
    return userMapper.toUserDTO(userRepository.save(user.get()));
  }

  @Override
  public CommonResponseDTO lockUser(String userId) {
    return null;
  }

  @Override
  public CommonResponseDTO deleteUser(String userId) {
    Optional<User> user = userRepository.findById(userId);
    checkNotFoundUserById(user, userId);
    userRepository.delete(user.get());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
  }

  @Override
  public CommonResponseDTO changeAvatar(MultipartFile avatar, UserPrincipal principal) {
    User user = userRepository.getUser(principal);
    try {
      if(user.getAvatar() != null) {
        uploadFile.removeImageFromUrl(user.getAvatar());
      }
      user.setAvatar(uploadFile.getUrlFromFile(avatar));
      userRepository.save(user);
      return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.UPDATE_SUCCESS);
    } catch (Exception e) {
      return new CommonResponseDTO(CommonConstant.FALSE, e.getMessage());
    }
  }

  private void checkNotFoundUserById(Optional<User> user, String userId) {
    if (user.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.User.ERR_NOT_FOUND_ID, userId));
    }
  }

}
