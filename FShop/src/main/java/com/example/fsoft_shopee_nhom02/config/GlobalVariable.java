package com.example.fsoft_shopee_nhom02.config;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class GlobalVariable {
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public static java.sql.Date getCurrentDate() {
        return new Date(Calendar.getInstance().getTime().getTime());
    }

    public static String GetOTP() {
        Random r = new Random();
        return String.format("%06d", r.nextInt(999999));
    }
}
