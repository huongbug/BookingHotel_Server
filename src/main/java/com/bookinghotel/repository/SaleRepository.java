package com.bookinghotel.repository;

import com.bookinghotel.entity.Sale;
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
public interface SaleRepository extends JpaRepository<Sale, Long> {

  @Query("SELECT s FROM Sale s WHERE s.id = ?1 AND s.deleteFlag = false")
  Optional<Sale> findById(Long saleId);

  @Query(value = "SELECT * FROM sales s WHERE (:keyword is null or s.sale_percent LIKE CONCAT('%', :keyword, '%')) AND s.delete_flag = 0",
      countQuery = "SELECT COUNT(*) FROM sales s WHERE (:keyword is null or s.sale_percent LIKE CONCAT('%', :keyword, '%')) AND s.delete_flag = 0",
      nativeQuery = true)
  Page<Sale> findAllByKey(@Param("keyword") String keyword, Pageable pageable);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM sales WHERE delete_flag = ?1 AND DATEDIFF(NOW(), last_modified_date) >= ?2", nativeQuery = true)
  void deleteByDeleteFlag(boolean isDelete, int daysToDelete);

}
