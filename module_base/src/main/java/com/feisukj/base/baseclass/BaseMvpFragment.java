package com.feisukj.base.baseclass;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feisukj.base.R;
import com.feisukj.base.widget.loaddialog.LoadingDialog;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 */
public abstract class BaseMvpFragment<V, P extends BasePresenter<V>> extends RxFragment {
    public Activity mActivity;
    protected View rootView;
    protected P mPresenter;

    protected abstract int getLayoutId();

    protected abstract P createPresenter();

    protected LoadingDialog loadingDialog;//正在加载

//    protected DaoSession daoSession;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_base, container, false);
        ((ViewGroup) rootView.findViewById(R.id.fl_content)).addView(getLayoutInflater().inflate(getLayoutId(), null));
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
        loadingDialog = new LoadingDialog(mActivity);
//        daoSession = BaseApplication.getDaoSession();

        initView();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent() {

    }

    /**
     * 初始化View
     */
    protected void initView() {
    }

    public void showLoading() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }


    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }


    public void visible(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void invisible(View... views) {
        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    protected void gone(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }
}
