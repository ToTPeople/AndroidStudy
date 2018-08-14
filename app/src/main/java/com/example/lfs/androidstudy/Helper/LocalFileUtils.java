package com.example.lfs.androidstudy.Helper;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lfs on 2018/8/14.
 */

public class LocalFileUtils {
    private LocalFileUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isFileExists(final String filePath) {
        if (null == filePath) {
            return false;
        }
        File file = new File(filePath);
        if (null == file || !file.exists()) {
            return false;
        }
        return true;
    }

    // 获取SD卡目录下文件输入流
    public static FileInputStream getSDInputFile(final String filePath) throws IOException {
        return isSpace(filePath) ? null : new FileInputStream(filePath);
    }

    // 获取SD卡目录下文件输出流
    public static FileOutputStream getSDOutputFile(final String filePath) throws IOException {
        return isSpace(filePath) ? null : new FileOutputStream(filePath);
    }

    // 读写/data/data/<应用程序名>目录上的文件
    public static FileInputStream getDataInputFile(Context context, final String filePath) throws IOException {
        if (null == context || isSpace(filePath)) {
            return null;
        }
        return context.openFileInput(filePath);
    }
    public static FileOutputStream getDataOutputFile(Context context, final String filePath) throws IOException {
        if (null == context || isSpace(filePath)) {
            return null;
        }
        return context.openFileOutput(filePath, context.MODE_PRIVATE);
    }

    // 从resource的raw中读取文件数据(只能读取)
    public static InputStream getResRawInputFile(Activity activity, final int nId) {
        if (null == activity) {
            return null;
        }
        return activity.getResources().openRawResource(nId);
    }

    // 从resource的asset中读取文件数据(只能读取)
    public static InputStream getResAssetInputFile(Activity activity, final String filePath) throws IOException {
        if (null == activity) {
            return null;
        }
        return isSpace(filePath) ? null : activity.getResources().getAssets().open(filePath);
    }

    // 判断名称是否都是空格
    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
