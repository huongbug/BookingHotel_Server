package com.bookinghotel.repository;

import com.bookinghotel.constant.ErrorMessage;
import com.bookinghotel.entity.User;
import com.bookinghotel.exception.InvalidException;
import com.bookinghotel.exception.NotFoundException;
import com.bookinghotel.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  @Query(value = "SELECT u.* FROM users u " +
      "INNER JOIN roles r ON r.id = u.role_id " +
      "WHERE (r.role_name = 'ROLE_USER' AND u.is_locked = :isLocked) " +
      "AND ( " +
      "COALESCE(:keyword, '') = '' " +
      "OR u.last_name LIKE CONCAT('%', :keyword, '%')" +
      "OR u.first_name LIKE CONCAT('%', :keyword, '%')" +
      "OR u.email LIKE CONCAT('%', :keyword, '%')" +
      "OR u.phone_number LIKE CONCAT('%', :keyword, '%')" +
      "OR u.birthday LIKE CONCAT('%', :keyword, '%')" +
      ")",
      countQuery = "SELECT COUNT(*) FROM users u " +
          "INNER JOIN roles r ON r.id = u.role_id " +
          "WHERE (r.role_name = 'ROLE_USER' AND u.is_locked = :isLocked) " +
          "AND ( " +
          "COALESCE(:keyword, '') = '' " +
          "OR u.last_name LIKE CONCAT('%', :keyword, '%')" +
          "OR u.first_name LIKE CONCAT('%', :keyword, '%')" +
          "OR u.email LIKE CONCAT('%', :keyword, '%')" +
          "OR u.phone_number LIKE CONCAT('%', :keyword, '%')" +
          "OR u.birthday LIKE CONCAT('%', :keyword, '%')" +
          ")",
      nativeQuery = true)
  Page<User> findAllCustomer(@Param("keyword") String keyword, @Param("isLocked") Boolean isLocked, Pageable pageable);

  @Query("SELECT u FROM User u WHERE u.id = ?1")
  Optional<User> findById(String id);

  @Query("SELECT u FROM User u WHERE u.id = ?1 AND u.role.roleName = 'ROLE_USER'")
  Optional<User> findCustomerById(String id);

  @Query("SELECT u FROM User u WHERE u.email = ?1")
  Optional<User> findByEmail(String email);

  @Query("SELECT u FROM User u WHERE u.email = ?1 OR u.phoneNumber = ?1")
  Optional<User> findByEmailOrPhone(String emailOrPhone);

  default User getUser(UserPrincipal currentUser) {
    Optional<User> user = findByEmail(currentUser.getUsername());
    if (user.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.User.ERR_NOT_FOUND_ID, currentUser.getId()));
    } else {
      if (!user.get().getIsEnable()) {
        throw new InvalidException(ErrorMessage.Auth.ERR_ACCOUNT_NOT_ENABLED);
      }
      if (user.get().getIsLocked()) {
        throw new InvalidException((ErrorMessage.Auth.ERR_ACCOUNT_LOCKED));
      }
      return user.get();
    }
  }

}
