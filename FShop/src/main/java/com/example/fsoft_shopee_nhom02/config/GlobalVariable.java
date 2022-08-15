package com.example.fsoft_shopee_nhom02.config;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GlobalVariable {
    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static Timestamp getCurrentDateTime() throws ParseException {
        return new Timestamp(new Date().getTime());
    }

    public static String GetOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public static String GetRandom4DigitNumber() {
        return String.format("%04d", new Random().nextInt(9999));
    }

    public enum ORDER_STATUS {
        DONE,
        CANCELED,
        PENDING,
    }

    public static final String ACTIVE_STATUS = "Active";
    public static final String INACTIVE_STATUS = "Inactive";

    public static final String Notification_destination = "/client";
    public static final String Notification_endpoint = "/NotificationService";
    public static final String Notification_sendPrefix = "/message";
    public static final String Notification_pushPrefix = "/user";

    public static final int OrderPagingLimit = 10;

    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_LIMIT = "12";
    public static final String NOT_FOUND_PRODUCT_MESSAGE = "Not found product id ";
}


