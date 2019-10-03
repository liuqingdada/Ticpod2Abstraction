package com.mobvoi.android.common.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class ArrayUtilsTest {
    @Test
    public void toString_int() {
        int[] data = null;
        assertThat(ArrayUtils.toString(data), equalTo("Array[obj=null, length=0]"));

        data = new int[10];
        assertThat(ArrayUtils.toString(data), startsWith("Array[obj=[I@"));
        assertThat(ArrayUtils.toString(data), endsWith(", length=10]"));
    }

    @Test
    public void toString_boolean() {
        boolean[] data = null;
        assertThat(ArrayUtils.toString(data), equalTo("Array[obj=null, length=0]"));

        data = new boolean[11];
        assertThat(ArrayUtils.toString(data), startsWith("Array[obj=[Z@"));
        assertThat(ArrayUtils.toString(data), endsWith(", length=11]"));
    }

    @Test
    public void toString_long() {
        long[] data = null;
        assertThat(ArrayUtils.toString(data), equalTo("Array[obj=null, length=0]"));

        data = new long[12];
        assertThat(ArrayUtils.toString(data), startsWith("Array[obj=[J@"));
        assertThat(ArrayUtils.toString(data), endsWith(", length=12]"));
    }

    @Test
    public void toString_short() {
        short[] data = null;
        assertThat(ArrayUtils.toString(data), equalTo("Array[obj=null, length=0]"));

        data = new short[13];
        assertThat(ArrayUtils.toString(data), startsWith("Array[obj=[S@"));
        assertThat(ArrayUtils.toString(data), endsWith(", length=13]"));
    }

    @Test
    public void toString_byte() {
        byte[] data = null;
        assertThat(ArrayUtils.toString(data), equalTo("Array[obj=null, length=0]"));

        data = new byte[14];
        assertThat(ArrayUtils.toString(data), startsWith("Array[obj=[B@"));
        assertThat(ArrayUtils.toString(data), endsWith(", length=14]"));
    }

    @Test
    public void toString_float() {
        float[] data = null;
        assertThat(ArrayUtils.toString(data), equalTo("Array[obj=null, length=0]"));

        data = new float[15];
        assertThat(ArrayUtils.toString(data), startsWith("Array[obj=[F@"));
        assertThat(ArrayUtils.toString(data), endsWith(", length=15]"));
    }

    @Test
    public void toString_double() {
        double[] data = null;
        assertThat(ArrayUtils.toString(data), equalTo("Array[obj=null, length=0]"));

        data = new double[16];
        assertThat(ArrayUtils.toString(data), startsWith("Array[obj=[D@"));
        assertThat(ArrayUtils.toString(data), endsWith(", length=16]"));
    }

    @Test
    public void toString_char() {
        char[] data = null;
        assertThat(ArrayUtils.toString(data), equalTo("Array[obj=null, length=0]"));

        data = new char[17];
        assertThat(ArrayUtils.toString(data), startsWith("Array[obj=[C@"));
        assertThat(ArrayUtils.toString(data), endsWith(", length=17]"));
    }

    @Test
    public void toString_object() {
        String[] data = null;
        assertThat(ArrayUtils.toString(data), equalTo("Array[obj=null, length=0]"));

        data = new String[18];
        assertThat(ArrayUtils.toString(data), startsWith("Array[obj=[Ljava.lang.String;@"));
        assertThat(ArrayUtils.toString(data), endsWith(", length=18]"));
    }

    @Test
    public void test_matchAt() {
        byte[] data = new byte[] {0x23, (byte)0xa3, 0x26, 0x7a, (byte)0xe2, 0x04, (byte)0xe1,
                (byte)0xd4, 0x11};

        assertThat(ArrayUtils.matchAt(data, 0, new byte[0]), equalTo(false));
        assertThat(ArrayUtils.matchAt(data, 4, new byte[0]), equalTo(false));

        assertThat(ArrayUtils.matchAt(data, 0, new byte[]{0x23}), equalTo(true));
        assertThat(ArrayUtils.matchAt(data, 0, new byte[]{0x23, (byte)0xa3}), equalTo(true));
        assertThat(ArrayUtils.matchAt(data, 0, new byte[]{0x23, (byte)0x77}), equalTo(false));
        assertThat(ArrayUtils.matchAt(data, 0, new byte[]{0x77}), equalTo(false));
        assertThat(ArrayUtils.matchAt(data, 0, new byte[]{0x77, (byte)0xa3}), equalTo(false));

        assertThat(ArrayUtils.matchAt(data, 4, new byte[]{(byte)0xE2}), equalTo(true));
        assertThat(ArrayUtils.matchAt(data, 4, new byte[]{(byte)0xE2, 0x04, (byte)0xe1}), equalTo(true));
        assertThat(ArrayUtils.matchAt(data, 4, new byte[]{(byte)0xaa}), equalTo(false));
        assertThat(ArrayUtils.matchAt(data, 4, new byte[]{(byte)0xd3, 0x04, (byte)0xe1}), equalTo(false));

        assertThat(ArrayUtils.matchAt(data, 7, new byte[]{(byte)0xd4, (byte)0x11}), equalTo(true));
        assertThat(ArrayUtils.matchAt(data, 7, new byte[]{(byte)0xd4, (byte)0x11, 0x04}), equalTo(false));

        assertThat(ArrayUtils.matchAt(data, 8, new byte[]{(byte)0x11}), equalTo(true));
        assertThat(ArrayUtils.matchAt(data, 8, new byte[]{(byte)0x11, 0x04}), equalTo(false));
    }

    @Test
    public void test_startsWith() {
        byte[] data = new byte[] {0x23, (byte)0xa3, 0x26, 0x7a, (byte)0xe2, 0x04, (byte)0xe1,
                (byte)0xd4, 0x11};

        assertThat(ArrayUtils.startsWith(data, new byte[0]), equalTo(false));

        assertThat(ArrayUtils.startsWith(data, new byte[]{0x23}), equalTo(true));
        assertThat(ArrayUtils.startsWith(data, new byte[]{0x23, (byte)0xa3}), equalTo(true));
        assertThat(ArrayUtils.startsWith(data, new byte[]{0x23, (byte)0x77}), equalTo(false));
        assertThat(ArrayUtils.startsWith(data, new byte[]{0x77}), equalTo(false));
        assertThat(ArrayUtils.startsWith(data, new byte[]{0x77, (byte)0xa3}), equalTo(false));
    }

    @Test
    public void test_endsWith() {
        byte[] data = new byte[] {0x23, (byte)0xa3, 0x26, 0x7a, (byte)0xe2, 0x04, (byte)0xe1,
                (byte)0xd4, 0x11};

        assertThat(ArrayUtils.endsWith(data, new byte[0]), equalTo(false));

        assertThat(ArrayUtils.endsWith(data, new byte[]{(byte)0xd4, (byte)0x11}), equalTo(true));
        assertThat(ArrayUtils.endsWith(data, new byte[]{(byte)0xa3, (byte)0xd4, (byte)0x11}), equalTo(false));

        assertThat(ArrayUtils.endsWith(data, new byte[]{(byte)0x11}), equalTo(true));
        assertThat(ArrayUtils.endsWith(data, new byte[]{0x04, (byte)0x11}), equalTo(false));
    }
}
