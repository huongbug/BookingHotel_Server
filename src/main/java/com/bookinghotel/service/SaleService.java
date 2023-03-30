package com.bookinghotel.service;

import com.bookinghotel.dto.SaleCreateDTO;
import com.bookinghotel.dto.SaleDTO;
import com.bookinghotel.dto.SaleUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;

public interface SaleService {

  SaleDTO getSale(Long saleId);

  PaginationResponseDTO<SaleDTO> getSales(PaginationSearchSortRequestDTO requestDTO);

  SaleDTO createSale(SaleCreateDTO createDTO);

  SaleDTO updateSale(Long saleId, SaleUpdateDTO updateDTO);

  CommonResponseDTO deleteSale(Long saleId);

  void deleteSaleByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords);

}
