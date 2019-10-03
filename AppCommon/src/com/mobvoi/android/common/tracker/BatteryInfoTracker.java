package com.mobvoi.android.common.tracker;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import androidx.annotation.NonNull;

import com.mobvoi.android.common.utils.LogUtil;

public class BatteryInfoTracker extends WeakTracker<BatteryInfoTracker.BatteryInfoListener> {
    private static final String TAG = "BatteryInfoTracker";

    @SuppressLint("StaticFieldLeak")
    private static BatteryInfoTracker sInstance = null;

    public static class BatteryInfo {
        private int level;
        private int scale;
        public int percent;  // percent corrected by us
        public int status;
        public int plugged;

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof BatteryInfo)) {
                return false;
            }

            BatteryInfo otherInfo = (BatteryInfo) obj;
            return this.percent == otherInfo.percent && this.status == otherInfo.status
                    && this.plugged == otherInfo.plugged;
        }

        @Override
        public String toString() {
            return "BatteryInfo[level=" + level + ", scale=" + scale
                    + ", percent=" + percent + ", plugged=" + plugged + "]";
        }
    }

    public interface BatteryInfoListener {
        /**
         * @param newData Read-only, cannot be modified.
         */
        void onBatteryInfoUpdated(BatteryInfo newData);
    }

    private Context mContext;
    private BatteryInfo mBatteryInfo;
    private static int sBatteryScale = 100;

    private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryInfo(intent);
        }
    };

    public static synchronized BatteryInfoTracker getInsance(Context cxt) {
        if (sInstance == null) {
            sInstance = new BatteryInfoTracker(cxt);
        }
        return sInstance;
    }

    private BatteryInfoTracker(Context cxt) {
        mContext = cxt.getApplicationContext();
    }

    @Override
    protected void startTracker() {
        LogUtil.i(TAG, "BatteryInfo tracker is running");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = mContext.registerReceiver(mBatteryInfoReceiver, filter);
        if (intent != null) {
            updateBatteryInfo(intent);
        }
    }

    @Override
    protected void stopTracker() {
        LogUtil.i(TAG, "BatteryInfo tracker is stopped");
        mContext.unregisterReceiver(mBatteryInfoReceiver);
    }

    @Override
    protected void onListenerAdded(@NonNull BatteryInfoListener listener) {
        if (mBatteryInfo != null) {
            listener.onBatteryInfoUpdated(mBatteryInfo);
        }
    }

    private static BatteryInfo parseBatteryChangeIntent(Intent intent) {
        final BatteryInfo data = new BatteryInfo();
        data.level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        data.scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        data.status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_UNKNOWN);
        data.plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);

        fixData(data);

        int reportedPercent = data.scale < 1 ? data.level : (data.level * 100 / data.scale);
        if (reportedPercent >= 0 && reportedPercent <= 100)
            data.percent = reportedPercent;
        else if (reportedPercent < 0) {
            data.percent = 0;
        } else if (reportedPercent > 100) {
            data.percent = 100;
        }

        return data;
    }

    private void updateBatteryInfo(Intent intent) {
        final BatteryInfo data = parseBatteryChangeIntent(intent);
        if (data.equals(mBatteryInfo)) {
            return;
        }

        LogUtil.d(TAG, "battery info updated: %s", data);
        mBatteryInfo = data;
        notifyListeners(new NotifyAction<BatteryInfoListener>() {
            @Override
            public void notify(BatteryInfoListener listener) {
                listener.onBatteryInfoUpdated(data);
            }
        });
    }

    private static void fixData(BatteryInfo data) {
        // We may need to update 'sBatteryScale'
        if (data.level > data.scale) {
            LogUtil.e(TAG, "Bad battery data! level: %d, scale: %d, sBatteryScale: %d",
                    data.level, data.scale, sBatteryScale);
            if (data.level % 100 == 0) {
                sBatteryScale = data.level;
            }
        }

        // We may need to correct the 'data.scale'
        if (data.scale < sBatteryScale) {
            data.scale = sBatteryScale;
        }
    }

    /**
     * Get current battery info from System.
     */
    @NonNull
    public static BatteryInfo getBatteryInfo(Context cxt) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = cxt.registerReceiver(null, filter);
        if (intent == null) {
            BatteryInfo batteryInfo = new BatteryInfo();
            batteryInfo.status = BatteryManager.BATTERY_STATUS_UNKNOWN;
            return batteryInfo;
        } else {
            return parseBatteryChangeIntent(intent);
        }
    }
}
