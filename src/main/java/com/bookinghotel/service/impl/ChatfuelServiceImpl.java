package com.bookinghotel.service.impl;

import com.bookinghotel.constant.CommonConstant;
import com.bookinghotel.constant.ErrorMessage;
import com.bookinghotel.constant.MediaType;
import com.bookinghotel.dto.chatfuel.ChatfuelResponse;
import com.bookinghotel.dto.chatfuel.GalleriesResponse;
import com.bookinghotel.dto.chatfuel.SendImageResponse;
import com.bookinghotel.dto.chatfuel.TextResponse;
import com.bookinghotel.entity.*;
import com.bookinghotel.exception.NotFoundException;
import com.bookinghotel.repository.RoomRepository;
import com.bookinghotel.repository.ServiceRepository;
import com.bookinghotel.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ChatfuelServiceImpl implements ChatBotService {

  private final RoomRepository roomRepository;

  private final ServiceRepository serviceRepository;

  private final NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CommonConstant.PATTERN_DATE_TIME);

  @Value("${booking-hotel.server.url}")
  private String serverBaseUrl;

  @Override
  public ChatfuelResponse<GalleriesResponse> getRoomsByType(String typeRoom) {
    List<Room> rooms = roomRepository.findByTypeForChatBot(typeRoom);
    GalleriesResponse.Attachment.Payload payload = new GalleriesResponse.Attachment.Payload();
    List<GalleriesResponse.Attachment.Payload.Element> elements = new LinkedList<>();
    for(Room room : rooms) {
      elements.add(setElementRoomByType(room));
    }
    payload.setElements(elements);
    return setGalleriesResponse(payload);
  }

  @Override
  public ChatfuelResponse<Object> getRoomById(Long id) {
    Room room = roomRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(String.format(ErrorMessage.Room.ERR_NOT_FOUND_ID, id)));
    ChatfuelResponse<Object> chatfuelResponse = new ChatfuelResponse<>();
    List<Object> objects = new LinkedList<>();
    for(Media media : room.getMedias()) {
      if(media.getType().equals(MediaType.Image)) {
        objects.add(setSendImageResponse(media.getUrl()));
      }
    }
    objects.addAll(setTextResponseRoomDetail(room));
    Sale sale = room.getSale();
    LocalDateTime now = LocalDateTime.now();
    if (ObjectUtils.isNotEmpty(sale) && sale.getDeleteFlag().equals(CommonConstant.FALSE)
        && sale.getDayStart().isBefore(now) && now.isAfter(sale.getDayEnd())) {
      objects.addAll(setTextResponseSale(sale));
    }
    chatfuelResponse.setMessages(objects);
    return chatfuelResponse;
  }

  @Override
  public ChatfuelResponse<GalleriesResponse> getHotelServices() {
    List<Service> services = serviceRepository.findAllForChatBot();
    GalleriesResponse.Attachment.Payload payload = new GalleriesResponse.Attachment.Payload();
    List<GalleriesResponse.Attachment.Payload.Element> elements = new LinkedList<>();
    for(Service service : services) {
      elements.add(setElementService(service));
    }
    payload.setElements(elements);
    return setGalleriesResponse(payload);
  }

  @Override
  public ChatfuelResponse<Object> getHotelServiceById(Long id) {
    Service service = serviceRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(String.format(ErrorMessage.Service.ERR_NOT_FOUND_ID, id)));
    ChatfuelResponse<Object> chatfuelResponse = new ChatfuelResponse<>();
    List<Object> objects = new LinkedList<>();
    objects.add(setSendImageResponse(service.getThumbnail()));
    objects.addAll(setTextResponseServiceDetail(service));
    if(CollectionUtils.isNotEmpty(service.getProducts())) {
      List<Product> products = new ArrayList<>(service.getProducts());
      int productsSize = products.size();
      objects.add(new TextResponse("Dịch vụ sẽ đi kèm với các sản phẩm sau:"));
      for(int i = 0; i < productsSize; i++) {
        objects.add(new TextResponse("Thông tin sản phẩm thứ " + (i + 1)));
        objects.add(setSendImageResponse(products.get(i).getThumbnail()));
        objects.addAll(setTextResponseProductDetail(products.get(i)));
      }
    }
    chatfuelResponse.setMessages(objects);
    return chatfuelResponse;
  }

  private ChatfuelResponse<GalleriesResponse> setGalleriesResponse(GalleriesResponse.Attachment.Payload payload) {
    ChatfuelResponse<GalleriesResponse> chatfuelResponse = new ChatfuelResponse<>();
    GalleriesResponse galleriesResponse = new GalleriesResponse();
    GalleriesResponse.Attachment attachment = new GalleriesResponse.Attachment();
    attachment.setPayload(payload);
    galleriesResponse.setAttachment(attachment);
    chatfuelResponse.setMessages(List.of(galleriesResponse));
    return chatfuelResponse;
  }

  private GalleriesResponse.Attachment.Payload.Element setElementRoomByType(Room room) {
    GalleriesResponse.Attachment.Payload.Element element = new GalleriesResponse.Attachment.Payload.Element();
    element.setTitle(room.getName());
    List<Media> media = new LinkedList<>(room.getMedias());
    element.setImage_url(media.get(0).getUrl());
    element.setSubtitle(currencyVN.format(room.getPrice()) + "/đêm");
    GalleriesResponse.Attachment.Payload.Element.Button button = new GalleriesResponse.Attachment.Payload.Element.Button();
    button.setTitle("Xem chi tiết");
    button.setType("json_plugin_url");
    button.setUrl(serverBaseUrl + "/chatbot/room/" + room.getId());
    element.setButtons(List.of(button));
    return element;
  }

  private SendImageResponse setSendImageResponse(String url) {
    SendImageResponse imageResponse = new SendImageResponse();
    SendImageResponse.Attachment attachment = new SendImageResponse.Attachment();
    SendImageResponse.Attachment.Payload payload = new SendImageResponse.Attachment.Payload();
    payload.setUrl(url);
    attachment.setPayload(payload);
    imageResponse.setAttachment(attachment);
    return imageResponse;
  }

  private List<TextResponse> setTextResponseRoomDetail(Room room) {
    List<TextResponse> textResponses = new LinkedList<>();
    textResponses.add(new TextResponse(room.getName()));
    textResponses.add(new TextResponse("\uD83D\uDCA5 Giá " + currencyVN.format(room.getPrice()) + "/đêm"));
    textResponses.add(new TextResponse("Loại phòng " + room.getType()));
    textResponses.add(new TextResponse(room.getBed()));
    textResponses.add(new TextResponse(String.format("Phòng có diện tích %sm2", room.getSize())));
    textResponses.add(new TextResponse(String.format("Phòng ở tối đa được %s người", room.getCapacity())));
    textResponses.add(new TextResponse("Phòng có các dịch vụ sẵn có như: " + room.getServices()));
    textResponses.add(new TextResponse(room.getDescription()));
    return textResponses;
  }

  private List<TextResponse> setTextResponseSale(Sale sale) {
    List<TextResponse> textResponses = new LinkedList<>();
    textResponses.add(new TextResponse("Phòng đang được giảm giá " + sale.getSalePercent() + "%"));
    textResponses.add(new TextResponse("Ngày kết thúc: " + formatter.format(sale.getDayEnd())));
    return textResponses;
  }

  private GalleriesResponse.Attachment.Payload.Element setElementService(Service service) {
    GalleriesResponse.Attachment.Payload.Element element = new GalleriesResponse.Attachment.Payload.Element();
    element.setTitle(service.getTitle());
    element.setImage_url(service.getThumbnail());
    element.setSubtitle(currencyVN.format(service.getPrice()) + "/người");
    GalleriesResponse.Attachment.Payload.Element.Button button = new GalleriesResponse.Attachment.Payload.Element.Button();
    button.setTitle("Xem chi tiết");
    button.setType("json_plugin_url");
    button.setUrl(serverBaseUrl + "/chatbot/service/" + service.getId());
    element.setButtons(List.of(button));
    return element;
  }

  private List<TextResponse> setTextResponseServiceDetail(Service service) {
    List<TextResponse> textResponses = new LinkedList<>();
    textResponses.add(new TextResponse(service.getTitle()));
    textResponses.add(new TextResponse("\uD83D\uDCA5 Giá " + currencyVN.format(service.getPrice()) + "/người"));
    textResponses.add(new TextResponse(service.getDescription()));
    return textResponses;
  }

  private List<TextResponse> setTextResponseProductDetail(Product product) {
    List<TextResponse> textResponses = new LinkedList<>();
    textResponses.add(new TextResponse(product.getName()));
    textResponses.add(new TextResponse(product.getDescription()));
    return textResponses;
  }

}
