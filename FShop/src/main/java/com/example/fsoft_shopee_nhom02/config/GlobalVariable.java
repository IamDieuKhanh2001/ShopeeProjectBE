package com.example.fsoft_shopee_nhom02.config;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GlobalVariable {
    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public static Timestamp getCurrentDateTime() throws ParseException {
        return new Timestamp(datetimeFormat.parse(datetimeFormat.format(new Date())).getTime());
    }

    public static String GetOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

}
