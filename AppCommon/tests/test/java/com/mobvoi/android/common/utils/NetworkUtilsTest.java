package com.mobvoi.android.common.utils;

import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;

import org.junit.Test;

import static com.mobvoi.android.common.utils.NetworkUtils.NETWORK_TYPE_BLUETOOTH;
import static com.mobvoi.android.common.utils.NetworkUtils.NETWORK_TYPE_MOBILE;
import static com.mobvoi.android.common.utils.NetworkUtils.TYPE_WEAR_OS_COMPANION_PROXY;
import static com.mobvoi.android.common.utils.NetworkUtils.NETWORK_TYPE_WIFI;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NetworkUtilsTest {
    @Test
    public void getNetworkType_common() {
        // phone Wi-Fi
        assertThat(NetworkUtils.getNetworkType(ConnectivityManager.TYPE_WIFI, 0),
                is(NETWORK_TYPE_WIFI));
        // faked subTypes
        for (int i = 1; i < 20; i++) {
            assertThat(NetworkUtils.getNetworkType(ConnectivityManager.TYPE_WIFI, i),
                    is(NETWORK_TYPE_WIFI));
        }

        // phone 4G
        assertThat(NetworkUtils.getNetworkType(ConnectivityManager.TYPE_MOBILE, TelephonyManager.NETWORK_TYPE_LTE),
                is(NETWORK_TYPE_MOBILE));
        // phone 3G
        assertThat(NetworkUtils.getNetworkType(ConnectivityManager.TYPE_MOBILE, TelephonyManager.NETWORK_TYPE_UMTS),
                is(NETWORK_TYPE_MOBILE));
        assertThat(NetworkUtils.getNetworkType(ConnectivityManager.TYPE_MOBILE, TelephonyManager.NETWORK_TYPE_HSPAP),
                is(NETWORK_TYPE_MOBILE));
        // real or faked subTypes
        for (int i = 1; i < 20; i++) {
            assertThat(NetworkUtils.getNetworkType(ConnectivityManager.TYPE_MOBILE, i),
                    is(NETWORK_TYPE_MOBILE));
        }
    }

    @Test
    public void getNetworkType_wearOs() {
        // companion proxy: phone Wi-Fi or mobile
        assertThat(NetworkUtils.getNetworkType(TYPE_WEAR_OS_COMPANION_PROXY, 0),
                is(NETWORK_TYPE_BLUETOOTH));
        // faked subTypes
        for (int i = 1; i < 20; i++) {
            assertThat(NetworkUtils.getNetworkType(TYPE_WEAR_OS_COMPANION_PROXY, i),
                    is(NETWORK_TYPE_BLUETOOTH));
        }
    }

    @Test
    public void getNetworkType_ticwear() {
        // BT proxy: phone mobile
        assertThat(NetworkUtils.getNetworkType(ConnectivityManager.TYPE_BLUETOOTH, ConnectivityManager.TYPE_MOBILE),
                is(NetworkUtils.NETWORK_TYPE_BLUETOOTH_MOBILE));
        // BT proxy: phone Wi-Fi
        assertThat(NetworkUtils.getNetworkType(ConnectivityManager.TYPE_BLUETOOTH, ConnectivityManager.TYPE_WIFI),
                is(NetworkUtils.NETWORK_TYPE_BLUETOOTH_WIFI));
        // BT proxy: phone unknown network
        assertThat(NetworkUtils.getNetworkType(ConnectivityManager.TYPE_BLUETOOTH, 7),
                is(NETWORK_TYPE_BLUETOOTH));
    }
}
