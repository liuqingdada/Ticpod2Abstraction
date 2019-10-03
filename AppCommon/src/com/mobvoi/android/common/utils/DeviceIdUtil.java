// Copyright 2014 Mobvoi Inc. All Rights Reserved
package com.mobvoi.android.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.IntDef;
import androidx.annotation.RequiresPermission;
import androidx.annotation.VisibleForTesting;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.mobvoi.android.common.utils.DeviceIdUtil.DeviceIdVersion.V1;
import static com.mobvoi.android.common.utils.DeviceIdUtil.DeviceIdVersion.V2;

@SuppressLint("HardwareIds")
public class DeviceIdUtil {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({V1, V2})
    public @interface DeviceIdVersion {
        /**
         * Only for Ticwear Companion.
         */
        int V1 = 1;

        /**
         * New app should use this version to generate phone device ID.
         */
        int V2 = 2;
    }

    private static final String TAG = "DeviceIdUtil";
    private static final String PREFERENCE_FILE_NAME = "com_mobvoi_devices_id";
    public static final String ILLEGAL_DEVICE_ID = "illegal_device_id";
    private static final String DEVICES_ID = "com_mobvoi_log_devices_id";

    @VisibleForTesting
    static final String ILLEGAL_BLUETOOTH_ADDRESS = "00:00:00:00:00:00";
    @VisibleForTesting
    static final String DEFAULT_BLUETOOTH_ADDRESS = "00:00:46:66:30:01";
    @VisibleForTesting
    static final String ANDROID_M_FAKE_ADDRESS = HardwareIdUtils.ANDROID_M_FAKE_ADDRESS;

    private static int sDeviceIdVersion = V1;
    private static String sDeviceId;
    @VisibleForTesting
    static String sBtAddress;

    public static void initForPhone(@DeviceIdVersion int version) {
        sDeviceIdVersion = version;
        LogUtil.d(TAG, "initForPhone: %d", version);
    }

    @SuppressLint("MissingPermission")
    public static String getPhoneDeviceId(Context context) {
        if (!StringUtils.isEmpty(sDeviceId)) {
            return sDeviceId;
        }

        synchronized (DeviceIdUtil.class) {
            String deviceId = getFromLocal(context);
            if (StringUtils.isEmpty(deviceId)) {
                if (sDeviceIdVersion == V2) {
                    deviceId = generatePhoneDeviceIdV2(context);
                } else {
                    deviceId = generatePhoneDeviceId(context);
                }
                if (ILLEGAL_DEVICE_ID.equals(deviceId)) {
                    // don't set 'sDeviceId', retry to generate device id later
                    return ILLEGAL_DEVICE_ID;
                } else {
                    setToLocal(context, deviceId);
                }
            }
            sDeviceId = deviceId;
        }

        return sDeviceId;
    }

    public static boolean illegalDeviceId(String deviceId) {
        return StringUtils.isEmpty(deviceId) || DeviceIdUtil.ILLEGAL_DEVICE_ID.equals(deviceId);
    }

    private static String getFromLocal(Context context) {
        String deviceId = null;
        if (Build.VERSION.SDK_INT < 23) {
            deviceId = Settings.System.getString(context.getContentResolver(), DEVICES_ID);
        }
        if (StringUtils.isEmpty(deviceId)) {
            deviceId = getDevicesIdFromPreferences(context);
        }
        return deviceId;
    }

    private static void setToLocal(Context context, String deviceId) {
        saveDevicesIdToPreferences(context, deviceId);
    }

    @RequiresPermission(allOf = {
            Manifest.permission.BLUETOOTH,
            "android.permission.LOCAL_MAC_ADDRESS"
    })
    public static String getBtAddress() {
        if (!StringUtils.isEmpty(sBtAddress)) {
            return sBtAddress;
        }

        String btAddress = HardwareIdUtils.getBtAddress();
        if (StringUtils.isEmpty(btAddress) || ILLEGAL_BLUETOOTH_ADDRESS.equals(btAddress)) {
            return ILLEGAL_BLUETOOTH_ADDRESS; // don't cache
        }
        if (ANDROID_M_FAKE_ADDRESS.equals(btAddress)) {
            return ANDROID_M_FAKE_ADDRESS; // don't cache
        }

        sBtAddress = btAddress;
        return sBtAddress;
    }

