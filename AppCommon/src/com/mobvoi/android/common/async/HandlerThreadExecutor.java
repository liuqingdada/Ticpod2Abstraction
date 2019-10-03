package com.mobvoi.android.common.async;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import androidx.annotation.NonNull;

@SuppressWarnings({"unused", "WeakerAccess"})
public class HandlerThreadExecutor extends HandlerExecutor {
    private Handler mTaskHandler;

    public HandlerThreadExecutor(@NonNull String name) {
        super(startThread(name));
    }

    private static Looper startThread(@NonNull String name) {
        HandlerThread thread = new HandlerThread(name);
        thread.start();
        return thread.getLooper();
    }
}
