package com.mobvoi.android.common.utils;

import org.hamcrest.MatcherAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class DigestEncodingUtilsTest {
    @Rule
    public ExpectedException thrownRule = ExpectedException.none();

    @Test
    public void encodeWithHex_case1() throws UnsupportedEncodingException {
        // test cases from http://grepcode.com/file/repo1.maven.org/maven2/commons-codec/commons-codec/1.10/org/apache/commons/codec/binary/HexTest.java#HexTest
        MatcherAssert.assertThat(DigestEncodingUtils.encodeWithHex(new byte[0]).length(), is(0));

        String result = DigestEncodingUtils.encodeWithHex(new byte[36]);
        assertThat(result, equalTo("000000000000000000000000000000000000000000000000000000000000000000000000"));

        result = DigestEncodingUtils.encodeWithHex("Hello World".getBytes("UTF-8"));
        assertThat(result, equalTo("48656C6C6F20576F726C64"));

        result = DigestEncodingUtils.encodeWithHex("Hello World".getBytes("UTF-8"), false);
        assertThat(result, equalTo("48656c6c6f20576f726c64"));
    }

    @Test
    public void encodeWithHexV2() {
        byte[] data = new byte[] {0x23, (byte)0xa3, 0x26, 0x7a, (byte)0xe2, 0x04, (byte)0xe1};
        String result = DigestEncodingUtils.encodeWithHex(data, 0, data.length);
        assertThat(result, equalTo("23A3267AE204E1"));
        result = DigestEncodingUtils.encodeWithHex(data, 1, 4, true);
        assertThat(result, equalTo("A3267A"));
        result = DigestEncodingUtils.encodeWithHex(data, 3, 10);
        assertThat(result, equalTo("7AE204E1"));

        result = DigestEncodingUtils.encodeWithHex(data, 0, data.length, false);
        assertThat(result, equalTo("23a3267ae204e1"));
        result = DigestEncodingUtils.encodeWithHex(data, 1, 4, false);
        assertThat(result, equalTo("a3267a"));
        result = DigestEncodingUtils.encodeWithHex(data, 3, 10, false);
        assertThat(result, equalTo("7ae204e1"));
    }

    @Test
    public void test_fromHexString1() {
        String hexStr = "010000000102000002";
        String hexStr2 = " 010 00000 01020000 02";
        String hexStr3 = " 010 00  000 010200   00 02  ";
        byte[] data = new byte[] {0x01, 0x00, 0x00, 0x00, 0x01, 0x02, 0x00, 0x00, 0x02};
        assertThat(DigestEncodingUtils.fromHexString(hexStr), equalTo(data));
        assertThat(DigestEncodingUtils.fromHexString(hexStr2), equalTo(data));
        assertThat(DigestEncodingUtils.fromHexString(hexStr3), equalTo(data));
    }

    @Test
    public void test_fromHexString2() {
        String hexStr = "0100000000000002EB3701";
        String hexStr2 = " 0 100000 0000000   02EB3701 ";
        String hexStr3 = "0100 000   000000  002EB 3701";
        byte[] data = new byte[] {0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, (byte)0xEB, 0x37, 0x001};
        assertThat(DigestEncodingUtils.fromHexString(hexStr), equalTo(data));
        assertThat(DigestEncodingUtils.fromHexString(hexStr2), equalTo(data));
        assertThat(DigestEncodingUtils.fromHexString(hexStr3), equalTo(data));
    }

    @Test
    public void test_illegalLength() {
        thrownRule.expect(IllegalArgumentException.class);
        thrownRule.expectMessage(startsWith("Bad length: 010000000102000002A"));

        String hexStr = "010000000102000002A";
        DigestEncodingUtils.fromHexString(hexStr);
    }

    @Test
    public void test_illegalCharacter() {
        thrownRule.expect(IllegalArgumentException.class);
        thrownRule.expectMessage(startsWith("Not hex string: 0100000001020000020Z"));

        String hexStr = "0100000001020000020Z";
        DigestEncodingUtils.fromHexString(hexStr);
    }

    @Test
    public void sha1_case1() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // test cases from http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.1.1_r1/android/core/Sha1Test.java#Sha1Test
        String result = DigestEncodingUtils.sha1("abc");
        assertThat(result, equalTo("a9993e364706816aba3e25717850c26c9cd0d89d"));

        result = DigestEncodingUtils.sha1("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq");
        assertThat(result, equalTo("84983e441c3bd26ebaae4aa1f95129e5e54670f1"));
    }

    @Test
    public void computeCrc32() {
        byte[] EMPTY = new byte[0];
        assertThat(DigestEncodingUtils.computeCrc32(EMPTY), is(0L));

        // copy unit test cases from https://github.com/bakercp/CRC32/blob/master/tests/CRC32/CRC32.ino
        byte[] ALL_ZEROS = { 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00 };
        assertThat(DigestEncodingUtils.computeCrc32(ALL_ZEROS), is(0x190A55ADL));

        byte[] ALL_ONES =  { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
                (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
                (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
                (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
                (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
                (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
                (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
                (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, };
        assertThat(DigestEncodingUtils.computeCrc32(ALL_ONES), is(0xFF6CAB0BL));

        byte[] INCREASING = { 0x00, 0x01, 0x02, 0x03,
                0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0A, 0x0B,
                0x0C, 0x0D, 0x0E, 0x0F,
                0x10, 0x11, 0x12, 0x13,
                0x14, 0x15, 0x16, 0x17,
                0x18, 0x19, 0x1A, 0x1B,
                0x1C, 0x1D, 0x1E, 0x1F };
        assertThat(DigestEncodingUtils.computeCrc32(INCREASING), is(0x91267E8AL));

        byte[] DECREASING = { 0x1F, 0x1E, 0x1D, 0x1C,
                0x1B, 0x1A, 0x19, 0x18,
                0x17, 0x16, 0x15, 0x14,
                0x13, 0x12, 0x11, 0x10,
                0x0F, 0x0E, 0x0D, 0x0C,
                0x0B, 0x0A, 0x09, 0x08,
                0x07, 0x06, 0x05, 0x04,
                0x03, 0x02, 0x01, 0x00 };
        assertThat(DigestEncodingUtils.computeCrc32(DECREASING), is(0x9AB0EF72L));
    }
}