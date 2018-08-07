package com.example.lfs.androidstudy;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.example.lfs.androidstudy.ContentProviderLoad.DataLoadService;
import com.example.lfs.androidstudy.data.NewsInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lfs on 2018/8/2.
 */

public class ResourceLoad {
    public boolean has_load_image = false;                  // 本地图片是否已加载
    public boolean has_load_video = false;                  // 本地视频是否已加载
    public boolean has_load_file = false;                   // 本地文件是否已加载
    public boolean has_load_net_data = false;
    public List<String> fileList = new ArrayList<>();       // 保存 图片、视频、文件 名称
    private String urlText;
    private String savePathDir;                             // 网络下载图片保存到的　文件夹名称

    public enum LoadType {
        LOAD_TYPE_DOWNLOAD,                                 // 下载网络图片
        LOAD_TYPE_NEED_GET,                                 // 需要时才加载网络图片
    };
    final static LoadType IMAGE_LOAD_TYPE = LoadType.LOAD_TYPE_DOWNLOAD;       // 使用下载方式
//    final static LoadType IMAGE_LOAD_TYPE = LoadType.LOAD_TYPE_NEED_GET;       // 使用临时从网络加载获取

    public static final String NOTIFICATION = "network.data.has.load";
    public static final String RESULT = "result";
    public static final String IMAGE_DATA = "image_data";

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

    public void getUrlJsonData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean use_get = true;
                    use_get = false;

                    String url_s = "http://v.juhe.cn/toutiao/index?type=keji&key=71197f609745fc5bcd254a7c73988994"; // 科技
                            // "http://v.juhe.cn/toutiao/index?type=guoji&key=71197f609745fc5bcd254a7c73988994";    // 国际
                            //"http://v.juhe.cn/toutiao/index?type=top&key=71197f609745fc5bcd254a7c73988994";   // 头条
                    if (!use_get) {
                        url_s = "http://v.juhe.cn/toutiao/index";
                    }

                    URL url = new URL(url_s);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                    if (use_get) {
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setConnectTimeout(8000);
                        httpURLConnection.setReadTimeout(8000);
                        Log.i("Web", "------------ 1- ------------");
                        // connect
                        httpURLConnection.connect();
                        Log.i("Web", "------------ 2- ------------");
                    } else {
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setUseCaches(false);
                        httpURLConnection.setInstanceFollowRedirects(true);
                        httpURLConnection.connect();

                        // output写到上传内容（参数），推到web
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                        bufferedWriter.write("type=keji&key=71197f609745fc5bcd254a7c73988994");

                        bufferedWriter.flush();

                        bufferedWriter.close();
                        outputStream.close();
                    }


                    // 读取web返回数据
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader input = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(input);
                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {   // 200意味着返回的是"OK"
                        String inputLine;
                        Log.i("Web", "------------ 3- ------------");
                        StringBuffer resultData = new StringBuffer();
                        while ( (inputLine = bufferedReader.readLine()) != null ) {
                            resultData.append(inputLine);
                            Log.i("Web", "------------ 3.3- ------------");
                        }
                        urlText = resultData.toString();
                    }
                    input.close();
                    httpURLConnection.disconnect();
                    Log.i("Web", "------------ 4- ------------");

                    // 加载网络数据完成，解析JSON数据
                    parserJson();

                    Log.i("Web", urlText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void parserJson() {
        try {
            List<NewsInfo> listNewsInfo = new ArrayList<>();
            listNewsInfo.clear();
            fileList.clear();

            JSONObject jsonObject = new JSONObject(urlText);
            if (jsonObject.has("result")){
                JSONObject result = jsonObject.getJSONObject("result");
                if (result.has("stat")) {
                    int stat = result.getInt("stat");
                    if (stat == 1){
                        if (result.has("data")) {
                            JSONArray datas = result.getJSONArray("data");
                            int len = datas.length();
                            JSONObject tmpJsonObj;
                            String path;
                            for (int i = 0; i < len; ++i) {
                                tmpJsonObj = datas.getJSONObject(i);
                                NewsInfo tmpNewsInfo = new NewsInfo();
                                if (tmpJsonObj.has("title")) {
                                    tmpNewsInfo.setTitle(tmpJsonObj.getString("title"));
                                }
                                if (tmpJsonObj.has("date")) {
                                    tmpNewsInfo.setmDate(tmpJsonObj.getString("date"));
                                }
                                if (tmpJsonObj.has("category")) {
                                    tmpNewsInfo.setmCategory(tmpJsonObj.getString("category"));
                                }
                                if (tmpJsonObj.has("author_name")) {
                                    tmpNewsInfo.setmAuthor_name(tmpJsonObj.getString("author_name"));
                                }
                                if (tmpJsonObj.has("url")) {
                                    tmpNewsInfo.setmUrl(tmpJsonObj.getString("url"));
                                }
                                if (tmpJsonObj.has("thumbnail_pic_s")) {
                                    path = tmpJsonObj.getString("thumbnail_pic_s");
                                    tmpNewsInfo.setmThumbnail_pic_s(path);
                                    saveImg(path);
                                }
                                if (tmpJsonObj.has("thumbnail_pic_s02")) {
                                    path = tmpJsonObj.getString("thumbnail_pic_s02");
                                    tmpNewsInfo.setmThumbnail_pic_s02(path);
                                    saveImg(path);
                                }
                                if (tmpJsonObj.has("thumbnail_pic_s03")) {
                                    path = tmpJsonObj.getString("thumbnail_pic_s03");
                                    tmpNewsInfo.setmThumbnail_pic_s03(path);
                                    saveImg(path);
                                }
                            }
                        }
                    }
                }
            }

            //
            has_load_net_data = true;
            {
                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra(RESULT, 1);
                context.sendBroadcast(intent);
            }
//            DataLoadService.dataHandler.sendEmptyMessage(DataLoadService.NET_DATA_LOADED);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveImg(String path) {
        if (null == path) {
            return;
        }

        if (LoadType.LOAD_TYPE_DOWNLOAD == IMAGE_LOAD_TYPE) {
            String imgName = getFileName(path);
            String savePath = savePathDir + imgName;

            try {
                URL myFileUrl = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                conn.disconnect();

                File file = new File(savePath);
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                path = savePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        fileList.add(path);
    }

    public String getFileName(String pathandname) {

        int start = pathandname.lastIndexOf("/");
//        int end = pathandname.lastIndexOf(".");
        int end = pathandname.length();
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return null;
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

}
