package me.lhfcws.multiprocessing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5
 */
public class Md5Util {
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    private static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    private static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    private static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;

        for (int j = 0; i < l; ++i) {
            out[j++] = toDigits[(240 & data[i]) >>> 4];
            out[j++] = toDigits[15 & data[i]];
        }

        return out;
    }

    public static String md5(byte[] key) {
        return md5(key, 0, key.length);
    }

    public static String md5(byte[] key, int offset, int length) {
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(key, offset, length);
            byte[] digest = e.digest();
            return new String(encodeHex(digest));
        } catch (NoSuchAlgorithmException var5) {
            throw new RuntimeException("Error computing MD5 hash", var5);
        }
    }

    public static String md5(String str) {
        return md5(str.getBytes());
    }
}
