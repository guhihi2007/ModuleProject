package com.feisukj.base.baseclass;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.feisukj.base.BaseApplication;
import com.feisukj.base.R;
import com.feisukj.base.widget.ActionBar;
import com.feisukj.base.widget.loaddialog.LoadingDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 * 需要网络请求的activity
 * 需要actionBar时，返回true，默认false
 */
public abstract class BaseMvpActivity<V, P extends BasePresenter<V>> extends RxAppCompatActivity {
    public Activity mContext;
    public P mPresenter;

    protected ImmersionBar mImmersionBar;
//    protected DaoSession daoSession;
    protected LoadingDialog loadingDialog;//正在加载
    protected ActionBar actionBar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mContext = this;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setCancelable(cancelDialog());
//        daoSession = BaseApplication.getDaoSession();
        if (isActionBar()) {
            setContentView(R.layout.activity_base);
            ((ViewGroup) findViewById(R.id.fl_content)).addView(getLayoutInflater().inflate(getLayoutId(), null));
            actionBar = findViewById(R.id.actionbar);
        } else {
            setContentView(getLayoutId());
        }
        //沉浸式状态栏
        initImmersionBar(getStatusBarColor());
        //加入Activity管理器
        BaseApplication.getApplication().getActivityManage().addActivity(this);
        initView();
        initData();

    }

    protected int getStatusBarColor() {
        return R.color.theme;
    }

    /**
     * 沉浸栏颜色
     */
    protected void initImmersionBar(int color) {
        mImmersionBar = ImmersionBar.with(this);
        if (color != 0) {
            mImmersionBar.statusBarColor(color);
        }
        mImmersionBar.init();
    }

    protected abstract int getLayoutId();

    protected abstract P createPresenter();


    public void showLoading() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing())
                loadingDialog.dismiss();
        }
    }

    /**
     * 初始化View
     */
    protected void initView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 是否需要ActionBar
     */
    protected boolean isActionBar() {
        return false;
    }

    protected boolean cancelDialog() {
        return true;
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

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void receive(Event event) {
//
//    }

    protected void setTitleText(String title) {
        if (actionBar != null) {
            actionBar.setCenterText(title);
        }
    }

    protected void setTransparentActionBar() {
        if (actionBar != null) {
            actionBar.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }
}