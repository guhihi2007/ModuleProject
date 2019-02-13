package com.feisukj.base.baseclass;

import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.observers.DisposableObserver;

/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 */
public abstract class RxRequestCallBack<T> extends DisposableObserver<HttpResult<T>> {

    private static final String TAG = "RxRequestCallBack";

    public abstract void onSuccess(HttpResult<T> httpResult);

    public abstract void onFailed(HttpResult<T> tHttpResult);

    public abstract void onTokenInvalid();

    @Override
    public void onNext(HttpResult<T> httpResult) {
        Log.e(TAG, "onNext:   code--" + httpResult.getCode() + "--msg--" + httpResult.getMsg());
        if (httpResult.getCode() == 0) {
            onSuccess(httpResult);
            Log.e(TAG, "onNext:  onSuccess");
        } else if (httpResult.getCode() == 10024) {//refresh_token 无效或过期
            onTokenInvalid();
            Log.e(TAG, "onNext:  onTokenInvalid");
        } else if (httpResult.getCode() == 10025) {//access_token 无效或过期
            onTokenInvalid();
            Log.e(TAG, "onNext:  onTokenInvalid");
        } else {
            onFailed(httpResult);
        }

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Log.e(TAG, "SocketTimeoutException: 网络中断，请检查您的网络状态");
        } else if (e instanceof ConnectException) {
            Log.e(TAG, "ConnectException: 网络中断，请检查您的网络状态");
        } else if (e instanceof UnknownHostException) {
            Log.e(TAG, "UnknownHostException: 网络中断，请检查您的网络状态");
        } else {
            Log.e(TAG, "onError:其他错误：" + e.getMessage() + "  cause: " + e.getCause());
        }
    }

    @Override
    public void onComplete() {

    }
}
