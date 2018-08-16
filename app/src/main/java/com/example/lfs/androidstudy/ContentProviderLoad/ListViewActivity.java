package com.example.lfs.androidstudy.ContentProviderLoad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.example.lfs.androidstudy.Helper.ReceiverUtils;
import com.example.lfs.androidstudy.Helper.UIHelper;
import com.example.lfs.androidstudy.R;
import com.example.lfs.androidstudy.ResourceLoad;
import com.example.lfs.androidstudy.data.NewsInfo;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {
    private boolean use_list_view = false;
    private GridView listView;
//    private
    ImageGridAdapter arrayAdapter;

    private ListView mListView;
    NewsListAdapter newsListAdapter;

    private RelativeLayout mRelativeLayout;         // 加载中提示
    private TextView m_strLoading;                  // 加载中提示

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BroadCast_Test", "[onReceive]");
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                int result = bundle.getInt(ResourceLoad.RESULT);
                List<NewsInfo> newsList = (List<NewsInfo>) bundle.getSerializable(ResourceLoad.NEWS_DATA);

                Log.i("BoradCast", "Download result is: " + result + ", newsList: " + newsList.size());
                if (1 == result) {
                    if (use_list_view) {
                        if (null != newsList) {
                            showView(0 < newsList.size());
                        }
                        newsListAdapter.updateData(newsList);
                        newsListAdapter.notifyDataSetChanged();
                    } else {
                        arrayAdapter.updateData(newsList);
                        arrayAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(ListViewActivity.this, "Download failed", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private void showView(boolean hasData) {
        if (hasData) {
            if (null != mListView) {
                mListView.setVisibility(View.VISIBLE);
            }
            if (null != mRelativeLayout) {
                mRelativeLayout.setVisibility(View.GONE);
            }
        } else {
            if (null != mListView) {
                mListView.setVisibility(View.GONE);
            }
            if (null != mRelativeLayout) {
                mRelativeLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resourse_model);
        use_list_view = true;

        initView();
    }

    private void initView() {
        if (use_list_view) {
            // 加载中提示
            mRelativeLayout = new RelativeLayout(ListViewActivity.this);
            RelativeLayout.LayoutParams reLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            m_strLoading = new TextView(ListViewActivity.this);
            m_strLoading.setText("Loading...");
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mRelativeLayout.addView(m_strLoading, layoutParams);

            // ListView
            mListView = new ListView(ListViewActivity.this);
            setContentView(mListView);

            addContentView(mRelativeLayout, reLayout);

            List<NewsInfo> listInfos = new ArrayList<>();
            ResourceLoad.getInstance().getListNewsInfo(listInfos);
            newsListAdapter = new NewsListAdapter(ListViewActivity.this, listInfos, mListView);
            mListView.setAdapter(newsListAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Object o = mListView.getItemAtPosition(i);
                    NewsInfo newsInfo = (NewsInfo) o;
                    UIHelper.viewUrl(ListViewActivity.this, newsInfo.getmUrl(), null);
                }
            });

            boolean hasData = (null != listInfos && 0 < listInfos.size());
            showView(hasData);
        } else {
            listView = new GridView(ListViewActivity.this);
            int nWidth = ScreenUtils.getScreenWidth() / 4;
            listView.setColumnWidth(nWidth);
            listView.setNumColumns(4);
//        listView.setHorizontalSpacing(10);
//        listView.setVerticalSpacing(10);
            setContentView(listView);

            List<NewsInfo> listInfos = new ArrayList<>();
            ResourceLoad.getInstance().getListNewsInfo(listInfos);
            arrayAdapter = new ImageGridAdapter(ListViewActivity.this, listInfos, listView);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Object o = listView.getItemAtPosition(i);
                    NewsInfo newsInfo = (NewsInfo) o;
                    String path = newsInfo.getmThumbnail_pic_s();
                    Toast.makeText(ListViewActivity.this, "Selected :" + " " + path, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReceiverUtils.registerLocalReceiver(broadcastReceiver, new IntentFilter(ResourceLoad.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        ReceiverUtils.unregisterLocalReceiver(broadcastReceiver);
    }
}


