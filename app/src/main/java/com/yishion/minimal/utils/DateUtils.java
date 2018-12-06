package com.yishion.minimal.utils;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String TIME_24_FORMAT = "MMM dd, yyyy HH:mm:ss a";
    private static final String TIME_12_FORMAT = "MMM dd, yyyy k:mm:ss";

    public static final String DATE_FORMAT = "MMM d, yyyy";

    public static final String TIME_FORMAT_12 = "hh:mm";
    public static final String TIME_FORMAT_24 = "HH:mm";

    private static SimpleDateFormat sdf = new SimpleDateFormat(TIME_24_FORMAT, Locale.getDefault());

    public static String format(Context context, Date date) {
        if (context != null) {
            boolean time24 = DateFormat.is24HourFormat(context);
            if (time24) {
                sdf.applyPattern(TIME_24_FORMAT);
            }
            else {
                sdf.applyPattern(TIME_12_FORMAT);
            }
            return sdf.format(date);
        }

        return "";

    }


    public static String formatDate(Context context, Date date) {
        if (context != null) {
            sdf.applyPattern(DATE_FORMAT);
            return sdf.format(date);
        }
        return "";
    }


    public static String formatTime(Context context, Date date) {
        if (context != null) {
            boolean time24 = DateFormat.is24HourFormat(context);
            if (time24) {
                sdf.applyPattern(TIME_FORMAT_24);
            }
            else {
                sdf.applyPattern(TIME_FORMAT_12);
            }
            return sdf.format(date);
        }

        return "";

    }

    public static String format(Context context, String format, Date date) {
        if (context != null) {
            boolean time24 = DateFormat.is24HourFormat(context);
            if (time24) {
                sdf.applyPattern(format);
                return sdf.format(date);
            }
        }

        return "";

    }

}
