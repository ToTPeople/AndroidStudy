package com.example.lfs.androidstudy.ContentProviderLoad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                int result = bundle.getInt(ResourceLoad.RESULT);
                List<NewsInfo> newsList = (List<NewsInfo>) bundle.getSerializable(ResourceLoad.NEWS_DATA);

                Log.i("BoradCast", "Download result is: " + result);
                if (1 == result) {
                    if (use_list_view) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resourse_model);
//        use_list_view = true;

        initView();
    }

    private void initView() {
        if (use_list_view) {
            mListView = new ListView(ListViewActivity.this);
            setContentView(mListView);

            newsListAdapter = new NewsListAdapter(ListViewActivity.this
                    , ResourceLoad.getInstance().has_load_net_data ? ResourceLoad.getInstance().mListNewsInfo : null
                    , mListView);
            mListView.setAdapter(newsListAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Object o = mListView.getItemAtPosition(i);
                    NewsInfo newsInfo = (NewsInfo) o;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(newsInfo.getmUrl());
                    intent.setData(uri);
//                    startActivity(intent);
                    startActivity(Intent.createChooser(intent, "选择要使用的浏览器"));
                }
            });
        } else {
            listView = new GridView(ListViewActivity.this);
            listView.setColumnWidth(300);
            listView.setNumColumns(4);
//        listView.setHorizontalSpacing(10);
//        listView.setVerticalSpacing(10);
            setContentView(listView);

            arrayAdapter = new ImageGridAdapter(ListViewActivity.this
                    , ResourceLoad.getInstance().has_load_net_data ? ResourceLoad.getInstance().mListNewsInfo : null
                    , listView);
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
        registerReceiver(broadcastReceiver, new IntentFilter(ResourceLoad.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}


