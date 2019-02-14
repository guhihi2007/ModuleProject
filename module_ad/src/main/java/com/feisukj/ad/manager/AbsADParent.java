package com.feisukj.ad.manager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.feisukj.ad.ADConstants;
import com.feisukj.base.bean.ad.AD;
import com.feisukj.base.util.LogUtils;
import com.feisukj.base.util.SPUtil;


/**
 * Created by Gpp on 2018/1/22.
 */
abstract class AbsADParent {
    public static final String TAG = "controller";

    protected TextView mSkipVew;
    protected ImageView mLogo;
    protected Activity mActivity;
    protected FrameLayout mContainer;
    protected boolean isNight;
    protected String mPage;
    protected boolean isLoading;
    protected ImageView splashHolder;
    protected FrameLayout nativeLayout;//阅读页原生广告容器

    protected abstract void showAdView(AD.AdType type);

    protected View view;

    public void setSkipVew(TextView mSkipVew) {
        this.mSkipVew = mSkipVew;
    }


    public void setLogo(ImageView mLogo) {
        this.mLogo = mLogo;
    }


    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setContainer(FrameLayout mContainer) {
        this.mContainer = mContainer;
    }

    public void setSplashHolder(ImageView splashHolder) {
        this.splashHolder=splashHolder;
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public abstract void destroy(AD.AdType type);

    public void setPage(String page) {
        mPage = page;
    }

    public void setNativeLayout(FrameLayout nativeLayout) {
        this.nativeLayout = nativeLayout;
    }

    protected void saveInsertShowTime() {

        //记录此次显示时间
        SPUtil.getInstance().putLong(mPage + ADConstants.AD_INSERT_LAST_SHOW, System.currentTimeMillis());

        LogUtils.INSTANCE.i("页面:" + mPage + ",广告被点击,保存这次显示广告时间:" + System.currentTimeMillis());
    }

    //夜间模式给广告加蒙层变暗
//    protected Drawable getNightDrawable() {
//        Drawable night = ContextCompat.getDrawable(mActivity, R.drawable.background_rectangle);
//        night.setBounds(0, 0, mContainer.getWidth(), mContainer.getHeight());
//        return night;
//    }
//
//    protected Drawable getDayDrawable() {
//        Drawable night = ContextCompat.getDrawable(mActivity, R.drawable.background_transparent);
//        night.setBounds(0, 0, mContainer.getWidth(), mContainer.getHeight());
//        return night;
//    }
    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }

    }


}
