package com.feisukj.ad.manager;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.feisukj.base.bean.ad.ADConstants;
import com.feisukj.ad.SplashActivity;
import com.feisukj.base.BaseApplication;
import com.feisukj.base.bean.ad.AD;
import com.feisukj.base.bean.ad.OriginBean;
import com.feisukj.base.bean.ad.StatusBean;
import com.feisukj.base.bean.ad.TypeBean;
import com.feisukj.base.util.GsonUtils;
import com.feisukj.base.util.LogUtils;
import com.feisukj.base.util.NetworkUtils;
import com.feisukj.base.util.SPUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by Gpp on 2018/1/22.
 */

public class AdManager implements OnInsertADListener {
    public static final String TAG = "controller";
    private Activity activity;
    private String page;
    private ImageView logo;
    private FrameLayout container;
    private TextView skipView;
    //banner切换scheduledExecutorService timerTask
    ScheduledExecutorService scheduledExecutorService;
    private TimerTask timerTask;
    private ExecutorService pool;
    private TypeBean pageBean;
    private AbsADParent adView;
    private boolean isLoading;
    private long pages;
    private boolean isRunning = true;
    private HashMap<AD.AdType, List<OriginBean>> map;
    private String tag_ad;
    private FrameLayout nativeAdLayout;

    public AdManager(Activity activity, String page, FrameLayout container, ImageView logo, TextView skipView, boolean isLoading, String tag_ad, FrameLayout nativeAdLayout) {
        this.activity = activity;
        this.page = page;
        this.container = container;
        this.logo = logo;
        this.skipView = skipView;
        this.isLoading = isLoading;
        this.tag_ad = tag_ad;
        this.nativeAdLayout = nativeAdLayout;

        pool = Executors.newFixedThreadPool(1);
    }

