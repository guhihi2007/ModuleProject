package com.feisukj.ad.manager;


import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.feisukj.ad.BuildConfig;
import com.feisukj.ad.bean.LoadEvent;
import com.feisukj.base.ARouterConfig;
import com.feisukj.base.BaseApplication;
import com.feisukj.base.bean.ad.AD;
import com.feisukj.base.util.IntentUtils;
import com.feisukj.base.util.LogUtils;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.cfg.BrowserType;
import com.qq.e.ads.cfg.DownAPPConfirmPolicy;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * Created by Gpp on 2018/1/20.
 */

public class GDT_AD extends AbsADParent {
    private SplashAD splashAD;
    private BannerView banner_gdt;
    private InterstitialAD insert_gdt;
    private Drawable night;
    private boolean status;
    private NativeADDataRef adItem;
    private NativeAD nativeAD;
    private NativeExpressAD nativeExpressAD;
    private NativeExpressADView nativeExpressADView;

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
            case NATIVE:
//                loadNativeInit();
                refreshAd();
                break;
        }
    }


    @Override
    public void destroy(AD.AdType type) {
        switch (type) {
            case SPLASH:
//                if (splashAD!=null){
//                    splashAD = null;
//                }
                break;
            case INSET:
                if (insert_gdt != null) {
                    insert_gdt.destroy();
                }
                break;
            case BANNER:
                if (banner_gdt != null) {
                    banner_gdt.destroy();
                }
                break;
            case NATIVE:
//                if (nativeAD!=null){
//                    nativeAD = null;
//                }
                break;
        }
    }

    /**
     * 闪屏广告
     * */
    private void showSplashView() {

        splashAD = new SplashAD(mActivity, mContainer, mSkipVew, BuildConfig.GDT_APP_KEY, BuildConfig.GDT_SPLASH_ID, new SplashADListener() {
            @Override
            public void onADDismissed() {
                LogUtils.INSTANCE.i(TAG, "onADDismissed");
                if (null != mActivity) {
                    if (isLoading) {
                        mActivity.finish();
                        return;
                    }
                    IntentUtils.toActivity(ARouterConfig.HOME_ACTIVITY);
                    mActivity.finish();
                } else {
                    EventBus.getDefault().post(new LoadEvent(true));
                }
            }

            @Override
            public void onNoAD(AdError adError) {
                if (null != mActivity) {
                    if (mLogo != null) {
                        mLogo.setVisibility(View.GONE);
                    }
                    LogUtils.INSTANCE.i(TAG, "onNoAD");
                    if (isLoading) {
                        mActivity.finish();
                        return;
                    }
                    IntentUtils.toActivity(ARouterConfig.HOME_ACTIVITY);
                    mActivity.finish();

                } else {
                    EventBus.getDefault().post(new LoadEvent(true));
                }
            }

            @Override
            public void onADPresent() {
                if (null != mActivity) {
                    mSkipVew.setVisibility(View.VISIBLE);
                    if (mLogo != null) {
                        mLogo.setVisibility(View.VISIBLE);
                    }
                    LogUtils.INSTANCE.i(TAG, "onADPresent");
                } else {
                    EventBus.getDefault().post(new LoadEvent(true));
                }
            }

            @Override
            public void onADClicked() {
                if (null != mActivity) {
                    LogUtils.INSTANCE.i(TAG, "onADClicked");
                } else {
                    EventBus.getDefault().post(new LoadEvent(true));
                }
            }

            @Override
            public void onADTick(long l) {

                LogUtils.INSTANCE.i(TAG, "SplashADTick: " + l + "ms");
                mSkipVew.setText(String.format("点击跳过 %d", Math.round(l / 1000f)));
            }

            @Override
            public void onADExposure() {
                LogUtils.INSTANCE.i(TAG, "onADExposure:");
            }
        }, 0);

    }

    /**
     * Banner广告
     */
    private void showBannerView() {
        BaseApplication.handler.post(new Runnable() {
            @Override
            public void run() {
                if (banner_gdt == null) {
                    banner_gdt = new BannerView(mActivity, ADSize.BANNER, BuildConfig.GDT_APP_KEY, BuildConfig.GDT_BANNER_ID);
                    LogUtils.INSTANCE.i(TAG, "初始化广点通Banner广告");
                }
//                banner_gdt.setRefresh(30);//设置30秒自动刷新
                banner_gdt.setADListener(new AbstractBannerADListener() {

                    @Override
                    public void onNoAD(AdError adError) {
                        if (null != mActivity) {
                            LogUtils.INSTANCE.i(TAG, "banner_gdt_NoAD:" + adError.getErrorCode());
                            mContainer.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onADReceiv() {
                        mContainer.setVisibility(View.VISIBLE);
                        if (null != mActivity) {
                            LogUtils.INSTANCE.i(TAG, "banner_gdt_onADReceiv");
                        }
                    }

                    @Override
                    public void onADClicked() {
                        super.onADClicked();
                        if (null != mActivity) {
                            LogUtils.INSTANCE.i(TAG, "banner_gdt_onADClicked");
                        }
                    }

                    @Override
                    public void onADClosed() {
                        super.onADClosed();
                        mContainer.setVisibility(View.GONE);
                        LogUtils.INSTANCE.i(TAG, "banner_gdt_onADClosed");
                    }

                    @Override
                    public void onADLeftApplication() {
                        super.onADLeftApplication();
                        LogUtils.INSTANCE.i(TAG, "banner_gdt_onADLeftApplication");

                    }
                });

                if (mContainer != null) {

                    mContainer.removeAllViews();


                    mContainer.addView(banner_gdt);
                    banner_gdt.loadAD();
                }

            }
        });
    }

    /**
     * 插屏广告
     */
    private void showInsertView() {

        if (insert_gdt == null) {

            insert_gdt = new InterstitialAD(mActivity, BuildConfig.GDT_APP_KEY, BuildConfig.GDT_INSERT_ID);

            LogUtils.INSTANCE.i(TAG, "初始化广点通Insert广告");

        }
        insert_gdt.setADListener(new AbstractInterstitialADListener() {

            @Override
            public void onADOpened() {
                super.onADOpened();
                status = true;

                //记录此次显示时间
                saveInsertShowTime();

                LogUtils.INSTANCE.i(TAG, "initGDT_Insert_onADOpened");
            }

            @Override
            public void onADReceive() {
                LogUtils.INSTANCE.i(TAG, "initGDT_Insert_onADReceive");
                if (null != mActivity && !BaseApplication.isBackGround) {
                    status = true;
                    insert_gdt.show();
                    //记录此次显示时间
                    saveInsertShowTime();
                }
            }

            @Override
            public void onNoAD(AdError adError) {
                LogUtils.INSTANCE.i(TAG, "initGDT_Insert_onNoAD:" + adError.getErrorCode());
                if (null != mActivity) {
                    status = true;
                }
            }

            @Override
            public void onADClicked() {
                super.onADClicked();
                if (null != mActivity) {
                    status = true;
                    LogUtils.INSTANCE.i(TAG, "initGDT_Insert_onADClicked");
                    //记录此次显示时间
                    saveInsertShowTime();
                }
            }

            @Override
            public void onADClosed() {
                super.onADClosed();
                status = true;
                //记录此次显示时间
                saveInsertShowTime();

                LogUtils.INSTANCE.i(TAG, "initGDT_Insert_onADClosed");
            }
        });

        insert_gdt.loadAD();


        //如果load失败，再load一次
//        XApplication.getMainThreadHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!status) {
//
//                    LogUtils.INSTANCE.i(TAG, "initGDT_Insert_loadAD");
//                    insert_gdt.loadAD();
//                }
//            }
//        }, 1500);
    }

    /**
     * 原生广告(初始化)NativeExpressAD
     * */
    private void refreshAd() {

            /**
             *  如果选择支持视频的模版样式，请使用{@link Constants#NativeExpressSupportVideoPosID}
             */
            nativeExpressAD = new NativeExpressAD(mActivity,
                    new com.qq.e.ads.nativ.ADSize(com.qq.e.ads.nativ.ADSize.AUTO_HEIGHT, com.qq.e.ads.nativ.ADSize.FULL_WIDTH),
                    BuildConfig.GDT_APP_KEY,
                    BuildConfig.GDT_NATIVE_ID,
                    new NativeExpressAD.NativeExpressADListener() {
                @Override
                public void onNoAD(AdError adError) {
                    LogUtils.INSTANCE.i(TAG, "initGDT_NativeExpressAD_onNoAD"+adError.getErrorMsg());
                }

                @Override
                public void onADLoaded(List<NativeExpressADView> list) {
                    LogUtils.INSTANCE.i(TAG, "initGDT_NativeExpressAD_onADLoaded");
                    // 释放前一个展示的NativeExpressADView的资源
                    if (nativeExpressADView != null) {
                        nativeExpressADView.destroy();
                    }

                    if (mContainer.getVisibility() != View.VISIBLE) {
                        mContainer.setVisibility(View.VISIBLE);
                    }

                    if (mContainer.getChildCount() > 0) {
                        mContainer.removeAllViews();
                    }

                    nativeExpressADView = list.get(0);
                    if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                        nativeExpressADView.setMediaListener(mediaListener);
                    }
                    // 广告可见才会产生曝光，否则将无法产生收益。
                    mContainer.addView(nativeExpressADView);
                    nativeExpressADView.render();
                }

                @Override
                public void onRenderFail(NativeExpressADView nativeExpressADView) {
                    LogUtils.INSTANCE.i(TAG, "initGDT_NativeExpressAD_onRenderFail");
                }

                @Override
                public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                    LogUtils.INSTANCE.i(TAG, "initGDT_NativeExpressAD_onRenderSuccess");

                }

                @Override
                public void onADExposure(NativeExpressADView nativeExpressADView) {
                    LogUtils.INSTANCE.i(TAG, "initGDT_NativeExpressAD_onADExposure");

                }

                @Override
                public void onADClicked(NativeExpressADView nativeExpressADView) {
                    LogUtils.INSTANCE.i(TAG, "initGDT_NativeExpressAD_onADClicked");

                }

                @Override
                public void onADClosed(NativeExpressADView nativeExpressADView) {
                    LogUtils.INSTANCE.i(TAG, "initGDT_NativeExpressAD_onADClosed");

                }

                @Override
                public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
                    LogUtils.INSTANCE.i(TAG, "initGDT_NativeExpressAD_onADLeftApplication");

                }

                @Override
                public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
                    LogUtils.INSTANCE.i(TAG, "initGDT_NativeExpressAD_onADOpenOverlay");

                }

                @Override
                public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
                    LogUtils.INSTANCE.i(TAG, "initGDT_NativeExpressAD_onADCloseOverlay");

                }
            }); // 这里的Context必须为Activity
