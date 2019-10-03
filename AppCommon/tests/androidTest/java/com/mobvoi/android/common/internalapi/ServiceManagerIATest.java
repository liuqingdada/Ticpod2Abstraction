package com.mobvoi.android.common.internalapi;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ServiceManagerIATest {
    private static final String TAG = "ServiceManagerIATest";

    @Test
    public void test_reflect_getService() {
        assertThat(ServiceManagerIA.reflect_getService(), notNullValue());
    }

    @Test
    public void test_reflect_checkService() {
        assertThat(ServiceManagerIA.reflect_checkService(), notNullValue());
    }

    @Test
    public void test_reflect_addService() {
        assertThat(ServiceManagerIA.reflect_addService(), notNullValue());
    }

    @Test
    public void test_reflect_listServices() {
        assertThat(ServiceManagerIA.reflect_listServices(), notNullValue());
    }
}
