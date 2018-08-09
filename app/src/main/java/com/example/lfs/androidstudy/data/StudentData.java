package com.example.lfs.androidstudy.data;

/**
 * Created by lfs on 2018/8/8.
 */

public class StudentData {
    private String mName;
    private String mSex;
    private String mNickName;

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(String mSex) {
        this.mSex = mSex;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    @Override
    public String toString() {
        return "StudentData{" +
                "name = '" + mName + '\'' +
                ", sex = '" + mSex + '\'' +
                ", nickName = '" + mNickName + '\'' +
                "}";
    }
}
