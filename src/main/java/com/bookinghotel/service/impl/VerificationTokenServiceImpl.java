package com.bookinghotel.service.impl;

import com.bookinghotel.entity.User;
import com.bookinghotel.entity.VerificationToken;
import com.bookinghotel.repository.VerificationTokenRepository;
import com.bookinghotel.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

  private final VerificationTokenRepository verificationTokenRepository;

  @Override
  public VerificationToken createVerificationToken(User user) {
    UUID token = UUID.randomUUID();
    VerificationToken verificationToken = new VerificationToken(user, token.toString());
    return verificationTokenRepository.save(verificationToken);
  }

  @Override
  public void deleteAllJunkVerificationToken() {
    List<VerificationToken> verificationTokenList = verificationTokenRepository.findAll();
    Calendar cal = Calendar.getInstance();
    for (VerificationToken verificationToken : verificationTokenList) {
      if ((verificationToken.getExpirationTime().getTime() - cal.getTime().getTime()) <= 0) {
        verificationTokenRepository.delete(verificationToken);
      }
    }
  }

}
