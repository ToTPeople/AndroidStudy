package com.example.lfs.androidstudy.data;

/**
 * Created by lfs on 2018/8/3.
 */

public class NewsInfo {

    public static final int MAX_COUNT = 0;

    private static String sName;

    private String uniquekey;

    private String mTitle;

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }
}
