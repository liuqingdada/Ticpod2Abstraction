package com.mobvoi.android.common.ui;

import android.graphics.Outline;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.view.View;
import android.view.ViewOutlineProvider;

public class ViewUtils {
    /**
     * Clip a view to round shape.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void clipToRound(@NonNull View view) {
        view.setClipToOutline(true);
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(-1, -1, view.getWidth() + 1, view.getHeight() + 1);
            }
        });
    }
}