    public void show() {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                String string = SPUtil.getInstance().getString(page);
                LogUtils.INSTANCE.e(TAG, "pool run page==" + page + ",string==" + string);

                if (page != null) {
                    if (!TextUtils.isEmpty(string))
                        pageBean = GsonUtils.parseObject(string, TypeBean.class);
                    LogUtils.INSTANCE.e(TAG, "pool run pageBean==" + SPUtil.getInstance().getString(page));
                    showByType();
                }
            }
        });
    }

    private void showByType() {
        //获取当前页面要展示的广告类型和源
        map = getShowTypeAndOrigin();
        if (map.size() == 0 && page.equals(ADConstants.START_PAGE)) {
            LogUtils.INSTANCE.e(TAG, "START_PAGE无配置数据");
            goHome();
            return;
        }

        for (AD.AdType type : map.keySet()) {
            List<OriginBean> originList = map.get(type);
            changeAdByTime(type, originList);
        }
    }

    private void changeAdByTime(final AD.AdType type, final List<OriginBean> originList) {
        switch (type) {
            case BANNER:
                executeBanner(type, originList);
                break;
            case INSET:
                executeInsert(type, originList);
                break;
            case SPLASH:
                if (pageBean.getSpread_screen() == null) return;
                if (!pageBean.getSpread_screen().getStatus() ||
                        !NetworkUtils.isConnected(activity.getApplicationContext())) {
                    LogUtils.INSTANCE.e(TAG, "SPLASH-----开关关闭,或者无网络不执行");
                    goHome();
                    return;
                }
                LogUtils.INSTANCE.e(TAG, "SPLASH--");
                showByOrigin(getAdOriginByPercent(originList), type);
                break;
            case NATIVE:
                executeNative(type, originList);
                break;
        }
    }

    private void executeInsert(final AD.AdType type, final List<OriginBean> originList) {
        if (pageBean.getInsert_screen() == null) return;

        if (!pageBean.getInsert_screen().getStatus() || !NetworkUtils.isConnected(activity.getApplicationContext())) {
            LogUtils.INSTANCE.e(TAG, "INSET--" + page + "---开关关闭，或者无网络不执行");
            return;
        }

        long period = SPUtil.getInstance().getLong(page + ADConstants.AD_INSERT_SHOW_PERIOD);
        long time = System.currentTimeMillis();
        long last = SPUtil.getInstance().getLong(page + ADConstants.AD_INSERT_LAST_SHOW);
        long offset = time - last;
        LogUtils.INSTANCE.i(TAG, "页面:" + page + ",当前时间(" + getDate(time) + ")-上次展示时间(" + getDate(last) + "):" + time + "-" + last + "=" + offset);
        LogUtils.INSTANCE.i(TAG, "页面:" + page + ",读取插屏广告周期:" + period + "-" + offset + "=" + (period - offset));
        LogUtils.INSTANCE.i(TAG, "页面:" + page + ",需要显示吗:" + (time - last >= period));
        if (last == 0) {
            //第一次进入Application 记录此次显示时间
            SPUtil.getInstance().putLong(page + ADConstants.AD_INSERT_LAST_SHOW, System.currentTimeMillis());
        }
        if (last > 0 && time - last >= period) {
            LogUtils.INSTANCE.e(TAG, "INSET---时间到显示");
            LogUtils.INSTANCE.e(TAG, "AdManager:tag_ad:" + tag_ad);
            BaseApplication.handler.post(new Runnable() {
                @Override
                public void run() {
                    showByOrigin(getAdOriginByPercent(originList), type);
                }
            });
        } else {
            LogUtils.INSTANCE.e(TAG, "INSET---时间未到不显示");
        }
    }

    private String getDate(long time) {
        return new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(time);
    }

    private void goHome() {
        if (isLoading) {
            LogUtils.INSTANCE.i(TAG, "From BackGround");
            activity.finish();
        } else {
            LogUtils.INSTANCE.i(TAG, "From Start");
            BaseApplication.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((SplashActivity) activity).checkIn();
//                    IntentUtils.toActivity(ARouterConfig.HOME_ACTIVITY);
                }
            }, 2000);
        }
    }

    private void executeBanner(final AD.AdType type, final List<OriginBean> originList) {
        //开关关闭不执行
        if (!pageBean.getBanner_screen().getStatus() ||
                !NetworkUtils.isConnected(activity.getApplicationContext())) {
            LogUtils.INSTANCE.e(TAG, "BANNER-----开关关闭，或者无网络不执行");
            return;
        }
//        boolean isTimer = SPUtil.getInstance().getBoolean(page + ADConstants.AD_BANNER_IS_TIMER);
//        if (isTimer) {
////            此页面已经开启了定时器
//            LogUtils.INSTANCE.e(TAG, "BANNER-----isTimer:" + page + isTimer);
//            return;
//        }
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
//        开启定时切换
        timerTask = new TimerTask() {
            @Override
            public void run() {
                BaseApplication.handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
//                            cancelTask();取消定时器方法
                                                            if (isRunning) {
                                                                LogUtils.INSTANCE.e(TAG, "BANNER-----定时切换广告源" + page + ADConstants.AD_BANNER_LAST_CHANGE);
                                                                LogUtils.INSTANCE.e(TAG, "AdManager:tag_ad:" + tag_ad);
                                                                SPUtil.getInstance().putLong(page + ADConstants.AD_BANNER_LAST_CHANGE, System.currentTimeMillis());
                                                                showByOrigin(getAdOriginByPercent(originList), type);
                                                            } else {
                                                                timerTask.cancel();
                                                            }
                                                        }
                                                    }
                        , 0);
            }
        };
        try {
            scheduledExecutorService.scheduleWithFixedDelay(timerTask, 0, pageBean.getBanner_screen().getChange_times() * 1000, TimeUnit.MILLISECONDS);
            SPUtil.getInstance().putBoolean(page + ADConstants.AD_BANNER_IS_TIMER, true);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            SPUtil.getInstance().putBoolean(page + ADConstants.AD_BANNER_IS_TIMER, false);
        }
        //如果后台配置了关闭广告时间，执行关闭
        closeBannerByTiming();
    }

    private void executeNative(AD.AdType type, List<OriginBean> originList) {
        if (!pageBean.getNative_screen().getStatus() ||
                !NetworkUtils.isConnected(activity.getApplicationContext())) {
            LogUtils.INSTANCE.e(TAG, "Native--" + page + "---开关关闭，或者无网络不执行");
            return;
        }
        long period = SPUtil.getInstance().getLong(page + ADConstants.AD_NATIVE_SHOW_PERIOD);
        long time = System.currentTimeMillis();
        long last = SPUtil.getInstance().getLong(page + ADConstants.AD_NATIVE_LAST_SHOW);
        long offset = time - last;
        LogUtils.INSTANCE.i(TAG, "页面:" + page + ",当前时间(" + getDate(time) + ")-上次展示时间(" + getDate(last) + "):" + time + "-" + last + "=" + offset);
        LogUtils.INSTANCE.i(TAG, "页面:" + page + ",读取广告周期:" + period + "-" + offset + "=" + (period - offset));
        LogUtils.INSTANCE.i(TAG, "页面:" + page + ",需要显示吗:" + (time - last >= period));

        if (time - last >= period) {
            LogUtils.INSTANCE.e(TAG, "INSET---时间到显示");
            showByOrigin(getAdOriginByPercent(originList), type);
        } else {
            LogUtils.INSTANCE.e(TAG, "INSET---时间未到不显示");
        }
    }

    /**
     * 关闭广告时间到了，执行关闭
     */
    private void closeBannerByTiming() {

        if (pageBean.getBanner_screen().getTimes() != 0) {

            long closeTime = pageBean.getBanner_screen().getTimes();

            LogUtils.INSTANCE.i(TAG, closeTime + " 秒后关闭Banner广告," + pageBean.getBanner_screen().getChange_times() + "秒切换广告源" + "---" + page + "");

            BaseApplication.handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    isRunning = false;

                    cancelTask();

                }
            }, closeTime * 1000);
        }
    }

    /**
     * Random 选取广告源
     * 目前只做了2种广告源随机
     */
    private AD.AdOrigin getAdOriginByPercent(List<OriginBean> originList) {

        if (originList.size() == 1) {

            return originList.get(0).getOrigin();

        }

        OriginBean first = originList.get(0);

        OriginBean sec = originList.get(1);

        Random random = new Random();

        int next = random.nextInt(101);


        if (first.getPrecent() < sec.getPrecent()) {

            return next < first.getPrecent() ? first.getOrigin() : sec.getOrigin();

        } else {

            return next <= sec.getPrecent() ? sec.getOrigin() : first.getOrigin();
        }
//        return sec.getOrigin();
    }

    //根据广告广告源显示相应的广告类型
    private void showByOrigin(AD.AdOrigin origin, AD.AdType type) {
        switch (origin) {
            case gdt:
                adView = new GDT_AD();
                adView.setSkipVew(skipView);
                LogUtils.INSTANCE.i(TAG, "AdManager showByOrigin   广点通- gdt - " + page + " ---" + type);
                break;
            case google:
                LogUtils.INSTANCE.i(TAG, "AdManager showByOrigin   Google-- " + page + " ---" + type);
                adView = new Google_AD();
                break;
            case bd:
                LogUtils.INSTANCE.i(TAG, "AdManager showByOrigin   百度-- " + page + " ---" + type);
                adView = new BD_AD();
            case toutiao:
        LogUtils.INSTANCE.i(TAG, "AdManager showByOrigin   头条-- " + page + " ---" + type);
                adView = new TT_AD();
                break;
        }

        adView.setActivity(activity);
        adView.setContainer(container);
        adView.setNativeAdLayout(nativeAdLayout);

        adView.setPage(page);
        adView.setLogo(logo);
        adView.setLoading(isLoading);
        //有网进入阅读页，后关闭网络，banner会显示无网络图片
        if (!NetworkUtils.isConnected(activity.getApplicationContext())) {
            if (null != container) {
                container.setVisibility(View.GONE);
                LogUtils.INSTANCE.i(TAG, "关闭网络，不显示banner容器");
            }
            return;
        }

        if (!isRunning && AD.AdType.BANNER.equals(type)) {

            LogUtils.INSTANCE.i(TAG, "已经到时间关闭了，不能展示广告~~");

            return;
        }

        adView.showAdView(type);
    }

    //获取当前页面需要显示的广告类型，和广告源，添加到map
    private HashMap<AD.AdType, List<OriginBean>> getShowTypeAndOrigin() {

        HashMap<AD.AdType, List<OriginBean>> map = new HashMap<>();
        //1.先获取页面的广告类型
        StatusBean spreadScreenBean = null;
        StatusBean bannerScreenBean = null;
        StatusBean insertScreenBean = null;
        StatusBean native_advertising = null;//原生

        if (pageBean != null) {
            spreadScreenBean = pageBean.getSpread_screen();
            bannerScreenBean = pageBean.getBanner_screen();
            insertScreenBean = pageBean.getInsert_screen();
            native_advertising = pageBean.getNative_screen();
        }

        if (spreadScreenBean != null) {
            map.put(AD.AdType.SPLASH, getOriginBean(spreadScreenBean));
            SPUtil.getInstance().putBoolean(ADConstants.AD_SPLASH_STATUS, spreadScreenBean.getStatus());
            SPUtil.getInstance().putLong(ADConstants.AD_SPREAD_PERIOD, spreadScreenBean.getTimes());
        }

        if (bannerScreenBean != null) {
            map.put(AD.AdType.BANNER, getOriginBean(bannerScreenBean));
        }

        if (insertScreenBean != null) {
            map.put(AD.AdType.INSET, getOriginBean(insertScreenBean));
            //播放页保存广告切换周期
            if (!page.equals(ADConstants.LISTENING_PAGE)) {
                LogUtils.INSTANCE.i(TAG, "页面:" + page + ",插屏广告周期:" + insertScreenBean.getTimes() * 1000);
                SPUtil.getInstance().putLong(page + ADConstants.AD_INSERT_SHOW_PERIOD, insertScreenBean.getTimes() * 1000);
            }
        }
        if (native_advertising != null) {
            map.put(AD.AdType.NATIVE, getOriginBean(native_advertising));
        }
        return map;
    }

    //获取广告源对应的percent
    private List<OriginBean> getOriginBean(StatusBean statusBean) {
        List<OriginBean> list = new ArrayList<>();

        String origin = statusBean.getAd_origin();

        String percent = statusBean.getAd_percent();

        String replace = origin.replace("_", "");

        int times = origin.length() - replace.length();

        for (int i = 0; i <= times; i++) {

            OriginBean originBean = new OriginBean();

            int lastIndex = origin.lastIndexOf("_");

            String sub = origin.substring(lastIndex + 1, origin.length());

            originBean.setOrigin(AD.AdOrigin.valueOf(sub));

            if (lastIndex > 0) {
                origin = origin.substring(0, lastIndex);
            }
            int lastIndexPercent = percent.lastIndexOf("_");

            String subPercent = percent.substring(lastIndexPercent + 1, percent.length());

            originBean.setPrecent(Integer.valueOf(subPercent));
            if (lastIndexPercent > 0) {
                percent = percent.substring(0, lastIndexPercent);
            }

            list.add(originBean);

        }
        return list;
    }

    public void destroy() {

        cancelTask();

        if (adView != null) {
            adView.destroy(AD.AdType.INSET);
        }

        if (pool != null) {
            pool.shutdown();
            pool = null;
        }

    }

    private void cancelTask() {

        isRunning = false;

        if (scheduledExecutorService != null) {
            if (!scheduledExecutorService.isShutdown() || !scheduledExecutorService.isTerminated()) {
                scheduledExecutorService.shutdownNow();
                scheduledExecutorService = null;
            }
        }
        SPUtil.getInstance().putBoolean(page + ADConstants.AD_BANNER_IS_TIMER, false);//记录定时器已消失
        LogUtils.INSTANCE.e(TAG, "BANNER-----记录定时器已消失:" + page + ADConstants.AD_BANNER_IS_TIMER);
        if (adView != null) {

            adView.destroy(AD.AdType.BANNER);
        }

        if (container != null && container.isShown()) {

            container.removeAllViews();

            container.setVisibility(View.GONE);

            container = null;

            LogUtils.INSTANCE.i(TAG, "" + page + " 关闭Banner广告");

        }
    }

    @Override
    public void clickNextPage(boolean showNative) {

        ++pages;

        LogUtils.INSTANCE.i(TAG, "click Next Page ==" + pages);

        if (null != pageBean && null != pageBean.getInsert_screen()) {

            if (pageBean.getInsert_screen().getTimes() == pages
                    && pageBean.getInsert_screen().getStatus()) {

                LogUtils.INSTANCE.i(TAG, "Pages ==" + pageBean.getInsert_screen().getTimes());

                showByOrigin(getAdOriginByPercent(map.get(AD.AdType.INSET)), AD.AdType.INSET);

            }
        }
    }

}