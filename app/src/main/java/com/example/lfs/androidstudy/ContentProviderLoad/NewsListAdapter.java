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
import com.example.lfs.androidstudy.data.NewsInfo;

import java.util.List;

/**
 * Created by lfs on 2018/8/6.
 */

public class NewsListAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private Context context;
    private List<NewsInfo> mListData;
    private ListView mListView;
    private LayoutInflater mInflater;

    private int mStart;
    private int mEnd;
    private boolean isFirstIn;

    public NewsListAdapter(Context context, List<NewsInfo> data, ListView listView){
        if (null != mListData) {
            mListData.clear();
        }
        this.context = context;
        mListData = data;
        mListView = listView;
        mListView.setOnScrollListener(this);
        isFirstIn = true;
        mInflater = LayoutInflater.from(context);

        ImageLoadHelper.getInstance().init(mListData, mListView);
    }

    public void updateData(List<NewsInfo> data) {
        if (null != mListData) {
//            mListData.clear();
        }

        mListData = data;
        Log.i("BoradCast", "mListData: " + mListData + ", data: " + data);
        ImageLoadHelper.getInstance().init(mListData, mListView);
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
    public int getCount() {
        return (null == mListData) ? 0 : mListData.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        NewsListItem newsListItem = null;
        if (null == view) {
            newsListItem = new NewsListItem();
            view = mInflater.inflate(R.layout.news_item, null);
            if (null == view) {
                return view;
            }

            newsListItem.mImageView = (ImageView) view.findViewById(R.id.img);
            newsListItem.mTitleTextView = (TextView) view.findViewById(R.id.txtTitle);
            newsListItem.mDateTextView = (TextView) view.findViewById(R.id.txtDate);

            view.setTag(newsListItem);
        } else {
            newsListItem = (NewsListItem) view.getTag();
        }

        NewsInfo newsInfo = mListData.get(i);
        if (null != newsInfo) {
            String path = newsInfo.getmThumbnail_pic_s();
            Log.i("Scroll", "item index: " + i);

            newsListItem.mImageView.setTag(path);
            ImageLoadHelper.getInstance().showImage(newsListItem.mImageView, path);

            newsListItem.mTitleTextView.setText(newsInfo.getTitle());
            newsListItem.mDateTextView.setText(newsInfo.getmDate());
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
    public void onScroll(AbsListView absListView, int nStartPos, int nShowCnt, int i2) {
        mStart = nStartPos;
        mEnd = nStartPos + nShowCnt;

        if (isFirstIn && nShowCnt > 0) {
            isFirstIn = false;
            ImageLoadHelper.getInstance().loadImages(mStart, mEnd);
        }
    }
}
