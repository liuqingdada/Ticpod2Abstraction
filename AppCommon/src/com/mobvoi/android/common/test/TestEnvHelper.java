package com.mobvoi.android.common.test;

import android.os.Build;

public class TestEnvHelper {
    public static boolean isRobolectricTest() {
        return "robolectric".equals(Build.FINGERPRINT);
    }
}
