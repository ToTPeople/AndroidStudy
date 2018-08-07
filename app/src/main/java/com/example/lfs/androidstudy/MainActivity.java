package com.example.lfs.androidstudy;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lfs.androidstudy.ContentProviderLoad.DataLoadService;
import com.example.lfs.androidstudy.ContentProviderLoad.ResourseModel;
import com.example.lfs.androidstudy.InternalStorageDemo.InternalStorageDemo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private Button btnInterStorage;
    private Button btnExterStorage;
    private Button btnResourceModel;
    private ImageView imageView;
    private static boolean hasDataLoaded = false;
    private static boolean hasBindService = false;
    private boolean hasNetDataLoadComplete = false;

    final public static int REQUEST_CODE_ASK_EXTERNAL_STORAGE = 123;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                int resultCode = bundle.getInt(DataLoadService.RESULT);
                if (RESULT_OK == resultCode) {
                    hasNetDataLoadComplete = true;
                    Toast.makeText(MainActivity.this, "Network data load complete!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Network data load error!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    // 启动服务加载数据
    private void startPreparedData() {
        if (!hasBindService) {
            hasBindService = true;
//            Intent intent = new Intent(MainActivity.this, NetDataLoadIntentService.class);
            Intent intent = new Intent(MainActivity.this, DataLoadService.class);
            startService(intent);
//            bindService(intent, MainActivity.this, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ResourceLoad.getInstance().context = MainActivity.this;

        // 申请权限
        requestPermission();

        registerReceiver(receiver, new IntentFilter());

        // inter storage
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

        // exter storage
        btnExterStorage = findViewById(R.id.btnExteralStorage);

        // listview
        btnResourceModel = findViewById(R.id.btnResourceModel);
        if (null != btnResourceModel) {
            btnResourceModel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (hasNetDataLoadComplete) {
                        Intent intent = new Intent(MainActivity.this, ResourseModel.class);
                        startActivity(intent);
//                    } else {
//                        Toast.makeText(MainActivity.this, "Network data loading, please wait...", Toast.LENGTH_LONG);
//                    }
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    // 申请权限
    private void requestPermission() {
        List<String> permissionList = new ArrayList<>();
        int check = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (check != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        check = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (check != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        check = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET);
        if (check != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.INTERNET);
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, permissionList.toArray(new String[permissionList.size()]), REQUEST_CODE_ASK_EXTERNAL_STORAGE);
        } else {
            if (!hasDataLoaded) {
                hasDataLoaded = true;
                // 启动服务加载数据
                startPreparedData();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_EXTERNAL_STORAGE:
            {
                if (grantResults.length > 0) {
                    boolean isSuccess = true;
                    for (int i = 0; i < grantResults.length; i++) {

                        int grantResult = grantResults[i];
                        if (grantResult == PackageManager.PERMISSION_DENIED){ //这个是权限拒绝
                            String s = permissions[i];
                            isSuccess = false;
                            Toast.makeText(this,s+"权限被拒绝了", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (isSuccess) {
                        if (!hasDataLoaded) {
                            hasDataLoaded = true;
                            // 启动服务加载数据
                            startPreparedData();
                        }
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        //
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        //
    }

}
