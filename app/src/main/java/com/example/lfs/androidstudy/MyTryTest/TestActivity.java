package com.example.lfs.androidstudy.MyTryTest;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.example.lfs.androidstudy.Demo.MapTraversing;
import com.example.lfs.androidstudy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestActivity extends FragmentActivity implements UIoperator {
    private MyThread m_thread;
    private boolean m_bPressed = false;
    private Button m_btnPause;
    private String m_strValue = "My ";
    List<String> m_listValue = new ArrayList<>();

    public enum TestType {
        TEST_TYPE_PAUSE_THREAD,             // 线程暂停
        TEST_TYPE_FRAGMENT_ADD,             // fragment添加
    }
//    private TestType m_eTestType = TestType.TEST_TYPE_PAUSE_THREAD;
    private TestType m_eTestType = TestType.TEST_TYPE_FRAGMENT_ADD;

    private FirstFragment m_firstFragment;

    private List<Integer>   m_listId = new ArrayList<>();           // Fragment list id
    private BaseFragment    m_curFragment = null;                   // 当前显示Fragment
    private int             m_curId = -1;                           // 当前显示Fragment id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Log.i("Fragment lift", "[TestActivity onCreate]");

        m_btnPause = findViewById(R.id.btnPause);
        if (null != m_btnPause) {
            m_btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TestType.TEST_TYPE_PAUSE_THREAD == m_eTestType) {
                        if (m_bPressed) {
                            m_thread.resumeThread();
                            m_btnPause.setText("Pause");
                        } else {
                            m_thread.pauseThread();
                            m_btnPause.setText("Resume");
                        }
                        m_bPressed = !m_bPressed;
                    } else if (TestType.TEST_TYPE_FRAGMENT_ADD == m_eTestType) {
                        onOpenFragment(FragmentID.FIRST);
                    }
                }
            });
        }

        if (TestType.TEST_TYPE_PAUSE_THREAD == m_eTestType) {
            m_thread = new MyThread();
            m_thread.start();
        } else if (TestType.TEST_TYPE_FRAGMENT_ADD == m_eTestType) {
            m_firstFragment = new FirstFragment();
        }
    }

    @Override
    public void updata() {
        ToastUtils.showShort("Updating");
    }

    @Override
    public void backTo() {
        ToastUtils.showShort("Back to preview!!!");
    }

    @Override
    public void nextTo() {
        ToastUtils.showShort("Go to next view!!!");
    }

    private BaseFragment createFragmentById(int nId) {
        BaseFragment baseFragment = null;
        if (FragmentID.FIRST == nId) {
            baseFragment = new FirstFragment();
        } else if (FragmentID.SECOND == nId) {
            baseFragment = new SecondFragment();
        }

        return baseFragment;
    }

    public void onOpenFragment(int nId) {
        HashMap<String, Object> param = null;
        if (null != m_curFragment) {
            param = m_curFragment.setNextData();
            getSupportFragmentManager().beginTransaction().remove(m_curFragment).commit();
            m_curFragment = null;
        }

        m_curFragment = createFragmentById(nId);
        if (null != m_curFragment) {
            getSupportFragmentManager().beginTransaction().add(R.id.layoutTryTest, m_curFragment).commit();
            if (null != param) {
                m_curFragment.setData(param);
            }
            m_curId = nId;
            // 进list
            m_listId.add(nId);

            if (null != m_btnPause && m_btnPause.isShown()) {
                m_btnPause.setVisibility(View.GONE);
            }
        }
    }

    public void onBackFragment() {
        HashMap<String, Object> param = null;
        if (null != m_curFragment) {
            param = m_curFragment.setBackData();
            getSupportFragmentManager().beginTransaction().remove(m_curFragment).commit();
            m_curFragment = null;
            // 出list
            m_listId.remove(m_listId.indexOf(m_curId));
            m_curId = -1;
        }

        if (m_listId.size() > 0) {
            int nId = m_listId.get(m_listId.size() - 1);
            m_curFragment = createFragmentById(nId);
            if (null != m_curFragment) {
                getSupportFragmentManager().beginTransaction().add(R.id.layoutTryTest, m_curFragment).commit();
                if (null != param) {
                    m_curFragment.backData(param);
                }
                m_curId = nId;
            }
        } else {
            if (null != m_btnPause) {
                m_btnPause.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Fragment lift", "[TestActivity onStart]");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Fragment lift", "[TestActivity onStop]");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Fragment lift", "[TestActivity onResume]");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Fragment lift", "[TestActivity onPause]");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Fragment lift", "[TestActivity onRestart]");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private class MyThread extends Thread {
        private final Object lock = new Object();
        private boolean pause = false;

        /**
         * 调用这个方法实现暂停线程
         */
        void pauseThread() {
            pause = true;
        }

        /**
         * 调用这个方法实现恢复线程的运行
         */
        void resumeThread() {
            pause = false;
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        /**
         * 注意：这个方法只能在run方法里调用，不然会阻塞主线程，导致页面无响应
         */
        void onPause() {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                int index = 0;
                while (true) {
                    Log.i("GoOnTest", "before pause!!! " + m_listValue.size());
                    // 让线程处于暂停等待状态
                    while (pause) {
                        onPause();
                    }
                    m_strValue = changeString(m_strValue, m_listValue);
                    Log.i("GoOnTest", "after pause!!! " + m_listValue.size());
                    try {
                        System.out.println(index);
                        Thread.sleep(500);
                        ++index;
                    } catch (InterruptedException e) {
                        //捕获到异常之后，执行break跳出循环
                        break;
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        private String changeString(String strStr, List<String> list) {
            strStr += "2";
            list.add(strStr);
            return strStr;
        }
    }
}
