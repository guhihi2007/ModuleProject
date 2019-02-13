package com.feisukj.ad

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.feisukj.ad.manager.AdController
import com.feisukj.base.ARouterConfig
import com.feisukj.base.BaseApplication
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Author : Gupingping
 * Date : 2018/10/25
 * QQ : 464955343
 */
@Route(path = ARouterConfig.SPLASH_ACTIVITY_AD)
class SplashActivityAD : AppCompatActivity() {

    lateinit var builder: AdController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_splash_ad)
        BaseApplication.isBackGround = false
        BaseApplication.isFromStart = true
        builder = AdController.Builder(this)
                .setPage(ADConstants.START_PAGE)
                .setContainer(splash_container)
                .setSkipView(skip_view)
                .setLogo(app_logo)
                .setLoading(true)
                .create()
        builder.show()

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        builder.destroy()
    }
}
