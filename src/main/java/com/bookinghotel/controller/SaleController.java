package com.bookinghotel.controller;

import com.bookinghotel.base.RestApiV1;
import com.bookinghotel.base.VsResponseUtil;
import com.bookinghotel.constant.RoleConstant;
import com.bookinghotel.constant.UrlConstant;
import com.bookinghotel.dto.SaleCreateDTO;
import com.bookinghotel.dto.SaleUpdateDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;
import com.bookinghotel.security.AuthorizationInfo;
import com.bookinghotel.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tags(@Tag(name = "sale-controller-admin"))
@RequiredArgsConstructor
@RestApiV1
public class SaleController {

  private final SaleService saleService;

  @Operation(summary = "API get sale by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @GetMapping(UrlConstant.Sale.GET_SALE)
  public ResponseEntity<?> getSaleById(@PathVariable Long saleId) {
    return VsResponseUtil.ok(saleService.getSale(saleId));
  }

  @Operation(summary = "API get all sale")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @GetMapping(UrlConstant.Sale.GET_SALES)
  public ResponseEntity<?> getSales(@Valid @ParameterObject PaginationSearchSortRequestDTO requestDTO) {
    return VsResponseUtil.ok(saleService.getSales(requestDTO));
  }

  @Operation(summary = "API create sale")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @PostMapping(UrlConstant.Sale.CREATE_SALE)
  public ResponseEntity<?> createSale(@Valid @RequestBody SaleCreateDTO saleCreateDTO) {
    return VsResponseUtil.ok(saleService.createSale(saleCreateDTO));
  }

  @Operation(summary = "API update sale by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @PutMapping(UrlConstant.Sale.UPDATE_SALE)
  public ResponseEntity<?> updateSaleById(@PathVariable Long saleId, @Valid @RequestBody SaleUpdateDTO saleUpdateDTO) {
    return VsResponseUtil.ok(saleService.updateSale(saleId, saleUpdateDTO));
  }

  @Operation(summary = "API delete sale by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @DeleteMapping(UrlConstant.Sale.DELETE_SALE)
  public ResponseEntity<?> deleteSaleById(@PathVariable Long saleId) {
    return VsResponseUtil.ok(saleService.deleteSale(saleId));
  }

  @Operation(summary = "API add sale to room")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @PostMapping(UrlConstant.Sale.ADD_SALE_TO_ROOM)
  public ResponseEntity<?> addSaleToRoom(@PathVariable Long saleId, @RequestBody List<Long> roomIds) {
    return VsResponseUtil.ok(saleService.addSalesToRoom(saleId, roomIds));
  }

  @Operation(summary = "API remove sale from room")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @PostMapping(UrlConstant.Sale.REMOVE_SALE_FROM_ROOM)
  public ResponseEntity<?> removeSaleFromRoom(@PathVariable Long saleId, @PathVariable Long roomId) {
    return VsResponseUtil.ok(saleService.removeSaleFromRoom(saleId, roomId));
  }

}