//            nativeExpressAD.setVideoOption(new VideoOption.Builder()
//                    .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // 设置什么网络环境下可以自动播放视频
//                    .setAutoPlayMuted(true) // 设置自动播放视频时，是否静音
//                    .build()); // setVideoOption是可选的，开发者可根据需要选择是否配置

            nativeExpressAD.loadAD(1);
            nativeExpressAD.setBrowserType(BrowserType.Default);
            nativeExpressAD.setDownAPPConfirmPolicy(DownAPPConfirmPolicy.Default);

    }

    /**
     * 获取广告数据
     * @param nativeExpressADView
     * @return
     */
    private String getAdInfo(NativeExpressADView nativeExpressADView) {
        AdData adData = nativeExpressADView.getBoundData();
        if (adData != null) {
            StringBuilder infoBuilder = new StringBuilder();
            infoBuilder.append("title:").append(adData.getTitle()).append(",")
                    .append("desc:").append(adData.getDesc()).append(",")
                    .append("patternType:").append(adData.getAdPatternType());
            if (adData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                infoBuilder.append(", video info: ").append(getVideoInfo(adData.getProperty(AdData.VideoPlayer.class)));
            }
            return infoBuilder.toString();
        }
        return null;
    }

    /**
     * 获取播放器实例
     *
     * 仅当视频回调{@link NativeExpressMediaListener#onVideoInit(NativeExpressADView)}调用后才会有返回值
     *
     * @param videoPlayer
     * @return
     */
    private String getVideoInfo(AdData.VideoPlayer videoPlayer) {
        if (videoPlayer != null) {
            StringBuilder videoBuilder = new StringBuilder();
            videoBuilder.append("{state:").append(videoPlayer.getVideoState()).append(",")
                    .append("duration:").append(videoPlayer.getDuration()).append(",")
                    .append("position:").append(videoPlayer.getCurrentPosition()).append("}");
            return videoBuilder.toString();
        }
        return null;
    }

    private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {
        @Override
        public void onVideoInit(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoInit: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoLoading(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoLoading");
        }

        @Override
        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
            Log.i(TAG, "onVideoReady");
        }

        @Override
        public void onVideoStart(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoStart: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoPause(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPause: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoComplete(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoComplete: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
            Log.i(TAG, "onVideoError");
        }

        @Override
        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPageOpen");
        }

        @Override
        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPageClose");
        }
    };

}