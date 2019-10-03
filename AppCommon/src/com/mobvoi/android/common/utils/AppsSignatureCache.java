package com.mobvoi.android.common.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

public class AppsSignatureCache {
    private static final String TAG = "AppsSignatureCache";

    private Context mAppContext;
    private final ConcurrentHashMap<String, String[]> mCachedSigns = new ConcurrentHashMap<>();

    @SuppressLint("StaticFieldLeak")
    private static volatile AppsSignatureCache sInstance;

    private AppsSignatureCache(Context context) {
        mAppContext = context.getApplicationContext();
        registerReceiver();
    }

    public static AppsSignatureCache getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppsSignatureCache.class) {
                if (sInstance == null) {
                    sInstance = new AppsSignatureCache(context);
                }
            }
        }
        return sInstance;
    }

    @NonNull
    public String[] getSignatures(String pkgName) throws PackageManager.NameNotFoundException {
        String[] signs = mCachedSigns.get(pkgName);
        if (signs != null) {
            return signs;
        }

        try {
            PackageManager pm = mAppContext.getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pkgInfo = pm.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
            signs = new String[pkgInfo.signatures.length];
            for (int i = 0; i < pkgInfo.signatures.length; i++) {
                String sha1 = DigestEncodingUtils.sha1(pkgInfo.signatures[i].toByteArray());
                signs[i] = sha1;
            }

            mCachedSigns.put(pkgName, signs);
            return signs;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Should never happen", e);
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED);
        filter.addDataScheme("package");
        mAppContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_PACKAGE_FULLY_REMOVED.equals(action)) {
                String pkgName = intent.getData().getSchemeSpecificPart();
                LogUtil.d(TAG, "package removed: %s", pkgName);
                mCachedSigns.remove(pkgName);
            }
        }
    };
}
