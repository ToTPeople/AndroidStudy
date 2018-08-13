package com.example.lfs.androidstudy.MyTryTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lfs.androidstudy.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private MyThread m_thread;
    private boolean m_bPressed = false;
    private Button m_btnPause;
    private String m_strValue = "My ";
    List<String> m_listValue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        m_btnPause = findViewById(R.id.btnPause);
        if (null != m_btnPause) {
            m_btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_bPressed) {
                        m_thread.resumeThread();
                        m_btnPause.setText("Pause");
                    } else {
                        m_thread.pauseThread();
                        m_btnPause.setText("Resume");
                    }
                    m_bPressed = !m_bPressed;
                }
            });
        }

        m_thread = new MyThread();
        m_thread.start();
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
