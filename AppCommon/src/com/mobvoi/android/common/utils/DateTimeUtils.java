package com.mobvoi.android.common.utils;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtils {
    private static final SimpleDateFormat READABLE_TIMESTAMP_FORMATTER
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.US);

    private static void updateTimeZoneIfNeeded() {
        if (!TimeZone.getDefault().equals(READABLE_TIMESTAMP_FORMATTER.getTimeZone())) {
            READABLE_TIMESTAMP_FORMATTER.setTimeZone(TimeZone.getDefault());
        }
    }

    /**
     * @param timeStr Time string in the format "yyyy-MM-dd HH:mm:ss:SSS"
     */
    public static long parseTimestamp(String timeStr) throws ParseException {
        updateTimeZoneIfNeeded();
        return READABLE_TIMESTAMP_FORMATTER.parse(timeStr).getTime();
    }

    /**
     * Generate file name from system time in the format "yyyy-MM-dd HH:mm:ss:SSS",
     * @param timeStamp System time in milliseconds
     */
    @NonNull
    public static String getReadableTimeStamp(long timeStamp) {
        updateTimeZoneIfNeeded();
        return READABLE_TIMESTAMP_FORMATTER.format(new Date(timeStamp));
    }

    /**
     * @param time Time in milliseconds. Must >= 0.
     */
    public static String getReadableTimeDuration(long time) {
        if (time <= 0) {
            return "0ms";
        }

        long milliseconds = time % 1000;

        time = time / 1000; // seconds
        long seconds = time % 60;

        time = time / 60; // to minutes
        long minutes = time % 60;

        time = time / 60; // to hours
        long hours = time % 24;

        long days = time / 24;

        String str = "";
        if (milliseconds > 0) {
            str = milliseconds + "ms";
        }
        if (seconds > 0) {
            str = seconds + "s" + str;
        }
        if (minutes > 0) {
            str = minutes + "m" + str;
        }
        if (hours > 0) {
            str = hours + "h" + str;
        }
        if (days > 0) {
            str = days + "d" + str;
        }

        return str;
    }
}
