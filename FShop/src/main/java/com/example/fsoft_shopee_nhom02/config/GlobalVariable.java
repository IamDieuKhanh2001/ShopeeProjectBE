package com.example.fsoft_shopee_nhom02.config;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class GlobalVariable {
    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static Timestamp getCurrentDateTime() throws ParseException {
        return new Timestamp(new Date().getTime());
    }

    public static String GetOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public static String GetRandom3DigitNumber() {
        return String.format("%04d", new Random().nextInt(9999));
    }
    public enum ORDER_STATUS {
        DONE,
        CANCELED,
        PENDING,
    }
}
