package com.bookinghotel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

  private String id;

  private String email;

  private String phoneNumber;

  private String firstName;

  private String lastName;

  private String gender;

  private LocalDate birthday;

  private String address;

  private Boolean enabled;

  private String roleName;

}
