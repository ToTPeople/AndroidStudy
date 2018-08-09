package com.example.lfs.androidstudy.Helper;

import com.example.lfs.androidstudy.data.StudentData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by lfs on 2018/8/8.
 */

public class SAXParserXML {

    //throws Exception
    public static List<StudentData> saxParserXML(InputStream inputStream) {
        SAXHandler saxHandler = null;
        if (null == inputStream) {
            return null;
        }

        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            //初始化Sax解析器
            SAXParser saxParser = saxParserFactory.newSAXParser();
            //新建解析处理器
            saxHandler = new SAXHandler();
            //将解析交给处理器
            saxParser.parse(inputStream, saxHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return saxHandler.getListStudentData();
    }

    // 解析XML
    public static class SAXHandler extends DefaultHandler {
        private List<StudentData> mListStudentData;
        private StudentData mStudentData;
        //用于存储读取的临时变量
        private String mTmpString;

        public List<StudentData> getListStudentData() {
            return mListStudentData;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            mTmpString = new String(ch, start, length);

            super.characters(ch, start, length);
        }

        @Override
        public void startDocument() throws SAXException {
            mListStudentData = new ArrayList<>();
            super.startDocument();
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("student")) {
                mStudentData = new StudentData();
            } else if (qName.equals("name")) {
                mStudentData.setName(qName);
                mStudentData.setSex(attributes.getValue("sex"));
            }

            super.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("student")) {
                mListStudentData.add(mStudentData);
            }
            if (qName.equals("name")) {
                mStudentData.setName(mTmpString);
            } else if (qName.equals("nickName")) {
                mStudentData.setNickName(mTmpString);
            }

            super.endElement(uri, localName, qName);
        }
    }
}
