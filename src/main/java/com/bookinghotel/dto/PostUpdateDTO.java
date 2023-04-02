package com.bookinghotel.dto;

import com.bookinghotel.annotation.ValidFiles;
import com.bookinghotel.constant.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostUpdateDTO {

  @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
  private String content;

  private List<Long> mediaIds;

  @ValidFiles
  private List<MultipartFile> files = new ArrayList<>();

}
