package com.bookinghotel.service;

import com.bookinghotel.dto.ProductCreateDTO;
import com.bookinghotel.dto.ProductDTO;
import com.bookinghotel.dto.ProductUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;

public interface ProductService {

  ProductDTO getProduct(Long productId);

  PaginationResponseDTO<ProductDTO> getProducts(PaginationSearchSortRequestDTO requestDTO);

  PaginationResponseDTO<ProductDTO> getProductsByServiceId(Long serviceId, PaginationSearchSortRequestDTO requestDTO);

  ProductDTO createProduct(ProductCreateDTO productCreateDTO);

  ProductDTO updateProduct(Long productId, ProductUpdateDTO productUpdateDTO);

  CommonResponseDTO deleteProduct(Long productId);

}
