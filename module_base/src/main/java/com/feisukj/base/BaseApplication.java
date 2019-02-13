package com.feisukj.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.feisukj.base.manager.ActivityManage;
import com.feisukj.base.util.SPUtil;
import com.feisukj.base.util.Utils;

/**
 * Author : Gupingping
 * Date : 2019/1/9
 * QQ : 464955343
 */
public class BaseApplication extends Application {
    public static BaseApplication application;
    private ActivityManage activityManage;
    public static Handler handler;
    public static boolean isBackGround;
    public static boolean isForeground;
    public static boolean isFromStart;
    public static String channel;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        application = this;
        //MultiDex分包方法 必须最先初始化
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        activityManage = new ActivityManage();
        handler = new Handler(Looper.getMainLooper());
        activityManage = new ActivityManage();
        initARouter();
        SPUtil.init(this);
        Utils.init(this);
        initDao();
    }

    private void initDao() {
//        MySQLiteOpenHelper migrationHelper = new MySQLiteOpenHelper(this, "ytkWeather", null);
//        SQLiteDatabase db = migrationHelper.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(db);
//        daoSession = daoMaster.newSession();
    }

    /**
     * 初始化路由
     */
    private void initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();  // 打印日志
            ARouter.openDebug(); // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application);// 尽可能早，推荐在Application中初始化
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        exitApp();
    }

    /**
     * 退出应用
     */
    private void exitApp() {
        activityManage.finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public ActivityManage getActivityManage() {
        if (activityManage == null) {
            activityManage = new ActivityManage();
        }
        return activityManage;
    }

    public static BaseApplication getApplication() {
        return application;
    }

    public static String getChannel() {
        return channel;
    }

    public static void setChannel(String channel) {
        BaseApplication.channel = channel;
    }
}

