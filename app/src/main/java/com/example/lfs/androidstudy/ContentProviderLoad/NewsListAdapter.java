package com.example.lfs.androidstudy.ContentProviderLoad;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lfs.androidstudy.ImageLoadHelper;
import com.example.lfs.androidstudy.R;

import java.util.List;

/**
 * Created by lfs on 2018/8/6.
 */

public class NewsListAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private Context context;
    private List<String> listData;
    private ListView mListView;
    private LayoutInflater mInflater;

    private int mStart;
    private int mEnd;
    private boolean isFirstIn;

    public NewsListAdapter(Context context, List<String> data, ListView listView){
        if (null != listData) {
            listData.clear();
        }
        this.context = context;
        listData = data;
        mListView = listView;
        mListView.setOnScrollListener(this);
        isFirstIn = true;
        mInflater = LayoutInflater.from(context);

//        ImageLoadHelper.getInstance().init(listData, mListView);
        Log.i("Init", "[ImageGridAdapter] data size is: " + listData.size());
    }


    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        NewsListItem newsListItem = null;
        if (null == view) {
            view = mInflater.inflate(R.layout.news_item, null);

            newsListItem.mImageView = (ImageView) view.findViewById(R.id.img);
            newsListItem.mTitleTextView = (TextView) view.findViewById(R.id.txtTitle);
            newsListItem.mDateTextView = (TextView) view.findViewById(R.id.txtDate);

            view.setTag(newsListItem);
        } else {
            newsListItem = (NewsListItem) view.getTag();
        }

        String path = listData.get(i);
        if (null != path) {
            Log.i("Scroll", "item index: " + i);

//            imageView.setTag(path);
//            ImageLoadHelper.getInstance().showImage(imageView, path);
        }

        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == i) {
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
