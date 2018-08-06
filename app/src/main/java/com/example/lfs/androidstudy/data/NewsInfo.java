package com.example.lfs.androidstudy.data;

/**
 * Created by lfs on 2018/8/3.
 */

public class NewsInfo {

    public static final int MAX_COUNT = 0;

    private static String sName;

    private String uniquekey;

    private String mTitle;          // 标题
    private String mDate;           // 日期
    private String mCategory;       // 类别
    private String mAuthor_name;    // 来源
    private String mUrl;            // web link
    private String mThumbnail_pic_s;// 配图
    private String mThumbnail_pic_s02;
    private String mThumbnail_pic_s03;

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

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmAuthor_name() {
        return mAuthor_name;
    }

    public void setmAuthor_name(String mAuthor_name) {
        this.mAuthor_name = mAuthor_name;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmThumbnail_pic_s() {
        return mThumbnail_pic_s;
    }

    public void setmThumbnail_pic_s(String mThumbnail_pic_s) {
        this.mThumbnail_pic_s = mThumbnail_pic_s;
    }

    public String getmThumbnail_pic_s02() {
        return mThumbnail_pic_s02;
    }

    public void setmThumbnail_pic_s02(String mThumbnail_pic_s02) {
        this.mThumbnail_pic_s02 = mThumbnail_pic_s02;
    }

    public String getmThumbnail_pic_s03() {
        return mThumbnail_pic_s03;
    }

    public void setmThumbnail_pic_s03(String mThumbnail_pic_s03) {
        this.mThumbnail_pic_s03 = mThumbnail_pic_s03;
    }
}
