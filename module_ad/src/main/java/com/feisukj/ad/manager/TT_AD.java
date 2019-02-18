package com.feisukj.ad.manager;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AQuery2;
import com.androidquery.callback.ImageOptions;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTBannerAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTInteractionAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.feisukj.ad.BuildConfig;
import com.feisukj.ad.R;
import com.feisukj.ad.SplashActivity;
import com.feisukj.ad.bean.LoadEvent;
import com.feisukj.base.BaseApplication;
import com.feisukj.base.bean.ad.AD;
import com.feisukj.base.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Gupingping
 * Date : 2019/1/24
 * QQ : 464955343
 */
public class TT_AD extends AbsADParent implements WeakHandler.IHandler {

    private TTAdNative mTTAdNative;
    //开屏广告加载超时时间,建议大于1000,这里为了冷启动第一次加载到广告并且展示,示例设置了2000ms
    private static final int AD_TIME_OUT = 2000;
    private static final int MSG_GO_MAIN = 1;

    //开屏广告是否已经加载
    private boolean mHasLoaded;
    private AQuery2 mAQuery;
    //开屏广告加载发生超时但是SDK没有及时回调结果的时候，做的一层保护。
    private final WeakHandler mHandler = new WeakHandler(Looper.getMainLooper(),this);

    public TT_AD() {
        TTAdManagerHolder.init(BaseApplication.getApplication());
    }

