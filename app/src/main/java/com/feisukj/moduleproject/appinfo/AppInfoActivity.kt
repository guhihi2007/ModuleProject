package com.feisukj.moduleproject.appinfo

import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.feisukj.base.ARouterConfig
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.moduleproject.BuildConfig
import com.feisukj.moduleproject.R
import kotlinx.android.synthetic.main.activity_app_info.*


/**
 * Author : Gupingping
 * Date : 2018/10/25
 * QQ : 464955343
 */
@Route(path = ARouterConfig.APPINFO_ACTIVITY)
class AppInfoActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_app_info

    private val list = mutableListOf<AppInfoBean>()

    private var adapter: InfoAdapter? = null

    override fun isActionBar(): Boolean = true

    override fun initView() {
        setTitleText("关于天气预报")
        list.add(AppInfoBean("版本号", BuildConfig.VERSION_NAME))
        list.add(AppInfoBean("版本代号", BuildConfig.VERSION_CODE.toString()))
        list.add(AppInfoBean("版本渠道", BuildConfig.FLAVOR))
        list.add(AppInfoBean("OpenLog", ""))
        list.add(AppInfoBean("CloseLog", ""))
        info_recycler.layoutManager = LinearLayoutManager(this) as LinearLayoutManager
        adapter = InfoAdapter(list)
        info_recycler.adapter = adapter

    }
}
