package com.example.lfs.androidstudy.Helper;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

/**
 * Created by lfs on 2018/8/14.
 */

public class UIHelper {
    private UIHelper() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    public static void viewUrl(Context context, String url, String mimeType) {
        if (null == url || null == context) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (null == mimeType || mimeType.isEmpty()) {
            intent.setData(Uri.parse(url));
        } else {
            intent.setDataAndType(Uri.parse(url), mimeType);
        }

        if (null != context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)) {
            try {
                context.startActivity(Intent.createChooser(intent, "选择要使用的浏览器"));
            } catch (ActivityNotFoundException e) {
                Log.d("Log-Debug", "activity not found " + " over " + Uri.parse(url), e);
            }
        }
    }
}
