package com.example.lfs.androidstudy.ContentProviderLoad;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.lfs.androidstudy.ImageLoadHelper;
import com.example.lfs.androidstudy.data.NewsInfo;

import java.util.List;

/**
 * Created by lfs on 2018/8/1.
 */

class ImageGridAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private Context context;
    private List<NewsInfo> mListData;
    private GridView mListView;

    private int mStart;
    private int mEnd;
    private boolean isFirstIn;


    public ImageGridAdapter(Context context, List<NewsInfo> data, GridView listView){
        if (null != mListData) {
            mListData.clear();
        }
        this.context = context;
        mListData = data;
        mListView = listView;
        mListView.setOnScrollListener(this);
        isFirstIn = true;

        ImageLoadHelper.getInstance().init(mListData, mListView);
    }

    public void updateData(List<NewsInfo> data) {
        if (null != mListData) {
            mListData.clear();
        }

        mListData = data;
        ImageLoadHelper.getInstance().init(mListData, mListView);
    }

    @Override
    public int getCount() {
        return (null == mListData) ? 0 : mListData.size();
    }

    @Override
    public Object getItem(int i) {
        return (null == mListData) ? 0 : mListData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == mListData) {
            return view;
        }

        final ImageView imageView;
        if (null == view) {
            RelativeLayout relativeLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 250);
            imageView = new ImageView(context);
            imageView.setId(i);
            relativeLayout.addView(imageView, layoutParams);

            view = relativeLayout;
            view.setTag(imageView);
        } else {
            imageView = (ImageView) view.getTag();
        }

        NewsInfo newsInfo = mListData.get(i);
        if (null != newsInfo) {
            String path = newsInfo.getmThumbnail_pic_s();

            Log.i("Scroll", "item index: " + i);

            imageView.setTag(path);
            ImageLoadHelper.getInstance().showImage(imageView, path);

            // video add
//            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
//            bitmap = ThumbnailUtils.extractThumbnail(bitmap, 300, 250);
//            imageView.setImageBitmap(bitmap);
        }

        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            ImageLoadHelper.getInstance().loadImages(mStart, mEnd);
        } else {
            ImageLoadHelper.getInstance().cancelAllTask();
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        mStart = i;
        mEnd = i + i1;

        if (isFirstIn && i1 > 0) {
            isFirstIn = false;
            ImageLoadHelper.getInstance().loadImages(mStart, mEnd);
        }
    }
}
