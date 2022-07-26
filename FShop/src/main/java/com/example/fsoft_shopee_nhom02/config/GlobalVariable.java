package com.example.fsoft_shopee_nhom02.config;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class GlobalVariable {
    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public static Timestamp getCurrentDate() {
        return new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public static String GetOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
