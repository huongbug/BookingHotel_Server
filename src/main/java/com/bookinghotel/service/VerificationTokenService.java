package com.bookinghotel.service;

import com.bookinghotel.entity.User;
import com.bookinghotel.entity.VerificationToken;

public interface VerificationTokenService {

  VerificationToken createVerificationToken(User user);

  //Xóa các token rác
  void deleteAllJunkVerificationToken();

}
