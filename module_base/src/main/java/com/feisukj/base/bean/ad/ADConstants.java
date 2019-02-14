package com.feisukj.base.bean.ad;


/**
 * Created by Gpp on 2018/1/17.
 */

public class ADConstants {

    /************************页面命名 存储广告显示配置*************************/
    public static final String START_PAGE = "start_page";
    public static final String CATEGORY_PAGE = "category_page";//分类页面
    public static final String DETAIL_PAGE = "music_detail";//详情页面

    public static final String HOME_PAGE_LIST1 = "home_page_list1";//主页列表1
    public static final String HOME_PAGE_LIST2 = "home_page_list2";//主页列表2
    public static final String HOME_PAGE_LIST3 = "home_page_list3";//主页列表3
    public static final String HOME_PAGE_LIST4 = "home_page_list4";//主页列表4

    public static final String LISTENING_PAGE = "listening_page";//播放页面
    /************************页面命名*************************/

    /************************开屏广告*************************/
    public static final String AD_APP_LOAD_TIME = "ad_app_load_time";//App启动时间
    public static final String AD_APP_BACKGROUND_TIME = "ad_app_background_time";//App退到后台时间
    public static final String AD_SPREAD_PERIOD = "ad_spread_period";//开屏后台设置的时间间隔
    public static final String AD_SPLASH_STATUS = "ad_splash_status";//开屏开关
    /************************开屏广告*************************/


    /************************插屏广告*************************/
    public static final String AD_INSERT_SHOW_PERIOD = "ad_insert_change_period";//插屏广告显示间隔
    public static final String AD_INSERT_LAST_SHOW = "ad_insert_last_origin";//插屏广告上展示时间
    /************************插屏广告*************************/
    /**
     * 是否开启了页面banner定时器
     * */
    public static final String AD_BANNER_IS_TIMER= "ad_banner_is_timer";

    public static final String AD_BANNER_LAST_CHANGE = "AD_BANNER_LAST_CHANGE";

    /************************原生广告*************************/
    public static final String AD_NATIVE_SHOW_PERIOD = "ad_native_change_period";//原生广告显示间隔
    public static final String AD_NATIVE_LAST_SHOW = "ad_native_last_origin";//原生广告上ci展示时间
    /************************原生广告*************************/

}
