package com.example.lfs.androidstudy.MyTryTest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import java.util.HashMap;

/**
 * Created by lfs on 2018/8/14.
 */

public class FirstFragment extends BaseFragment implements View.OnClickListener {
    private TestActivity m_testActivity;
    private Button m_btnBack;
    private Button m_btnNext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        Log.i("Fragment lift", "[FirstFragment onCreateView]");

        RelativeLayout relativeLayout = new RelativeLayout(getContext());
//        relativeLayout.setBackgroundColor(0xfff00fff);
        relativeLayout.setFocusable(true);
        relativeLayout.setFocusableInTouchMode(true);

        m_btnBack = new Button(getContext());
        m_btnBack.setText("Back");
        m_btnBack.setOnClickListener(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLayout.addView(m_btnBack, layoutParams);

        m_btnNext = new Button(getContext());
        m_btnNext.setText("Next");
        m_btnNext.setOnClickListener(this);
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLayout.addView(m_btnNext, layoutParams);

        return relativeLayout;
    }

    @Override
    public void onClick(View view) {
        if (getActivity() instanceof TestActivity) {
            TestActivity testActivity = (TestActivity)getActivity();
            if (null != testActivity) {
                if (view == m_btnBack) {
                    testActivity.onBackFragment();
                } else if (view == m_btnNext) {
                    testActivity.onOpenFragment(FragmentID.SECOND);
                }
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
        if (null != backData) {
            ToastUtils.showShort("Back from second: " + backData.get("Second"));
        }
    }

    @Override
    public HashMap<String, Object> setBackData() {
        return null;
    }

    @Override
    public void setData(HashMap<String, Object> data) {
    }

    @Override
    public HashMap<String, Object> setNextData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("First", "I'm First!!!");
        return param;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Fragment lift", "[FirstFragment onActivityCreated]");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("Fragment lift", "[FirstFragment onAttach]");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Fragment lift", "[FirstFragment onCreate]");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Fragment lift", "[FirstFragment onStart]");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Fragment lift", "[FirstFragment onStop]");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Fragment lift", "[FirstFragment onPause]");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Fragment lift", "[FirstFragment onResume]");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("Fragment lift", "[FirstFragment onDestroyView]");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Fragment lift", "[FirstFragment onDestroy]");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("Fragment lift", "[FirstFragment onDetach]");
    }
}
