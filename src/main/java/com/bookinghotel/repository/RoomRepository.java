package com.bookinghotel.repository;

import com.bookinghotel.dto.RoomFilterDTO;
import com.bookinghotel.entity.Room;
import com.bookinghotel.projection.RoomProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

  @Query("SELECT r FROM Room r WHERE r.id = ?1 AND r.deleteFlag = false")
  Optional<Room> findById(Long id);

  @Query(value = "SELECT r.id, r.title, r.price, r.type, r.max_num AS maxNum, r.floor, r.description, " +
      "s.id AS saleId, s.day_start AS saleDayStart, s.day_end AS saleDayEnd, s.sale_percent AS saleSalePercent, " +
      "createdBy.id AS createdById, " +
      "createdBy.first_name AS createdByFirstName, " +
      "createdBy.last_name AS createdByLastName, " +
      "createdBy.avatar AS createdByAvatar, " +
      "lastModifiedBy.id AS lastModifiedById, " +
      "lastModifiedBy.first_name AS lastModifiedByFirstName, " +
      "lastModifiedBy.last_name AS lastModifiedByLastName, " +
      "lastModifiedBy.avatar AS lastModifiedByAvatar " +
      "FROM rooms r " +
      "LEFT JOIN users createdBy ON r.created_by = createdBy.id " +
      "LEFT JOIN users lastModifiedBy ON r.last_modified_by = lastModifiedBy.id " +
      "LEFT JOIN sales s ON s.id = r.sale_id " +
      "WHERE r.id = ?1 AND r.delete_flag = 0",
      nativeQuery = true)
  RoomProjection findRoomById(Long id);

  @Query("SELECT r FROM Room r WHERE r.id IN ?1 AND r.deleteFlag = false")
  List<Room> findAllByIds(List<Long> ids);

  @Query(value = "SELECT r.id, r.title, r.price, r.type, r.max_num AS maxNum, r.floor, r.description, " +
      "s.id AS saleId, s.day_start AS saleDayStart, s.day_end AS saleDayEnd, s.sale_percent AS saleSalePercent, " +
      "createdBy.id AS createdById, " +
      "createdBy.first_name AS createdByFirstName, " +
      "createdBy.last_name AS createdByLastName, " +
      "createdBy.avatar AS createdByAvatar, " +
      "lastModifiedBy.id AS lastModifiedById, " +
      "lastModifiedBy.first_name AS lastModifiedByFirstName, " +
      "lastModifiedBy.last_name AS lastModifiedByLastName, " +
      "lastModifiedBy.avatar AS lastModifiedByAvatar " +
      "FROM rooms r " +
      "LEFT JOIN users createdBy ON r.created_by = createdBy.id " +
      "LEFT JOIN users lastModifiedBy ON r.last_modified_by = lastModifiedBy.id " +
      "LEFT JOIN sales s ON s.id = r.sale_id " +
      "WHERE (:keyword is null or r.title LIKE CONCAT('%', :keyword, '%')) " +
      "AND (:filter is null or r.type = :filter) AND r.delete_flag = 0",
      countQuery = "SELECT COUNT(*) FROM rooms r " +
          "LEFT JOIN users createdBy ON r.created_by = createdBy.id " +
          "LEFT JOIN users lastModifiedBy ON r.last_modified_by = lastModifiedBy.id " +
          "LEFT JOIN sales s ON s.id = r.sale_id " +
          "WHERE (:keyword is null or r.title LIKE CONCAT('%', :keyword, '%')) " +
          "AND (:filter is null or r.type = :filter) AND r.delete_flag = 0",
      nativeQuery = true)
  Page<RoomProjection> findAllByKey(@Param("keyword") String keyword, @Param("filter") String filter,
                                    Pageable pageable);

  @Query(value =
      "WITH data_check_checkin AS ( " +
          "SELECT r.* FROM rooms r " +
          "INNER JOIN booking_room_details brd ON brd.room_id = r.id " +
          "INNER JOIN bookings b ON b.id = brd.booking_id " +
          "WHERE b.expected_check_in <= :#{#roomFilter.checkin} " +
          "AND b.expected_check_out >= DATE_SUB(:#{#roomFilter.checkin}, INTERVAL 2 HOUR) " +
          "AND b.status NOT IN ('CANCEL', 'CHECKED_OUT') " +
      "), " +
      "data_check_checkout AS ( " +
          "SELECT r.* FROM rooms r " +
          "INNER JOIN booking_room_details brd ON brd.room_id = r.id " +
          "INNER JOIN bookings b ON b.id = brd.booking_id " +
          "WHERE b.expected_check_in <= DATE_ADD(:#{#roomFilter.checkout}, INTERVAL 2 HOUR) " +
          "AND b.expected_check_out >= :#{#roomFilter.checkout} " +
          "AND b.status NOT IN ('CANCEL', 'CHECKED_OUT') " +
      "), " +
      "data_check AS ( " +
          "SELECT * FROM data_check_checkin data_1 " +
          "UNION " +
          "SELECT * FROM data_check_checkout data_2 " +
      ") " +
      "SELECT r.id, r.title, r.price, r.type, r.max_num AS maxNum, r.floor, r.description," +
      "s.id AS saleId, s.day_start AS saleDayStart, s.day_end AS saleDayEnd, s.sale_percent AS saleSalePercent, " +
      "createdBy.id AS createdById, " +
      "createdBy.first_name AS createdByFirstName, " +
      "createdBy.last_name AS createdByLastName, " +
      "createdBy.avatar AS createdByAvatar, " +
      "lastModifiedBy.id AS lastModifiedById, " +
      "lastModifiedBy.first_name AS lastModifiedByFirstName, " +
      "lastModifiedBy.last_name AS lastModifiedByLastName, " +
      "lastModifiedBy.avatar AS lastModifiedByAvatar " +
      "FROM rooms r " +
      "LEFT JOIN users createdBy ON r.created_by = createdBy.id " +
      "LEFT JOIN users lastModifiedBy ON r.last_modified_by = lastModifiedBy.id " +
      "LEFT JOIN sales s ON s.id = r.sale_id " +
      "WHERE r.id NOT IN (SELECT id FROM data_check) " +
      "AND (:#{#roomFilter.maxNum} is null or r.max_num LIKE CONCAT('%', :#{#roomFilter.maxNum}, '%')) " +
      "AND (:typeRoom is null or r.type LIKE CONCAT('%', :typeRoom, '%')) " +
      "AND (:keyword is null or r.title LIKE CONCAT('%', :keyword, '%')) " +
      "AND r.delete_flag = 0",
      countQuery = "WITH data_check_checkin AS ( " +
          "SELECT r.* FROM rooms r " +
          "INNER JOIN booking_room_details brd ON brd.room_id = r.id " +
          "INNER JOIN bookings b ON b.id = brd.booking_id " +
          "WHERE b.expected_check_in <= :#{#roomFilter.checkin} " +
          "AND b.expected_check_out >= DATE_SUB(:#{#roomFilter.checkin}, INTERVAL 2 HOUR) " +
          "AND b.status NOT IN ('CANCEL', 'CHECKED_OUT') " +
          "), " +
          "data_check_checkout AS ( " +
          "SELECT r.* FROM rooms r " +
          "INNER JOIN booking_room_details brd ON brd.room_id = r.id " +
          "INNER JOIN bookings b ON b.id = brd.booking_id " +
          "WHERE b.expected_check_in <= DATE_ADD(:#{#roomFilter.checkout}, INTERVAL 2 HOUR) " +
          "AND b.expected_check_out >= :#{#roomFilter.checkout} " +
          "AND b.status NOT IN ('CANCEL', 'CHECKED_OUT') " +
          "), " +
          "data_check AS ( " +
          "SELECT * FROM data_check_checkin data_1 " +
          "UNION " +
          "SELECT * FROM data_check_checkout data_2 " +
          ") " +
          "SELECT COUNT(*) FROM rooms r " +
          "LEFT JOIN users createdBy ON r.created_by = createdBy.id " +
          "LEFT JOIN users lastModifiedBy ON r.last_modified_by = lastModifiedBy.id " +
          "LEFT JOIN sales s ON s.id = r.sale_id " +
          "WHERE r.id NOT IN (SELECT id FROM data_check) " +
          "AND (:#{#roomFilter.maxNum} is null or r.max_num LIKE CONCAT('%', :#{#roomFilter.maxNum}, '%')) " +
          "AND (:typeRoom is null or r.type LIKE CONCAT('%', :typeRoom, '%')) " +
          "AND (:keyword is null or r.title LIKE CONCAT('%', :keyword, '%'))" +
          " AND r.delete_flag = 0",
      nativeQuery = true)
  Page<RoomProjection> findAllAvailable(@Param("keyword") String keyword, @Param("roomFilter") RoomFilterDTO roomFilter,
                              @Param("typeRoom") String typeRoom, Pageable pageable);

  @Query(value =
      "WITH data_check_checkin AS ( " +
          "SELECT r.* FROM rooms r " +
          "INNER JOIN booking_room_details brd ON brd.room_id = r.id " +
          "INNER JOIN bookings b ON b.id = brd.booking_id " +
          "WHERE b.expected_check_in <= :checkin " +
          "AND b.expected_check_out >= DATE_SUB(:checkin, INTERVAL 2 HOUR) " +
          "AND b.status NOT IN ('CANCEL', 'CHECKED_OUT') " +
      "), " +
      "data_check_checkout AS ( " +
          "SELECT r.* FROM rooms r " +
          "INNER JOIN booking_room_details brd ON brd.room_id = r.id " +
          "INNER JOIN bookings b ON b.id = brd.booking_id " +
          "WHERE b.expected_check_in <= DATE_ADD(:checkout, INTERVAL 2 HOUR) " +
          "AND b.expected_check_out >= :checkout " +
          "AND b.status NOT IN ('CANCEL', 'CHECKED_OUT') " +
      ") " +
      "SELECT * FROM data_check_checkin data_1 " +
      "UNION " +
      "SELECT * FROM data_check_checkout data_2",
      nativeQuery = true)
  List<Room> findAllUnavailable(@Param("checkin") LocalDateTime checkin, @Param("checkout") LocalDateTime checkout);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM rooms WHERE delete_flag = ?1 AND DATEDIFF(NOW(), last_modified_date) >= ?2",
      nativeQuery = true)
  void deleteByDeleteFlag(boolean isDelete, int daysToDelete);


}
