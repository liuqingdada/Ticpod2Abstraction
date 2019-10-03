package com.mobvoi.android.common.utils;

import androidx.annotation.NonNull;

import java.util.Locale;

public class ArrayUtils {
    public static String toString(int[] data) {
        return String.format(Locale.US, "Array[obj=%s, length=%d]",
                data, (data != null ? data.length : 0));
    }

    public static String toString(boolean[] data) {
        return String.format(Locale.US, "Array[obj=%s, length=%d]",
                data, (data != null ? data.length : 0));
    }

    public static String toString(long[] data) {
        return String.format(Locale.US, "Array[obj=%s, length=%d]",
                data, (data != null ? data.length : 0));
    }

    public static String toString(short[] data) {
        return String.format(Locale.US, "Array[obj=%s, length=%d]",
                data, (data != null ? data.length : 0));
    }

    public static String toString(byte[] data) {
        return String.format(Locale.US, "Array[obj=%s, length=%d]",
                data, (data != null ? data.length : 0));
    }

    public static String toString(float[] data) {
        return String.format(Locale.US, "Array[obj=%s, length=%d]",
                data, (data != null ? data.length : 0));
    }

    public static String toString(double[] data) {
        return String.format(Locale.US, "Array[obj=%s, length=%d]",
                data, (data != null ? data.length : 0));
    }

    public static String toString(char[] data) {
        return String.format(Locale.US, "Array[obj=%s, length=%d]",
                data, (data != null ? data.length : 0));
    }

    public static String toString(Object[] data) {
        return String.format(Locale.US, "Array[obj=%s, length=%d]",
                data, (data != null ? data.length : 0));
    }

    public static boolean matchAt(@NonNull byte[] data, int startPos, @NonNull byte[] pattern) {
        if (data.length - startPos < pattern.length || pattern.length == 0) {
            return false;
        }

        for (int i = 0; i < pattern.length; i++) {
            if (data[startPos + i] != pattern[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean startsWith(@NonNull byte[] data, @NonNull byte[] pattern) {
        return matchAt(data, 0, pattern);
    }

    public static boolean endsWith(@NonNull byte[] data, @NonNull byte[] pattern) {
        int startPos = data.length - pattern.length;
        return matchAt(data, startPos, pattern);
    }
}
