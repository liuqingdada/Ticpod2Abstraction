package com.mobvoi.android.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

@SuppressLint("HardwareIds")
@SuppressWarnings({"unused", "WeakerAccess"})
public class HardwareIdUtils {
    private static final String TAG = "HardwareIdUtils";

    public static final String ANDROID_M_FAKE_ADDRESS = "02:00:00:00:00:00";

    /**
     * Convenient for mock test
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * Convenient for mock test
     */
    public static int getSdkInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * Get SN of the device and ignore the possible permission deny.
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("MissingPermission")
    public static String getSerialQuietly() {
        try {
            return getSerial();
        } catch (SecurityException e) {
            return Build.UNKNOWN;
        }
    }

    @SuppressLint("MissingPermission")
    @SuppressWarnings("deprecation")
    @RequiresPermission(anyOf = {
//            Manifest.permission.READ_PHONE_STATE,
            "android.permission.READ_PRIVILEGED_PHONE_STATE"
    })
    public static String getSerial() {
        if (Build.VERSION.SDK_INT >= 28) { // Android P
            String sn = Build.SERIAL;
            if (!sn.equals(Build.UNKNOWN)) {
                // when 'targetSdk' is set to versions lower than Android P
                return sn;
            }

            // the 'targetSdk' is set to Android P and more higher version
            return Build.getSerial();
        } else {
            return Build.SERIAL;
        }
    }

    @RequiresPermission(allOf = {
            Manifest.permission.ACCESS_WIFI_STATE,
            "android.permission.LOCAL_MAC_ADDRESS"
    })
    @NonNull
    public static String getWifiAddress(Context cxt) {
        WifiManager wifiMgr = (WifiManager) cxt.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr == null) {
            return "";
        }
        String mac = wifiMgr.getConnectionInfo().getMacAddress();
        return mac != null ? mac : "";
    }

    @RequiresPermission(allOf = {
            Manifest.permission.BLUETOOTH,
            "android.permission.LOCAL_MAC_ADDRESS"
    })
    @NonNull
    public static String getBtAddress() {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            return "";
        }
        String mac = btAdapter.getAddress();
        return mac != null ? mac : "";
    }

    @RequiresPermission(anyOf = {
            Manifest.permission.READ_PHONE_STATE,
            "android.permission.READ_PRIVILEGED_PHONE_STATE"
    })
    @NonNull
    public static String getAndroidDeviceId(Context cxt) {
        TelephonyManager telMgr = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
        if (telMgr == null) {
            return "";
        }
        String id = telMgr.getDeviceId();
        return id != null ? id : "";
    }

    public static String getAndroidId(Context cxt) {
        return Settings.Secure.getString(cxt.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