    @Override
    protected void showAdView(AD.AdType type) {
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
                showNativeView();
                break;
        }
    }

    private void showInsertView() {
        //step2:创建TTAdNative对象,用于调用广告请求接口，createAdNative(Context context) 插屏广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mActivity);

        //step4:创建插屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(BuildConfig.TOUTIAO_INSERT_ID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(600, 600) //根据广告平台选择的尺寸，传入同比例尺寸
                .build();
        //step5:请求广告，调用插屏广告异步请求接口
        mTTAdNative.loadInteractionAd(adSlot, new TTAdNative.InteractionAdListener() {
            @Override
            public void onError(int code, String message) {
                LogUtils.INSTANCE.i(TAG, "onError code: " + code + "  message: " + message);
            }

            @Override
            public void onInteractionAdLoad(TTInteractionAd ttInteractionAd) {
                LogUtils.INSTANCE.i(TAG, "onInteractionAdLoad type:" + ttInteractionAd.getInteractionType());

                ttInteractionAd.setAdInteractionListener(new TTInteractionAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked() {
                        Log.d(TAG, "被点击");
                    }

                    @Override
                    public void onAdShow() {
                        Log.d(TAG, "被展示");
                    }

                    @Override
                    public void onAdDismiss() {
                        Log.d(TAG, "插屏广告消失");
                    }
                });
                //如果是下载类型的广告，可以注册下载状态回调监听
                if (ttInteractionAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ttInteractionAd.setDownloadListener(new TTAppDownloadListener() {
                        @Override
                        public void onIdle() {
                            Log.d(TAG, "点击开始下载");
                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            Log.d(TAG, "下载中");
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            Log.d(TAG, "下载暂停");
                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            Log.d(TAG, "下载失败");
                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                            Log.d(TAG, "下载完成");
                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {
                            Log.d(TAG, "安装完成");
                        }
                    });
                }
                //弹出插屏广告
                ttInteractionAd.showInteractionAd(mActivity);
            }
        });
    }

    private void showBannerView() {
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mActivity);
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(BuildConfig.TOUTIAO_BANNER_ID) //广告位id
                .setSupportDeepLink(true)
                .setImageAcceptedSize(600, 257)
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerAd(adSlot, new TTAdNative.BannerAdListener() {

            @Override
            public void onError(int code, String message) {
                mContainer.removeAllViews();
            }

            @Override
            public void onBannerAdLoad(final TTBannerAd ad) {
                if (ad == null) {
                    return;
                }
                View bannerView = ad.getBannerView();
                if (bannerView == null) {
                    return;
                }
                //设置轮播的时间间隔  间隔在30s到120秒之间的值，不设置默认不轮播
                ad.setSlideIntervalTime(30 * 1000);
                mContainer.removeAllViews();
                mContainer.setVisibility(View.VISIBLE);
                mContainer.addView(bannerView);
                //设置广告互动监听回调
                ad.setBannerInteractionListener(new TTBannerAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        LogUtils.INSTANCE.i(TAG, "头条banner 广告被点击");

                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        LogUtils.INSTANCE.i(TAG, "头条banner 广告展示");

                    }
                });
                //（可选）设置下载类广告的下载监听
                bindDownloadListener(ad);
                //在banner中显示网盟提供的dislike icon，有助于广告投放精准度提升
                ad.setShowDislikeIcon(new TTAdDislike.DislikeInteractionCallback() {
                    @Override
                    public void onSelected(int position, String value) {
                        LogUtils.INSTANCE.i(TAG, "点击 " + value);

                        //用户选择不喜欢原因后，移除广告展示
                        mContainer.removeAllViews();
                    }

                    @Override
                    public void onCancel() {
                        LogUtils.INSTANCE.i(TAG, "点击取消 ");
                    }
                });

                //获取网盟dislike dialog，您可以在您应用中本身自定义的dislike icon 按钮中设置 mTTAdDislike.showDislikeDialog();
                /*mTTAdDislike = ad.getDislikeDialog(new TTAdDislike.DislikeInteractionCallback() {
                        @Override
                        public void onSelected(int position, String value) {
                            TToast.show(mContext, "点击 " + value);
                        }

                        @Override
                        public void onCancel() {
                            TToast.show(mContext, "点击取消 ");
                        }
                    });
                if (mTTAdDislike != null) {
                    XXX.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTTAdDislike.showDislikeDialog();
                        }
                    });
                } */

            }
        });
    }

    private void showNativeView() {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mActivity);
        mAQuery = new AQuery2(mActivity);
        final AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(BuildConfig.TOUTIAO_NATIVE_ID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(600, 257)
                .setNativeAdType(AdSlot.TYPE_BANNER) //请求原生广告时候，请务必调用该方法，设置参数为TYPE_BANNER或TYPE_INTERACTION_AD
                .setAdCount(1)
                .build();

        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadNativeAd(adSlot, new TTAdNative.NativeAdListener() {
            @Override
            public void onError(int code, String message) {
            }

            @Override
            public void onNativeAdLoad(List<TTNativeAd> ads) {
                if (ads.get(0) == null) {
                    return;
                }
                View bannerView = LayoutInflater.from(mActivity).inflate(R.layout.toutiao_native_ad, nativeLayout, false);
                if (bannerView == null) {
                    return;
                }
                nativeLayout.removeAllViews();
                nativeLayout.setVisibility(View.VISIBLE);
                nativeLayout.addView(bannerView);
                //绑定原生广告的数据
                setAdData(bannerView, ads.get(0));
            }
        });

    }

    private Button mCreativeButton;

    private void setAdData(View nativeView, TTNativeAd nativeAd) {
        ((TextView) nativeView.findViewById(R.id.tv_native_ad_title)).setText(nativeAd.getTitle());
        ((TextView) nativeView.findViewById(R.id.tv_native_ad_desc)).setText(nativeAd.getDescription());
        ImageView imgDislike = nativeView.findViewById(R.id.img_native_dislike);
        bindDislikeAction(nativeAd, imgDislike);
        if (nativeAd.getImageList() != null && !nativeAd.getImageList().isEmpty()) {
            TTImage image = nativeAd.getImageList().get(0);
            if (image != null && image.isValid()) {
                mAQuery.id(nativeView.findViewById(R.id.iv_native_image)).image(image.getImageUrl());
            }
        }
        TTImage icon = nativeAd.getIcon();
        if (icon != null && icon.isValid()) {
            ImageOptions options = new ImageOptions();
            mAQuery.id((nativeView.findViewById(R.id.iv_native_icon))).image(icon.getImageUrl(), options);
        }
        mCreativeButton = (Button) nativeView.findViewById(R.id.btn_native_creative);
        //可根据广告类型，为交互区域设置不同提示信息
        switch (nativeAd.getInteractionType()) {
            case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
                //如果初始化ttAdManager.createAdNative(getApplicationContext())没有传入activity 则需要在此传activity，否则影响使用Dislike逻辑
                nativeAd.setActivityForDownloadApp(mActivity);
                mCreativeButton.setVisibility(View.VISIBLE);
                nativeAd.setDownloadListener(mDownloadListener); // 注册下载监听器
                break;
            case TTAdConstant.INTERACTION_TYPE_DIAL:
                mCreativeButton.setVisibility(View.VISIBLE);
                mCreativeButton.setText("立即拨打");
                break;
            case TTAdConstant.INTERACTION_TYPE_LANDING_PAGE:
            case TTAdConstant.INTERACTION_TYPE_BROWSER:
                mCreativeButton.setVisibility(View.VISIBLE);
                mCreativeButton.setText("查看详情");
                break;
            default:
                mCreativeButton.setVisibility(View.GONE);
                Log.e(TAG, "交互类型异常");
        }

        //可以被点击的view, 也可以把nativeView放进来意味整个广告区域可被点击
        List<View> clickViewList = new ArrayList<>();
        clickViewList.add(nativeView);

        //触发创意广告的view（点击下载或拨打电话）
        List<View> creativeViewList = new ArrayList<>();
        //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入
        //creativeViewList.add(nativeView);
        creativeViewList.add(mCreativeButton);

        //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
        nativeAd.registerViewForInteraction((ViewGroup) nativeView, clickViewList, creativeViewList, imgDislike, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ad) {
                if (ad != null) {
                    Log.e(TAG, "广告" + ad.getTitle() + "被点击");
                }
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {
                if (ad != null) {
                    Log.e(TAG, "广告" + ad.getTitle() + "被创意按钮被点击");
                }
            }

            @Override
            public void onAdShow(TTNativeAd ad) {
                if (ad != null) {
                    Log.e(TAG, "广告" + ad.getTitle() + "展示");
                }
            }
        });
    }

    //接入网盟的dislike 逻辑，有助于提示广告精准投放度
    private void bindDislikeAction(TTNativeAd ad, View dislikeView) {
        final TTAdDislike ttAdDislike = ad.getDislikeDialog(mActivity);
        if (ttAdDislike != null) {
            ttAdDislike.setDislikeInteractionCallback(new TTAdDislike.DislikeInteractionCallback() {
                @Override
                public void onSelected(int position, String value) {
                    nativeLayout.removeAllViews();
                }

                @Override
                public void onCancel() {

                }
            });
        }
        dislikeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ttAdDislike != null)
                    ttAdDislike.showDislikeDialog();
            }
        });
    }

    private void showSplashView() {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mActivity);
        mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT);

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(BuildConfig.TOUTIAO_SPLASH_ID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                Log.d(TAG, message);
                mHasLoaded = true;
                if (null != mActivity) {
                    if (isLoading) {//不是启动页就finish
                        LogUtils.INSTANCE.i(TAG, "onADDismissed isLoading finish");
                        mActivity.finish();
                        return;
                    }
                    ((SplashActivity) mActivity).checkIn();
                } else {
                    EventBus.getDefault().post(new LoadEvent(true));
                }
            }

            @Override
            @MainThread
            public void onTimeout() {
                mHasLoaded = true;
                if (null != mActivity) {
                    if (isLoading) {//不是启动页就finish
                        LogUtils.INSTANCE.i(TAG, "onADDismissed isLoading finish");
                        mActivity.finish();
                        return;
                    }
                    ((SplashActivity) mActivity).checkIn();
                } else {
                    EventBus.getDefault().post(new LoadEvent(true));
                }
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                Log.d(TAG, "开屏广告请求成功");
                mHasLoaded = true;
                if (ad == null) {
                    if (null != mActivity) {
                        if (isLoading) {//不是启动页就finish
                            LogUtils.INSTANCE.i(TAG, "onADDismissed isLoading finish");
                            mActivity.finish();
                            return;
                        }
                        ((SplashActivity) mActivity).checkIn();
                    } else {
                        EventBus.getDefault().post(new LoadEvent(true));
                    }
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                mContainer.removeAllViews();
                //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                mContainer.addView(view);
                //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                //ad.setNotAllowSdkCountdown();

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d(TAG, "onAdClicked");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d(TAG, "onAdShow");
                    }

                    @Override
                    public void onAdSkip() {
                        Log.d(TAG, "onAdSkip");
                        if (null != mActivity) {
                            if (isLoading) {//不是启动页就finish
                                LogUtils.INSTANCE.i(TAG, "onADDismissed isLoading finish");
                                mActivity.finish();
                                return;
                            }
                            ((SplashActivity) mActivity).checkIn();
                        }

                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.d(TAG, "onAdTimeOver");
                        if (null != mActivity) {
                            if (isLoading) {//不是启动页就finish
                                LogUtils.INSTANCE.i(TAG, "onADDismissed isLoading finish");
                                mActivity.finish();
                                return;
                            }
                            ((SplashActivity) mActivity).checkIn();
                        }
                    }
                });
            }
        }, AD_TIME_OUT);
    }

    @Override
    public void handleMsg(Message msg) {
        if (msg.what == MSG_GO_MAIN) {
            if (!mHasLoaded) {
                if (null != mActivity) {
                    if (isLoading) {//不是启动页就finish
                        LogUtils.INSTANCE.i(TAG, "onADDismissed isLoading finish");
                        mActivity.finish();
                        return;
                    }
                    ((SplashActivity) mActivity).checkIn();
                } else {
                    EventBus.getDefault().post(new LoadEvent(true));
                }
            }
        }
    }

    private final TTAppDownloadListener mDownloadListener = new TTAppDownloadListener() {
        @Override
        public void onIdle() {
            if (mCreativeButton != null) {
                mCreativeButton.setText("开始下载");
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
            if (mCreativeButton != null) {
                if (totalBytes <= 0L) {
                    mCreativeButton.setText("下载中 percent: 0");
                } else {
                    mCreativeButton.setText("下载中 percent: " + (currBytes * 100 / totalBytes));
                }
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
            if (mCreativeButton != null) {
                mCreativeButton.setText("下载暂停 percent: " + (currBytes * 100 / totalBytes));
            }
        }

        @Override
        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
            if (mCreativeButton != null) {
                mCreativeButton.setText("重新下载");
            }
        }

        @Override
        public void onInstalled(String fileName, String appName) {
            if (mCreativeButton != null) {
                mCreativeButton.setText("点击打开");
            }
        }

        @Override
        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
            if (mCreativeButton != null) {
                mCreativeButton.setText("点击安装");
            }
        }
    };

    @Override
    public void destroy(AD.AdType type) {
        switch (type) {

            case SPLASH:

                break;
            case INSET:
//                if (insert_gdt != null) {
//                    insert_gdt.destroy();
//                }

                break;
            case BANNER:

//                if (banner_gdt != null) {
//                    banner_gdt.destroy();
//                }

                break;
            case NATIVE:
//                if (nativeLayout != null && (nativeLayout.isShown() || nativeLayout.getChildCount() > 0)) {
//                    nativeLayout.removeAllViews();
//                    nativeLayout.setVisibility(View.GONE);
//                }
//                if (nativeExpressADView != null) {
//                    nativeExpressADView.destroy();
//                }
                break;
        }
    }

    private boolean mHasShowDownloadActive = false;

    private void bindDownloadListener(TTBannerAd ad) {
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                LogUtils.INSTANCE.i(TAG, "点击图片开始下载");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    LogUtils.INSTANCE.i(TAG, "下载中，点击图片暂停");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                LogUtils.INSTANCE.i(TAG, "下载中，点击图片继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                LogUtils.INSTANCE.i(TAG, "下载失败，点击图片重新下载");

            }

            @Override
            public void onInstalled(String fileName, String appName) {
                LogUtils.INSTANCE.i(TAG, "安装完成，点击图片打开");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                LogUtils.INSTANCE.i(TAG, "点击图片安装");
            }
        });
    }
}
