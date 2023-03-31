package com.bookinghotel.repository;

import com.bookinghotel.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

  @Query(value = "SELECT * FROM services s WHERE (:keyword IS NULL OR s.title LIKE CONCAT('%', :keyword, '%')) AND s.delete_flag = 0",
      countQuery = "SELECT COUNT(*) FROM services s WHERE (:keyword IS NULL OR s.title LIKE CONCAT('%', :keyword, '%')) AND s.delete_flag = 0",
      nativeQuery = true)
  Page<Service> findAllByKey(Pageable pageable, String keyword);

  @Query("SELECT s FROM Service s WHERE s.id IN ?1 AND s.deleteFlag = false")
  List<Service> findAllByIds(List<Long> ids);

  @Query("SELECT s FROM Service s WHERE s.id = ?1 AND s.deleteFlag = false")
  Optional<Service> findById(Long id);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM services WHERE delete_flag = ?1 AND DATEDIFF(NOW(), last_modified_date) >= ?2", nativeQuery = true)
  void deleteByDeleteFlag(boolean isDelete, int daysToDelete);

}
