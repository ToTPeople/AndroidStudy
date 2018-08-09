package com.example.lfs.androidstudy.Helper;

import android.util.Xml;

import com.example.lfs.androidstudy.data.StudentData;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lfs on 2018/8/8.
 */

public class PullParserXML {

    public List<StudentData> getListStudentData(InputStream inputStream) {
        List<StudentData> listData = null;
        StudentData studentData = null;

        try {
            //创建xmlPull解析器
            XmlPullParser xmlPullParser = Xml.newPullParser();
            ///初始化xmlPull解析器
            xmlPullParser.setInput(inputStream, "utf-8");
            //读取文件的类型
            int type = xmlPullParser.getEventType();

            String strName;
            //无限判断文件类型进行读取
            while (type != XmlPullParser.END_DOCUMENT) {
                strName = xmlPullParser.getName();
                if (null != strName) {
                    switch (type) {
                        //开始标签
                        case XmlPullParser.START_TAG:
                            if (strName.equals("students")) {
                                listData = new ArrayList<>();
                            } else if (strName.equals("student")) {
                                studentData = new StudentData();
                            } else if (strName.equals("name")) {
                                studentData.setSex(xmlPullParser.getAttributeValue(null, "sex"));
                                studentData.setName(xmlPullParser.nextText());
                            } else if (strName.equals("nickName")) {
                                studentData.setNickName(xmlPullParser.nextText());
                            }
                            break;

                        //结束标签
                        case XmlPullParser.END_TAG:
                            if (strName.equals("student")) {
                                listData.add(studentData);
                            }
                            break;

                        default:
                            break;
                    }
                }

                type = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }
}
