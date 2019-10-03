package com.android.lib.ticpod.fastble.callback;

import com.android.lib.ticpod.fastble.data.BleDevice;

public interface BleScanPresenterImp {

    void onScanStarted(boolean success);

    void onScanning(BleDevice bleDevice);

}
