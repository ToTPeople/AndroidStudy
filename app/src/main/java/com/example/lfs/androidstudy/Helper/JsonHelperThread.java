package com.example.lfs.androidstudy.Helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.example.lfs.androidstudy.ResourceLoad;
import com.example.lfs.androidstudy.data.NewsInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lfs on 2018/8/13.
 */

public class JsonHelperThread extends Thread {
    private Context mContext;
    private final Object lock = new Object();
    private boolean pause = false;
    private int mPerPageCount = 1;//6;                         // ListView每页显示个数
    private String savePathDir;                             // 网络下载图片保存到的　文件夹名称

    public List<NewsInfo> mListNewsInfo;        // Json解析数据
    private Map<String, Boolean> m_mpNews = new HashMap<String, Boolean>();
    private String urlText;

    public JsonHelperThread(Context context, List<NewsInfo> listNewsInfo, String strSavePathDir) {
        super();
        super.setName("JsonHelperThread");
        mContext = context;
        mListNewsInfo = listNewsInfo;
        savePathDir = strSavePathDir;
    }

    /**
     * 调用这个方法实现暂停线程
     */
    public void pauseThread() {
        pause = true;
    }

    /**
     * 调用这个方法实现恢复线程的运行
     */
    public void resumeThread() {
        pause = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    /**
     * 注意：这个方法只能在run方法里调用，不然会阻塞主线程，导致页面无响应
     */
    private void onPause() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        super.run();
        try {
            // load news.json from storage
            boolean bFirstLoad = loadJson();

            // down from network
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
            parserJson(urlText, true, bFirstLoad);

            Log.i("Web", urlText);
        } catch (Exception e) {
            //捕获到异常之后，执行break跳出循环
            e.printStackTrace();
        }
    }

    public boolean loadJson() {
        boolean bFirstLoad = true;
        try {
            String strJson = savePathDir + "news.json";
            Log.i("Load", "Load path is: " + strJson);
            if (FileUtils.isFileExists(strJson)) {
                FileInputStream fileInputStream = new FileInputStream(strJson);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                String strLine;
                StringBuffer buffer = new StringBuffer();
                while ((strLine = reader.readLine()) != null) {
                    buffer.append(strLine);
                }
                reader.close();
                fileInputStream.close();
                parserJson(buffer.toString(), false, bFirstLoad);
                bFirstLoad = false;
                Log.i("Load", "Load content is: " + buffer.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bFirstLoad;
    }

    public void saveJson(String strPath) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(strPath);
            Writer writer = new OutputStreamWriter(fileOutputStream);

            // data array
            JSONArray jsonDataArray = new JSONArray();
            int len = mListNewsInfo.size();
            NewsInfo newsInfo;
            for (int i = 0; i < len; ++i) {
                newsInfo = mListNewsInfo.get(i);
                if (null != newsInfo) {
                    jsonDataArray.put(newsInfo.toJson());
                }
            }
            // result obj
            JSONObject jsonResultObject = new JSONObject();
            jsonResultObject.put("stat", "1");
            jsonResultObject.put("data", jsonDataArray);
            // top obj
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("reason", "成功的返回");
            jsonObject.put("result", jsonResultObject);

            writer.write(jsonObject.toString());
            writer.close();

            Log.i("Save", "save path is: " + strPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parserJson(String strText, boolean bIsNet, boolean bFirstIn) {
        try {
            if (null == mListNewsInfo) {
                mListNewsInfo = new ArrayList<>();
            } else {
                if (bFirstIn) {
                    mListNewsInfo.clear();
                }
            }

            JSONObject jsonObject = new JSONObject(strText);
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
                            int nCnt = 0;
                            for (int i = 0; i < len; ++i) {
                                // 让线程处于暂停等待状态
                                while (pause) {
                                    onPause();
                                }

                                tmpJsonObj = datas.getJSONObject(i);
                                NewsInfo tmpNewsInfo = new NewsInfo();
                                if (tmpJsonObj.has("uniquekey")) {
                                    tmpNewsInfo.setUniquekey(tmpJsonObj.getString("uniquekey"));
                                    // 有存在，则不重复加载
                                    if (m_mpNews.containsKey(tmpNewsInfo.getUniquekey())) {
                                        continue;
                                    }
                                }
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
                                    if (bIsNet) {
                                        path = saveImg(path);
                                    } else {
                                        String imgName = getFileName(path);
                                        path = savePathDir + imgName;
                                    }
                                    tmpNewsInfo.setmThumbnail_pic_s(path);
                                }
                                if (tmpJsonObj.has("thumbnail_pic_s02")) {
                                    path = tmpJsonObj.getString("thumbnail_pic_s02");
                                    if (bIsNet) {
                                        path = saveImg(path);
                                    } else {
                                        String imgName = getFileName(path);
                                        path = savePathDir + imgName;
                                    }
                                    tmpNewsInfo.setmThumbnail_pic_s02(path);
                                }
                                if (tmpJsonObj.has("thumbnail_pic_s03")) {
                                    path = tmpJsonObj.getString("thumbnail_pic_s03");
                                    if (bIsNet) {
                                        path = saveImg(path);
                                    } else {
                                        String imgName = getFileName(path);
                                        path = savePathDir + imgName;
                                    }
                                    tmpNewsInfo.setmThumbnail_pic_s03(path);
                                }

                                m_mpNews.put(tmpNewsInfo.getUniquekey(), true);
                                mListNewsInfo.add(tmpNewsInfo);

                                if (bIsNet) {
                                    if ((nCnt+1)%mPerPageCount == 0) {
                                        Log.i("Log", "cur i is: " + i);
                                        sendLoadData();
                                    }
                                }

                                ++nCnt;
                            }
                        }
                    }
                }
            }

            if (mListNewsInfo.size() % mPerPageCount != 0) {
                sendLoadData();
            }

            if (bIsNet) {
                // save to storage
                String strJson = savePathDir + "news.json";
                saveJson(strJson);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendLoadData() {
        Intent intent = new Intent(ResourceLoad.getInstance().NOTIFICATION);
        intent.putExtra(ResourceLoad.getInstance().RESULT, 1);
        Bundle bundle = new Bundle();
//        bundle.putSerializable(IMAGE_DATA, (Serializable)fileList);
        bundle.putSerializable(ResourceLoad.getInstance().NEWS_DATA, (Serializable)mListNewsInfo);
//        bundle.putParcelable(NEWS_DATA, (Parcelable)mListNewsInfo);
        intent.putExtras(bundle);

        mContext.sendBroadcast(intent);
    }

    public String saveImg(String path) {
        if (null == path) {
            return path;
        }

        if (ResourceLoad.LoadType.LOAD_TYPE_DOWNLOAD == ResourceLoad.getInstance().IMAGE_LOAD_TYPE) {
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
                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                path = savePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return path;
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
}
