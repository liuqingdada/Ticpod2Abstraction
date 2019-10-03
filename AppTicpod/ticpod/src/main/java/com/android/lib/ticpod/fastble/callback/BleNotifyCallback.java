package com.android.lib.ticpod.fastble.callback;

import com.android.lib.ticpod.fastble.exception.BleException;

public abstract class BleNotifyCallback extends BleBaseCallback {

    public abstract void onNotifySuccess();

    public abstract void onNotifyFailure(BleException exception);

    public abstract void onCharacteristicChanged(byte[] data);

}
