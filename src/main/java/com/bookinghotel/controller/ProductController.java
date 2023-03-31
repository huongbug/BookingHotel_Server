package com.bookinghotel.controller;

import com.bookinghotel.base.RestApiV1;
import com.bookinghotel.base.VsResponseUtil;
import com.bookinghotel.constant.RoleConstant;
import com.bookinghotel.constant.UrlConstant;
import com.bookinghotel.dto.ProductCreateDTO;
import com.bookinghotel.dto.ProductUpdateDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;
import com.bookinghotel.security.AuthorizationInfo;
import com.bookinghotel.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestApiV1
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "API get product by id")
  @GetMapping(UrlConstant.Product.GET_PRODUCT)
  public ResponseEntity<?> getProductById(@PathVariable Long productId) {
    return VsResponseUtil.ok(productService.getProduct(productId));
  }

  @Operation(summary = "API get all product")
  @GetMapping(UrlConstant.Product.GET_PRODUCTS)
  public ResponseEntity<?> getProducts(@Valid @ParameterObject PaginationSearchSortRequestDTO requestDTO) {
    return VsResponseUtil.ok(productService.getProducts(requestDTO));
  }

  @Tag(name = "product-controller-admin")
  @Operation(summary = "API create product")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @PostMapping(value = UrlConstant.Product.CREATE_PRODUCT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createProductById(@Valid @ModelAttribute ProductCreateDTO productCreateDTO) {
    return VsResponseUtil.ok(productService.createProduct(productCreateDTO));
  }

  @Tag(name = "product-controller-admin")
  @Operation(summary = "API update product by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @PatchMapping(value = UrlConstant.Product.UPDATE_PRODUCT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updateProductById(@PathVariable Long productId, @Valid @ModelAttribute ProductUpdateDTO productUpdateDTO) {
    return VsResponseUtil.ok(productService.updateProduct(productId, productUpdateDTO));
  }

  @Tag(name = "product-controller-admin")
  @Operation(summary = "API delete product by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @DeleteMapping(UrlConstant.Product.DELETE_PRODUCTS)
  public ResponseEntity<?> deleteProductById(@PathVariable Long productId) {
    return VsResponseUtil.ok(productService.deleteProduct(productId));
  }

}
