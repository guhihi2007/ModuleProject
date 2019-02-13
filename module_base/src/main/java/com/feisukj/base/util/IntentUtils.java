package com.feisukj.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.feisukj.base.bean.BaseBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author : Gupingping
 * Date : 2019/1/3
 * QQ : 464955343
 */
public class IntentUtils {

    /**
     * 普通Activity之间跳转
     *
     * @param activity activity
     * @param clazz    目标activity
     */
    public static void toActivity(Context activity, Class<? extends Activity> clazz) {
        toActivity(activity, clazz, null);
    }

    /**
     * 普通Activity之间跳转
     *
     * @param activity activity
     * @param clazz    目标activity
     * @param params   参数
     */
    public static void toActivity(Context activity, Class<? extends Activity> clazz,
                                  Map<String, ?> params) {
        Intent intent = new Intent(activity, clazz);
        if (params != null) {
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = params.get(key);
                if (value instanceof String) {
                    intent.putExtra(key, (String) value);
                } else if (value instanceof Boolean) {
                    intent.putExtra(key, (boolean) value);
                } else if (value instanceof Integer) {
                    intent.putExtra(key, (int) value);
                } else if (value instanceof Float) {
                    intent.putExtra(key, (float) value);
                } else if (value instanceof Double) {
                    intent.putExtra(key, (double) value);
                } else if (value instanceof Long) {
                    intent.putExtra(key, (long) value);
                } else if (value instanceof Short) {
                    intent.putExtra(key, (short) value);
                } else if (value instanceof BaseBean) {
                    intent.putExtra(key, (BaseBean) value);
                } else if (value instanceof ArrayList) {
                    intent.putExtra(key, (ArrayList) value);
                } else if (value instanceof HashMap) {
                    intent.putExtra(key, (HashMap) value);
                }
            }
        }
        activity.startActivity(intent);
    }

    /**
     * ARouter跳转Activity
     *
     * @param url 目标Activity Url
     */
    public static void toActivity(String url) {
        toActivity(url, null);
    }

    /**
     * ARouter跳转Activity
     *
     * @param url    目标Activity Url
     * @param params 参数
     */
    public static void toActivity(String url, Map<String, ?> params) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Postcard postcard = ARouter.getInstance()
                .build(url);
        if (params != null) {
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = params.get(key);
                if (value instanceof String) {
                    postcard.withString(key, (String) value);
                } else if (value instanceof Boolean) {
                    postcard.withBoolean(key, (boolean) value);
                } else if (value instanceof Integer) {
                    postcard.withInt(key, (int) value);
                } else if (value instanceof Float) {
                    postcard.withFloat(key, (float) value);
                } else if (value instanceof Double) {
                    postcard.withDouble(key, (double) value);
                } else if (value instanceof Long) {
                    postcard.withLong(key, (long) value);
                } else if (value instanceof Short) {
                    postcard.withShort(key, (short) value);
                } else if (value instanceof BaseBean) {
                    postcard.withSerializable(key, (BaseBean) value);
                } else if (value instanceof ArrayList) {
                    postcard.withSerializable(key, (ArrayList) value);
                } else if (value instanceof HashMap) {
                    postcard.withSerializable(key, (HashMap) value);
                }
            }
        }
        postcard.navigation();
    }
}
