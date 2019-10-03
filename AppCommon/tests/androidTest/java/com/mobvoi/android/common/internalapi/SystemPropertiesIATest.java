package com.mobvoi.android.common.internalapi;

import android.os.Build;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;

@RunWith(AndroidJUnit4.class)
public class SystemPropertiesIATest {
    private static final String TEST_KEY_NONE = "test.internalapis.none";

    @Test
    public void test_reflect_get() {
        assertThat(SystemPropertiesIA.reflect_get(), notNullValue());
    }

    @Test
    public void test_get() {
        String defValue = "test.defValue";
        String actual = SystemPropertiesIA.get(TEST_KEY_NONE, defValue);
        assertThat(actual, equalTo(defValue));

        actual = SystemPropertiesIA.get("ro.product.model", defValue);
        assertThat(actual, equalTo(Build.MODEL));

        actual = SystemPropertiesIA.get("ro.build.fingerprint", defValue);
        assertThat(actual, equalTo(Build.FINGERPRINT));
    }

    @Test
    public void test_reflect_getInt() {
        assertThat(SystemPropertiesIA.reflect_getInt(), notNullValue());
    }

    @Test
    public void test_getInt() {
        int defValue = 123;
        int actual = SystemPropertiesIA.getInt(TEST_KEY_NONE, defValue);
        assertThat(actual, equalTo(defValue));

        actual = SystemPropertiesIA.getInt("ro.build.version.sdk", defValue);
        assertThat(actual, equalTo(Build.VERSION.SDK_INT));
    }

    @Test
    public void test_reflect_getLong() {
        assertThat(SystemPropertiesIA.reflect_getLong(), notNullValue());
    }

    @Test
    public void test_getLong() {
        long defValue = 123;
        long actual = SystemPropertiesIA.getLong(TEST_KEY_NONE, defValue);
        assertThat(actual, equalTo(defValue));

        // Build.getLong("ro.build.date.utc") * 1000
        actual = SystemPropertiesIA.getLong("ro.build.date.utc", defValue) * 1000;
        assertThat(actual, equalTo(Build.TIME));
    }

    @Test
    public void test_reflect_getBoolean() {
        assertThat(SystemPropertiesIA.reflect_getBoolean(), notNullValue());
    }

    @Test
    public void test_getBoolean() {
        final boolean defValue = true;
        boolean actual = SystemPropertiesIA.getBoolean(TEST_KEY_NONE, defValue);
        assertThat(actual, equalTo(defValue));

        actual = SystemPropertiesIA.getBoolean("ro.debuggable", true);
        boolean actual2 = SystemPropertiesIA.getBoolean("ro.debuggable", false);
        assertThat(actual2, equalTo(actual));
    }

    @Test
    public void test_reflect_set() {
        assertThat(SystemPropertiesIA.reflect_set(), notNullValue());
    }

    @Test
    public void test_set() {
        // cannot run real tests in device because of permission
        SystemPropertiesIA.set(TEST_KEY_NONE, "test_value");
        String savedValue = SystemPropertiesIA.get(TEST_KEY_NONE, "");
        assertThat(savedValue, isEmptyOrNullString());
    }
}
