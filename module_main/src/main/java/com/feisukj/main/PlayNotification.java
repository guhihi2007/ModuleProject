package com.feisukj.main;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.feisukj.base.BuildConfig;
import com.feisukj.base.R;
import com.feisukj.base.notification.BaseNotification;

/**
 * Author : Gupingping
 * Date : 2019/2/14
 * QQ : 464955343
 */
public class PlayNotification extends BaseNotification {

    private Context context;
    private Class clz;

    /**
     * @param base 上下文
     * @param cls  需要启动的activity
     */
    public PlayNotification(Context base, Class cls) {
        super(base);
        this.context = base;
        this.clz = cls;
    }

    @Override
    protected RemoteViews getRemoteViews() {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_play);
        remoteViews.setImageViewResource(R.id.iv_icon, R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.tv_title, "标题");
        remoteViews.setTextViewText(R.id.tv_subtitle, "内容");
        //TODO 设置按钮点击PendingIntent


        return remoteViews;
    }

    @Override
    protected PendingIntent getPendingIntent() {
        Intent intent = new Intent(context, clz);
        intent.putExtra(BuildConfig.APPLICATION_ID, true);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
