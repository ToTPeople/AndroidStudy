package com.example.lfs.androidstudy.ContentProviderLoad;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.lfs.androidstudy.Helper.FrescoUtils;
import com.example.lfs.androidstudy.Helper.ImageLoadHelper;
import com.example.lfs.androidstudy.Item.FrescoImageItem;
import com.example.lfs.androidstudy.R;
import com.example.lfs.androidstudy.ResourceLoad;
import com.example.lfs.androidstudy.data.NewsInfo;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

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

    private final static boolean use_fresco_item = true;

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

        if (use_fresco_item) {
            SimpleDraweeView simpleDraweeView;
            if (null == view) {
                FrescoImageItem frescoImageItem = new FrescoImageItem(context);
                simpleDraweeView = frescoImageItem.getSimpleDraweeView();
                simpleDraweeView.setId(i);

                GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
//                hierarchy.setPlaceholderImage(R.drawable.ic_launcher_foreground);
                hierarchy.setFailureImage(R.drawable.ic_launcher_background);

                view = frescoImageItem;
                view.setTag(simpleDraweeView);
            } else {
                simpleDraweeView = (SimpleDraweeView) view.getTag();
            }

            NewsInfo newsInfo = mListData.get(i);
            if (null != newsInfo) {
                String path = newsInfo.getmThumbnail_pic_s();

                if (ResourceLoad.LoadType.LOAD_TYPE_NEED_GET == ResourceLoad.getInstance().IMAGE_LOAD_TYPE) {
//                    FrescoLoader.getInstance().loadUrl(simpleDraweeView, path);
                    FrescoUtils.load(Uri.parse(path), simpleDraweeView, null, 100, 150, null);
                } else if (ResourceLoad.LoadType.LOAD_TYPE_DOWNLOAD == ResourceLoad.getInstance().IMAGE_LOAD_TYPE) {
//                    FrescoLoader.getInstance().loadFile(simpleDraweeView, path);
                    FrescoUtils.loadFile(path, simpleDraweeView, null, 100, 150, null);
                }
            }
        } else {
            ImageView imageView;
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

                // 自定义实现加载
//            imageView.setTag(path);
//            ImageLoadHelper.getInstance().showImage(imageView, path);

                // Glide库加载
                try {
//                URL url = new URL(path);
//                Glide.with(context)
//                        .load(url)
//                        .into(imageView);

                    Glide.with(context)
                            .load(path)
                            .into(imageView);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 加载Video封面
                // video add
//            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
//            bitmap = ThumbnailUtils.extractThumbnail(bitmap, 300, 250);
//            imageView.setImageBitmap(bitmap);
            }
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
