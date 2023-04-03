package com.bookinghotel.mapper;

import com.bookinghotel.dto.BookingRoomDetailDTO;
import com.bookinghotel.dto.BookingServiceDetailDTO;
import com.bookinghotel.entity.Booking;
import com.bookinghotel.entity.BookingRoomDetail;
import com.bookinghotel.entity.BookingServiceDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookingMapper {

  Booking toBookingCreated(Booking booking);

  @Mappings({
      @Mapping(target = "id", source = "bookingRoomDetail.room.id"),
      @Mapping(target = "title", source = "bookingRoomDetail.room.title"),
      @Mapping(target = "type", source = "bookingRoomDetail.room.type"),
      @Mapping(target = "maxNum", source = "bookingRoomDetail.room.maxNum"),
      @Mapping(target = "floor", source = "bookingRoomDetail.room.floor"),
      @Mapping(target = "price", source = "bookingRoomDetail.price"),
      @Mapping(target = "salePercent", source = "bookingRoomDetail.salePercent")
  })
  BookingRoomDetailDTO toBookingRoomDetailDTO(BookingRoomDetail bookingRoomDetail);

  List<BookingRoomDetailDTO> toBookingRoomDetailDTOs(Set<BookingRoomDetail> bookingRoomDetails);

  @Mappings({
      @Mapping(target = "service.id", source = "bookingServiceDetail.service.id"),
      @Mapping(target = "service.title", source = "bookingServiceDetail.service.title"),
      @Mapping(target = "service.thumbnail", source = "bookingServiceDetail.service.thumbnail"),
      @Mapping(target = "service.price", source = "bookingServiceDetail.price"),
      @Mapping(target = "amount", source = "bookingServiceDetail.amount")
  })
  BookingServiceDetailDTO toBookingServiceDetailDTO(BookingServiceDetail bookingServiceDetail);

  List<BookingServiceDetailDTO> toBookingServiceDetailDTOs(Set<BookingServiceDetail> bookingServiceDetails);

}
