package com.bookinghotel.mapper;

import com.bookinghotel.dto.ServiceCreateDTO;
import com.bookinghotel.dto.ServiceDTO;
import com.bookinghotel.dto.ServiceUpdateDTO;
import com.bookinghotel.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

  Service toService(ServiceDTO serviceDTO);

  List<Service> toServices(List<ServiceDTO> serviceDTOs);

  ServiceDTO toServiceDTO(Service service);

  List<ServiceDTO> toServiceDTOs(List<Service> services);

  Service createDtoToProduct(ServiceCreateDTO createDTO);

  void updateProductFromDTO(ServiceUpdateDTO updateDTO, @MappingTarget Service service);

}
