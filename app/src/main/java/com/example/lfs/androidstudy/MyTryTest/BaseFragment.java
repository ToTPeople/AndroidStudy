package com.example.lfs.androidstudy.MyTryTest;

import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * Created by lfs on 2018/8/15.
 */

public abstract class BaseFragment extends Fragment {

    public abstract void backData(HashMap<String, Object> backData);
    public abstract HashMap<String, Object> setBackData();

    public abstract void setData(HashMap<String, Object> data);
    public abstract HashMap<String, Object> setNextData();
}
