package com.alibaba.cardscanner.service.openapi.utils.aes;

import java.nio.charset.Charset;
import java.util.Arrays;

/*
 * PKCS7ç®—æ³•çš„åŠ å¯†å¡«å…?
 */

public class PKCS7Padding {
    private final static Charset CHARSET    = Charset.forName("utf-8");
    private final static int     BLOCK_SIZE = 32;

    /**
     * Ìî³ämode×Ö½Ú
     * @param count
     * @return
     */
    public static byte[] getPaddingBytes(int count) {
        int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
        if (amountToPad == 0) {
            amountToPad = BLOCK_SIZE;
        }
        char padChr = chr(amountToPad);
        String tmp = new String();
        for (int index = 0; index < amountToPad; index++) {
            tmp += padChr;
        }
        return tmp.getBytes(CHARSET);
    }

    /**
     * ÒÆ³ýmodeÌî³ä×Ö½Ú
     * @param decrypted
     * @return
     */
    public static byte[] removePaddingBytes(byte[] decrypted) {
        int pad = (int) decrypted[decrypted.length - 1];
        if (pad < 1 || pad > BLOCK_SIZE) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    private static char chr(int a) {
        byte target = (byte) (a & 0xFF);
        return (char) target;
    }

}
