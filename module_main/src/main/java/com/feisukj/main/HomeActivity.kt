package com.feisukj.main

import com.alibaba.android.arouter.facade.annotation.Route
import com.feisukj.base.ARouterConfig
import com.feisukj.base.baseclass.BaseActivity

/**
 * Author : Gupingping
 * Date : 2019/2/12
 * QQ : 464955343
 */
@Route(path = ARouterConfig.HOME_ACTIVITY)
class HomeActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_home
}