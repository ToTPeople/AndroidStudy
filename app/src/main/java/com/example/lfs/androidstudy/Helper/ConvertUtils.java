package com.example.lfs.androidstudy.Helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by lfs on 2018/8/15.
 */

public class ConvertUtils {
    private ConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static byte[] string2Byte(final String string) {
        if (null == string) {
            return null;
        }

        return string.getBytes(StandardCharsets.UTF_8);
    }

    public static String byte2String(byte[] bytes) {
        if (null == bytes) {
            return null;
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] inputStream2Byte(final InputStream inputStream) {
        if (null == inputStream) {
            return null;
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int nBufferSize = 1024;
            byte[] buffer = new byte[nBufferSize];
            int len;
            while ( (len = inputStream.read(buffer, 0, nBufferSize)) != -1 ) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static InputStream byte2InputStream(final byte[] bytes) {
        if (null == bytes || bytes.length <= 0) {
            return null;
        }

        return new ByteArrayInputStream(bytes);
    }

}
