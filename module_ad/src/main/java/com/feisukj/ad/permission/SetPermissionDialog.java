package com.feisukj.ad.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.feisukj.ad.R;
import com.yanzhenjie.permission.AndPermission;


/**
 * Author : Gupingping
 * Date : 2018/8/8
 * Mail : gu12pp@163.com
 */


public class SetPermissionDialog extends PDialogHolder {
    private View empty_view, mlayout;
    private TextView mTitle, mContent, mBtn, mNotice;

    public SetPermissionDialog(Activity activity, int requestCode) {
        super(activity, requestCode);
    }

    public SetPermissionDialog(Activity activity, int requestCode, String msg) {
        super(activity, requestCode, msg);
    }

    @Override
    protected void findViews() {
        mlayout = findViewById(R.id.layout_View);
        empty_view = findViewById(R.id.blank_View);
        mTitle = (TextView) findViewById(R.id.title_Tv);
        mContent = (TextView) findViewById(R.id.Content);
        mBtn = (TextView) findViewById(R.id.set_Tv);
//        mNotice = (TextView) findViewById(R.id.notice);
        mBtn.setOnClickListener(this);
    }

    @Override
    protected int layoutID() {
        return R.layout.permission_notice_dialog;
    }

    @Override
    protected TextView msgTextView() {
        return mContent;
    }

    @Override
    protected void setMsg() {
        String str;
        if (TextUtils.isEmpty(mMsg)) {
            str = "由于获取不了存储空间权限，无法正常使用漫画大全。";
        } else {
            str = "由于获取不了" + mMsg + "权限，无法正常使用漫画大全。";
        }
        Spannable spannable = new SpannableString(str);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FF8800")), 6, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        msgTextView().setText(spannable);
    }

    @Override
    public void onClick(View v) {
        if (mRequestCode == 100) {
            AndPermission.with(mActivity).requestCode(mRequestCode).permission(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)//.rationale(retionalListenser)
                    .start();
            dismiss();
            return;
        }
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
        intent.setData(uri);
        mActivity.startActivityForResult(intent, mRequestCode);
        dismiss();
    }
}
