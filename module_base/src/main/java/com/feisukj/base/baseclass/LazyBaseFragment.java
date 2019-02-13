package com.feisukj.base.baseclass;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * 创建时间:2018/4/3
 * 创建人: yb
 * 描述:懒加载
 */

public abstract class LazyBaseFragment<V, P extends BasePresenter<V>>  extends RxFragment{

    protected P mPresenter;

    protected View mContentView;
    protected Context mContext;
    protected boolean isVisible;
    private boolean isPrepared;
    private boolean isFirst = true;


    //--------------------system method callback------------------------//
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
    }

    @Override

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
        }
    }

    @Override

    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {

            setUserVisibleHint(true);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView==null){
            mContentView = inflater.inflate(getLayoutId(),container,false);
        }
        return mContentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }

    /**
     * 这里获取数据，刷新界面
     */
    private void init() {
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
        initViews();
        bindEvent();
        loadData();
    }

    //--------------------------------method---------------------------//

    /**
     * 懒加载
     */
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
        init();
        isFirst = false;
    }

    //--------------------------abstract method------------------------//

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void bindEvent();

    protected abstract void loadData();

    protected abstract P getPresenter();


}
