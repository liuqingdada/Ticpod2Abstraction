package com.mobvoi.android.common.utils;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HardwareIdUtils.class})
public class DeviceIdUtilTest {
    @Before
    public void setup() {
        LogUtil.enableJvmLogger();
        DeviceIdUtil.sBtAddress = null; // reset cache
    }

    @Test
    public void test_getBtAddress_normal() {
        PowerMockito.mockStatic(HardwareIdUtils.class);

        final String BT_ADDR1 = "30:95:E3:9B:45:2C";
        final String BT_ADDR2 = "30:95:E3:96:59:45";
        final String BT_ADDR3 = "30:95:E3:96:5C:D4";

        // group 1
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn(BT_ADDR1);
        assertThat(DeviceIdUtil.getBtAddress(), equalTo(BT_ADDR1));

        // group 2
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn(BT_ADDR2);
        // cache works!
        assertThat(DeviceIdUtil.getBtAddress(), equalTo(BT_ADDR1));

        // group 3
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn(BT_ADDR2);
        DeviceIdUtil.sBtAddress = null; // reset cache
        assertThat(DeviceIdUtil.getBtAddress(), equalTo(BT_ADDR2));

        // group 4
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn(BT_ADDR3);
        // cache works!
        assertThat(DeviceIdUtil.getBtAddress(), equalTo(BT_ADDR2));

        // group 5
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn(BT_ADDR3);
        DeviceIdUtil.sBtAddress = null; // reset cache
        assertThat(DeviceIdUtil.getBtAddress(), equalTo(BT_ADDR3));
    }

    @Test
    public void test_getBtAddress_illegal() {
        PowerMockito.mockStatic(HardwareIdUtils.class);

        // group 1
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn(DeviceIdUtil.ILLEGAL_BLUETOOTH_ADDRESS);
        assertThat(DeviceIdUtil.getBtAddress(), equalTo(DeviceIdUtil.ILLEGAL_BLUETOOTH_ADDRESS));

        // group 2
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn(DeviceIdUtil.ANDROID_M_FAKE_ADDRESS);
        assertThat(DeviceIdUtil.getBtAddress(), equalTo(DeviceIdUtil.ANDROID_M_FAKE_ADDRESS));

        // group 3
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn(DeviceIdUtil.DEFAULT_BLUETOOTH_ADDRESS);
        assertThat(DeviceIdUtil.getBtAddress(), equalTo(DeviceIdUtil.DEFAULT_BLUETOOTH_ADDRESS));

        // group 4
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn(DeviceIdUtil.ILLEGAL_BLUETOOTH_ADDRESS);
        // cache works!
        assertThat(DeviceIdUtil.getBtAddress(), equalTo(DeviceIdUtil.DEFAULT_BLUETOOTH_ADDRESS));

        // group 5
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn(DeviceIdUtil.ILLEGAL_BLUETOOTH_ADDRESS);
        DeviceIdUtil.sBtAddress = null; // reset cache
        assertThat(DeviceIdUtil.getBtAddress(), equalTo(DeviceIdUtil.ILLEGAL_BLUETOOTH_ADDRESS));
    }

    @Test
    public void test_generateWatchDeviceId_normal() throws Exception {
        PowerMockito.mockStatic(HardwareIdUtils.class);

        // group 1
        Mockito.when(HardwareIdUtils.getModel()).thenReturn("Ticwatch2-nfc");
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn("30:95:E3:9B:45:2C");
        String deviceId = DeviceIdUtil.generateWatchDeviceId();
        assertThat(deviceId, equalTo("b4f390af60ce3e244d5a0337bc8d78d3"));

        // group 2 (BT address cache)
        Mockito.when(HardwareIdUtils.getModel()).thenReturn("Ticwatch2-nfc");
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn("30:95:E3:96:59:45"); // changed!
        deviceId = DeviceIdUtil.generateWatchDeviceId();
        assertThat(deviceId, equalTo("b4f390af60ce3e244d5a0337bc8d78d3"));

        // group 3
        Mockito.when(HardwareIdUtils.getModel()).thenReturn("Ticwatch2_3G");
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn("30:95:E3:96:59:45");
        DeviceIdUtil.sBtAddress = null; // reset cache
        deviceId = DeviceIdUtil.generateWatchDeviceId();
        assertThat(deviceId, equalTo("37e795383b05dbb2d0ce2a6cccf50317"));

        // group 4
        Mockito.when(HardwareIdUtils.getModel()).thenReturn("Ticwatch2_3G");
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn("30:95:E3:96:5C:D4");
        DeviceIdUtil.sBtAddress = null; // reset cache
        deviceId = DeviceIdUtil.generateWatchDeviceId();
        assertThat(deviceId, equalTo("796e9ece9a7bf291a432c13d82ea2c2d"));
    }

    @Test
    public void test_generateWatchDeviceId_defaultBtAddress() throws Exception {
        PowerMockito.mockStatic(HardwareIdUtils.class);

        // group 1
        Mockito.when(HardwareIdUtils.getModel()).thenReturn("Ticwatch2_3G");
        Mockito.when(HardwareIdUtils.getSerialQuietly()).thenReturn("N7VWQGFU99999999");
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn("00:00:46:66:30:01");
        String deviceId = DeviceIdUtil.generateWatchDeviceId();
        assertThat(deviceId, equalTo("94fb6d1d02bfaf90a493e1f73ef577d6"));

        // group 2
        Mockito.when(HardwareIdUtils.getModel()).thenReturn("Ticwatch2_3G");
        Mockito.when(HardwareIdUtils.getSerialQuietly()).thenReturn("SOP79HY999999999");
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn("00:00:46:66:30:01");
        deviceId = DeviceIdUtil.generateWatchDeviceId();
        assertThat(deviceId, equalTo("298309d8f741e85de5fec890b797c084"));
    }

