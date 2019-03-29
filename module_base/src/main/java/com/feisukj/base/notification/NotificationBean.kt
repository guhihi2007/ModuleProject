package com.feisukj.base.notification

import android.graphics.Bitmap

/**
 * Author : Gupingping
 * Date : 2019/3/26
 * QQ : 464955343
 * @param type: 0 表示是收音机类型  1 表示是有声小说类型，需要next按钮
 * @param pendingCls 点击通知栏跳转的activity
 */
data class NotificationBean(var title: String) {
    var type: Int = radio
    var isPlaying = true
    var content: String? = null
    var imageBitmap: Bitmap? = null
    var pendingCls: Class<*>? = null
    companion object {
        const val radio=0
        const val novel=1
    }
}