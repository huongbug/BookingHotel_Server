package com.bookinghotel.dto;

import com.bookinghotel.annotation.ValidFiles;
import com.bookinghotel.constant.ErrorMessage;
import com.bookinghotel.constant.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomCreateDTO {

  @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
  private String title;

  @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
  private Long price;

  @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
  private RoomType type;

  @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
  private Integer maxNum;

  @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
  private Integer floor;

  @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
  private String description;

  @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
  @ValidFiles
  private List<MultipartFile> files;

}
