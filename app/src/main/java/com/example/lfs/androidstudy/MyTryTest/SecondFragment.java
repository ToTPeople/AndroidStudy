package com.example.lfs.androidstudy.MyTryTest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import java.util.HashMap;

/**
 * Created by lfs on 2018/8/15.
 */

public class SecondFragment extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
//        relativeLayout.setBackgroundColor(0xffffffff);

        Button btnBack = new Button(getContext());
        btnBack.setText("Back to First");
        btnBack.setOnClickListener(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        relativeLayout.addView(btnBack, layoutParams);

        return relativeLayout;
    }

    @Override
    public void onClick(View view) {
        if (getActivity() instanceof TestActivity) {
            TestActivity testActivity = (TestActivity)getActivity();
            if (null != testActivity) {
                testActivity.onBackFragment();
            }
        }
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.remove(this);
//        fragmentTransaction.commit();
//        if (getActivity() instanceof TestActivity) {
//            TestActivity testActivity = (TestActivity)getActivity();
//            if (null != testActivity) {
//                testActivity.updata();
////            testActivity.backTo();
//            }
//        }
    }

    @Override
    public void backData(HashMap<String, Object> backData) {
        //
    }

    @Override
    public HashMap<String, Object> setBackData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("Second", "Call me Second.");
        return param;
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        if (null != data) {
            ToastUtils.showShort("From First: " + data.get("First"));
        }
    }

    @Override
    public HashMap<String, Object> setNextData() {
        return null;
    }
}
