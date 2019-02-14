package com.feisukj.ad

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.feisukj.ad.manager.AdController
import com.feisukj.ad.permission.AuthorityDialog
import com.feisukj.ad.permission.SetPermissionDialog
import com.feisukj.base.ARouterConfig
import com.feisukj.base.BaseApplication
import com.feisukj.base.api.AdService
import com.feisukj.base.bean.ad.ADConstants
import com.feisukj.base.bean.ad.AdsConfig
import com.feisukj.base.retrofitnet.HttpUtils
import com.feisukj.base.util.IntentUtils
import com.feisukj.base.util.PackageUtils
import com.feisukj.base.util.SPUtil
import com.feisukj.base.util.lg
import com.google.gson.Gson
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Author : Gupingping
 * Date : 2018/10/25
 * QQ : 464955343
 */
@Route(path = ARouterConfig.SPLASH_ACTIVITY)
class SplashActivity : AppCompatActivity() {

    private var builder: AdController? = null
    private val WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"
    private val REQUEST_PERMISSION_CODE = 100
    private var isForResult: Boolean = false
    private val FORRESULT_CODE: Int = 400
    private val STORAGE_MESSAGE: String = "存储空间"
    private var canJump = false
    private var channel: String? = null

    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        compositeDisposable = CompositeDisposable()
        BaseApplication.isFromStart = true
        channel = PackageUtils.getAppMetaData(this, "CHANNEL")
        BaseApplication.channel = channel
        askPermissions()
    }


    private fun askPermissions() {
        HttpUtils.setServiceForADConfig(AdService::class.java).getADConfig(channel = channel!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<AdsConfig> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.addAll(d)
                    }

                    override fun onNext(t: AdsConfig) {
                        val gson = Gson()
                        SPUtil.getInstance().putString(ADConstants.START_PAGE, gson.toJson(t.data?.start_page))
                        SPUtil.getInstance().putString(ADConstants.CATEGORY_PAGE, gson.toJson(t.data?.category_page))
                        SPUtil.getInstance().putString(ADConstants.DETAIL_PAGE, gson.toJson(t.data?.detail_page))
                        SPUtil.getInstance().putString(ADConstants.START_PAGE, gson.toJson(t.data?.start_page))
                        SPUtil.getInstance().putString(ADConstants.START_PAGE, gson.toJson(t.data?.start_page))
                    }

                    override fun onError(e: Throwable) {

                    }
                })

        if (hasPhoneermission() && hasStoragePermission()) {
//        if (hasStoragePermission() &&
//                hasLocationPermission() &&
//                hasCOARSEPermission() &&
//                hasPhoneermission()) {
//            BaseApplication.isBackGround = false
            builder = AdController.Builder(this)
                    .setPage(ADConstants.START_PAGE)
                    .setContainer(splash_container)
                    .setSkipView(skip_view)
                    .setLogo(app_logo)
                    .create()
            builder?.show()
        } else {
            val dialog = object : AuthorityDialog(this) {
                override fun onClick(v: View?) {
                    sendRequest()
                    dismiss()
                    return
                }
            }
            dialog.show()
        }
    }

    private fun hasStoragePermission(): Boolean {
        return AndPermission.hasPermission(this, WRITE_EXTERNAL_STORAGE)
    }

    private fun hasLocationPermission(): Boolean {
        return AndPermission.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun hasCOARSEPermission(): Boolean {
        return AndPermission.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun hasPhoneermission(): Boolean {
        return AndPermission.hasPermission(this, Manifest.permission.READ_PHONE_STATE)
    }

    private fun sendRequest() {
        AndPermission.with(this).requestCode(REQUEST_PERMISSION_CODE).permission(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ).callback(permissionListener).start()
    }

    private val permissionListener = object : PermissionListener {
        override fun onSucceed(requestCode: Int, grantPermissions: List<String>) {
            val hasStoragePermission = hasStoragePermission()
            if (requestCode == REQUEST_PERMISSION_CODE) {
                if (hasStoragePermission) {
                    checkIn()
                }
            }
        }

        override fun onFailed(requestCode: Int, deniedPermissions: List<String>) {
            if (requestCode == REQUEST_PERMISSION_CODE) {
                lg("拒绝权限回调")
                val hasStoragePermission = hasStoragePermission()
                val isDeniedStorage = deniedPermissions.contains(WRITE_EXTERNAL_STORAGE)
                if (AndPermission.hasAlwaysDeniedPermission(this@SplashActivity, deniedPermissions) && isDeniedStorage) {
                    lg("永久拒绝存储，弹出相应权限提示")
                    val dialog = SetPermissionDialog(this@SplashActivity, FORRESULT_CODE, STORAGE_MESSAGE)
                    dialog.show()
                    isForResult = true
                    return
                }
                if (hasStoragePermission) {
                    //拒绝电话权限，有存储权限，可以启动
                    checkIn()
                } else {

                    object : SetPermissionDialog(this@SplashActivity, REQUEST_PERMISSION_CODE, STORAGE_MESSAGE) {
                        override fun onClick(v: View) {
                            sendRequest()
                            dismiss()
                        }
                    }.show()

                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        canJump = false
    }


    override fun onResume() {
        super.onResume()
        if (canJump) {
            next()
        }
        canJump = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //勾选了永久拒绝，从设置返回才会进入
        if (requestCode == FORRESULT_CODE && isForResult) {
            //设置里拒绝存储权限
            if (!hasStoragePermission()) {
                SetPermissionDialog(this@SplashActivity, FORRESULT_CODE, STORAGE_MESSAGE).show()
                return
            }
            //设置里开启存储权限
            checkIn()
        }
    }

    fun checkIn() {
        IntentUtils.toActivity(ARouterConfig.HOME_ACTIVITY)
        BaseApplication.isFromStart = false
        finish()
    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    private operator fun next() {
        if (canJump) {
            checkIn()
        } else {
            canJump = true
        }
    }

    /** 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费  */

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            true
        } else super.onKeyDown(keyCode, event)
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun receiveFinish(event: LoadEvent) {
//        if (event.isFinish) {
//            finish()
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        builder?.destroy()
        compositeDisposable.dispose()
    }
}
