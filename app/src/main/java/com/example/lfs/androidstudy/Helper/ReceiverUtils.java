package com.example.lfs.androidstudy.Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by lfs on 2018/8/16.
 */

public class ReceiverUtils {
    private static Context m_context;
    private static LocalBroadcastManager m_localBroadcastManager;

    public static void init(Context context) {
        if (null == context) {
            return;
        }

        m_context = context;
        m_localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public static void sendLocalBroadcast(Intent intent) {
        if (null != intent && null != m_localBroadcastManager) {
            m_localBroadcastManager.sendBroadcast(intent);
        }
    }

    public static void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (null != m_localBroadcastManager) {
            m_localBroadcastManager.registerReceiver(receiver, filter);
        }
    }

    public static void unregisterLocalReceiver(BroadcastReceiver receiver) {
        if (null != m_localBroadcastManager) {
            m_localBroadcastManager.unregisterReceiver(receiver);
        }
    }
}
