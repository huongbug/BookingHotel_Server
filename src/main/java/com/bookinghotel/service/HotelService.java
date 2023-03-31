package com.bookinghotel.service;

import com.bookinghotel.dto.ServiceCreateDTO;
import com.bookinghotel.dto.ServiceDTO;
import com.bookinghotel.dto.ServiceUpdateDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;

public interface HotelService {

  ServiceDTO getServiceById(Long serviceId);

  PaginationResponseDTO<ServiceDTO> getServices(PaginationSearchSortRequestDTO requestDTO);

  ServiceDTO createService(ServiceCreateDTO serviceCreateDTO);

  ServiceDTO updateService(Long serviceId, ServiceUpdateDTO serviceUpdateDTO);

  CommonResponseDTO deleteService(Long serviceId);

  void deleteServiceByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords);

}
