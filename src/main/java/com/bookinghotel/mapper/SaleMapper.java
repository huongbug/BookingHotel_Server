package com.bookinghotel.mapper;

import com.bookinghotel.dto.SaleCreateDTO;
import com.bookinghotel.dto.SaleDTO;
import com.bookinghotel.dto.SaleUpdateDTO;
import com.bookinghotel.entity.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {

  SaleDTO toSaleDTO(Sale sale);

  List<SaleDTO> toSaleDTOs(List<Sale> sales);

  Sale createDtoToSale(SaleCreateDTO createDTO);

  void updateSaleFromDTO(SaleUpdateDTO updateDTO, @MappingTarget Sale sale);

}
