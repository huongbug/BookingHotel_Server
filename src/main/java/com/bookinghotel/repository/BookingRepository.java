package com.bookinghotel.repository;

import com.bookinghotel.dto.BookingFilterDTO;
import com.bookinghotel.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

  @Query("SELECT b FROM Booking b WHERE b.id = ?1")
  Optional<Booking> findById(Long id);

  @Query(value = "SELECT * FROM bookings b WHERE b.user_id = :userId",
      countQuery = "SELECT COUNT(*) FROM bookings b WHERE b.user_id = :userId",
      nativeQuery = true)
  Page<Booking> findAllForUser(@Param("userId") String userId, Pageable pageable);

  @Query(value = "SELECT * FROM bookings b " +
      "WHERE (:#{#filter.fromDate} IS NULL OR b.expected_check_in >= :#{#filter.fromDate}) " +
      "AND (:#{#filter.toDate} IS NULL OR b.expected_check_out <= :#{#filter.toDate}) " +
      "AND (:status IS NULL OR b.status = :status)",
      countQuery = "SELECT COUNT(*) FROM bookings b " +
          "WHERE (:#{#filter.fromDate} IS NULL OR b.expected_check_in >= :#{#filter.fromDate}) " +
          "AND (:#{#filter.toDate} IS NULL OR b.expected_check_out <= :#{#filter.toDate}) " +
          "AND (:status IS NULL OR b.status = :status)",
      nativeQuery = true)
  Page<Booking> findAllForAdmin(@Param("filter") BookingFilterDTO filter, @Param("status") String bookingStatus, Pageable pageable);

  @Query(value = "SELECT COUNT(*) FROM bookings b " +
      "WHERE (:month IS NULL OR MONTH(b.expected_check_in) = :month) " +
      "AND YEAR(b.expected_check_out) = :year " +
      "AND b.status = :status",
      countQuery = "SELECT COUNT(*) FROM bookings b " +
          "WHERE (:month IS NULL OR MONTH(b.expected_check_in) = :month) " +
          "AND YEAR(b.expected_check_out) = :year " +
          "AND b.status = :status",
      nativeQuery = true)
  Long countBookingByStatus(@Param("month") Integer month, @Param("year") Integer year, @Param("status") String bookingStatus);


  //  @Query(value = "SELECT * FROM bookings b " +
//      "INNER JOIN booking_room_details brd ON brd.booking_id = b.id " +
//      "INNER JOIN booking_service_details bsd ON bsd.booking_id = b.id " +
//      "WHERE (:fromMonth IS NULL OR :toMonth IS NULL OR (MONTH(b.created_date) BETWEEN :fromMonth AND :toMonth)) " +
//      "AND YEAR(b.created_date) BETWEEN :fromYear AND toYear " +
//      "AND b.status = 'CHECKED_OUT'" +
//      "GROUP BY b.id", nativeQuery = true)
//  List<Booking> statisticRevenueProjection(@Param("fromMonth") Integer fromMonth, @Param("toMonth") Integer toMonth,
//                                           @Param("fromYear") Integer fromYear, @Param("toYear") Integer toYear);

  @Query(value = "SELECT * FROM bookings b " +
      "INNER JOIN booking_room_details brd ON brd.booking_id = b.id " +
      "INNER JOIN booking_service_details bsd ON bsd.booking_id = b.id " +
      "WHERE (:month IS NULL OR MONTH(b.expected_check_out) = :month) " +
      "AND YEAR(b.expected_check_out) = :year " +
      "AND b.status = 'CHECKED_OUT'" +
      "GROUP BY b.id", nativeQuery = true)
  List<Booking> statisticRevenueProjection(@Param("month") Integer month, @Param("year") Integer year);

}
