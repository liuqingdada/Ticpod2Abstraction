package com.android.lib.ticpod.fastble.callback;

import com.android.lib.ticpod.fastble.exception.BleException;

public abstract class BleReadCallback extends BleBaseCallback {

    public abstract void onReadSuccess(byte[] data);

    public abstract void onReadFailure(BleException exception);

}
