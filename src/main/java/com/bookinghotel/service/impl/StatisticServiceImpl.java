package com.bookinghotel.service.impl;

import com.bookinghotel.constant.BookingStatus;
import com.bookinghotel.constant.CommonConstant;
import com.bookinghotel.constant.SortByDataConstant;
import com.bookinghotel.dto.*;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSearchSortRequestDTO;
import com.bookinghotel.dto.pagination.PagingMeta;
import com.bookinghotel.entity.Booking;
import com.bookinghotel.mapper.RoomMapper;
import com.bookinghotel.mapper.UserMapper;
import com.bookinghotel.projection.StatisticCustomerTopBookingProjection;
import com.bookinghotel.projection.StatisticRoomBookedProjection;
import com.bookinghotel.repository.BookingRepository;
import com.bookinghotel.repository.RoomRepository;
import com.bookinghotel.repository.UserRepository;
import com.bookinghotel.service.BookingService;
import com.bookinghotel.service.StatisticService;
import com.bookinghotel.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class StatisticServiceImpl implements StatisticService {

  private final UserRepository userRepository;

  private final RoomRepository roomRepository;

  private final BookingRepository bookingRepository;

  private final BookingService bookingService;

  private final RoomMapper roomMapper;

  private final UserMapper userMapper;

  @Override
  public PaginationResponseDTO<Map<String, Object>> statisticRoomBookedForMonth(PaginationSearchSortRequestDTO request,
                                                                                Integer month, Integer year) {
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(request, SortByDataConstant.ROOM);
    Page<StatisticRoomBookedProjection> roomBookedMonthProjections =
        roomRepository.statisticRoomBookedForMonth(month, year, request.getKeyword(), request.getSortType(), pageable);
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(request, SortByDataConstant.ROOM, roomBookedMonthProjections);
    List<Map<String, Object>> result = new LinkedList<>();
    for (StatisticRoomBookedProjection statisticRoomBookedProjection : roomBookedMonthProjections) {
      Map<String, Object> objectMap = new HashMap<>();
      RoomSummaryDTO roomSummaryDTO = roomMapper.statisticRoomToRoomDTO(statisticRoomBookedProjection);
      objectMap.put("room", roomSummaryDTO);
      objectMap.put("value", statisticRoomBookedProjection.getValue());
      result.add(objectMap);
    }
    return new PaginationResponseDTO<Map<String, Object>>(meta, result);
  }

  @Override
  public List<Map<String, Object>> statisticCustomerTopBooking() {
    List<StatisticCustomerTopBookingProjection> customerTopBooking = userRepository.findAllCustomerTopBooking();
    List<Map<String, Object>> result = new LinkedList<>();
    for(StatisticCustomerTopBookingProjection statisticCustomerTopBooking : customerTopBooking) {
      Map<String, Object> objectMap = new HashMap<>();
      UserDTO userDTO = userMapper.statisticCustomerTopBookingProjectionToUserDTO(statisticCustomerTopBooking);
      objectMap.put("user", userDTO);
      objectMap.put("value", statisticCustomerTopBooking.getValue());
      result.add(objectMap);
    }
    return result;
  }

  @Override
  public List<RevenueDTO> statisticRevenue(RevenueRequestDTO request) {
    List<RevenueDTO> revenueDTOs = new LinkedList<>();
    for(int year = request.getFromYear(); year <= request.getToYear(); year++) {
      List<RevenueDTO.RevenueMonthDTO> revenueMonthDTOs = new LinkedList<>();
      int totalBookingYear = 0;
      long totalRevenueYear = 0;
      //loop month
      for(int month = request.getFromMonth(); month <= request.getToMonth(); month++) {
        List<Booking> bookings = bookingRepository.statisticRevenueProjection(month, year);
        System.out.println(bookings.size());
        long totalRevenueMonth = 0;
        for(Booking booking : bookings) {
          totalRevenueMonth += bookingService.calculateTotalRoomPrice(booking);
          totalRevenueMonth += bookingService.calculateTotalServicePrice(booking);
          List<BookingSurchargeDTO> surchargeDTOs = bookingService.calculateSurcharge(booking);
          for(BookingSurchargeDTO surchargeDTO : surchargeDTOs) {
            totalRevenueMonth += surchargeDTO.getRoomSurcharge();
          }
        }
        RevenueDTO.RevenueMonthDTO revenueMonthDTO = new RevenueDTO.RevenueMonthDTO();
        revenueMonthDTO.setMonth(convertMonthNumberToString(month));
        revenueMonthDTO.setTotalBooking(bookings.size());
        revenueMonthDTO.setTotalRevenue(totalRevenueMonth);
        revenueMonthDTOs.add(revenueMonthDTO);
        totalBookingYear += bookings.size();
        totalRevenueYear += totalRevenueMonth;
      }
      //end loop month
      RevenueDTO revenueDTO = new RevenueDTO();
      revenueDTO.setYear(year);
      revenueDTO.setTotalBooking(totalBookingYear);
      revenueDTO.setTotalRevenue(totalRevenueYear);
      revenueDTO.setRevenueMonths(revenueMonthDTOs);
      revenueDTOs.add(revenueDTO);
    }
    return revenueDTOs;
  }

  @Override
  public BookingStatisticDTO statisticBookingForMonth(Integer month, Integer year) {
    BookingStatisticDTO bookingStatisticDTO = new BookingStatisticDTO();
    bookingStatisticDTO.setTotalBookingCheckin(bookingRepository.countBookingByStatus(month, year, BookingStatus.CHECKED_IN.toString()));
    bookingStatisticDTO.setTotalBookingCheckout(bookingRepository.countBookingByStatus(month, year, BookingStatus.CHECKED_OUT.toString()));
    bookingStatisticDTO.setTotalBookingPending(bookingRepository.countBookingByStatus(month, year, BookingStatus.PENDING.toString()));
    bookingStatisticDTO.setTotalBookingCancel(bookingRepository.countBookingByStatus(month, year, BookingStatus.CANCEL.toString()));
    return bookingStatisticDTO;
  }

  private String convertMonthNumberToString(int monthNumber) {
    switch (monthNumber) {
      case 1: return "January";
      case 2: return "February";
      case 3: return "March";
      case 4: return "April";
      case 5: return "May";
      case 6: return "June";
      case 7: return "July";
      case 8: return "August";
      case 9: return "September";
      case 10: return "October";
      case 11: return "November";
      case 12: return "December";
      default: return CommonConstant.EMPTY_STRING;
    }
  }

}
