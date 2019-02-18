package com.feisukj.main

import com.alibaba.android.arouter.facade.annotation.Route
import com.feisukj.ad.manager.AdController
import com.feisukj.base.ARouterConfig
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.bean.ad.ADConstants
import kotlinx.android.synthetic.main.activity_home.*

/**
 * Author : Gupingping
 * Date : 2019/2/12
 * QQ : 464955343
 */
@Route(path = ARouterConfig.HOME_ACTIVITY)
class HomeActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_home

    override fun initView() {
        val notification = PlayNotification(this, HomeActivity::class.java)
        notification.sendNotification("HomeActivity", "PlayNotification")

        AdController.Builder(this)
                .setContainer(home_ll_ad)
                .setPage(ADConstants.HOME_PAGE)
                .create()
                .show()
    }
}