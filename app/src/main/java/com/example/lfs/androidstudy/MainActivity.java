package com.example.lfs.androidstudy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lfs.androidstudy.ContentProviderLoad.ResourseModel;
import com.example.lfs.androidstudy.InternalStorageDemo.InternalStorageDemo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnInterStorage;
    private Button btnExterStorage;
    private Button btnResourceModel;
    private ImageView imageView;

    final public static int REQUEST_CODE_ASK_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ResourceLoad.getInstance().context = MainActivity.this;

        // 申请权限
        List<String> permissionList = new ArrayList<>();
        int check = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (check != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_EXTERNAL_STORAGE);
        }
        check = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET);
        if (check != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.INTERNET);
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, permissionList.toArray(new String[permissionList.size()]), REQUEST_CODE_ASK_EXTERNAL_STORAGE);
        } else {
//            ResourceLoad.getInstance().getImages();
            ResourceLoad.getInstance().getUrlJsonData();
        }
//        testParserJson();
//        ResourceLoad.getInstance();

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

        btnExterStorage = findViewById(R.id.btnExteralStorage);

        btnResourceModel = findViewById(R.id.btnResourceModel);
        if (null != btnResourceModel) {
            btnResourceModel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ResourseModel.class);
                    startActivity(intent);

//                    String path = "http://04.imgmini.eastday.com/mobile/20180803/20180803145041_2afaa556716c3f05a9f1cf9c580a107c_1_mwpm_03200403.jpg";
//                    new my_NewsAsyncTask(path, imageView).execute(path);
                }
            });
        }

        imageView = findViewById(R.id.imageView);

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
                            Toast.makeText(this,s+"权限被拒绝了",Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (isSuccess) {
//                        ResourceLoad.getInstance().getImages();
                        ResourceLoad.getInstance().getUrlJsonData();
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void testParserJson() {
//        InputStream json1 = {"name":"sam","age":18,"weight":60} //一个json对象
//        InputStream json2 = [12,13,15]                    //json2 一个json数组
//        InputStream json3 = [{"name":"sam","age":18},{"name":"leo","age":19},{"name":"sky", "age":20}] //含对象的数组

        String strJson = "{\"reason\":\"成功的返回\",\"result\":{\"stat\":\"1\",\"data\":[{\"uniquekey\":\"d5fb4c151b318427c76ac2bba50dfaaa\",\"title\":\"“云雀”在上海登陆！浙江成了暴雨最大的地方！还有一个坏消息\",\"date\":\"2018-08-03 14:50\",\"category\":\"头条\",\"author_name\":\"钱江晚报\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180803145041658.html\",\"thumbnail_pic_s\":\"http:\\/\\/04.imgmini.eastday.com\\/mobile\\/20180803\\/20180803145041_2afaa556716c3f05a9f1cf9c580a107c_1_mwpm_03200403.jpg\"},{\"uniquekey\":\"b3d42acbbda4b9d234e359a31a712756\",\"title\":\"医生接生完婴儿, 发现婴儿手上握着什么东西, 细看后不淡定了\",\"date\":\"2018-08-03 14:44\",\"category\":\"头条\",\"author_name\":\"新农晓事\",\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/180803144437592.html\",\"thumbnail_pic_s\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180803\\/20180803144437_aae3a5c3bf5113aa69cac0998e1ab613_2_mwpm_03200403.jpg\",\"thumbnail_pic_s02\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180803\\/20180803144437_aae3a5c3bf5113aa69cac0998e1ab613_3_mwpm_03200403.jpg\",\"thumbnail_pic_s03\":\"http:\\/\\/03.imgmini.eastday.com\\/mobile\\/20180803\\/20180803144437_aae3a5c3bf5113aa69cac0998e1ab613_1_mwpm_03200403.jpg\"}]}}\n";
        //
//        InputStream input;
//        JsonReader reader = new JsonReader(new InputStreamReader(strJson));
        try {
            JSONObject obj = new JSONObject(strJson);
            JSONObject objResult = obj.getJSONObject("result");
            JSONArray jsonArray = objResult.getJSONArray("data");
            int len = jsonArray.length();
            for (int i = 0; i < len; ++i) {
                obj = jsonArray.getJSONObject(i);
                String path = obj.getString("thumbnail_pic_s");
                Log.i("Web", "------------ 6 path is: " + path);
            }
//            List<Object> ages = new ArrayList<Object>();
//            reader.beginArray();
//            while (reader.hasNext()) {
//                reader.biginObject();
//                while(reader.hasNext()){
//                    String keyName = reader.nextName();
//                    if("name".equals(keyName)){
//                        String name = reader.nextString();
//                    }else if("age".equals(keyName)){
//                        int age = reader.nextInt();
//                    }else if("weight".equals(keyName)){
//                        int weight = reader.nextInt();
//                    }
//                }
//                reader.endObject();
//            }
//            reader.endArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class my_NewsAsyncTask extends AsyncTask<String,Void,Bitmap> {
        private String path;
        private ImageView imageView;

        my_NewsAsyncTask(String strPath, ImageView imageView) {
            path = strPath;
            this.imageView = imageView;
        }

        //String...params是可变参数接受execute中传过来的参数
        @Override
        protected Bitmap doInBackground(String... params) {

            String path = params[0];
            Bitmap bitmap = null;

            try {
                URL myFileUrl = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();


//                URL mUrl = new URL(path);
//                HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
//                BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
//                bitmap = BitmapFactory.decodeStream(is);
//                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }


            //这里同样调用我们的getBitmapFromeUrl
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            //预加载
//            BitmapFactory.decodeFile(path, options);
//            //获取预加载之后的宽高
//            int originalw = options.outWidth;
//            int originalh = options.outHeight;
//            options.inSampleSize = getSimpleSize(originalw, originalh, 300, 250);
//            options.inJustDecodeBounds=false;
//
//            Bitmap bitmap = BitmapFactory.decodeFile(path, options);

            return bitmap;
        }
        //这里的bitmap是从doInBackgroud中方法中返回过来的
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (null != imageView) {
                super.onPostExecute(bitmap);
                imageView.setImageBitmap(bitmap);
            }
        }

        //将预加载的宽高与要显示的宽高进行采样率计算
        private int getSimpleSize(int originalw, int originalh, int pixelW, int pixelH) {
            int simpleSize=1;
            if (originalw>originalh && originalw>pixelW) {
                simpleSize=originalw/pixelW;
            } else if (originalw<originalh && originalh>pixelH) {
                simpleSize=originalh/pixelH;
            }
            if (simpleSize<1) {
                simpleSize=1;
            }
            return simpleSize;
        }
    }
}
