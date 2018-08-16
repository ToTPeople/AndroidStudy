package com.example.lfs.androidstudy;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.lfs.androidstudy.Helper.DomParserXML;
import com.example.lfs.androidstudy.Helper.JsonHelperThread;
import com.example.lfs.androidstudy.Helper.PullParserXML;
import com.example.lfs.androidstudy.Helper.SAXParserXML;
import com.example.lfs.androidstudy.data.NewsInfo;
import com.example.lfs.androidstudy.data.StudentData;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lfs on 2018/8/2.
 */

public class ResourceLoad {
    public boolean has_load_image = false;                  // 本地图片是否已加载
    public boolean has_load_video = false;                  // 本地视频是否已加载
    public boolean has_load_file = false;                   // 本地文件是否已加载
    public List<String> fileList = new ArrayList<>();       // 保存 图片、视频、文件 名称
    private List<NewsInfo> mListNewsInfo = new ArrayList<>();        // Json解析数据
    private String savePathDir;                             // 网络下载图片保存到的　文件夹名称

    private JsonHelperThread m_threadJsonHelper = null;

    public enum LoadType {
        LOAD_TYPE_DOWNLOAD,                                 // 下载网络图片
        LOAD_TYPE_NEED_GET,                                 // 需要时才加载网络图片
    };
    public final static LoadType IMAGE_LOAD_TYPE = LoadType.LOAD_TYPE_DOWNLOAD;       // 使用下载方式
//    public final static LoadType IMAGE_LOAD_TYPE = LoadType.LOAD_TYPE_NEED_GET;       // 使用临时从网络加载获取

    public static final String NOTIFICATION = "network.data.has.load";
    public static final String RESULT = "result";
    public static final String IMAGE_DATA = "image_data";
    public static final String NEWS_DATA = "news_data";

    public Context context;
    private static final ResourceLoad ourInstance = new ResourceLoad();         // 单例

    static public ResourceLoad getInstance() {
        return ourInstance;
    }

    // 构造函数
    private ResourceLoad() {
        File extStore = Environment.getExternalStorageDirectory();
        savePathDir = extStore.getAbsolutePath() + "/";
    }

    public void getListNewsInfo(List<NewsInfo> listInfo) {
        if (null != listInfo) {
            if (null != m_threadJsonHelper) {
                m_threadJsonHelper.pauseThread();
            }
            listInfo.clear();
            for (int i = 0; i < mListNewsInfo.size(); ++i) {
                listInfo.add(mListNewsInfo.get(i));
            }
            if (null != m_threadJsonHelper) {
                m_threadJsonHelper.resumeThread();
            }
        }
    }

    public void getUrlJsonData() {
        if (null == m_threadJsonHelper) {
            m_threadJsonHelper = new JsonHelperThread(context, mListNewsInfo, savePathDir);
        }
        if (null != m_threadJsonHelper) {
            m_threadJsonHelper.start();
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    public void getImages() {
        has_load_image = false;
        //开启线程扫描
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = context.getContentResolver();
                //只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null, null, null, null);
                if (mCursor == null) {
                    return;
                }
                while (mCursor.moveToNext()) {
                    //获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.i("PrintImage:", "-=-=--=-=- path is: " + path);
                    fileList.add(path);
                }

                //通知Handler扫描图片完成
                has_load_image = true;
            }
        }).start();

    }

    private void getVideo() {
        has_load_video = false;

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        ContentResolver contentResolver = context.getContentResolver();
                        //
                        Cursor cursor = contentResolver.query(videoUri, null, null, null, null);
                        if (null == cursor) {
                            return;
                        }

                        while (cursor.moveToNext()) {
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                            if (null != path && new File(path).exists()) {
                                Log.i("PrintVideo:", "== path is: " + path);
                                fileList.add(path);
                            }
                        }

                        //通知Handler扫描图片完成
                        has_load_video = true;
                    }
                }
        ).start();
    }

    private void getFile() {
        has_load_file = false;

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Uri fileUri = MediaStore.Files.getContentUri("external");
                        ContentResolver contentResolver = context.getContentResolver();

                        // every column, although that is huge waste, you probably need
// BaseColumns.DATA (the path) only.
                        String[] projection = null;

// exclude media files, they would be here also.
                        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                                + MediaStore.Files.FileColumns.MEDIA_TYPE_NONE;
                        String[] selectionArgs = null; // there is no ? in selection so null here

                        String sortOrder = null; // unordered
                        Cursor cursor = contentResolver.query(fileUri, projection, selection, selectionArgs, sortOrder);

//                        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
//                        String selectionMimeType = MediaStore.Files.FileColumns.TITLE + " = ?";
////                        String selectionMimeType = MediaStore.Files.FileColumns.DISPLAY_NAME + " = ?";
//                        String[] selectionArgsPdf = new String[]{ "app" };
//                        Cursor cursor = contentResolver.query(fileUri, null, selectionMimeType, selectionArgsPdf, null);
                        //
//                        Cursor cursor = contentResolver.query(fileUri, null, null, null, null);
                        if (null == cursor) {
                            return;
                        }

                        while (cursor.moveToNext()) {
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                            if (null != path && new File(path).exists()) {
                                Log.i("PrintFile:", "== path is: " + path);
                                fileList.add(path);
                            }
                        }

                        //通知Handler扫描图片完成
                        has_load_file = true;
                    }
                }
        ).start();
    }

    // 解析本地XML文件
    public List<StudentData> getListStudentData(String path) {
        InputStream inputStream = null;
        try {
            if (new File(path).exists()) {
                File file = new File(path);
                inputStream = new FileInputStream(file);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return SAXParserXML.saxParserXML(inputStream);
//        return new PullParserXML().getListStudentData(inputStream);
//        return new DomParserXML().getListStudentData(inputStream);
    }
}
