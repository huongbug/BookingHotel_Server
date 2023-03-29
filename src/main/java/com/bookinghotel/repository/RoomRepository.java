package com.bookinghotel.repository;

import com.bookinghotel.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

  @Query("SELECT r FROM Room r WHERE r.id = ?1 AND r.deleteFlag = false")
  Optional<Room> findById(Long id);

  @Query(value = "SELECT * FROM rooms r WHERE (:keyword is null or r.title LIKE CONCAT('%', :keyword, '%')) " +
      "AND (:filter is null or r.type = :filter) AND r.delete_flag = 0",
      countQuery = "SELECT COUNT(*) FROM rooms r WHERE (:keyword is null or r.title LIKE CONCAT('%', :keyword, '%')) " +
          "AND (:filter is null or r.type = :filter) AND r.delete_flag = 0",
      nativeQuery = true)
  Page<Room> findAllByKey(@Param("keyword") String keyword, @Param("filter") String filter, Pageable pageable);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM rooms WHERE delete_flag = ?1 AND DATEDIFF(NOW(), last_modified_date) >= ?2", nativeQuery = true)
  void deleteByDeleteFlag(boolean isDelete, int daysToDelete);


}
