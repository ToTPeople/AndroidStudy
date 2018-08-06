package com.example.lfs.androidstudy.ContentProviderLoad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.lfs.androidstudy.R;
import com.example.lfs.androidstudy.ResourceLoad;

import java.util.ArrayList;
import java.util.List;

public class ResourseModel extends AppCompatActivity {
    private List<String> fileList = new ArrayList<>();
    private GridView listView;
    ImageGridAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resourse_model);

        initView();
    }

    private void initView() {
//        listView = new ListView(ResourseModel.this);
        listView = new GridView(ResourseModel.this);
        listView.setColumnWidth(300);
        listView.setNumColumns(4);
//        listView.setHorizontalSpacing(10);
//        listView.setVerticalSpacing(10);
        setContentView(listView);

//        if (ResourceLoad.getInstance().has_load_image) {
            arrayAdapter = new ImageGridAdapter(ResourseModel.this, ResourceLoad.getInstance().fileList, listView);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Object o = listView.getItemAtPosition(i);
                    String path = (String) o;
                    Toast.makeText(ResourseModel.this, "Selected :" + " " + path, Toast.LENGTH_LONG).show();
                }
            });
//        }
    }
}


