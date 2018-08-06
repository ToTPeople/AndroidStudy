package com.example.lfs.androidstudy.ContentProviderLoad;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.example.lfs.androidstudy.ResourceLoad;

public class DataLoadService extends Service {
    private final IBinder mBinder = new MyBinder();
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private static boolean hasDataLoaded = false;

    public final static String RESULT = "result";

    public final static int NET_DATA_LOADED = 0x10;
    public final static Handler dataHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NET_DATA_LOADED:
                    DataLoadService.publishResults(-1);
                    break;
            }
        }

    };

    public DataLoadService() {
    }

    public final static void publishResults(int result) {
//        Intent intent = new Intent("com.example.lfs.androidstudy.MainActivity");
//        intent.putExtra(RESULT, result);
//        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (!hasDataLoaded) {
            hasDataLoaded = true;
            ResourceLoad.getInstance().getUrlJsonData();
        }
        return mBinder;
    }

    @Override
    public void onCreate() {
        HandlerThread handlerThread = new HandlerThread("ServiceStartArguments");
        handlerThread.start();

        mServiceLooper = handlerThread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        if (null != intent) {
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            mServiceHandler.sendMessage(msg);
        }

        return START_NOT_STICKY;
    }

    // startService时使用这个类
    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            ResourceLoad.getInstance().getUrlJsonData();
            stopSelf();
        }
    }

    // bindService时使用这个类
    private class MyBinder extends Binder {
        public MyBinder() {
            super();
        }
    }
}
