package com.example.lfs.androidstudy.ContentProviderLoad;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.example.lfs.androidstudy.ResourceLoad;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NetDataLoadIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    public static final String ACTION_GET_NET_DATA = "com.example.lfs.androidstudy.ContentProviderLoad.action.GetNetData";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.lfs.androidstudy.ContentProviderLoad.extra.PARAM1";

    public NetDataLoadIntentService() {
        super("NetDataLoadIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
//            ResourceLoad.getInstance().getImages();
            if (ACTION_GET_NET_DATA.equals(action)) {
                ResourceLoad.getInstance().getUrlJsonData();
            }
        }
    }
}
