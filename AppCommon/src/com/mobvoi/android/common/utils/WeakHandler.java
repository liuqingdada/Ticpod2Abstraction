package com.mobvoi.android.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

public class WeakHandler extends Handler {
    public interface MessageHandler {
        void handleMessage(Message msg);
    }

    private WeakReference<MessageHandler> mTargetHandler;

    public WeakHandler(@NonNull MessageHandler msgHandler) {
        mTargetHandler = new WeakReference<>(msgHandler);
    }

    public WeakHandler(@NonNull MessageHandler msgHandler, Looper looper) {
        super(looper);
        mTargetHandler = new WeakReference<>(msgHandler);
    }

    @Override
    public void handleMessage(Message msg) {
        MessageHandler realHandler = mTargetHandler.get();
        if (realHandler != null) {
            realHandler.handleMessage(msg);
        }
    }
}
