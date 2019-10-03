package com.mobvoi.android.common.internalapi;

import android.content.Context;
import android.os.IBinder;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class PowerManagerIATest {
    @Test
    public void test_asInterface() {
        IBinder binder = ServiceManagerIA.getService(Context.POWER_SERVICE);
        assertThat(binder, notNullValue());

        Object service = PowerManagerIA.asInterface(binder);
        assertThat(service, notNullValue());
    }

    @Test
    public void test_getIPowerManager() {
        assertThat(PowerManagerIA.getIPowerManager(), notNullValue());
    }

    @Test
    public void test_reboot() {
        assertThat(PowerManagerIA.reflect_reboot(), notNullValue());
    }
}