    @Test
    public void test_generateWatchDeviceId_illegalBtAddress() throws Exception {
        PowerMockito.mockStatic(HardwareIdUtils.class);

        // group 1
        Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn("00:00:00:00:00:00");
        String deviceId = DeviceIdUtil.generateWatchDeviceId();
        assertThat(deviceId, equalTo("illegal_device_id"));

        // group 2
        try {
            Mockito.when(HardwareIdUtils.getBtAddress()).thenReturn("02:00:00:00:00:00");
            DeviceIdUtil.generateWatchDeviceId();
            fail("expected exception not thrown");
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void test_generatePhoneDeviceId_normal() throws Exception {
        PowerMockito.mockStatic(HardwareIdUtils.class);
        Context context = Mockito.mock(Context.class);

        // group 1 (Android 7.0)
        Mockito.when(HardwareIdUtils.getModel()).thenReturn("Nexus 5X");
        Mockito.when(HardwareIdUtils.getSerialQuietly()).thenReturn("01224c0537d0a4ec");
        Mockito.when(HardwareIdUtils.getAndroidDeviceId(context)).thenReturn("354360070174602");
        Mockito.when(HardwareIdUtils.getWifiAddress(context)).thenReturn(DeviceIdUtil.ANDROID_M_FAKE_ADDRESS);
        String deviceId = DeviceIdUtil.generatePhoneDeviceId(context);
        assertThat(deviceId, equalTo("5f9696f7151bb4e70707f8a580981705"));

        // group 2 (Android 5.1)
        Mockito.when(HardwareIdUtils.getModel()).thenReturn("8681-M02");
        Mockito.when(HardwareIdUtils.getSerialQuietly()).thenReturn("I7C6SO65UWMVWWMV");
        Mockito.when(HardwareIdUtils.getAndroidDeviceId(context)).thenReturn("867886022618700");
        Mockito.when(HardwareIdUtils.getWifiAddress(context)).thenReturn("d0:37:42:03:8d:b3");
        DeviceIdUtil.sBtAddress = null; // reset cache
        deviceId = DeviceIdUtil.generatePhoneDeviceId(context);
        assertThat(deviceId, equalTo("f13bbb233ae1dd52146d81327fa39a6b"));

        // group 3 (Android 6.0)
        Mockito.when(HardwareIdUtils.getModel()).thenReturn("HUAWEI NXT-DL00");
        Mockito.when(HardwareIdUtils.getSerialQuietly()).thenReturn("DLQ0215C21002895");
        Mockito.when(HardwareIdUtils.getAndroidDeviceId(context)).thenReturn("868406020823464");
        Mockito.when(HardwareIdUtils.getWifiAddress(context)).thenReturn("74:a5:28:eb:0c:2a");
        DeviceIdUtil.sBtAddress = null; // reset cache
        deviceId = DeviceIdUtil.generatePhoneDeviceId(context);
        assertThat(deviceId, equalTo("b51d7314f0630eb8479326970cac8a8f"));
    }

    @Test
    public void test_generatePhoneDeviceIdV2() throws Exception {
        PowerMockito.mockStatic(HardwareIdUtils.class);
        Context context = Mockito.mock(Context.class);

        // group 1 (Android 5.1)
        Mockito.when(HardwareIdUtils.getModel()).thenReturn("Nexus 5X");
        Mockito.when(HardwareIdUtils.getSerialQuietly()).thenReturn("01224c0537d0a4ec");
        Mockito.when(HardwareIdUtils.getAndroidId(context)).thenReturn("a1d02ec0f14a072a");
        Mockito.when(HardwareIdUtils.getSdkInt()).thenReturn(22);
        assertThat(DeviceIdUtil.generatePhoneDeviceIdV2(context),
                equalTo("a28b4c3c6bcffb2025473f24d7f58813"));

        // group 2 (Android 6.0)
        Mockito.when(HardwareIdUtils.getModel()).thenReturn("Nexus 5X");
        Mockito.when(HardwareIdUtils.getSerialQuietly()).thenReturn("01224c0537d0a4ec");
        Mockito.when(HardwareIdUtils.getAndroidId(context)).thenReturn("a1d02ec0f14a072a");
        Mockito.when(HardwareIdUtils.getSdkInt()).thenReturn(23);
        assertThat(DeviceIdUtil.generatePhoneDeviceIdV2(context),
                equalTo("a28b4c3c6bcffb2025473f24d7f58813"));

        // group 3 (Android 7.0)
        Mockito.when(HardwareIdUtils.getModel()).thenReturn("Nexus 5X");
        Mockito.when(HardwareIdUtils.getSerialQuietly()).thenReturn("01224c0537d0a4ec");
        Mockito.when(HardwareIdUtils.getAndroidId(context)).thenReturn("a1d02ec0f14a072a");
        Mockito.when(HardwareIdUtils.getSdkInt()).thenReturn(24);
        assertThat(DeviceIdUtil.generatePhoneDeviceIdV2(context),
                equalTo("3d8d5f49a95ea84227d2dc5229e43233"));
    }
}
