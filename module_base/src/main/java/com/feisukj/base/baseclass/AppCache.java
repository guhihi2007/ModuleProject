package com.feisukj.base.baseclass;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;


/**
 * Created on 2019/2/13.
 */
public class AppCache {
    private Context mContext;

    private AppCache() {
    }

    private static class SingletonHolder {
        private static AppCache instance = new AppCache();
    }

    public static AppCache get() {
        return SingletonHolder.instance;
    }

    public void init(Application application) {
        mContext = application.getApplicationContext();
        CrashHandler.getInstance().init(application.getApplicationContext());
        ForegroundObserver.init(application);
    }

    public Context getContext() {
        return mContext;
    }



}
