package com.bookinghotel.util;

import com.bookinghotel.exception.UploadImageException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
public class UploadFileUtil {

  private final Cloudinary cloudinary;

  public String getUrlFromFile(MultipartFile multipartFile) {
    try {
      Map<?, ?> map = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
      return map.get("secure_url").toString();
    } catch (IOException e) {
      throw new UploadImageException("Upload image failed!");
    }
  }

  public String getUrlFromLargeFile(MultipartFile multipartFile) {
    try {
      Map<?, ?> map = cloudinary.uploader().uploadLarge(multipartFile.getBytes(), ObjectUtils.asMap("resource_type", "video"));
      return map.get("secure_url").toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void removeImageFromUrl(String url) {
    int startIndex = url.lastIndexOf("/") + 1;
    int endIndex = url.lastIndexOf(".");
    String publicId = url.substring(startIndex, endIndex);
    try {
      Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
      log.info(String.format("Remove image public id %s %s", publicId, result.toString()));
    } catch (IOException e) {
      throw new UploadImageException("Remove image failed!");
    }
  }
}
