package com.example.lfs.androidstudy.ContentProviderLoad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.lfs.androidstudy.R;
import com.example.lfs.androidstudy.ResourceLoad;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {
//    private List<String> fileList = new ArrayList<>();
    private GridView listView;
    ImageGridAdapter arrayAdapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                int result = bundle.getInt(ResourceLoad.RESULT);
//                bundle.getStringArrayList();
                Log.i("BoradCast", "Download result is: " + result);
                if (1 == result) {
                    arrayAdapter.updateData(ResourceLoad.getInstance().fileList);
                    arrayAdapter.notifyDataSetChanged();
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

        initView();
    }

    private void initView() {
        listView = new GridView(ListViewActivity.this);
        listView.setColumnWidth(300);
        listView.setNumColumns(4);
//        listView.setHorizontalSpacing(10);
//        listView.setVerticalSpacing(10);
        setContentView(listView);

        arrayAdapter = new ImageGridAdapter(ListViewActivity.this
                , ResourceLoad.getInstance().has_load_net_data ? ResourceLoad.getInstance().fileList : null
                , listView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = listView.getItemAtPosition(i);
                String path = (String) o;
                Toast.makeText(ListViewActivity.this, "Selected :" + " " + path, Toast.LENGTH_LONG).show();
            }
        });
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


