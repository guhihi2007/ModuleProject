package com.feisukj.base.baseclass;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ActivityManager {

    private static ActivityManager sInstance;
    private final List<Activity> mActivityStack = new ArrayList<>();
    private WeakReference<Activity> sCurrentActivityWeakRef;

    private ActivityManager() {

    }

    public static ActivityManager getInstance() {
        if (sInstance == null){
            sInstance = new ActivityManager();
        }
        return sInstance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }

}
