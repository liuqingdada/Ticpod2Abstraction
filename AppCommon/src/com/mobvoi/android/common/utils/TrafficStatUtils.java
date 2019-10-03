package com.mobvoi.android.common.utils;

import android.net.TrafficStats;

public class TrafficStatUtils {
    private static final String TAG = "TrafficStatUtils";

    private TrafficStatUtils() {
        // nothing to do
    }

    public static void setThreadStatsTag(int tag) {
        int previousTag = TrafficStats.getThreadStatsTag();
        if (previousTag != 0 && previousTag != -1 && previousTag != tag) {
            LogUtil.w(TAG, "tag [%d] already exist when set tag [0x%x]", previousTag, tag);
        }
        TrafficStats.setThreadStatsTag(tag);
    }

    public static void clearThreadStatsTag() {
        TrafficStats.clearThreadStatsTag();
    }
}
