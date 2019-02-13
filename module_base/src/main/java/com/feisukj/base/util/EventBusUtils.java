package com.feisukj.base.util;



import org.greenrobot.eventbus.EventBus;

/**
 * Describe：EventBusUtils
 */

public class EventBusUtils {

    /**
     * 注册事件
     */
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    /**
     * 解除事件
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }
}
