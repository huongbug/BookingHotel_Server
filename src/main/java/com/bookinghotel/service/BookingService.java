package com.bookinghotel.service;

import com.bookinghotel.dto.BookingCreateDTO;
import com.bookinghotel.dto.BookingDTO;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.common.DateFilterDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSortRequestDTO;
import com.bookinghotel.security.UserPrincipal;

public interface BookingService {

  BookingDTO getBookingById(Long bookingId);

  PaginationResponseDTO<BookingDTO> getBookingsForUser(PaginationSortRequestDTO requestDTO, UserPrincipal principal);

  PaginationResponseDTO<BookingDTO> getBookingsForAdmin(PaginationSortRequestDTO requestDTO, DateFilterDTO dateFilterDTO);

  BookingDTO createBooking(BookingCreateDTO bookingCreateDTO, UserPrincipal principal);

  BookingDTO checkIn(Long bookingId);

  BookingDTO checkOutAndPayment(Long bookingId);

  CommonResponseDTO cancelBooking(Long bookingId, String note);

}
