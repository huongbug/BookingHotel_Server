package com.bookinghotel.job;

import com.bookinghotel.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableAsync
@Configuration
@EnableScheduling
@ConditionalOnExpression("true")
@RequiredArgsConstructor
public class LockUserRefuseToCheckinJob {

  private final BookingService bookingService;

  /**
   * This job starts at 5:00 AM everyday
   */
  @Scheduled(cron = "0 0 5 * * *")
  void lockUserRefuseToCheckin() {
    bookingService.lockUserRefuseToCheckIn();
  }

}
