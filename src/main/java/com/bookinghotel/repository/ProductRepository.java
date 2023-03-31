package com.bookinghotel.repository;

import com.bookinghotel.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query(value = "SELECT * FROM products p WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) AND p.delete_flag = 0",
      countQuery = "SELECT COUNT(*) FROM Product p WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) AND p.delete_flag = 0",
      nativeQuery = true)
  Page<Product> findAllByKey(Pageable pageable, String keyword);

  @Query(value = "SELECT * FROM products p WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
      "AND p.service_id = :serviceId AND p.delete_flag = 0",
      countQuery = "SELECT COUNT(*) FROM products p WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
          "AND p.service_id = :serviceId AND p.delete_flag = 0",
      nativeQuery = true)
  Page<Product> findAllByServiceId(Pageable pageable, Long serviceId, String keyword);

  @Query("SELECT p FROM Product p WHERE p.id = ?1 AND p.deleteFlag = false")
  Optional<Product> findById(Long id);

  @Transactional
  @Modifying
  @Query("delete from Product p where p.deleteFlag = ?1")
  void deleteByDeleteFlag(boolean isDelete);

}
