package com.example.lfs.androidstudy.Item;

import android.content.Context;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by lfs on 2018/8/9.
 */

public class FrescoImageItem extends RelativeLayout {
    private SimpleDraweeView simpleDraweeView;

    public FrescoImageItem(Context context) {
        super(context);
        init(context);
    }

    public SimpleDraweeView getSimpleDraweeView() {
        return simpleDraweeView;
    }

    private void init(Context context) {
        simpleDraweeView = new SimpleDraweeView(context);
        LayoutParams layoutParams = new LayoutParams(300, 250);
        simpleDraweeView.setLayoutParams(layoutParams);
        this.addView(simpleDraweeView);
    }
}
