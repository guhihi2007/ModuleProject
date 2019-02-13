package com.feisukj.base.widget.loaddialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.feisukj.base.R


/**
 * Author : Gupingping
 * Date : 2018/11/28
 * QQ : 464955343
 */
class LoadingDialog(private val context: Context) {

    private var mLoadingView: LVCircularRing? = null
    private var mLoadingDialog: Dialog? = null
    private var msg = "加载中···"
    private var cancelable = true
    var isShowing: Boolean = false
        private set

    /**
     * 设置提示信息
     */
    fun setTitleText(msg: String): LoadingDialog {
        this.msg = msg
        return this
    }

    /**
     * 返回键是否可用
     */
    fun setCancelable(cancelable: Boolean): LoadingDialog {
        this.cancelable = cancelable
        return this
    }

    fun show() {
        val view = View.inflate(context, R.layout.dialog_loading, null)
        // 获取整个布局
        val layout = view.findViewById<LinearLayout>(R.id.dialog_view)
        // 页面中的LoadingView
        mLoadingView = view.findViewById(R.id.lvcr_loading)
        // 页面中显示文本
        val loadingText = view.findViewById<TextView>(R.id.loading_text)
        // 显示文本
        loadingText.text = msg
        // 创建自定义样式的Dialog
        mLoadingDialog = Dialog(context, R.style.LoadingDialog)
        // 设置返回键无效
        mLoadingDialog!!.setCancelable(cancelable)
        mLoadingDialog!!.setCanceledOnTouchOutside(cancelable)
        mLoadingDialog!!.setContentView(layout, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT))
        mLoadingDialog!!.show()
        mLoadingView!!.startAnim()
        isShowing = true
    }

    fun dismiss() {
        if (mLoadingDialog != null && isShowing) {
            mLoadingView!!.stopAnim()
            mLoadingDialog!!.dismiss()
            mLoadingDialog = null
            isShowing = false
        }
    }

}
