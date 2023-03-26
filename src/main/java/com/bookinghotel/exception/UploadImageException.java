package com.bookinghotel.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class UploadImageException extends RuntimeException {

  private String message;

  private HttpStatus statusCode;

  public UploadImageException(String message) {
    super(message);
    this.message = message;
    this.statusCode = HttpStatus.BAD_GATEWAY;
  }

  public UploadImageException(HttpStatus httpStatus, String message) {
    super(message);
    this.message = message;
    this.statusCode = httpStatus;
  }

}
