package com.example.cem300assignment.Common;

import android.location.Location;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final  String APP_ID ="6027c029fd9273df4d35f9c5bb8e7fd3";
    public static Location current_location = null;

    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM EEE dd/MM/yy");
        String formatted = sdf.format(date);
        return formatted;
    }

    public static String convertUnixToHour(long dt) {
        Date date = new Date(dt * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM ");
        return sdf.format(date);
    }
}
