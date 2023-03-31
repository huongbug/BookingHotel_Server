package com.bookinghotel.constant;

public class UrlConstant {

  public static class Auth {
    private static final String PRE_FIX = "/auth";

    public static final String LOGIN = PRE_FIX + "/login";

    public static final String SIGNUP = PRE_FIX + "/signup";
    public static final String VERIFY_SIGNUP = SIGNUP + "/verify";

    public static final String FORGOT_PASS = PRE_FIX + "/forgot-password";
    public static final String VERIFY_FORGOT_PASS = FORGOT_PASS + "/verify";

    public static final String LOGOUT = PRE_FIX + "/logout";

    private Auth() {
    }
  }

  public static class User {
    private static final String PRE_FIX = "/user";

    public static final String GET_USERS = PRE_FIX;
    public static final String GET_USER = PRE_FIX + "/{userId}";
    public static final String GET_CURRENT_USER = PRE_FIX + "/current";

    public static final String UPDATE_USER = PRE_FIX + "/update/{userId}";
    public static final String DELETE_USER = PRE_FIX + "/delete/{userId}";
    public static final String CHANGE_AVT_USER = PRE_FIX + "/avatar";

    private User() {
    }
  }

  public static class Room {
    private static final String PRE_FIX = "/room";

    public static final String GET_ROOMS = PRE_FIX;
    public static final String GET_ROOMS_AVAILABLE = PRE_FIX + "/available";
    public static final String GET_ROOM = PRE_FIX + "/{roomId}";

    public static final String CREATE_ROOM = PRE_FIX + "/create";
    public static final String UPDATE_ROOM = PRE_FIX + "/update/{roomId}";
    public static final String DELETE_ROOM = PRE_FIX + "/delete/{roomId}";

    private Room() {
    }
  }

  public static class Sale {
    private static final String PRE_FIX = "/sale";

    public static final String GET_SALES = PRE_FIX;
    public static final String GET_SALE = PRE_FIX + "/{saleId}";

    public static final String CREATE_SALE = PRE_FIX + "/create";

    public static final String UPDATE_SALE = PRE_FIX + "/update/{saleId}";

    public static final String DELETE_SALE = PRE_FIX + "/delete/{saleId}";

    public static final String ADD_SALE_TO_ROOM = PRE_FIX + "/add/room/{saleId}";
    public static final String REMOVE_SALE_FROM_ROOM = PRE_FIX + "/remove/room/{saleId}/{roomId}";

    private Sale() {
    }
  }

  public static class Service {
    private static final String PRE_FIX = "/service";

    public static final String GET_SERVICES = PRE_FIX;
    public static final String GET_SERVICE = PRE_FIX + "/{serviceId}";
    public static final String GET_PRODUCTS_BY_SERVICE = PRE_FIX + "/{serviceId}/products";

    public static final String CREATE_SERVICE = PRE_FIX + "/create";

    public static final String UPDATE_SERVICE = PRE_FIX + "/update/{serviceId}";

    public static final String DELETE_SERVICE = PRE_FIX + "/delete/{serviceId}";

    private Service() {
    }
  }

  public static class Product {
    private static final String PRE_FIX = "/product";

    public static final String GET_PRODUCTS = PRE_FIX;
    public static final String GET_PRODUCT = PRE_FIX + "/{productId}";

    public static final String CREATE_PRODUCT = PRE_FIX + "/create";

    public static final String UPDATE_PRODUCT = PRE_FIX + "/update/{productId}";

    public static final String DELETE_PRODUCTS = PRE_FIX + "/delete/{productId}";

    private Product() {
    }
  }

}
