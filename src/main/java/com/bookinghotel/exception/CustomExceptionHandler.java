package com.bookinghotel.exception;

import com.bookinghotel.base.RestData;
import com.bookinghotel.base.VsResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

  //Error validate for param
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
    List<String> errors = new ArrayList<>();
    ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));
    Map<String, List<String>> result = new HashMap<>();
    result.put("errors", errors);
    return VsResponseUtil.error(HttpStatus.BAD_REQUEST, result);
  }

  //Error validate for body
  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> handleValidException(BindException ex, WebRequest req) {
    Map<String, String> result = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      result.put(fieldName, errorMessage);
    });
    return VsResponseUtil.error(HttpStatus.BAD_REQUEST, result);
  }

  @ExceptionHandler(VsException.class)
  public ResponseEntity<RestData<?>> handleVsException(VsException ex, WebRequest req) {
    return VsResponseUtil.error(ex.getStatusCode(), ex.getErrMessage());
  }

  @ExceptionHandler(DuplicateException.class)
  public ResponseEntity<RestData<?>> handleDuplicateException(DuplicateException ex, WebRequest req) {
    return VsResponseUtil.error(ex.getStatusCode(), ex.getMessage());
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<RestData<?>> handleAccessDeniedException(ForbiddenException ex, WebRequest req) {
    return VsResponseUtil.error(ex.getStatusCode(), ex.getMessage());
  }

  @ExceptionHandler(InternalServerException.class)
  public ResponseEntity<RestData<?>> handlerInternalServerException(InternalServerException ex, WebRequest req) {
    return VsResponseUtil.error(ex.getStatusCode(), ex.getMessage());
  }

  @ExceptionHandler(InvalidException.class)
  public ResponseEntity<RestData<?>> handlerInvalidException(InvalidException ex, WebRequest req) {
    return VsResponseUtil.error(ex.getStatusCode(), ex.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<RestData<?>> handlerNotFoundException(NotFoundException ex, WebRequest req) {
    return VsResponseUtil.error(ex.getStatusCode(), ex.getMessage());
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<RestData<?>> handleUnauthorizedException(UnauthorizedException ex, WebRequest req) {
    return VsResponseUtil.error(ex.getStatusCode(), ex.getMessage());
  }

  @ExceptionHandler(UploadImageException.class)
  public ResponseEntity<RestData<?>> handleUploadImageException(UploadImageException ex, WebRequest req) {
    return VsResponseUtil.error(ex.getStatusCode(), ex.getMessage());
  }

}