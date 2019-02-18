package com.feisukj.ad.manager;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.baidu.mobads.InterstitialAd;
import com.baidu.mobads.InterstitialAdListener;
import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.feisukj.ad.BuildConfig;
import com.feisukj.ad.SplashActivity;
import com.feisukj.base.BaseApplication;
import com.feisukj.base.bean.ad.AD;
import com.feisukj.base.util.LogUtils;

import org.json.JSONObject;


/**
 * Created by Gpp on 2018/1/22.
 */

public class BD_AD extends AbsADParent {

    private AdView banner_bd;
    private SplashAd splash_bd;
    private InterstitialAd insert_bd;

    private Drawable night;


    @Override
    protected void showAdView(final AD.AdType type) {

        switch (type) {
            case BANNER:

                showBannerView();

                break;
            case INSET:

                showInsertView();

                break;
            case SPLASH:

                showSplashView();

                break;
        }


    }


    private void showSplashView() {
        try {
            splash_bd = new SplashAd(mActivity, mContainer, new SplashAdListener() {
                @Override
                public void onAdPresent() {
                    LogUtils.INSTANCE.i(TAG, "BAIdu-Splash-onAdPresent");
                    gone(splashHolder);
                    visible(mSkipVew, mLogo);
                    mSkipVew.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LogUtils.INSTANCE.e("mSkipVew setOnClickListener");
                            if (isLoading) {//不是启动页就finish
                                LogUtils.INSTANCE.i(TAG, "onADDismissed isLoading finish");
                                mActivity.finish();
                                return;
                            }
                            ((SplashActivity) mActivity).checkIn();
                        }
                    });
                }

                @Override
                public void onAdDismissed() {
                    LogUtils.INSTANCE.i(TAG, "BAIdu-Splash-onAdDismissed");
                    if (isLoading) {
                        mActivity.finish();
                        return;
                    }
                    ((SplashActivity) mActivity).checkIn();
                }

                @Override
                public void onAdFailed(String s) {
                    LogUtils.INSTANCE.i(TAG, "BAIdu-Splash-onAdFailed:" + s);

                    if (mLogo != null) {
                        mLogo.setVisibility(View.GONE);
                    }
                    if (mActivity instanceof SplashActivity) {
                        ((SplashActivity) mActivity).checkIn();
                        return;
                    }
//                    if (mActivity instanceof SplashActivityAD) {
//                        mActivity.finish();
//                    }

                }

                @Override
                public void onAdClick() {
                    LogUtils.INSTANCE.i(TAG, "BAIdu-onAdClick");
                }
            }, BuildConfig.BAIDU_SPLASH_ID, true);
        } catch (Throwable e) {
            e.printStackTrace();
            ((SplashActivity) mActivity).checkIn();
        }

    }

    private void showBannerView() {
        BaseApplication.handler.post(new Runnable() {
            @Override
            public void run() {
                if (banner_bd == null) {
                    banner_bd = new AdView(mActivity, BuildConfig.BAIDU_BANNER_ID);
                    LogUtils.INSTANCE.i(TAG, "初始化百度Banner广告");
                }
                banner_bd.setListener(
                        new AdViewListener() {
                            @Override
                            public void onAdReady(AdView adView) {
                                LogUtils.INSTANCE.i(TAG, "BAIdu-banner_bd:已缓存广告");
                            }

                            @Override
                            public void onAdShow(JSONObject jsonObject) {
                                mContainer.setVisibility(View.VISIBLE);
                                LogUtils.INSTANCE.i(TAG, "onAdShow:" + jsonObject.toString());
                            }

                            @Override
                            public void onAdClick(JSONObject jsonObject) {
                                LogUtils.INSTANCE.i(TAG, "onAdClick:" + jsonObject.toString());
                            }

                            @Override
                            public void onAdFailed(String s) {
                                LogUtils.INSTANCE.i(TAG, "onAdFailed:" + s);
                                //加载失败时，设置不可见，不会出现容器空白
                                mContainer.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdSwitch() {
                                LogUtils.INSTANCE.i(TAG, "onAdSwitch:切换");
                            }

                            @Override
                            public void onAdClose(JSONObject jsonObject) {
                                LogUtils.INSTANCE.i(TAG, "onAdClose:" + jsonObject.toString());
                                mContainer.setVisibility(View.GONE);
                            }
                        });


                if (mContainer != null) {
                    mContainer.removeAllViews();

                    //百度广告按20:3宽高比展示
                    DisplayMetrics dm = new DisplayMetrics();
                    ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
                    int winW = dm.widthPixels;
                    int winH = dm.heightPixels;
                    int width = Math.min(winW, winH);
                    int height = width * 3 / 20;

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


//                    if (isNight && ADConstants.READER_PAGE.equals(mPage)) {
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                            banner_bd.setForeground(getNightDrawable());
//                        }

//                        mContainer.setForeground(getNightDrawable());
//                    }

                    mContainer.addView(banner_bd, layoutParams);
                }

            }
        });
    }


    private void showInsertView() {

        if (insert_bd == null) {

            insert_bd = new InterstitialAd(mActivity, BuildConfig.BAIDU_INSERT_ID);

            LogUtils.INSTANCE.i(TAG, "初始化百度Insert广告");

        }

        insert_bd.setListener(new InterstitialAdListener() {

            @Override
            public void onAdClick(InterstitialAd arg0) {

                LogUtils.INSTANCE.i(TAG, "BAIDU_Insert_onAdClick:" + arg0);

                //记录此次显示时间
                saveInsertShowTime();


            }

            @Override
            public void onAdDismissed() {
                LogUtils.INSTANCE.i(TAG, "BAIDU_Insert_onAdDismissed");

                //记录此次显示时间
                saveInsertShowTime();
            }

            @Override
            public void onAdFailed(String arg0) {

                LogUtils.INSTANCE.i(TAG, "BAIDU_Insert_onAdFailed:" + arg0);

            }

            @Override
            public void onAdPresent() {

                LogUtils.INSTANCE.i(TAG, "BAIDU_Insert_onAdPresent");

                //记录此次显示时间
                saveInsertShowTime();

            }

            @Override
            public void onAdReady() {

                LogUtils.INSTANCE.i(TAG, "BAIDU_Insert_onAdReady");

                insert_bd.showAd(mActivity);
            }

        });

        insert_bd.loadAd();

        //记录此次显示时间
        saveInsertShowTime();

    }

    @Override
    public void destroy(AD.AdType type) {
        switch (type) {
            case SPLASH:
                break;
            case INSET:

                if (insert_bd != null) {

                    insert_bd.destroy();

                }

                break;
            case BANNER:

                if (banner_bd != null) {

                    banner_bd.destroy();

                }
                break;
        }
    }

}
