package im.helmsman.helmsmanandroid.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.CompoundButton;
import android.widget.Switch;
import butterknife.BindView;
import com.zhy.http.okhttp.OkHttpUtils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.presenter.NotifictionSettingPresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.NotificationSettingView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationSettingActivity extends Mvp2BaseActivity<NotificationSettingView, NotifictionSettingPresenter> implements NotificationSettingView, CompoundButton.OnCheckedChangeListener {
    private AlertDialog alertDialog;
    private Handler handler;
    private boolean isChange = false;

    @BindView(R.id.switch_at)
    Switch switchAt;

    @BindView(R.id.switch_comment)
    Switch switchComment;

    @BindView(R.id.switch_favourite)
    Switch switchFavourite;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.NotificationSettingView
    public void showSetMessage(String str) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public NotifictionSettingPresenter initPresenter() {
        return new NotifictionSettingPresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_notification_setting);
        setStatusBar(R.color.red_state);
        reloadConfig();
        initEvent();
        this.handler = new Handler();
    }

    private void initEvent() {
        this.switchAt.setOnCheckedChangeListener(this);
        this.switchFavourite.setOnCheckedChangeListener(this);
        this.switchComment.setOnCheckedChangeListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.NotificationSettingView
    public void reloadConfig() {
        Map<String, Boolean> pushSetting = Config.getPushSetting();
        if (pushSetting != null) {
            boolean zBooleanValue = pushSetting.get("comment").booleanValue();
            boolean zBooleanValue2 = pushSetting.get("mention").booleanValue();
            boolean zBooleanValue3 = pushSetting.get("favourite").booleanValue();
            this.switchAt.setChecked(zBooleanValue2);
            this.switchComment.setChecked(zBooleanValue);
            this.switchFavourite.setChecked(zBooleanValue3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTimeOutDialog() {
        SpannableString spannableString = new SpannableString(getString(R.string.addroute_discard));
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#007AFF")), 0, spannableString.length(), 18);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.set_push_timeout);
        builder.setNegativeButton(spannableString, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.NotificationSettingActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                NotificationSettingActivity.this.finish();
            }
        });
        builder.setPositiveButton(R.string.resend, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.NotificationSettingActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                NotificationSettingActivity.this.onBackPressed();
            }
        });
        AlertDialog alertDialogCreate = builder.create();
        this.alertDialog = alertDialogCreate;
        alertDialogCreate.show();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.isChange) {
            ((NotifictionSettingPresenter) this.presenter).changeNotificationSettingConfig(this.switchComment.isChecked(), this.switchAt.isChecked(), this.switchFavourite.isChecked());
            this.handler.postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.NotificationSettingActivity.3
                @Override // java.lang.Runnable
                public void run() {
                    if (!NotificationSettingActivity.this.isChange || NotificationSettingActivity.this.isDestroyed()) {
                        return;
                    }
                    NotificationSettingActivity.this.showTimeOutDialog();
                    ViewUtils.cancelProgressDialog();
                }
            }, OkHttpUtils.DEFAULT_MILLISECONDS);
        } else {
            super.onBackPressed();
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.NotificationSettingView
    public void setIsChange(boolean z) {
        this.isChange = z;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        this.isChange = true;
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.alertDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.alertDialog.dismiss();
        }
        ViewUtils.cancelProgressDialog();
    }
}
