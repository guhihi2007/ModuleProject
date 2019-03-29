//package com.feisukj.base.notification
//
//import android.annotation.SuppressLint
//import android.content.Context.NOTIFICATION_SERVICE
//import android.os.Build
//import android.annotation.TargetApi
//import android.app.*
//import android.content.Context
//import android.content.Intent
//import android.graphics.BitmapFactory
//import android.support.v4.app.NotificationCompat
//import android.view.View
//import android.widget.RemoteViews
//import com.feisukj.base.R
//import com.feisukj.base.util.LogUtils
//
///**
// * Author : Gupingping
// * Date : 2019/3/25
// * QQ : 464955343
// *音乐通知栏控制参考类，有展开的大通知栏，和正常的小通知栏
// */
//class NotificationMgr private constructor(context: Service) {
//
//    private var notificationManager: NotificationManager? = null
//    private var service = context
//    var pendingCls: Class<*>? = null
//
//    /**
//     * 通用方法：
//     * 创建指定的通知channel
//     * @param channelId 指定一个id
//     * @param channelName 当前channelId的名称，8.0以上通知栏设置内可以查看
//     * @param importance 优先级，默认最高
//     */
//    @TargetApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel(context: Context, channelId: String, channelName: String, importance: Int = NotificationManager.IMPORTANCE_HIGH) {
//        val channel = NotificationChannel(channelId, channelName, importance)
//        notificationManager = context.getSystemService(
//                NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager?.createNotificationChannel(channel)
//    }
//
//    fun init(pendingCls: Class<*>) {
//        this.pendingCls = pendingCls
//        this.notificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//    }
//
//    fun showPlay(context: Context, bean: NotificationBean) {
//        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, radioChnanelId)
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(radioChnanelId, radioName, NotificationManager.IMPORTANCE_HIGH)
//            notificationManager?.createNotificationChannel(channel)
//        }
//
//        //展开时的控制栏
//        val pullDownMode = RemoteViews(context.packageName, R.layout.layout_pull_down)
//
//        //正常控制栏
//        val normalMode = RemoteViews(context.packageName, R.layout.layout_normal)
//        if (bean.type == NotificationBean.radio) {
//            normalMode.setViewVisibility(R.id.play_next, View.GONE)
//            pullDownMode.setViewVisibility(R.id.play_next, View.GONE)
//
//            normalMode.setViewVisibility(R.id.play_pre, View.GONE)
//            pullDownMode.setViewVisibility(R.id.play_pre, View.GONE)
//        }
//        //设置图片/内容
//
//        normalMode.setTextViewText(R.id.tv_title, bean.title)
//        normalMode.setTextViewText(R.id.tv_subtitle, bean.content)
//
//        pullDownMode.setTextViewText(R.id.tv_title, bean.title)
//        pullDownMode.setTextViewText(R.id.tv_subtitle, bean.content)
//
//        pullDownMode.setImageViewBitmap(R.id.iv_icon, bean.imageBitmap)
//        normalMode.setImageViewBitmap(R.id.iv_icon, bean.imageBitmap)
//
//        normalMode.setImageViewResource(R.id.play_start, if (bean.isPlaying) R.mipmap.ic_pause else R.mipmap.ic_play)
//        pullDownMode.setImageViewResource(R.id.play_start, if (bean.isPlaying) R.mipmap.ic_pause else R.mipmap.ic_play)
//
//        //点击启动activity
//        val pendingIntent = PendingIntent.getActivity(context, 5, Intent(context, pendingCls), PendingIntent.FLAG_UPDATE_CURRENT)
//        normalMode.setOnClickPendingIntent(R.id.iv_icon, pendingIntent)
//
//        //点击播放暂停
//        val playIntent = Intent(NotificationActions.ation)
//        playIntent.putExtra(NotificationActions.extra, NotificationActions.play)
//        val playPending = PendingIntent.getBroadcast(context, 6, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        normalMode.setOnClickPendingIntent(R.id.play_start, playPending)
//        pullDownMode.setOnClickPendingIntent(R.id.play_start, playPending)
//
//        //点击next
//        val nextIntent = Intent(NotificationActions.ation)
//        nextIntent.putExtra(NotificationActions.extra, NotificationActions.next)
//        val nextPending = PendingIntent.getBroadcast(context, 7, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        normalMode.setOnClickPendingIntent(R.id.play_next, nextPending)
//        pullDownMode.setOnClickPendingIntent(R.id.play_next, nextPending)
//
//        //点击pre
//        val preIntent = Intent(NotificationActions.ation)
//        preIntent.putExtra(NotificationActions.extra, NotificationActions.pre)
//        val prePending = PendingIntent.getBroadcast(context, 8, preIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        normalMode.setOnClickPendingIntent(R.id.play_pre, prePending)
//        pullDownMode.setOnClickPendingIntent(R.id.play_pre, prePending)
//
//        //点击stop
//        val stopIntent = Intent(NotificationActions.ation)
//        stopIntent.putExtra(NotificationActions.extra, NotificationActions.stop)
//        val stopPending = PendingIntent.getBroadcast(context, 9, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        normalMode.setOnClickPendingIntent(R.id.iv_close, stopPending)
//        pullDownMode.setOnClickPendingIntent(R.id.iv_close, stopPending)
//
//
//        builder.setAutoCancel(false)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
//                .setContent(normalMode)
//                .setCustomBigContentView(pullDownMode)
//                .setPriority(NotificationCompat.PRIORITY_MAX)//设置了最大优先级，默认展示大通知栏
//                .setContentIntent(pendingIntent)
//
//        service.startForeground(notificationID, builder.build())
//    }
//
//    fun cancel() {
//        service.stopForeground(true)
//    }
//
//    companion object {
//        @SuppressLint("StaticFieldLeak")
//        @Volatile
//        var instance: NotificationMgr? = null
//        val radioName = "播放控制"
//        val radioChnanelId = "20000"
//        val notificationID = 102
//        fun getInstance(context: Service): NotificationMgr {
//            if (instance == null) {
//                synchronized(NotificationMgr::class) {
//                    if (instance == null) {
//                        instance = NotificationMgr(context)
//                    }
//                }
//            }
//            return instance!!
//        }
//    }
//}