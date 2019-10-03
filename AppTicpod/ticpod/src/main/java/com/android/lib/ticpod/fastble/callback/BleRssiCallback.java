package com.android.lib.ticpod.fastble.callback;

import com.android.lib.ticpod.fastble.exception.BleException;

public abstract class BleRssiCallback extends BleBaseCallback{

    public abstract void onRssiFailure(BleException exception);

    public abstract void onRssiSuccess(int rssi);

}