package io.egg.jiantu;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by apolloyujl on 7/18/14.
 */
public class JiantuApplication extends Application {

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static JiantuApplication mInstance;


    public static synchronized JiantuApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        JiantuApplication application = (JiantuApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
//
//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

//        refWatcher = LeakCanary.install(this);

        //初始化push推送服务
        if (shouldInit()) {
            MiPushClient.registerPush(this, Constants.MI_PUSH_APP_ID_ALPHA, Constants.MI_PUSH_APP_KEY_ALPHA);
        }
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();

        String mainProcessName = getPackageName();

        int myPid = android.os.Process.myPid();

        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }

        return false;
    }


}
