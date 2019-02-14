package com.feisukj.ad.manager;

import android.view.View;

import com.feisukj.ad.BuildConfig;
import com.feisukj.base.BaseApplication;
import com.feisukj.base.bean.ad.AD;
import com.feisukj.base.util.LogUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 */
public class Google_AD extends AbsADParent {

    private AdView banner;
    private InterstitialAd insert;

    public Google_AD() {}

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
                break;
            case NATIVE:
//                showNative();
                break;
        }
    }

    public void showInsertView() {
        if (insert == null) {
            insert = new InterstitialAd(mActivity);
        }
        insert.setAdUnitId(BuildConfig.GOOGLE_INSERT_ID);
        insert.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                LogUtils.INSTANCE.e("Google_AD", "google InterstitialAd onAdClosed ");
                //记录此次显示时间
                saveInsertShowTime();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                LogUtils.INSTANCE.e("Google_AD", "google InterstitialAd onAdFailedToLoad =" + i);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                LogUtils.INSTANCE.e("Google_AD", "google InterstitialAd onAdLoaded ");
                if (BaseApplication.isForeground){
                    //记录此次显示时间
                    saveInsertShowTime();
                    insert.show();
                }
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                LogUtils.INSTANCE.e("Google_AD", "google InterstitialAd onAdClicked ");
                //记录此次显示时间
                saveInsertShowTime();

            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                LogUtils.INSTANCE.e("Google_AD", "google InterstitialAd onAdImpression ");
            }
        });
        insert.loadAd(new AdRequest.Builder().build());
    }

    private void showBannerView() {
        if (mActivity != null) {
            if (banner == null) {
                banner = new AdView(mActivity);
            }
            banner.setAdSize(AdSize.BANNER);
            banner.setAdUnitId(BuildConfig.GOOGLE_BANNER_ID);
            banner.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    LogUtils.INSTANCE.e("Google_AD", "google Banner onAdClosed ");
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    LogUtils.INSTANCE.e("Google_AD", "google Banner onAdFailedToLoad =" + i);

                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    LogUtils.INSTANCE.e("Google_AD", "google Banner onAdClicked ");

                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    LogUtils.INSTANCE.e("Google_AD", "google Banner onAdImpression ");

                }
            });
            AdRequest request = new AdRequest.Builder().build();
            mContainer.addView(banner);
            mContainer.setVisibility(View.VISIBLE);
            banner.loadAd(request);
        }
    }


    @Override
    public void destroy(AD.AdType type) {
        switch (type) {
            case SPLASH:
                break;
            case INSET:
                break;
            case BANNER:
                if (banner != null) {
                    banner.destroy();
                    banner = null;
                }
            case NATIVE:
                break;
        }
    }

}
