package com.feisukj.base.baseclass;

import android.content.Context;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 */
public class BaseModule {
    protected BaseMvpActivity mActivity;
    protected BaseMvpFragment mFragment;
    protected Context context;

    public BaseModule(BaseMvpActivity act) {
        this.mActivity = act;
    }

    public BaseModule(BaseMvpFragment frag) {
        this.mFragment = frag;
    }

    //用于数据页面，如果首页请求token为空，这此次请求还会先请求token
    protected <T> void activityRequest(Observable<T> observable, DisposableObserver<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mActivity.<T>bindUntilEvent(ActivityEvent.DESTROY))//绑定生命周期，防止内存泄露
                .subscribe(subscriber);
    }


    //用于数据页面，如果首页请求token为空，这此次请求还会先请求token
    protected <T> void fragmentRequest(Observable<T> observable, DisposableObserver<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mFragment.<T>bindUntilEvent(FragmentEvent.DESTROY))//绑定生命周期，防止内存泄露
                .subscribe(subscriber);
    }


    //用于首页单独请求token
    protected <T> void requestToken(Observable<T> observable, DisposableObserver<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mFragment.<T>bindUntilEvent(FragmentEvent.DESTROY))//绑定生命周期，防止内存泄露
                .subscribe(subscriber);
    }
}
