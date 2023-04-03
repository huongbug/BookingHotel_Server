package com.bookinghotel.controller;

import com.bookinghotel.base.RestApiV1;
import com.bookinghotel.base.VsResponseUtil;
import com.bookinghotel.constant.RoleConstant;
import com.bookinghotel.constant.UrlConstant;
import com.bookinghotel.dto.BookingCreateDTO;
import com.bookinghotel.dto.common.DateFilterDTO;
import com.bookinghotel.dto.pagination.PaginationSortRequestDTO;
import com.bookinghotel.security.AuthorizationInfo;
import com.bookinghotel.security.CurrentUserLogin;
import com.bookinghotel.security.UserPrincipal;
import com.bookinghotel.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestApiV1
public class BookingController {

  private final BookingService bookingService;

  @Operation(summary = "API get booking by id")
  @AuthorizationInfo(role = { RoleConstant.ADMIN, RoleConstant.USER })
  @GetMapping(UrlConstant.Booking.GET_BOOKING)
  public ResponseEntity<?> getBookingById(@PathVariable Long bookingId) {
    return VsResponseUtil.ok(bookingService.getBookingById(bookingId));
  }

  @Operation(summary = "API get booking for user")
  @AuthorizationInfo(role = { RoleConstant.USER })
  @GetMapping(UrlConstant.Booking.GET_BOOKINGS_FOR_USER)
  public ResponseEntity<?> getBookingsForUser(@Valid @ParameterObject PaginationSortRequestDTO pagination,
                                              @Parameter(name = "principal", hidden = true) @CurrentUserLogin UserPrincipal principal) {
    return VsResponseUtil.ok(bookingService.getBookingsForUser(pagination, principal));
  }

  @Tag(name = "booking-controller-admin")
  @Operation(summary = "API get booking for admin")
  @AuthorizationInfo(role = { RoleConstant.ADMIN })
  @GetMapping(UrlConstant.Booking.GET_BOOKINGS_FOR_ADMIN)
  public ResponseEntity<?> getBookingsForAdmin(@Valid @ParameterObject PaginationSortRequestDTO pagination,
                                               @ParameterObject DateFilterDTO dateFilterDTO) {
    return VsResponseUtil.ok(bookingService.getBookingsForAdmin(pagination, dateFilterDTO));
  }

  @Operation(summary = "API create booking room")
  @AuthorizationInfo(role = { RoleConstant.ADMIN, RoleConstant.USER })
  @PostMapping(value = UrlConstant.Booking.CREATE_BOOKING)
  public ResponseEntity<?> createBooking(@Valid @RequestBody BookingCreateDTO bookingCreateDTO,
                                             @Parameter(name = "principal", hidden = true) @CurrentUserLogin UserPrincipal principal) {
    return VsResponseUtil.ok(bookingService.createBooking(bookingCreateDTO, principal));
  }

}


