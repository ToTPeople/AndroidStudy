package com.example.lfs.androidstudy;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
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
import com.example.lfs.androidstudy.ContentProviderLoad.ListViewActivity;
import com.example.lfs.androidstudy.InternalStorageDemo.InternalStorageDemo;
import com.example.lfs.androidstudy.XMLTest.XMLParserActivity;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private Button btnInterStorage;
    private Button btnExterStorage;
    private Button btnResourceModel;
    private Button btnXML;
    private ImageView imageView;
    private static boolean hasDataLoaded = false;
    private static boolean hasBindService = false;

    final public static int REQUEST_CODE_ASK_EXTERNAL_STORAGE = 123;


    // 启动服务加载数据
    private void startPreparedData() {
        if (!hasBindService) {
            hasBindService = true;
//            Intent intent = new Intent(MainActivity.this, NetDataLoadIntentService.class);
            Intent intent = new Intent(MainActivity.this, DataLoadService.class);
            startService(intent);                                                   // 启动服务
//            bindService(intent, MainActivity.this, Context.BIND_AUTO_CREATE);     // 绑定启动服务
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ResourceLoad.getInstance().context = MainActivity.this;

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
                    Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
                    startActivity(intent);
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
        check = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE);
        if (check != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
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
