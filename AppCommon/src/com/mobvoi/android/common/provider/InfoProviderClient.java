package com.mobvoi.android.common.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.mobvoi.android.common.utils.LogUtil;

public class InfoProviderClient {
    private static final String TAG = "InfoProviderClient";

    private ContentResolver mResolver;
    private String mAuthority;

    public InfoProviderClient(@NonNull Context cxt, @NonNull String authority) {
        mResolver = cxt.getApplicationContext().getContentResolver();
        mAuthority = authority;
    }

    public Uri getUriFor(@Nullable String table, @NonNull String name) {
        if (TextUtils.isEmpty(table)) {
            table = InfoProvider.TABLE_DEFAULT;
        }
        return new Uri.Builder().scheme("content").authority(mAuthority)
                .appendPath(table).appendPath(name)
                .build();
    }

    public void registerObserver(@Nullable String table, @NonNull String name,
            @NonNull ContentObserver observer) {
        Uri uri = getUriFor(table, name);
        mResolver.registerContentObserver(uri, true, observer);
    }

    public void registerObserver(@NonNull Uri uri, @NonNull ContentObserver observer) {
        mResolver.registerContentObserver(uri, true, observer);
    }

    public void unregisterObserver(@NonNull ContentObserver observer) {
        mResolver.unregisterContentObserver(observer);
    }

    public boolean remove(@Nullable String table, @NonNull String name) {
        try {
            Uri uri = getUriFor(table, name);
            Bundle args = new Bundle();
            args.putString(InfoProvider.KEY_TABLE, table);
            args.putString(InfoProvider.KEY_NAME, name);
            Bundle result = mResolver.call(uri, InfoProvider.METHOD_REMOVE, null, args);
            if (result == null) {
                LogUtil.e(TAG, "Cannot call method [%s]", InfoProvider.METHOD_REMOVE);
                return false;
            }

            boolean success = result.getBoolean(InfoProvider.KEY_STATUS);
            String oldValue = result.getString(InfoProvider.KEY_VALUE);
            if (success && !TextUtils.isEmpty(oldValue)) {
                mResolver.notifyChange(uri, null);
            }

            return success;
        } catch (SecurityException e) {
            throw e; // rethrow
        } catch (Exception e) {
            LogUtil.w(TAG, "Failed to remove [%s] in table [%s]", e, name, table);
            return false;
        }
    }

    public String getString(@Nullable String table, @NonNull String name, @Nullable String defValue) {
        try {
            Uri uri = getUriFor(table, name);
            Bundle args = new Bundle();
            args.putString(InfoProvider.KEY_TABLE, table);
            args.putString(InfoProvider.KEY_NAME, name);
            Bundle result = mResolver.call(uri, InfoProvider.METHOD_GET, null, args);
            if (result == null) {
                LogUtil.e(TAG, "Cannot call method [%s]", InfoProvider.METHOD_GET);
                return defValue;
            }

            String value = result.getString(InfoProvider.KEY_VALUE);
            if (value == null) {
                value = defValue;
            }
            return value;
        } catch (SecurityException e) {
            throw e; // rethrow
        } catch (Exception e) {
            LogUtil.w(TAG, "Failed to get value for [%s] in table [%s]", e, name, table);
        }
        return defValue;
    }

    public boolean putString(@Nullable String table, @NonNull String name, @NonNull String value) {
        try {
            Uri uri = getUriFor(table, name);
            Bundle args = new Bundle();
            args.putString(InfoProvider.KEY_TABLE, table);
            args.putString(InfoProvider.KEY_NAME, name);
            args.putString(InfoProvider.KEY_VALUE, value);
            Bundle result = mResolver.call(uri, InfoProvider.METHOD_PUT, null, args);
            if (result == null) {
                LogUtil.e(TAG, "Cannot call method [%s] for name [%s]",
                        InfoProvider.METHOD_PUT, name);
                return false;
            }

            boolean success = result.getBoolean(InfoProvider.KEY_STATUS);
            String oldValue = result.getString(InfoProvider.KEY_VALUE);
            if (success && !TextUtils.equals(oldValue, value)) {
                mResolver.notifyChange(uri, null);
            }

            return success;
        } catch (SecurityException e) {
            throw e; // rethrow
        } catch (Exception e) {
            LogUtil.w(TAG, "Failed to put value for [%s] in table [%s]", e, name, table);
        }
        return false;
    }

    public boolean getBoolean(@Nullable String table, @NonNull String name, boolean defValue) {
        String result = getString(table, name, null);
        if (!TextUtils.isEmpty(result)) {
            return Boolean.parseBoolean(result);
        }
        return defValue;
    }

    public boolean putBoolean(@Nullable String table, @NonNull String name, boolean value) {
        return putString(table, name, Boolean.toString(value));
    }

    public int getInt(@Nullable String table, @NonNull String name, int defValue) {
        try {
            String result = getString(table, name, null);
            if (!TextUtils.isEmpty(result)) {
                return Integer.parseInt(result);
            }
        } catch (NumberFormatException e) {
            LogUtil.w(TAG, "Failed to get int value for [%s] in table [%s]", e, name, table);
        }
        return defValue;
    }

    public boolean putInt(@Nullable String table, @NonNull String name, int value) {
        return putString(table, name, Integer.toString(value));
    }

    public long getLong(@Nullable String table, @NonNull String name, long defValue) {
        try {
            String result = getString(table, name, null);
            if (!TextUtils.isEmpty(result)) {
                return Long.parseLong(result);
            }
        } catch (NumberFormatException e) {
            LogUtil.w(TAG, "Failed to get long value for [%s] in table [%s]", e, name, table);
        }
        return defValue;
    }

    public boolean putLong(@Nullable String table, @NonNull String name, long value) {
        return putString(table, name, Long.toString(value));
    }

    public double getDouble(@Nullable String table, @NonNull String name, double defValue) {
        try {
            String result = getString(table, name, null);
            if (!TextUtils.isEmpty(result)) {
                return Double.parseDouble(result);
            }
        } catch (NumberFormatException e) {
            LogUtil.w(TAG, "Failed to get double value for [%s] in table [%s]", e, name, table);
        }
        return defValue;
    }

    public boolean putDouble(@Nullable String table, @NonNull String name, double value) {
        return putString(table, name, Double.toString(value));
    }

    public float getFloat(@Nullable String table, @NonNull String name, float defValue) {
        try {
            String result = getString(table, name, null);
            if (!TextUtils.isEmpty(result)) {
                return Float.parseFloat(result);
            }
        } catch (NumberFormatException e) {
            LogUtil.w(TAG, "Failed to get double value for [%s] in table [%s]", e, name, table);
        }
        return defValue;
    }

    public boolean putFloat(@Nullable String table, @NonNull String name, float value) {
        return putString(table, name, Float.toString(value));
    }
}
