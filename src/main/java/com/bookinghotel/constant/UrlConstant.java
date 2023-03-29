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

    public static final String UPDATE_USER = PRE_FIX + "/update/{userId}";

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

}
