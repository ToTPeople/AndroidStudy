package com.example.lfs.androidstudy.Helper;

import android.content.Context;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

/**
 * Created by lfs on 2018/8/9.
 */

public class FrescoLoader {
    private static FrescoLoader mInstance;

    private ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
//            super.onFinalImageSet(id, imageInfo, animatable);
            if (null == imageInfo) {
                return;
            }

            QualityInfo qualityInfo = imageInfo.getQualityInfo();
            FLog.d("Final image received! " +
                            "Size %d x %d",
                    "-=-=-=-=-=-=-=-=-=- Quality level %d, good enough: %s, full quality: %s",
                    imageInfo.getWidth(),
                    imageInfo.getHeight(),
                    qualityInfo.getQuality(),
                    qualityInfo.isOfGoodEnoughQuality(),
                    qualityInfo.isOfFullQuality());
        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
//            super.onIntermediateImageSet(id, imageInfo);
            FLog.d(getClass(), "-=-=-=-=-=-=-=-=-=- Intermediate image received");
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
//            super.onFailure(id, throwable);
            FLog.e(getClass(), throwable, "-=-=-=-=-=-=-=-=-=- Error loading %s", id);
        }
    };

//    private BaseImageLoaderStrategy mStrategy;

    public FrescoLoader() {
//        mStrategy = new GlideImageLoaderStrategy();
    }

    //单例模式，节省资源
    public static FrescoLoader getInstance() {
        if (mInstance == null) {
            synchronized (FrescoLoader.class) {
                if (mInstance == null) {
                    mInstance = new FrescoLoader();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        Fresco.initialize(context);
    }

    public void loadFile(SimpleDraweeView simpleDraweeView, String file) {
        showProgressiveJPEGs(simpleDraweeView, Uri.fromFile(new File(file)));
    }

    public void loadUrl(SimpleDraweeView simpleDraweeView, String url) {
        showProgressiveJPEGs(simpleDraweeView, Uri.parse(url));
    }

    /**
     * 演示：逐渐加载的图片，即，从模糊逐渐清晰。需要图片本身也支持这种方式
     */
    public void showProgressiveJPEGs(SimpleDraweeView simpleDraweeView, Uri uri) {
        if (null == uri || null == simpleDraweeView) {
            return;
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)
                .setAutoPlayAnimations(true)
                .setImageRequest(request)
                .setOldController(simpleDraweeView.getController())
                .setControllerListener(controllerListener)
                .build();
        simpleDraweeView.setController(controller);
    }
}
