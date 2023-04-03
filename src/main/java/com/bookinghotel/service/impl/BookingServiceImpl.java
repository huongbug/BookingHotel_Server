package com.bookinghotel.service.impl;

import com.bookinghotel.constant.BookingStatus;
import com.bookinghotel.constant.CommonConstant;
import com.bookinghotel.constant.ErrorMessage;
import com.bookinghotel.constant.SortByDataConstant;
import com.bookinghotel.dto.*;
import com.bookinghotel.dto.common.CommonResponseDTO;
import com.bookinghotel.dto.common.DateFilterDTO;
import com.bookinghotel.dto.pagination.PaginationResponseDTO;
import com.bookinghotel.dto.pagination.PaginationSortRequestDTO;
import com.bookinghotel.dto.pagination.PagingMeta;
import com.bookinghotel.entity.Booking;
import com.bookinghotel.entity.BookingRoomDetail;
import com.bookinghotel.entity.BookingServiceDetail;
import com.bookinghotel.entity.User;
import com.bookinghotel.exception.InvalidException;
import com.bookinghotel.exception.NotFoundException;
import com.bookinghotel.mapper.BookingMapper;
import com.bookinghotel.mapper.UserMapper;
import com.bookinghotel.repository.BookingRepository;
import com.bookinghotel.repository.UserRepository;
import com.bookinghotel.security.UserPrincipal;
import com.bookinghotel.service.BookingRoomDetailService;
import com.bookinghotel.service.BookingService;
import com.bookinghotel.service.BookingServiceDetailService;
import com.bookinghotel.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class BookingServiceImpl implements BookingService {

  private final BookingRepository bookingRepository;

  private final UserRepository userRepository;

  private final BookingRoomDetailService bookingRoomDetailService;

  private final BookingServiceDetailService bookingServiceDetailService;

  private final BookingMapper bookingMapper;

  private final UserMapper userMapper;

  @Override
  public BookingDTO getBookingById(Long bookingId) {
    Optional<Booking> booking = bookingRepository.findById(bookingId);
    checkNotFoundBookingById(booking, bookingId);
    Optional<User> creator = userRepository.findById(booking.get().getCreatedBy());
    Optional<User> updater = userRepository.findById(booking.get().getLastModifiedBy());
    return mapperToBookingDTO(booking.get(), creator.get(), updater.get());
  }

  @Override
  public PaginationResponseDTO<BookingDTO> getBookingsForUser(PaginationSortRequestDTO requestDTO, UserPrincipal principal) {
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.BOOKING);
    Page<Booking> bookings = bookingRepository.findAllForUser(pageable, principal.getId());
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.BOOKING, bookings);
    List<BookingDTO> bookingDTOs = mapperToBookingDTOs(bookings.getContent());
    return new PaginationResponseDTO<BookingDTO>(meta, bookingDTOs);
  }

  @Override
  public PaginationResponseDTO<BookingDTO> getBookingsForAdmin(PaginationSortRequestDTO requestDTO, DateFilterDTO filterDTO) {
    //Pagination
    Pageable pageable = PaginationUtil.buildPageable(requestDTO, SortByDataConstant.BOOKING);
    Page<Booking> bookings = bookingRepository.findAllForAdmin(pageable, filterDTO);
    //Create Output
    PagingMeta meta = PaginationUtil.buildPagingMeta(requestDTO, SortByDataConstant.BOOKING, bookings);
    List<BookingDTO> bookingDTOs = mapperToBookingDTOs(bookings.getContent());
    return new PaginationResponseDTO<BookingDTO>(meta, bookingDTOs);
  }

  @Override
  @Transactional
  public BookingDTO createBooking(BookingCreateDTO bookingCreateDTO, UserPrincipal principal) {
    LocalDate now = LocalDate.now();
    checkExpectedCheckIn(now, bookingCreateDTO.getExpectedCheckIn(), bookingCreateDTO.getExpectedCheckOut());
    checkExpectedCheckOut(now, bookingCreateDTO.getExpectedCheckIn(), bookingCreateDTO.getExpectedCheckOut());
    User creator = userRepository.getUser(principal);
    Booking booking = new Booking();
    booking.setExpectedCheckIn(bookingCreateDTO.getExpectedCheckIn());
    booking.setExpectedCheckOut(bookingCreateDTO.getExpectedCheckOut());
    booking.setStatus(BookingStatus.PENDING);
    booking.setUser(creator);
    bookingRepository.save(booking);
    //mapper to output
    Booking bookingCreated = bookingMapper.toBookingCreated(booking);
    Set<BookingRoomDetail> bookingRoomDetails = bookingRoomDetailService.createBookingRoomDetails(booking, bookingCreateDTO.getRoomIds());
    bookingCreated.setBookingRoomDetails(bookingRoomDetails);
    if (!bookingCreateDTO.getServices().isEmpty()) {
      Set<BookingServiceDetail> bookingServiceDetails =
          bookingServiceDetailService.createBookingServiceDetails(booking, bookingCreateDTO.getServices());
      bookingCreated.setBookingServiceDetails(bookingServiceDetails);
    }
    return mapperToBookingDTO(bookingCreated, creator, creator);
  }

  @Override
  public BookingDTO checkIn(Long bookingId) {
    Optional<Booking> booking = bookingRepository.findById(bookingId);
    checkNotFoundBookingById(booking, bookingId);
    return null;
  }

  @Override
  public BookingDTO checkOutAndPayment(Long bookingId) {
    Optional<Booking> booking = bookingRepository.findById(bookingId);
    checkNotFoundBookingById(booking, bookingId);
    return null;
  }

  @Override
  public CommonResponseDTO cancelBooking(Long bookingId, String note) {
    return null;
  }

  private List<BookingDTO> mapperToBookingDTOs(List<Booking> bookings) {
    List<BookingDTO> bookingDTOs = new LinkedList<>();
    for (Booking booking : bookings) {
      Optional<User> creator = userRepository.findById(booking.getCreatedBy());
      Optional<User> updater = userRepository.findById(booking.getLastModifiedBy());
      BookingDTO bookingDTO = mapperToBookingDTO(booking, creator.get(), updater.get());
      bookingDTOs.add(bookingDTO);
    }
    return bookingDTOs;
  }

  private BookingDTO mapperToBookingDTO(Booking booking, User creator, User updater) {
    BookingDTO bookingDTO = new BookingDTO();
    bookingDTO.setId(booking.getId());
    bookingDTO.setStatus(booking.getStatus());
    bookingDTO.setNote(booking.getNote());
    bookingDTO.setCreatedDate(booking.getCreatedDate());
    bookingDTO.setLastModifiedDate(booking.getLastModifiedDate());
    bookingDTO.setCreatedBy(userMapper.toCreatedByDTO(creator));
    bookingDTO.setLastModifiedBy(userMapper.toLastModifiedByDTO(updater));
    //check booking status to calculate surcharge and set checkin checkout
    if (booking.getStatus().equals(BookingStatus.CHECKED_IN)) {
      bookingDTO.setCheckIn(booking.getCheckIn());
      bookingDTO.setCheckOut(booking.getExpectedCheckOut());
      bookingDTO.setSurcharges(calculateSurcharge(booking));
    } else if (booking.getStatus().equals(BookingStatus.CHECKED_OUT)) {
      bookingDTO.setCheckIn(booking.getCheckIn());
      bookingDTO.setCheckOut(booking.getCheckOut());
      bookingDTO.setSurcharges(calculateSurcharge(booking));
    } else {
      bookingDTO.setCheckIn(booking.getExpectedCheckIn());
      bookingDTO.setCheckOut(booking.getExpectedCheckOut());
    }
    //room
    bookingDTO.setTotalRoomPrice(calculateTotalRoomPrice(booking));
    List<BookingRoomDetailDTO> bookingRoomDetailDTOs = bookingMapper.toBookingRoomDetailDTOs(booking.getBookingRoomDetails());
    bookingDTO.setRooms(bookingRoomDetailDTOs);
    //service
    bookingDTO.setTotalServicePrice(calculateTotalServicePrice(booking));
    List<BookingServiceDetailDTO> bookingServiceDetailDTOs = bookingMapper.toBookingServiceDetailDTOs(booking.getBookingServiceDetails());
    bookingDTO.setServices(bookingServiceDetailDTOs);
    return bookingDTO;
  }

  private Long calculateTotalRoomPrice(Booking booking) {
    long totalRoomPrice = 0L;
    Long totalDay = Math.round(ChronoUnit.HOURS.between(booking.getExpectedCheckIn(), booking.getExpectedCheckOut()) / 24d);
    for (BookingRoomDetail bookingRoomDetail : booking.getBookingRoomDetails()) {
      if(bookingRoomDetail.getSalePercent() != null) {
        double salePrice = bookingRoomDetail.getPrice() * (bookingRoomDetail.getSalePercent() / 100f);
        totalRoomPrice += (bookingRoomDetail.getPrice() - salePrice) * totalDay;
      } else {
        totalRoomPrice += bookingRoomDetail.getPrice() * totalDay;
      }
    }
    return totalRoomPrice;
  }

  private List<BookingSurchargeDTO> calculateSurcharge(Booking booking) {
    List<BookingSurchargeDTO> roomSurcharges = new LinkedList<>();
    if(booking.getCheckIn() != null && !booking.getCheckIn().equals(LocalDateTime.MIN)) {
      roomSurcharges.add(calculateTotalCheckInSurcharge(booking));
    }
    if(booking.getCheckOut() != null && !booking.getCheckOut().equals(LocalDateTime.MIN)) {
      roomSurcharges.add(calculateTotalCheckOutSurcharge(booking));
    }
    return roomSurcharges;
  }

  private BookingSurchargeDTO calculateTotalCheckInSurcharge(Booking booking) {
    LocalDateTime checkIn = booking.getCheckIn();
    LocalDate dateCheckIn = checkIn.toLocalDate();
    LocalDateTime date_5h = LocalDateTime.of(dateCheckIn, CommonConstant.TIME_5H00);
    LocalDateTime date_9h = LocalDateTime.of(dateCheckIn, CommonConstant.TIME_9H00);
    LocalDateTime date_14h = LocalDateTime.of(dateCheckIn, CommonConstant.TIME_14H00);
    BookingSurchargeDTO checkInSurcharge = new BookingSurchargeDTO();
    long totalCheckInSurcharge = 0L;
    if (checkIn.isBefore(date_5h)) {
      checkInSurcharge.setReason("You check-in before 5h. You pay 100% more of the total room price");
      for (BookingRoomDetail bookingRoomDetail : booking.getBookingRoomDetails()) {
        totalCheckInSurcharge += bookingRoomDetail.getPrice();
      }
    } else if (checkIn.isAfter(date_5h) && checkIn.isBefore(date_9h)) {
      checkInSurcharge.setReason("You check-in from 5h to 9h. You pay 50% more of the total room price");
      for (BookingRoomDetail bookingRoomDetail : booking.getBookingRoomDetails()) {
        totalCheckInSurcharge += Math.round(bookingRoomDetail.getPrice() * 0.5);
      }
    } else if (checkIn.isAfter(date_9h) && checkIn.isBefore(date_14h)) {
      checkInSurcharge.setReason("You check-in from 9h to 14h. You pay 30% more of the total room price");
      for (BookingRoomDetail bookingRoomDetail : booking.getBookingRoomDetails()) {
        totalCheckInSurcharge += Math.round(bookingRoomDetail.getPrice() * 0.3);
      }
    } else {
      return null;
    }
    checkInSurcharge.setRoomSurcharge(totalCheckInSurcharge);
    return checkInSurcharge;
  }

  private BookingSurchargeDTO calculateTotalCheckOutSurcharge(Booking booking) {
    LocalDateTime checkOut = booking.getCheckOut();
    LocalDate dateCheckOut = checkOut.toLocalDate();
    LocalDateTime date_12h = LocalDateTime.of(dateCheckOut, CommonConstant.TIME_12H00);
    LocalDateTime date_15h = LocalDateTime.of(dateCheckOut, CommonConstant.TIME_15H00);
    LocalDateTime date_18h = LocalDateTime.of(dateCheckOut, CommonConstant.TIME_18H00);
    BookingSurchargeDTO checkOutSurcharge = new BookingSurchargeDTO();
    long totalCheckOutSurcharge = 0L;
    if (checkOut.isAfter(date_12h) && checkOut.isBefore(date_15h)) {
      checkOutSurcharge.setReason("You check-out from 12h to 15h. You pay 30% more of the total room price");
      for (BookingRoomDetail bookingRoomDetail : booking.getBookingRoomDetails()) {
        totalCheckOutSurcharge += Math.round(bookingRoomDetail.getPrice() * 0.3);
      }
    } else if (checkOut.isAfter(date_15h) && checkOut.isBefore(date_18h)) {
      checkOutSurcharge.setReason("You check-out from 15h to 18h. You pay 50% more of the total room price");
      for (BookingRoomDetail bookingRoomDetail : booking.getBookingRoomDetails()) {
        totalCheckOutSurcharge += Math.round(bookingRoomDetail.getPrice() * 0.5);
      }
    } else if (checkOut.isAfter(date_18h)) {
      checkOutSurcharge.setReason("You check-out after 18h. You pay 100% more of the total room price");
      for (BookingRoomDetail bookingRoomDetail : booking.getBookingRoomDetails()) {
        totalCheckOutSurcharge += bookingRoomDetail.getPrice();
      }
    } else {
      return null;
    }
    checkOutSurcharge.setRoomSurcharge(totalCheckOutSurcharge);
    return checkOutSurcharge;
  }

  private Long calculateTotalServicePrice(Booking booking) {
    long totalServicePrice = 0L;
    for (BookingServiceDetail bookingServiceDetail : booking.getBookingServiceDetails()) {
      totalServicePrice += (bookingServiceDetail.getPrice() * bookingServiceDetail.getAmount());
    }
    return totalServicePrice;
  }

  private void checkExpectedCheckIn(LocalDate now, LocalDateTime expectedCheckIn, LocalDateTime expectedCheckOut) {
    LocalDate checkInLocalDate = expectedCheckIn.toLocalDate();
    LocalTime checkInLocalTime = expectedCheckIn.toLocalTime();
    LocalDate checkOutLocalDate = expectedCheckOut.toLocalDate();
    if(checkInLocalDate.isAfter(checkOutLocalDate) || checkInLocalDate.equals(checkOutLocalDate)
        || checkInLocalDate.isBefore(now) || !checkInLocalTime.equals(CommonConstant.TIME_14H00)) {
      throw new InvalidException(ErrorMessage.INVALID_DATE_CHECK_IN);
    }
  }

  private void checkExpectedCheckOut(LocalDate now, LocalDateTime expectedCheckIn, LocalDateTime expectedCheckOut) {
    LocalDate checkInLocalDate = expectedCheckIn.toLocalDate();
    LocalDate checkOutLocalDate = expectedCheckOut.toLocalDate();
    LocalTime checkOutLocalTime = expectedCheckOut.toLocalTime();
    if(checkOutLocalDate.isBefore(checkInLocalDate) || checkOutLocalDate.equals(checkInLocalDate)
        || checkOutLocalDate.isBefore(now) || checkOutLocalDate.equals(now)
        || !checkOutLocalTime.equals(CommonConstant.TIME_12H00)) {
      throw new InvalidException(ErrorMessage.INVALID_DATE_CHECK_OUT);
    }
  }

  private void checkNotFoundBookingById(Optional<Booking> booking, Long bookingId) {
    if (booking.isEmpty()) {
      throw new NotFoundException(String.format(ErrorMessage.Booking.ERR_NOT_FOUND_ID, bookingId));
    }
  }

}