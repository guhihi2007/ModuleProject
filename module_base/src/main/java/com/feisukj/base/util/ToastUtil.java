package com.feisukj.base.util;

import android.widget.Toast;

import com.feisukj.base.BaseApplication;

/**
 * 描述:Toast工具
 */
public class ToastUtil {
    public static void showShortToast(int rId) {
        showShortToast(BaseApplication.getApplication().getString(rId));
    }

    public static void showLongToast(int rId) {
        showLongToast(BaseApplication.getApplication().getString(rId));
    }

    public static void showShortToast(String content) {
        Toast.makeText(BaseApplication.getApplication(),content, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String content) {
        Toast.makeText(BaseApplication.getApplication(),content, Toast.LENGTH_LONG).show();
    }

    /*
     * 防止弹出多次吐司
     * */
    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getApplication(), s, Toast.LENGTH_SHORT);
            toast.show();
            oldMsg = s;
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

}
