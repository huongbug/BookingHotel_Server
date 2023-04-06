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
import com.bookinghotel.entity.Room;
import com.bookinghotel.entity.Sale;
import com.bookinghotel.entity.User;
import com.bookinghotel.exception.InvalidException;
import com.bookinghotel.exception.NotFoundException;
import com.bookinghotel.mapper.SaleMapper;
import com.bookinghotel.projection.SaleProjection;
import com.bookinghotel.repository.RoomRepository;
import com.bookinghotel.repository.SaleRepository;
import com.bookinghotel.repository.UserRepository;
import com.bookinghotel.security.UserPrincipal;
import com.bookinghotel.service.SaleService;
import com.bookinghotel.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SaleServiceImpl implements SaleService {

  private final SaleRepository saleRepository;

  private final RoomRepository roomRepository;

  private final UserRepository userRepository;

  private final SaleMapper saleMapper;

  @Override
  public SaleDTO getSale(Long saleId) {
    SaleProjection sale = saleRepository.findSaleById(saleId);
    checkNotFoundSaleById(sale, saleId);
    return saleMapper.saleProjectionToSaleDTO(sale);
  }

  @Override
  public PaginationResponseDTO<SaleDTO> getSales(PaginationSearchSortRequestDTO requestDTO, Boolean deleteFlag) {
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.SALE);
    Page<SaleProjection> sales = saleRepository.findAllSale(requestDTO.getKeyword(), deleteFlag, pageable);
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.SALE, sales);
    return new PaginationResponseDTO<SaleDTO>(meta, toSaleDTOs(sales));
  }

  @Override
  public SaleDTO createSale(SaleCreateDTO createDTO, UserPrincipal principal) {
    User creator = userRepository.getUser(principal);
    Sale sale = saleMapper.createDtoToSale(createDTO);
    return saleMapper.toSaleDTO(saleRepository.save(sale), creator, creator);
  }

  @Override
  public SaleDTO updateSale(Long saleId, SaleUpdateDTO updateDTO, UserPrincipal principal) {
    Optional<Sale> currentSale = saleRepository.findById(saleId);
    checkNotFoundSaleById(currentSale, saleId);
    saleMapper.updateSaleFromDTO(updateDTO, currentSale.get());
    User updater = userRepository.getUser(principal);
    User creator = userRepository.findById(currentSale.get().getCreatedBy()).get();
    return saleMapper.toSaleDTO(saleRepository.save(currentSale.get()), creator, updater);
  }

  @Override
  public CommonResponseDTO addSalesToRoom(Long saleId, List<Long> roomIds) {
    Optional<Sale> sale = saleRepository.findById(saleId);
    checkNotFoundSaleById(sale, saleId);
    List<Room> rooms = roomRepository.findAllByIds(roomIds);
    for(Room room : rooms) {
      if(ObjectUtils.isEmpty(room.getSale())) {
        room.setSale(sale.get());
        roomRepository.save(room);
      } else {
        throw new InvalidException(String.format(ErrorMessage.Room.ROOM_HAS_BEEN_DISCOUNTED, room.getId()));
      }
    }
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.ADD_SUCCESS);
  }

  @Override
  public CommonResponseDTO removeSaleFromRoom(Long saleId, Long roomId) {
    Optional<Room> room = roomRepository.findById(roomId);
    checkNotFoundRoomById(room, roomId);
    checkRoomNotForSale(room.get());
    Optional<Sale> sale = saleRepository.findById(saleId);
    checkNotFoundSaleById(sale, saleId);
    if(room.get().getSale().getId().equals(saleId)) {
      room.get().setSale(null);
      roomRepository.save(room.get());
      return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
    } else {
      throw new InvalidException(String.format(ErrorMessage.Room.ROOM_NOT_FOR_SALE, roomId, saleId));
    }
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
  public CommonResponseDTO deleteSalePermanently(Long saleId) {
    Optional<Sale> sale = saleRepository.findByIdAndIsDeleteFlag(saleId);
    checkNotFoundSaleIsDeleteFlagById(sale, saleId);
    saleRepository.delete(sale.get());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.DELETE_SUCCESS);
  }

  @Override
  public CommonResponseDTO restoreSale(Long saleId) {
    Optional<Sale> sale = saleRepository.findByIdAndIsDeleteFlag(saleId);
    checkNotFoundSaleIsDeleteFlagById(sale, saleId);
    sale.get().setDeleteFlag(CommonConstant.FALSE);
    saleRepository.save(sale.get());
    return new CommonResponseDTO(CommonConstant.TRUE, CommonMessage.RESTORE_SUCCESS);
  }

  @Override
  @Transactional
  public void deleteSaleByDeleteFlag(Boolean isDeleteFlag, Integer daysToDeleteRecords) {
    saleRepository.deleteByDeleteFlag(isDeleteFlag, daysToDeleteRecords);
  }

  private List<SaleDTO> toSaleDTOs(Page<SaleProjection> saleProjections) {
    List<SaleDTO> saleDTOs = new LinkedList<>();
    for(SaleProjection saleProjection : saleProjections) {
      saleDTOs.add(saleMapper.saleProjectionToSaleDTO(saleProjection));
    }
    return saleDTOs;
  }

  private void checkNotFoundSaleById(Optional<Sale> sale, Long saleId) {
    if (sale.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.Sale.ERR_NOT_FOUND_ID, saleId));
    }
  }

  private void checkNotFoundSaleIsDeleteFlagById(Optional<Sale> sale, Long saleId) {
    if (sale.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.Sale.ERR_NOT_FOUND_ID_IN_TRASH, saleId));
    }
  }

  private void checkNotFoundSaleById(SaleProjection saleProjection, Long saleId) {
    if (ObjectUtils.isEmpty(saleProjection)) {
      throw new NotFoundException(String.format(ErrorMessage.Sale.ERR_NOT_FOUND_ID, saleId));
    }
  }

  private void checkNotFoundRoomById(Optional<Room> room, Long roomId) {
    if (room.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.Room.ERR_NOT_FOUND_ID, roomId));
    }
  }

  private void checkRoomNotForSale(Room room) {
    if(ObjectUtils.isEmpty(room.getSale())) {
      throw new InvalidException(String.format(ErrorMessage.Room.ROOM_NO_SALE, room.getId()));
    }
  }

}
