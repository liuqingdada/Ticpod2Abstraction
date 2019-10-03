package com.mobvoi.android.common.ui;

import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.view.View;

/**
 * @author Jiaxiang Li, "jxli@mobvoi.com"
 * @date 2017/4/18
 */

public abstract class MultiTapOnClickListener implements View.OnClickListener {
    private static final int DEFAULT_MAX_INTERVAL = 800; // ms

    private final int multiClickAim;
    private final long maxIntervalMillis;

    private int clickCnt;
    private long preTimestamp;

    public MultiTapOnClickListener(int multiClickAim) {
        this(multiClickAim, DEFAULT_MAX_INTERVAL);
    }

    /**
     * @param multiClickAim Aim for multiple click detection
     * @param maxIntervalMillis The maximum time interval from the last click
     */
    public MultiTapOnClickListener(int multiClickAim, long maxIntervalMillis) {
        this.multiClickAim = multiClickAim;
        this.maxIntervalMillis = maxIntervalMillis;
        reset();
    }

    public abstract void onMultiClick(@Nullable View view);

    @Override
    public void onClick(@Nullable View view) {
        long curTimestamp = SystemClock.uptimeMillis();

        if (preTimestamp == 0 || (curTimestamp - preTimestamp) > maxIntervalMillis) {
            // Initial or timeout.
            clickCnt = 1;
        } else {
            clickCnt++;
        }
        preTimestamp = curTimestamp;

        if (clickCnt >= multiClickAim) {
            reset();
            onMultiClick(view);
        }

    }

    public void reset() {
        clickCnt = 0;
        preTimestamp = 0;
    }
}
