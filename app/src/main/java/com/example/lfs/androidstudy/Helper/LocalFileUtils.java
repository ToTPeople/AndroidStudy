package com.example.lfs.androidstudy.Helper;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

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
        if (isSpace(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (null == file || !file.exists()) {
            return false;
        }
        return true;
    }

    // 删除单个文件
    public static boolean delFile(final String path) {
        if (isSpace(path)) {
            return false;
        }

        File file = new File(path);
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }
    public static boolean delFile(File file) {
        if (null == file) {
            return false;
        }

        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    // 删除文件夹及其子文件
    public static void delDirector(final String path) {
        if (isSpace(path)) {
            return;
        }

        File dir = new File(path);
        if (!dir.exists()) {
            return;
        }
        if (dir.isFile()) {
            delFile(dir);
        } else if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
            }

            dir.delete();
        }
    }
    public static void delDirector(File dir) {
        if (null == dir || !dir.exists()) {
            return;
        }

        if (dir.isFile()) {
            dir.delete();
        } else if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                delDirector(file);
            }
            dir.delete();
        }
    }

    // 创建SD Directory
    public static boolean createSDDirectory(final String path) {
        if (isSpace(path)) {
            return false;
        }

        File dir = new File(path);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e("LocalFileUtils", "Cread SD directory fail, SD card not mount!!!");
        }

        return dir.mkdir();
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
