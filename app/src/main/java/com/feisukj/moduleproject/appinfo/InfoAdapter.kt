package com.feisukj.moduleproject.appinfo

import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.feisukj.base.util.LogUtils
import com.feisukj.moduleproject.R

/**
 * Author : Gupingping
 * Date : 2019/1/8
 * QQ : 464955343
 */
class InfoAdapter(data: List<AppInfoBean>) : BaseQuickAdapter<AppInfoBean, BaseViewHolder>(R.layout.item_app_info, data) {
    override fun convert(helper: BaseViewHolder, item: AppInfoBean) {

        helper.setText(R.id.info_key, item.name)
        helper.setText(R.id.info_value, item.value)
        helper.itemView.setOnClickListener {
            if (item.name == "OpenLog") {
                LogUtils.mDebuggable = LogUtils.LEVEL_ALL
                Toast.makeText(mContext, "开启日志", Toast.LENGTH_SHORT).show()
            }
            if (item.name == "CloseLog") {
                LogUtils.mDebuggable = LogUtils.LEVEL_OFF
                Toast.makeText(mContext, "关闭日志", Toast.LENGTH_SHORT).show()
            }
        }
    }
}