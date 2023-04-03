package com.bookinghotel.repository;

import com.bookinghotel.dto.common.DateFilterDTO;
import com.bookinghotel.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

  @Query("SELECT b FROM Booking b WHERE b.id = ?1")
  Optional<Booking> findById(Long id);

  @Query(value = "SELECT * FROM bookings b WHERE b.user_id = :userId",
      countQuery = "SELECT COUNT(*) FROM bookings b WHERE b.user_id = :userId",
      nativeQuery = true)
  Page<Booking> findAllForUser(Pageable pageable, @Param("userId") String userId);

  @Query(value = "SELECT * FROM bookings b WHERE (:#{#dateFilter.fromDate} IS NULL OR b.check_in >= :#{#dateFilter.fromDate}) " +
      "AND (:#{#dateFilter.toDate} IS NULL OR b.check_out <= :#{#dateFilter.toDate}) ",
      countQuery = "SELECT COUNT(*) FROM bookings b WHERE (:#{#dateFilter.fromDate} IS NULL OR b.check_in >= :#{#dateFilter.fromDate}) " +
          "AND (:#{#dateFilter.toDate} IS NULL OR b.check_out <= :#{#dateFilter.toDate}) ",
      nativeQuery = true)
  Page<Booking> findAllForAdmin(Pageable pageable, @Param("dateFilter") DateFilterDTO dateFilter);

}
