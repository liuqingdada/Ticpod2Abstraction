package com.mobvoi.android.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import androidx.annotation.NonNull;

public class IntentUtils {
    private static final String TAG = "IntentUtils";

    public static boolean canStartActivity(@NonNull Context context, @NonNull Intent intent) {
        // Use PackageManager.MATCH_DEFAULT_ONLY to behavior same as Context#startAcitivty()
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo != null;
    }

    public static boolean startActivity(@NonNull Context context, @NonNull Intent intent) {
        if (canStartActivity(context, intent)) {
            context.startActivity(intent);
            return true;
        } else {
            LogUtil.w(TAG, "cannot start Activity: %s", intent);
            return false;
        }
    }
}
