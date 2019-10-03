package com.mobvoi.android.common.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.ContentObserver;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.mobvoi.android.common.utils.LogUtil;

public abstract class InfoProviderHelper {
    private static final String TAG = "InfoProviderHelper";

    protected Context mAppContext;
    protected String mAuthority;
    protected InfoProviderClient mInfoClient;

    protected InfoProviderHelper(@NonNull Context context, @NonNull String authority) {
        mAppContext = context.getApplicationContext();
        mAuthority = authority;
        mInfoClient = new InfoProviderClient(mAppContext, authority);
    }

    public boolean isProviderExist() {
        return isProviderExist(mAppContext, mAuthority);
    }

    public static boolean isProviderExist(@NonNull Context context, @NonNull String authority) {
        PackageManager pm = context.getPackageManager();
        ProviderInfo info = pm.resolveContentProvider(authority, 0);
        LogUtil.d(TAG, "Provider: %s", info);
        return info != null;
    }

    public Uri getUriFor(@NonNull String table, @NonNull String name) {
        return mInfoClient.getUriFor(table, name);
    }

    public InfoProviderHelper registerObserver(@NonNull String table, @NonNull String name,
            @NonNull ContentObserver observer) {
        mInfoClient.registerObserver(table, name, observer);
        return this;
    }

    public InfoProviderHelper registerObserver(@NonNull Uri uri, @NonNull ContentObserver observer) {
        mInfoClient.registerObserver(uri, observer);
        return this;
    }

    public InfoProviderHelper unregisterObserver(@NonNull ContentObserver observer) {
        mInfoClient.unregisterObserver(observer);
        return this;
    }
}
