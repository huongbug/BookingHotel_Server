package com.bookinghotel.service.impl;

import com.bookinghotel.constant.CommonConstant;
import com.bookinghotel.constant.CommonMessage;
import com.bookinghotel.constant.ErrorMessage;
import com.bookinghotel.constant.SortByDataConstant;
import com.bookinghotel.dto.SaleCreateDTO;
import com.bookinghotel.dto.SaleDTO;
import com.bookinghotel.dto.SaleUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;
import com.bookinghotel.dto.pagination.PagingMeta;
import com.bookinghotel.entity.Sale;
import com.bookinghotel.exception.NotFoundException;
import com.bookinghotel.mapper.SaleMapper;
import com.bookinghotel.repository.SaleRepository;
import com.bookinghotel.service.SaleService;
import com.bookinghotel.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SaleServiceImpl implements SaleService {

  private final SaleRepository saleRepository;

  private final SaleMapper saleMapper;

  @Override
  public SaleDTO getSale(Long saleId) {
    Optional<Sale> sale = saleRepository.findById(saleId);
    checkNotFoundSaleById(sale, saleId);
    return saleMapper.toSaleDTO(sale.get());
  }

  @Override
  public PaginationResponseDTO<SaleDTO> getSales(PaginationSearchSortRequestDTO requestDTO) {
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.SALE);
    Page<Sale> sales = saleRepository.findAllByKey(requestDTO.getKeyword(), pageable);
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.SALE, sales);
    List<SaleDTO> saleDTOs = saleMapper.toSaleDTOs(sales.getContent());
    return new PaginationResponseDTO<SaleDTO>(meta, saleDTOs);
  }

  @Override
  public SaleDTO createSale(SaleCreateDTO createDTO) {
    Sale sale = saleMapper.createDtoToSale(createDTO);
    return saleMapper.toSaleDTO(saleRepository.save(sale));
  }

  @Override
  public SaleDTO updateSale(Long saleId, SaleUpdateDTO updateDTO) {
    Optional<Sale> currentSale = saleRepository.findById(saleId);
    checkNotFoundSaleById(currentSale, saleId);
    saleMapper.updateSaleFromDTO(updateDTO, currentSale.get());
    return saleMapper.toSaleDTO(saleRepository.save(currentSale.get()));
  }

  @Override
  public CommonResponseDTO deleteSale(Long saleId) {
    Optional<Sale> sale = saleRepository.findById(saleId);
    checkNotFoundSaleById(sale, saleId);
    sale.get().setDeleteFlag(CommonConstant.TRUE);
    saleRepository.save(sale.get());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
  }

  @Override
  @Transactional
  public void deleteSaleByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords) {
    saleRepository.deleteByDeleteFlag(isDeleteFlag, daysToDeleteRecords);
  }

  private void checkNotFoundSaleById(Optional<Sale> sale, Long saleId) {
    if (sale.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.Sale.ERR_NOT_FOUND_ID, saleId));
    }
  }

}
