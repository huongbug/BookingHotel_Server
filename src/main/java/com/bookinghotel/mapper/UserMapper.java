package com.bookinghotel.mapper;

import com.bookinghotel.dto.UserCreateDTO;
import com.bookinghotel.dto.UserDTO;
import com.bookinghotel.dto.UserUpdateDTO;
import com.bookinghotel.dto.common.CreatedByDTO;
import com.bookinghotel.dto.common.LastModifiedByDTO;
import com.bookinghotel.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

  User toUser(UserCreateDTO userCreateDTO);

  @Mappings({
      @Mapping(target = "roleName", source = "user.role.roleName"),
  })
  UserDTO toUserDTO(User user);

  List<UserDTO> toUserDTOs(List<User> user);

  @Mapping(target = "avatar", ignore = true)
  void updateUserFromDTO(UserUpdateDTO updateDTO, @MappingTarget User user);

  CreatedByDTO toCreatedByDTO(User creator);

  LastModifiedByDTO toLastModifiedByDTO(User updater);

}
