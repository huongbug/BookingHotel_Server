package com.bookinghotel.service.impl;

import com.bookinghotel.constant.CommonConstant;
import com.bookinghotel.constant.CommonMessage;
import com.bookinghotel.constant.ErrorMessage;
import com.bookinghotel.constant.SortByDataConstant;
import com.bookinghotel.dto.ProductCreateDTO;
import com.bookinghotel.dto.ProductDTO;
import com.bookinghotel.dto.ProductUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;
import com.bookinghotel.dto.pagination.PagingMeta;
import com.bookinghotel.entity.Product;
import com.bookinghotel.entity.User;
import com.bookinghotel.exception.InvalidException;
import com.bookinghotel.exception.NotFoundException;
import com.bookinghotel.mapper.ProductMapper;
import com.bookinghotel.projection.ProductProjection;
import com.bookinghotel.repository.ProductRepository;
import com.bookinghotel.repository.UserRepository;
import com.bookinghotel.security.UserPrincipal;
import com.bookinghotel.service.ProductService;
import com.bookinghotel.util.PaginationUtil;
import com.bookinghotel.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  private final UserRepository userRepository;

  private final ProductMapper productMapper;

  private final UploadFileUtil uploadFile;

  @Override
  public ProductDTO getProduct(Long productId) {
    ProductProjection product = productRepository.findProductById(productId);
    checkNotFoundProductById(product, productId);
    return productMapper.productProjectionToProductDTO(product);
  }

  @Override
  public PaginationResponseDTO<ProductDTO> getProducts(PaginationSearchSortRequestDTO requestDTO) {
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.PRODUCT);
    Page<ProductProjection> products = productRepository.findAllByKey(pageable, requestDTO.getKeyword());
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.PRODUCT, products);
    return new PaginationResponseDTO<ProductDTO>(meta, toProductDTOs(products));
  }

  @Override
  public PaginationResponseDTO<ProductDTO> getProductsByServiceId(Long serviceId, PaginationSearchSortRequestDTO requestDTO) {
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.PRODUCT);
    Page<ProductProjection> products = productRepository.findAllByServiceId(pageable, serviceId,requestDTO.getKeyword());
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.PRODUCT, products);
    return new PaginationResponseDTO<ProductDTO>(meta, toProductDTOs(products));
  }

  @Override
  public ProductDTO createProduct(ProductCreateDTO productCreateDTO, UserPrincipal principal) {
    User creator = userRepository.getUser(principal);
    Product product = productMapper.createDtoToProduct(productCreateDTO);
    product.setThumbnail(uploadFile.getUrlFromFile(productCreateDTO.getThumbnailFile()));
    return productMapper.toProductDTO(productRepository.save(product), creator, creator);
  }

  @Override
  public ProductDTO updateProduct(Long productId, ProductUpdateDTO productUpdateDTO, UserPrincipal principal) {
    Optional<Product> currentProduct = productRepository.findById(productId);
    checkNotFoundProductById(currentProduct, productId);
    productMapper.updateProductFromDTO(productUpdateDTO, currentProduct.get());
    //update thumbnail
    if(StringUtils.isEmpty(productUpdateDTO.getThumbnail())) {
      if(productUpdateDTO.getThumbnailFile() != null) {
        uploadFile.removeImageFromUrl(currentProduct.get().getThumbnail());
        currentProduct.get().setThumbnail(uploadFile.getUrlFromFile(productUpdateDTO.getThumbnailFile()));
      } else {
        throw new InvalidException(ErrorMessage.Product.ERR_PRODUCT_MUST_HAVE_THUMBNAIL);
      }
    }
    User updater = userRepository.getUser(principal);
    User creator = userRepository.findById(currentProduct.get().getCreatedBy()).get();
    return productMapper.toProductDTO(productRepository.save(currentProduct.get()), creator, updater);
  }

  @Override
  public CommonResponseDTO deleteProduct(Long productId) {
    Optional<Product> product = productRepository.findById(productId);
    checkNotFoundProductById(product, productId);
    product.get().setDeleteFlag(CommonConstant.TRUE);
    productRepository.save(product.get());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
  }

  private List<ProductDTO> toProductDTOs(Page<ProductProjection> productProjections) {
    List<ProductDTO> productDTOs = new LinkedList<>();
    for(ProductProjection productProjection : productProjections) {
      productDTOs.add(productMapper.productProjectionToProductDTO(productProjection));
    }
    return productDTOs;
  }

  private void checkNotFoundProductById(Optional<Product> product, Long productId) {
    if (product.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.Product.ERR_NOT_FOUND_ID, productId));
    }
  }

  private void checkNotFoundProductById(ProductProjection productProjection, Long productId) {
    if (ObjectUtils.isEmpty(productProjection)) {
      throw new NotFoundException(String.format(ErrorMessage.Product.ERR_NOT_FOUND_ID, productId));
    }
  }


}