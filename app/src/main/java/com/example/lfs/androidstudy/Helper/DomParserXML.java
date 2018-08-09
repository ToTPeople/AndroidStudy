package com.example.lfs.androidstudy.Helper;

import com.example.lfs.androidstudy.data.StudentData;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by lfs on 2018/8/8.
 */

public class DomParserXML {

    public List<StudentData> getListStudentData(InputStream inputStream) {
        List<StudentData> listData = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            //获得Document对象
            Document document = builder.parse(inputStream);
            //获得student的List
            NodeList nodeList = document.getElementsByTagName("student");

            int len = nodeList.getLength();
            for (int i = 0; i < len; ++i) {
                Node node = nodeList.item(i);
                //获得student标签里面的标签
                if (node.hasChildNodes()) {
                    NodeList childNodes = node.getChildNodes();

                    StudentData studentData = new StudentData();
                    for (int j = 0; j < childNodes.getLength(); ++j) {
                        Node childNode = childNodes.item(j);
                        String strName = childNode.getNodeName();
                        if (null == strName) {
                            continue;
                        }
                        if (strName.equals("name")) {
                            // set name
                            studentData.setName(childNode.getTextContent());

                            if (childNode.hasAttributes()) {
                                NamedNodeMap namedNodeMap = childNode.getAttributes();
                                Node sex = namedNodeMap.getNamedItem("sex");
                                // set sex
                                studentData.setSex(sex.getTextContent());
                            }
                        } else if (strName.equals("nickName")) {
                            // set nickName
                            studentData.setNickName(childNode.getTextContent());
                        }
                    }

                    listData.add(studentData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }
}
