package com.mobvoi.android.common.utils;

import android.app.Service;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class ConnectionUtils {
    private static final String TAG = "ConnectionUtils";
    private static final String DEFAULT_SIM_NUMERIC_PREFIX = "001";

    public static boolean isSimEnabled(Context cxt) {
        TelephonyManager tm = (TelephonyManager) cxt.getSystemService(Service.TELEPHONY_SERVICE);
        return tm != null
                && tm.getSimState() == TelephonyManager.SIM_STATE_READY
                && !tm.getSimOperator().startsWith(DEFAULT_SIM_NUMERIC_PREFIX);
    }
}
