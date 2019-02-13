package com.feisukj.base.retrofitnet;


import com.feisukj.base.util.LogUtils;

/**
 * Author : Gupingping
 * Date : 2018/9/28
 * QQ : 464955343
 */
public class Logger implements LoggingInterceptor.Logger {

    @Override
    public void log(String message) {
        LogUtils.INSTANCE.e("http----: " ,message);
    }
}
