package com.mobvoi.android.common.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PackageUtils {
    private static final String TAG = "PackageUtils";

    @Nullable
    private static PackageInfo getPackageInfo(@NonNull PackageManager pm, @NonNull String pkgName) {
        try {
            return pm.getPackageInfo(pkgName, 0);
        } catch (NameNotFoundException e) {
            // ignore
        }
        return null;
    }

    public static void setComponentEnabledState(Context context, ComponentName cn, int newState) {
        try {
            PackageManager pm = context.getPackageManager();
            int state = pm.getComponentEnabledSetting(cn);
            if (state != newState) {
                LogUtil.d(TAG, "set component state [%d] for [%s]", newState, cn);
                pm.setComponentEnabledSetting(cn, newState, PackageManager.DONT_KILL_APP);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "failed to set state for [%s]", cn);
        }
    }

    public static void enableComponent(Context context, ComponentName cn) {
        setComponentEnabledState(context, cn, PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
    }

    public static void disableComponent(Context context, ComponentName cn) {
        setComponentEnabledState(context, cn, PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
    }

    public static boolean isComponentDisabled(Context context, ComponentName cn) {
        PackageManager pm = context.getPackageManager();
        return pm.getComponentEnabledSetting(cn) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    }

    public static void setApplicationState(@NonNull Context context, @NonNull String pkgName, int newState) {
        try {
            PackageManager pm = context.getPackageManager();
            int state = pm.getApplicationEnabledSetting(pkgName);
            if (state != newState) {
                LogUtil.d(TAG, "set app state [%d] for [%s]", newState, pkgName);
                pm.setApplicationEnabledSetting(pkgName, newState, 0);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "failed to set state for [%s]", pkgName);
        }
    }

    public static void enableApplication(@NonNull Context context, @NonNull String pkgName) {
        setApplicationState(context, pkgName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
    }

    public static void disableApplication(@NonNull Context context, @NonNull String pkgName) {
        setApplicationState(context, pkgName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
    }

    /**
     * Check if the specified package is available (installed and enabled)
     */
    public static boolean isPackageAvailable(@NonNull PackageManager pm, @NonNull String pkgName) {
        PackageInfo info = getPackageInfo(pm, pkgName);
        return info != null && info.applicationInfo.enabled;
    }

    /**
     * @see #isPackageAvailable(PackageManager, String)
     */
    public static boolean isPackageAvailable(@NonNull Context context, @NonNull String pkgName) {
        return isPackageAvailable(context.getPackageManager(), pkgName);
    }
}
