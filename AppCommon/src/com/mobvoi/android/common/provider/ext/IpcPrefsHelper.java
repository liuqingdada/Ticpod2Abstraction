package com.mobvoi.android.common.provider.ext;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;

import com.mobvoi.android.common.provider.InfoProviderHelper;

public class IpcPrefsHelper extends InfoProviderHelper {
    private static final String AUTHORITY_SUFFIX = ".IpcPrefs";

    @SuppressLint("StaticFieldLeak")
    private static volatile IpcPrefsHelper sInstance;

    public static IpcPrefsHelper getInstance(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (IpcPrefsHelper.class) {
                if (sInstance == null) {
                    sInstance = new IpcPrefsHelper(context);
                }
            }
        }
        return sInstance;
    }

    private IpcPrefsHelper(@NonNull Context context) {
        super(context, context.getPackageName() + AUTHORITY_SUFFIX);
    }

    public void remove(@NonNull String prefsName, @NonNull String key) {
        mInfoClient.remove(prefsName, key);
    }

    public String getString(String prefsName, String key, String defValue) {
        return mInfoClient.getString(prefsName, key, defValue);
    }

    public int getInt(String prefsName, String key, int defValue) {
        return mInfoClient.getInt(prefsName, key, defValue);
    }

    public long getLong(String prefsName, String key, long defValue) {
        return mInfoClient.getLong(prefsName, key, defValue);
    }

    public boolean getBoolean(String prefsName, String key, boolean defValue) {
        return mInfoClient.getBoolean(prefsName, key, defValue);
    }

    public float getFloat(String prefsName, String key, float defValue) {
        return mInfoClient.getFloat(prefsName, key, defValue);
    }

    public double getDouble(String prefsName, String key, Double defValue) {
        return mInfoClient.getDouble(prefsName, key, defValue);
    }

    public IpcPrefsHelper putString(String prefsName, String key, String value) {
        mInfoClient.putString(prefsName, key, value);
        return this;
    }

    public IpcPrefsHelper putInt(String prefsName, String key, int value) {
        mInfoClient.putInt(prefsName, key, value);
        return this;
    }

    public IpcPrefsHelper putLong(String prefsName, String key, long value) {
        mInfoClient.putLong(prefsName, key, value);
        return this;
    }

    public IpcPrefsHelper putBoolean(String prefsName, String key, boolean value) {
        mInfoClient.putBoolean(prefsName, key, value);
        return this;
    }

    public IpcPrefsHelper putFloat(String prefsName, String key, float value) {
        mInfoClient.putFloat(prefsName, key, value);
        return this;
    }

    public IpcPrefsHelper putDouble(String prefsName, String key, double value) {
        mInfoClient.putDouble(prefsName, key, value);
        return this;
    }
}
