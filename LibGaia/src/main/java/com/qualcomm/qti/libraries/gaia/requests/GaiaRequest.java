/* ************************************************************************************************
 * Copyright 2018 Qualcomm Technologies International, Ltd.                                       *
 **************************************************************************************************/

package com.qualcomm.qti.libraries.gaia.requests;

import android.annotation.SuppressLint;

import com.qualcomm.qti.libraries.gaia.packets.GaiaPacket;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * The data structure to define a GAIA request.
 */
public class GaiaRequest {

    /**
     * All types of GAIA requests which can be sent to a device.
     */
    @IntDef(flag = true, value = { Type.SINGLE_REQUEST, Type.ACKNOWLEDGEMENT, Type.UNACKNOWLEDGED_REQUEST })
    @Retention(RetentionPolicy.SOURCE)
    @SuppressLint("ShiftFlags") // more readable this way
    public @interface Type {
        int SINGLE_REQUEST = 1;
        int ACKNOWLEDGEMENT = 2;
        int UNACKNOWLEDGED_REQUEST = 3;
    }

    /**
     * The type of the request.
     */
    @Type public final int type;

    /**
     * If this request is about a characteristic, the Bluetooth characteristic for this request.
     */
    public GaiaPacket packet;

    /**
     * To build a new object of the type request.
     */
    public GaiaRequest(@Type int type) {
        this.type = type;
    }

}
