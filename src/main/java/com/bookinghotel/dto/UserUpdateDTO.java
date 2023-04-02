package com.bookinghotel.dto;

import com.bookinghotel.constant.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDTO {

  @Pattern(regexp = "^\\d{9,11}$", message = ErrorMessage.INVALID_SOME_THING_FIELD)
  private String phoneNumber;

  private String firstName;

  private String lastName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate birthday;

  private String address;

}
