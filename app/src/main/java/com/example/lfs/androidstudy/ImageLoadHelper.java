package com.example.lfs.androidstudy;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lfs on 2018/8/2.
 */

public class ImageLoadHelper {
    private String mPaths[];
    private GridView mListView;
    private Set<NewsAsyncTask> mTasks;
    private LruCache<String,Bitmap> mMemoryCaches;
    private static final ImageLoadHelper ourInstance = new ImageLoadHelper();

    static public ImageLoadHelper getInstance() {
        return ourInstance;
    }

    private ImageLoadHelper() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSizes = maxMemory/5;

        mMemoryCaches = new LruCache<String, Bitmap>(cacheSizes) {
            @SuppressLint("NewApi") @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

        mTasks = new HashSet<NewsAsyncTask>();
    }

    public void init(List<String> data, GridView listView) {
        mListView = listView;
        mPaths = new String[data.size()];
        int tot = data.size();
        for (int i = 0; i < tot; ++i) {
            mPaths[i] = data.get(i);
        }
        Log.i("Init", "[ImageLoadHelper:init] data size is: " + tot);
    }

    // get/set image from Lru
    public Bitmap getBitmapFromLrucache(String path) {
        Log.i("Web", "load path is: " + path);
        return mMemoryCaches.get(path);
    }
    public void addBitmapToLrucaches(String path, Bitmap bitmap) {
        if (null == getBitmapFromLrucache(path)) {
            mMemoryCaches.put(path, bitmap);
        }
    }


    public void loadImages(int start, int end) {
        Log.i("Init", "[ImageLoadHelper:loadImages] end index is: " + end);
        for (int i = start; i < end; ++i) {
            String path = mPaths[i];
            if (null != path && getBitmapFromLrucache(path) != null) {
                ImageView imageView = (ImageView) mListView.findViewWithTag(path);

                Log.i("loadImage", "start, end, imageView: " + start + ", " + end + ", " + imageView);
                Log.i("loadImage", "path: " + path);
                if (null != imageView)
                    imageView.setImageBitmap(getBitmapFromLrucache(path));
            } else {
                NewsAsyncTask mNewsAsyncTask = new NewsAsyncTask(path);
                mTasks.add(mNewsAsyncTask);
                mNewsAsyncTask.execute(path);
            }
        }
    }

    //
    public void showImage(ImageView imageView, String path){
        //首先去LruCache中去找图片
        Bitmap bitmap = getBitmapFromLrucache(path);
        Log.i("Scroll", "path: " + path + ", bitmap: " + bitmap);
        //如果不为空，说明LruCache中已经缓存了该图片，则读取缓存直接显示，
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    public void cancelAllTask() {
        if (null != mTasks) {
            for (NewsAsyncTask newsAsyncTask : mTasks) {
                newsAsyncTask.cancel(false);
            }
        }
    }

    public static Bitmap getBitmap(Bitmap bitmap, int screenWidth, int screenHight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHight / h;
        // scale = scale < scale2 ? scale : scale2;
        matrix.postScale(scale, scale);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled())
        {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }


    class NewsAsyncTask extends AsyncTask<String,Void,Bitmap> {
        private String path;

        NewsAsyncTask(String strPath) {
            path = strPath;
        }

        //String...params是可变参数接受execute中传过来的参数
        @Override
        protected Bitmap doInBackground(String... params) {

            String path = params[0];
            Bitmap bitmap = null;

            if (ResourceLoad.LoadType.LOAD_TYPE_DOWNLOAD == ResourceLoad.getInstance().IMAGE_LOAD_TYPE) {
                //这里同样调用我们的getBitmapFromeUrl
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                //预加载
                BitmapFactory.decodeFile(path, options);
                //获取预加载之后的宽高
                int originalw = options.outWidth;
                int originalh = options.outHeight;
                options.inSampleSize = getSimpleSize(originalw, originalh, 300, 250);
                options.inJustDecodeBounds=false;

                bitmap = BitmapFactory.decodeFile(path, options);
                if (null != bitmap) {
                    addBitmapToLrucaches(path, bitmap);
                }
            } else {
                try {
                    URL myFileUrl = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    conn.disconnect();

                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();
                    bitmap = getBitmap(bitmap, 300, 250);
                    w = bitmap.getWidth();
                    h = bitmap.getHeight();


//                URL mUrl = new URL(path);
//                HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
//                BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
//                bitmap = BitmapFactory.decodeStream(is);
//                connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return bitmap;
        }
        //这里的bitmap是从doInBackgroud中方法中返回过来的
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = (ImageView)mListView.findViewWithTag(path);
            if (null != imageView) {
                super.onPostExecute(bitmap);
                imageView.setImageBitmap(bitmap);
            }

            mTasks.remove(this);
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
