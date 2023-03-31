package com.bookinghotel;

import com.bookinghotel.config.UserInfoProperties;
import com.bookinghotel.constant.RoleConstant;
import com.bookinghotel.entity.Role;
import com.bookinghotel.entity.User;
import com.bookinghotel.repository.RoleRepository;
import com.bookinghotel.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@EnableConfigurationProperties({UserInfoProperties.class})
@SpringBootApplication
public class BookingHotelApplication {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;


  public BookingHotelApplication(UserRepository userRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(BookingHotelApplication.class, args);
  }

  @Bean
  CommandLineRunner init(UserInfoProperties userInfo) {
    return args -> {
      PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

      if (roleRepository.count() == 0) {
        roleRepository.save(new Role(null, RoleConstant.ADMIN, null));
        roleRepository.save(new Role(null, RoleConstant.USER, null));
      }

      if (userRepository.count() == 0) {
        User admin = new User(userInfo.getEmail(), userInfo.getPhone(), passwordEncoder.encode(userInfo.getPassword()),
            userInfo.getFirstName(), userInfo.getLastName(), "Nữ", LocalDate.parse(userInfo.getBirthday()),
            userInfo.getAddress(), Boolean.TRUE, userInfo.getAvatar(), roleRepository.findByRoleName(RoleConstant.ADMIN));
        userRepository.save(admin);
      }
    };
  }

}
