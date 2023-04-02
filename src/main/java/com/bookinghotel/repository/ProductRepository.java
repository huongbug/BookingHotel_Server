package com.bookinghotel.repository;

import com.bookinghotel.entity.Product;
import com.bookinghotel.projection.ProductProjection;
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
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("SELECT p FROM Product p WHERE p.id = ?1 AND p.deleteFlag = false")
  Optional<Product> findById(Long id);

  @Query(value = "SELECT p.id, p.name, p.thumbnail, p.description, " +
      "p.created_date AS createdDate, p.last_modified_date AS lastModifiedDate, " +
      "createdBy.id AS createdById, " +
      "createdBy.first_name AS createdByFirstName, " +
      "createdBy.last_name AS createdByLastName, " +
      "createdBy.avatar AS createdByAvatar, " +
      "lastModifiedBy.id AS lastModifiedById, " +
      "lastModifiedBy.first_name AS lastModifiedByFirstName, " +
      "lastModifiedBy.last_name AS lastModifiedByLastName, " +
      "lastModifiedBy.avatar AS lastModifiedByAvatar " +
      "FROM products p " +
      "LEFT JOIN users createdBy ON p.created_by = createdBy.id " +
      "LEFT JOIN users lastModifiedBy ON p.last_modified_by = lastModifiedBy.id " +
      "WHERE p.id = ?1 AND p.delete_flag = 0",
      nativeQuery = true)
  ProductProjection findProductById(Long id);

  @Query(value = "SELECT p.id, p.name, p.thumbnail, p.description, " +
      "p.created_date AS createdDate, p.last_modified_date AS lastModifiedDate, " +
      "createdBy.id AS createdById, " +
      "createdBy.first_name AS createdByFirstName, " +
      "createdBy.last_name AS createdByLastName, " +
      "createdBy.avatar AS createdByAvatar, " +
      "lastModifiedBy.id AS lastModifiedById, " +
      "lastModifiedBy.first_name AS lastModifiedByFirstName, " +
      "lastModifiedBy.last_name AS lastModifiedByLastName, " +
      "lastModifiedBy.avatar AS lastModifiedByAvatar " +
      "FROM products p " +
      "LEFT JOIN users createdBy ON p.created_by = createdBy.id " +
      "LEFT JOIN users lastModifiedBy ON p.last_modified_by = lastModifiedBy.id " +
      "WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
      "AND p.delete_flag = 0",
      countQuery = "SELECT COUNT(*) FROM products p " +
          "LEFT JOIN users createdBy ON p.created_by = createdBy.id " +
          "LEFT JOIN users lastModifiedBy ON p.last_modified_by = lastModifiedBy.id " +
          "WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
          "AND p.delete_flag = 0",
      nativeQuery = true)
  Page<ProductProjection> findAllByKey(Pageable pageable, @Param("keyword") String keyword);

  @Query(value = "SELECT p.id, p.name, p.thumbnail, p.description, " +
      "p.created_date AS createdDate, p.last_modified_date AS lastModifiedDate, " +
      "createdBy.id AS createdById, " +
      "createdBy.first_name AS createdByFirstName, " +
      "createdBy.last_name AS createdByLastName, " +
      "createdBy.avatar AS createdByAvatar, " +
      "lastModifiedBy.id AS lastModifiedById, " +
      "lastModifiedBy.first_name AS lastModifiedByFirstName, " +
      "lastModifiedBy.last_name AS lastModifiedByLastName, " +
      "lastModifiedBy.avatar AS lastModifiedByAvatar " +
      "FROM products p " +
      "LEFT JOIN users createdBy ON p.created_by = createdBy.id " +
      "LEFT JOIN users lastModifiedBy ON p.last_modified_by = lastModifiedBy.id " +
      "WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
      "AND p.service_id = :serviceId AND p.delete_flag = 0",
      countQuery = "SELECT COUNT(*) FROM products p " +
          "LEFT JOIN users createdBy ON p.created_by = createdBy.id " +
          "LEFT JOIN users lastModifiedBy ON p.last_modified_by = lastModifiedBy.id " +
          "WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
          "AND p.service_id = :serviceId AND p.delete_flag = 0",
      nativeQuery = true)
  Page<ProductProjection> findAllByServiceId(Pageable pageable, @Param("serviceId") Long serviceId, @Param("keyword") String keyword);

  @Transactional
  @Modifying
  @Query("delete from Product p where p.deleteFlag = ?1")
  void deleteByDeleteFlag(boolean isDelete);

}