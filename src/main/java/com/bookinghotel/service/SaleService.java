package com.bookinghotel.service;

import com.bookinghotel.dto.SaleCreateDTO;
import com.bookinghotel.dto.SaleDTO;
import com.bookinghotel.dto.SaleUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;

import java.util.List;

public interface SaleService {

  SaleDTO getSale(Long saleId);

  PaginationResponseDTO<SaleDTO> getSales(PaginationSearchSortRequestDTO requestDTO);

  SaleDTO createSale(SaleCreateDTO createDTO);

  SaleDTO updateSale(Long saleId, SaleUpdateDTO updateDTO);

  CommonResponseDTO deleteSale(Long saleId);

  CommonResponseDTO addSalesToRoom(Long saleId, List<Long> roomIds);

  CommonResponseDTO removeSaleFromRoom(Long saleId, Long roomId);

  void deleteSaleByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords);

}
