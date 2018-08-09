package com.example.lfs.androidstudy.XMLTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lfs.androidstudy.Helper.SAXParserXML;
import com.example.lfs.androidstudy.R;
import com.example.lfs.androidstudy.ResourceLoad;
import com.example.lfs.androidstudy.data.StudentData;

import java.util.ArrayList;
import java.util.List;

public class XMLParserActivity extends AppCompatActivity {
    private ListView mListView;
    private List<StudentData> mListData;

    private final String XML_FILE = "/mnt/sdcard/tmp/student.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmlparser);

        mListView = findViewById(R.id.xml_parser_view);

        mListData = ResourceLoad.getInstance().getListStudentData(XML_FILE);
        if (null == mListData) {
            Toast.makeText(this, "File not exist:" + XML_FILE, Toast.LENGTH_LONG).show();
            mListData = new ArrayList<>();
        }
        ArrayAdapter<StudentData> arrayAdapter = new ArrayAdapter<StudentData>(this
                , android.R.layout.simple_list_item_1
                , mListData);

        mListView.setAdapter(arrayAdapter);
    }
}
