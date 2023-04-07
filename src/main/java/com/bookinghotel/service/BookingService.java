package com.bookinghotel.service;

import com.bookinghotel.dto.BookingCreateDTO;
import com.bookinghotel.dto.BookingDTO;
import com.bookinghotel.dto.BookingFilterDTO;
import com.bookinghotel.dto.BookingSurchargeDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSortRequestDTO;
import com.bookinghotel.entity.Booking;
import com.bookinghotel.security.UserPrincipal;

import java.util.List;

public interface BookingService {

  BookingDTO getBookingById(Long bookingId);

  PaginationResponseDTO<BookingDTO> getBookingsForUser(PaginationSortRequestDTO requestDTO, UserPrincipal principal);

  PaginationResponseDTO<BookingDTO> getBookingsForAdmin(PaginationSortRequestDTO requestDTO, BookingFilterDTO bookingFilterDTO);

  BookingDTO createBooking(BookingCreateDTO bookingCreateDTO, UserPrincipal principal);

  BookingDTO checkIn(Long bookingId, UserPrincipal principal);

  BookingDTO checkOutAndPayment(Long bookingId, UserPrincipal principal);

  CommonResponseDTO cancelBooking(Long bookingId, String note, UserPrincipal principal);

  void lockUserRefuseToCheckIn();

  List<BookingDTO> mapperToBookingDTOs(List<Booking> bookings);

  Long calculateTotalRoomPrice(Booking booking);

  Long calculateTotalServicePrice(Booking booking);

  List<BookingSurchargeDTO> calculateSurcharge(Booking booking);

}