    /**
     * Note: Prefer use {@link #getPhoneDeviceId(Context)} to get phone device id.
     */
    @RequiresPermission(allOf = {
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            "android.permission.LOCAL_MAC_ADDRESS"
    })
    @VisibleForTesting
    static String generatePhoneDeviceId(Context cxt) {
        String deviceId = ILLEGAL_DEVICE_ID;
        try {
            StringBuilder sb = new StringBuilder();
            String wifiAddress = HardwareIdUtils.getWifiAddress(cxt);
            if (ANDROID_M_FAKE_ADDRESS.equals(wifiAddress)) {
                sb.append(HardwareIdUtils.getModel()).append(HardwareIdUtils.getSerialQuietly());
            } else {
                sb.append(wifiAddress);
            }
            sb.append(HardwareIdUtils.getAndroidDeviceId(cxt));
            deviceId = sha1(sb.toString()).substring(0, 32);
        } catch (Exception e) {
            LogUtil.e(TAG, "generatePhoneDeviceId failed", e);
        }
        LogUtil.d(TAG, "generatePhoneDeviceId: %s", deviceId);
        return deviceId;
    }

    @VisibleForTesting
    static String generatePhoneDeviceIdV2(Context cxt) {
        String model = HardwareIdUtils.getModel();
        String androidId = HardwareIdUtils.getAndroidId(cxt);
        String data = model + "#" + androidId;

        String serial = "";
        if (HardwareIdUtils.getSdkInt() <= 23) {
            // Since Android O, reading SN needs READ_PHONE_STATE permission.
            // We expect device ID can be generated repeatedly, even after system OTA,
            // so we only add SN for Android M and below versions.
            serial = HardwareIdUtils.getSerialQuietly();
            data = data + "#" + serial;
        }

        String deviceId = ILLEGAL_DEVICE_ID;
        try {
            deviceId = DigestEncodingUtils.sha1(data).substring(0, 32);
        } catch (Exception e) {
            LogUtil.e(TAG, "failed to generate device ID", e);
        }
        LogUtil.d(TAG, "generatePhoneDeviceIdV2, model=[%s], androidId=[%s], serial=[%s]," +
                " deviceId=[%s]", model, androidId, serial, deviceId);
        return deviceId;
    }

    @RequiresPermission(allOf = {
            Manifest.permission.BLUETOOTH,
            "android.permission.LOCAL_MAC_ADDRESS"
    })
    public static String generateWatchDeviceId() {
        String btAddress = getBtAddress();
        if (ILLEGAL_BLUETOOTH_ADDRESS.equals(btAddress)) {
            return ILLEGAL_DEVICE_ID;
        }
        if (ANDROID_M_FAKE_ADDRESS.equals(btAddress)) {
            throw new RuntimeException("no permission to get BT address");
        }

        String data = btAddress + HardwareIdUtils.getModel();
        if (DEFAULT_BLUETOOTH_ADDRESS.equals(btAddress)) {
            // Only for Ticwear
            LogUtil.e(TAG, "default BT address found");
            data += HardwareIdUtils.getSerialQuietly();
        }

        try {
            return sha1(data).substring(0, 32);
        } catch (Exception e) {
            LogUtil.e(TAG, "generateWatchDeviceId failed", e);
            return ILLEGAL_DEVICE_ID;
        }
    }

    private static String sha1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1Hash = md.digest();
        return DigestEncodingUtils.encodeWithHex(sha1Hash, false);
    }

    private static void saveDevicesIdToPreferences(Context context, String devicesId) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEVICES_ID, devicesId);
        editor.apply();
    }

    private static String getDevicesIdFromPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(DEVICES_ID, "");
    }
}