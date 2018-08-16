package com.example.lfs.androidstudy.ContentProviderLoad;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lfs.androidstudy.Helper.LocalFileUtils;
import com.example.lfs.androidstudy.ResourceLoad;
import com.example.lfs.androidstudy.data.NewsInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lfs on 2018/8/16.
 */

public class DelSDDataService extends IntentService {
    public static final String ACTION_DEL_DATA = "com.example.lfs.androidstudy.ContentProviderLoad.action.DelData";
    private static int m_sCnt = 0;
    private static int m_sTot = 0;

    public DelSDDataService() {
        super("DelSDDataService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (null != intent) {
            String action = intent.getAction();
            if (ACTION_DEL_DATA.equals(action)) {
                Log.i("Service", "[DelSDDataService:onHandleIntent]");
                // .json文件
                File extStore = Environment.getExternalStorageDirectory();
                String jsonPath = extStore.getAbsolutePath() + "/news.json";
                LocalFileUtils.delFile(jsonPath);

                // *.jpeg文件
                List<NewsInfo> listInfo = new ArrayList<>();
                ResourceLoad.getInstance().getListNewsInfo(listInfo);
                int len = listInfo.size();
                m_sTot = len + 1;
                NewsInfo newsInfo;
                for (int i = 0; i < len; ++i) {
                    newsInfo = listInfo.get(i);
                    if (null != newsInfo) {
                        LocalFileUtils.delFile(newsInfo.getmThumbnail_pic_s());
                        LocalFileUtils.delFile(newsInfo.getmThumbnail_pic_s02());
                        LocalFileUtils.delFile(newsInfo.getmThumbnail_pic_s03());
                    }
                }
            }
        }
    }
}
