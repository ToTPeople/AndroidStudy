package com.example.lfs.androidstudy.ContentProviderLoad;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.example.lfs.androidstudy.MainActivity;
import com.example.lfs.androidstudy.MyAIDLService;
import com.example.lfs.androidstudy.R;
import com.example.lfs.androidstudy.ResourceLoad;

public class DataLoadService extends Service {
//    private final IBinder mBinder = new MyBinder();
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private static boolean hasDataLoaded = false;

    MyAIDLService.Stub mBinder = new MyAIDLService.Stub() {
        @Override
        public int plus(int a, int b) throws RemoteException {
            return (a+b);
        }

        @Override
        public String toUpperCase(String str) throws RemoteException {
            if (null != str) {
                return str.toUpperCase();
            }

            return null;
        }

        @Override
        public void downNetData() throws RemoteException {
//            if (!hasDataLoaded) {
//                Log.i("ServiceTest", "[downNetData], hasDataLoaded is " + hasDataLoaded);
//                hasDataLoaded = true;
//                ResourceLoad.getInstance().getUrlJsonData();
//            }
        }
    };

    public DataLoadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("ServiceTest", "[onBind], intent is " + intent);
        if (!hasDataLoaded) {
            hasDataLoaded = true;
            ResourceLoad.getInstance().getUrlJsonData();
        }
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.i("ServiceTest", "[onCreate] process id: " + android.os.Process.myPid());

        HandlerThread handlerThread = new HandlerThread("ServiceStartArguments");
        handlerThread.start();

        mServiceLooper = handlerThread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ServiceTest", "[onStartCommand], intent is " + intent);
//        return super.onStartCommand(intent, flags, startId);
        if (null != intent) {
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            mServiceHandler.sendMessage(msg);
        }

//        stopSelf();

//        return START_NOT_STICKY;
        return START_STICKY;
//        return START_REDELIVER_INTENT;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("ServiceTest", "[onDestroy]");
    }
}
