package com.android.lib.ticpod.compat

import android.content.Context
import android.os.Build
import android.provider.Settings


/**
 * Created by yangliuqing
 * 2019-10-03.
 * Email: 1239604859@qq.com
 */
class PermissionCompat {
    companion object {
        fun isLocationServiceAllowed(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var locationMode = Settings.Secure.LOCATION_MODE_OFF
                try {
                    locationMode = Settings.Secure.getInt(
                        context.contentResolver,
                        Settings.Secure.LOCATION_MODE
                    )
                } catch (e: Settings.SettingNotFoundException) {
                    // do nothing
                }

                return locationMode != Settings.Secure.LOCATION_MODE_OFF
            }
            return true
        }
    }
}