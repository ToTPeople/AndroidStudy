package com.example.lfs.androidstudy;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.example.lfs.androidstudy.data.NewsInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
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

    public boolean has_load_image = false;
    public boolean has_load_video = false;
    public boolean has_load_file = false;
    public List<String> fileList = new ArrayList<>();
    private String urlText;
    private final static int SCAN_OK = 1;
    private ProgressDialog mProgressDialog;

    public Context context;
    private static final ResourceLoad ourInstance = new ResourceLoad();

    static public ResourceLoad getInstance() {
        return ourInstance;
    }

    private ResourceLoad() {
        fileList.add("http://03.imgmini.eastday.com/mobile/20180803/20180803144437_aae3a5c3bf5113aa69cac0998e1ab613_2_mwpm_03200403.jpg");
        fileList.add("http://04.imgmini.eastday.com/mobile/20180803/20180803145041_2afaa556716c3f05a9f1cf9c580a107c_1_mwpm_03200403.jpg");
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    //扫描完毕,关闭进度dialog
                    mProgressDialog.dismiss();
                    parserJson();
                    break;
            }
        }

    };

    public void getUrlJsonData() {
        //显示进度dialog
        mProgressDialog = ProgressDialog.show(context, null, "正在加载...");

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
                    //通知Handler扫描图片完成
                    mHandler.sendEmptyMessage(SCAN_OK);
                    Log.i("Web", urlText);
                    parserJson();
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
                                    tmpNewsInfo.setmThumbnail_pic_s(tmpJsonObj.getString("thumbnail_pic_s"));
                                    fileList.add(tmpJsonObj.getString("thumbnail_pic_s"));
                                }
                                if (tmpJsonObj.has("thumbnail_pic_s02")) {
                                    tmpNewsInfo.setmThumbnail_pic_s02(tmpJsonObj.getString("thumbnail_pic_s02"));
                                    fileList.add(tmpJsonObj.getString("thumbnail_pic_s02"));
                                }
                                if (tmpJsonObj.has("thumbnail_pic_s03")) {
                                    tmpNewsInfo.setmThumbnail_pic_s03(tmpJsonObj.getString("thumbnail_pic_s03"));
                                    fileList.add(tmpJsonObj.getString("thumbnail_pic_s03"));
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
