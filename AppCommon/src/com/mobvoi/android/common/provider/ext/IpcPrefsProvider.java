package com.mobvoi.android.common.provider.ext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Process;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobvoi.android.common.provider.InfoProvider;
import com.mobvoi.android.common.utils.Preconditions;

public class IpcPrefsProvider extends InfoProvider {
    @Override
    protected boolean remove(@NonNull String table, @NonNull String name) {
        getStoragePrefs(table).edit().remove(name).apply();
        return true;
    }

    @Override
    protected String get(@NonNull String table, @NonNull String name) {
        return getStoragePrefs(table).getString(name, null);
    }

    @Override
    protected boolean put(@NonNull String table, @NonNull String name, @Nullable String value) {
        getStoragePrefs(table).edit().putString(name, value).apply();
        return true;
    }

    private SharedPreferences getStoragePrefs(String table) {
        return Preconditions.checkNotNull(getContext()).getSharedPreferences(table, Context.MODE_PRIVATE);
    }

    @Override
    protected void checkReadPermission() {
        int callerUid = Binder.getCallingUid();
        if (callerUid != Process.myUid()) {
            throw new SecurityException("UID[" + callerUid + "] is NOT allowed to read this provider");
        }
    }

    @Override
    protected void checkWritePermission() {
        int callerUid = Binder.getCallingUid();
        if (callerUid != Process.myUid()) {
            throw new SecurityException("UID[" + callerUid + "] is NOT allowed to write this provider");
        }
    }
}
