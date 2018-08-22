package com.example.lfs.androidstudy;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.Utils;
import com.example.lfs.androidstudy.ContentProviderLoad.DataLoadService;
import com.example.lfs.androidstudy.ContentProviderLoad.DelSDDataService;
import com.example.lfs.androidstudy.ContentProviderLoad.ListViewActivity;
import com.example.lfs.androidstudy.ContentProviderLoad.NetDataLoadIntentService;
import com.example.lfs.androidstudy.ContentProviderLoad.NotificationService;
import com.example.lfs.androidstudy.Helper.FrescoLoader;
import com.example.lfs.androidstudy.Helper.ReceiverUtils;
import com.example.lfs.androidstudy.InternalStorageDemo.InternalStorageDemo;
import com.example.lfs.androidstudy.MyTryTest.TestActivity;
import com.example.lfs.androidstudy.XMLTest.XMLParserActivity;



public class MainActivity extends AppCompatActivity {
    private Button btnInterStorage;
    private Button btnExterStorage;
    private Button btnResourceModel;
    private Button btnTryTest;
    private Button btnXML;
    private ImageView imageView;
    private static boolean hasDataLoaded = false;
    private static boolean hasBindService = false;
    private static boolean bDelData = false;

    private MyAIDLService myAIDLService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myAIDLService = MyAIDLService.Stub.asInterface(iBinder);
            try {
                int result = myAIDLService.plus(3, 5);
                String upperStr = myAIDLService.toUpperCase("hello world");
                Log.i("ServiceTest", "result is " + result);
                Log.i("ServiceTest", "upperStr is " + upperStr);
                myAIDLService.downNetData();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //
        }
    };

    // 启动服务加载数据
    private void startPreparedData() {
        if (!hasBindService) {
            hasBindService = true;
//            Intent intent = new Intent(MainActivity.this, NetDataLoadIntentService.class);
//            intent.setAction(NetDataLoadIntentService.ACTION_GET_NET_DATA);

//            Intent intent = new Intent(MainActivity.this, DataLoadService.class);
//            startService(intent);                                                   // 启动服务
//            bindService(intent, serviceConnection, BIND_AUTO_CREATE);     // 绑定启动服务
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("ServiceTest", "[MainActivity:onCreate] process id: " + android.os.Process.myPid());

        Utils.init(this);
        ReceiverUtils.init(this);
        ResourceLoad.getInstance().context = MainActivity.this;
        FrescoLoader.getInstance().init(this);

        // 申请权限
        requestPermission();

        // enter inter storage
        btnInterStorage = findViewById(R.id.btnInterStorage);
        if (null != btnInterStorage) {
            btnInterStorage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, InternalStorageDemo.class);
                    startActivity(intent);
                }
            });
        }

        // enter exter storage
        btnExterStorage = findViewById(R.id.btnExteralStorage);

        // enter listview
        btnResourceModel = findViewById(R.id.btnResourceModel);
        if (null != btnResourceModel) {
            btnResourceModel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (bDelData) {
//                        ToastUtils.showShort("Del data!!!");
//                    } else {
                        Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
                        startActivity(intent);
//                    }
                }
            });
            btnResourceModel.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    bDelData = true;
                    Intent intent = new Intent(MainActivity.this, DelSDDataService.class);
                    intent.setAction(DelSDDataService.ACTION_DEL_DATA);
                    startService(intent);

                    return false;
                }
            });
        }

        // enter XML
        btnXML = findViewById(R.id.btnXML);
        if (null != btnXML) {
            btnXML.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, XMLParserActivity.class);
                    startActivity(intent);
                }
            });
        }

        // try test
        btnTryTest = findViewById(R.id.btnTest);
        if (null != btnTryTest) {
            btnTryTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, TestActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    // 申请权限
    private void requestPermission() {
        String[] permissionArray = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        PermissionUtils permissionUtils = PermissionUtils.permission(permissionArray);
        permissionUtils.callback(new DealPermission());
        permissionUtils.request();
    }

    // 权限请求回调
    private class DealPermission implements PermissionUtils.SimpleCallback {
        @Override
        public void onGranted() {
            if (!hasDataLoaded) {
                hasDataLoaded = true;
                // 启动服务加载数据
                startPreparedData();
            }
        }

        @Override
        public void onDenied() {
            //
        }
    }

}
