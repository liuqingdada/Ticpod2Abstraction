package com.android.lib.ticpod.fastble.utils;

import android.util.Log;

public final class LogUtil {

    public static boolean isPrint = true;
    private static String defaultTag = "FastBle";

    public static void d(String msg) {
        if (isPrint && msg != null) {
            Log.d(defaultTag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isPrint && msg != null) {
            Log.d(tag, msg);
        }
    }

    public static void i(String msg) {
        if (isPrint && msg != null) {
            Log.i(defaultTag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isPrint && msg != null) {
            Log.i(tag, msg);
        }
    }

    public static void w(String msg) {
        if (isPrint && msg != null) {
            Log.w(defaultTag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isPrint && msg != null) {
            Log.w(tag, msg);
        }
    }

    public static void e(String msg) {
        if (isPrint && msg != null) {
            Log.e(defaultTag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isPrint && msg != null) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isPrint && msg != null) {
            Log.e(tag, msg, tr);
        }
    }
}
