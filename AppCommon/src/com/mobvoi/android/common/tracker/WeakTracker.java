package com.mobvoi.android.common.tracker;

import com.mobvoi.android.common.utils.WeakListenerManager;

public abstract class WeakTracker<IListener> extends WeakListenerManager<IListener> {
    protected abstract void startTracker();
    protected abstract void stopTracker();

    @Override
    protected void onFirstListenerAdd() {
        startTracker();
    }

    @Override
    protected void onLastListenerRemoved() {
        stopTracker();
    }
}
