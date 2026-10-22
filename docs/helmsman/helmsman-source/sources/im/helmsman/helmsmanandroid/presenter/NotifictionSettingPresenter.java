package im.helmsman.helmsmanandroid.presenter;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.JpushQueryTagAndAlias;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.NotificationSettingView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class NotifictionSettingPresenter extends BasePresenter<NotificationSettingView> {
    private String alias;
    private Map<String, Boolean> config = new HashMap();
    Set<String> tempTags;

    public void changeNotificationSettingConfig(final boolean z, final boolean z2, final boolean z3) {
        ViewUtils.showProgressDialog(R.string.loading);
        if (this.tempTags != null && this.alias != null) {
            initTags(z, z2, z3);
            uploadTagsAndAlias();
        } else {
            RequestInetUtils.instance().getJpushTagAndAlias(new Callback<JpushQueryTagAndAlias>() { // from class: im.helmsman.helmsmanandroid.presenter.NotifictionSettingPresenter.1
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<JpushQueryTagAndAlias> response) {
                    NotifictionSettingPresenter.this.tempTags = new HashSet();
                    JpushQueryTagAndAlias jpushQueryTagAndAliasBody = response.body();
                    NotifictionSettingPresenter.this.alias = jpushQueryTagAndAliasBody.getAlias();
                    List<String> tags = jpushQueryTagAndAliasBody.getTags();
                    if (tags != null) {
                        Iterator<String> it = tags.iterator();
                        while (it.hasNext()) {
                            NotifictionSettingPresenter.this.tempTags.add(it.next());
                        }
                    }
                    NotifictionSettingPresenter.this.initTags(z, z2, z3);
                    NotifictionSettingPresenter.this.uploadTagsAndAlias();
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    ViewUtils.ShowToast(MyApplication.getInstance().getString(R.string.failed_to_change_setting) + " " + errorCode.getMessage());
                    ViewUtils.cancelProgressDialog();
                    if (NotifictionSettingPresenter.this.view != null) {
                        ((NotificationSettingView) NotifictionSettingPresenter.this.view).reloadConfig();
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getJpushTagAndAlias() {
        RequestInetUtils.instance().getJpushTagAndAlias(new Callback<JpushQueryTagAndAlias>() { // from class: im.helmsman.helmsmanandroid.presenter.NotifictionSettingPresenter.2
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<JpushQueryTagAndAlias> response) {
                List<String> tags = response.body().getTags();
                NotifictionSettingPresenter.this.config.put("comment", false);
                NotifictionSettingPresenter.this.config.put("favourite", false);
                NotifictionSettingPresenter.this.config.put("mention", false);
                Iterator<String> it = tags.iterator();
                while (it.hasNext()) {
                    NotifictionSettingPresenter.this.config.put(it.next(), true);
                }
                Config.savePushSetting(NotifictionSettingPresenter.this.config);
                if (NotifictionSettingPresenter.this.view != null) {
                    ((NotificationSettingView) NotifictionSettingPresenter.this.view).reloadConfig();
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                ViewUtils.ShowToast(MyApplication.getInstance().getString(R.string.failed_to_change_setting) + " " + errorCode.getMessage());
                ViewUtils.cancelProgressDialog();
                if (NotifictionSettingPresenter.this.view != null) {
                    ((NotificationSettingView) NotifictionSettingPresenter.this.view).reloadConfig();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initTags(boolean z, boolean z2, boolean z3) {
        if (z2) {
            this.tempTags.add("mention");
        } else {
            this.tempTags.remove("mention");
        }
        if (z) {
            this.tempTags.add("comment");
        } else {
            this.tempTags.remove("comment");
        }
        if (z3) {
            this.tempTags.add("favourite");
        } else {
            this.tempTags.remove("favourite");
        }
    }

    public void uploadTagsAndAlias() {
        JPushInterface.setTags(MyApplication.getContext(), this.tempTags, new TagAliasCallback() { // from class: im.helmsman.helmsmanandroid.presenter.NotifictionSettingPresenter.3
            @Override // cn.jpush.android.api.TagAliasCallback
            public void gotResult(int i, String str, Set<String> set) {
                ViewUtils.cancelProgressDialog();
                if (i != 0) {
                    if (NotifictionSettingPresenter.this.view != null) {
                        ((NotificationSettingView) NotifictionSettingPresenter.this.view).showSetMessage("");
                    }
                    NotifictionSettingPresenter.this.getJpushTagAndAlias();
                    ViewUtils.ShowToast(MyApplication.getInstance().getString(R.string.failed_to_change_setting) + " ");
                    return;
                }
                NotifictionSettingPresenter.this.config.put("comment", false);
                NotifictionSettingPresenter.this.config.put("favourite", false);
                NotifictionSettingPresenter.this.config.put("mention", false);
                Iterator<String> it = NotifictionSettingPresenter.this.tempTags.iterator();
                while (it.hasNext()) {
                    NotifictionSettingPresenter.this.config.put(it.next(), true);
                }
                Config.savePushSetting(NotifictionSettingPresenter.this.config);
                ViewUtils.ShowToast(MyApplication.getInstance().getString(R.string.save_success));
                if (NotifictionSettingPresenter.this.view != null) {
                    ((NotificationSettingView) NotifictionSettingPresenter.this.view).setIsChange(false);
                    ((NotificationSettingView) NotifictionSettingPresenter.this.view).onBackPressed();
                }
            }
        });
    }
}
