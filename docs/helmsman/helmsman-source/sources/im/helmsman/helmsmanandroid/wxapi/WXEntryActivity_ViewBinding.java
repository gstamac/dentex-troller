package im.helmsman.helmsmanandroid.wxapi;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class WXEntryActivity_ViewBinding implements Unbinder {
    private WXEntryActivity target;
    private View view7f0800c8;
    private View view7f080122;
    private View view7f080123;
    private View view7f08019f;
    private View view7f080299;
    private View view7f08029a;

    public WXEntryActivity_ViewBinding(WXEntryActivity wXEntryActivity) {
        this(wXEntryActivity, wXEntryActivity.getWindow().getDecorView());
    }

    public WXEntryActivity_ViewBinding(final WXEntryActivity wXEntryActivity, View view) {
        this.target = wXEntryActivity;
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.civ_profile_avatar, "field 'civProfileAvatar' and method 'onClick'");
        wXEntryActivity.civProfileAvatar = (CircleImageView) Utils.castView(viewFindRequiredView, R.id.civ_profile_avatar, "field 'civProfileAvatar'", CircleImageView.class);
        this.view7f0800c8 = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.wxapi.WXEntryActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                wXEntryActivity.onClick(view2);
            }
        });
        wXEntryActivity.ivEditNewUserinfoEmail = (EditText) Utils.findRequiredViewAsType(view, R.id.iv_edit_new_userinfo_email, "field 'ivEditNewUserinfoEmail'", EditText.class);
        View viewFindRequiredView2 = Utils.findRequiredView(view, R.id.rel_edit_new_userinfo_sex, "field 'relEditNewUserinfoSex' and method 'onClick'");
        wXEntryActivity.relEditNewUserinfoSex = (RelativeLayout) Utils.castView(viewFindRequiredView2, R.id.rel_edit_new_userinfo_sex, "field 'relEditNewUserinfoSex'", RelativeLayout.class);
        this.view7f08029a = viewFindRequiredView2;
        viewFindRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.wxapi.WXEntryActivity_ViewBinding.2
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                wXEntryActivity.onClick(view2);
            }
        });
        View viewFindRequiredView3 = Utils.findRequiredView(view, R.id.rel_edit_new_userinfo_contry, "field 'relEditNewUserinfoContry' and method 'onClick'");
        wXEntryActivity.relEditNewUserinfoContry = (RelativeLayout) Utils.castView(viewFindRequiredView3, R.id.rel_edit_new_userinfo_contry, "field 'relEditNewUserinfoContry'", RelativeLayout.class);
        this.view7f080299 = viewFindRequiredView3;
        viewFindRequiredView3.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.wxapi.WXEntryActivity_ViewBinding.3
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                wXEntryActivity.onClick(view2);
            }
        });
        wXEntryActivity.ivEditNewUserinfoDescription = (EditText) Utils.findRequiredViewAsType(view, R.id.iv_edit_new_userinfo_description, "field 'ivEditNewUserinfoDescription'", EditText.class);
        View viewFindRequiredView4 = Utils.findRequiredView(view, R.id.et_edit_new_userinfo_contry, "field 'etEditNewUserinfoContry' and method 'onClick'");
        wXEntryActivity.etEditNewUserinfoContry = (EditText) Utils.castView(viewFindRequiredView4, R.id.et_edit_new_userinfo_contry, "field 'etEditNewUserinfoContry'", EditText.class);
        this.view7f080122 = viewFindRequiredView4;
        viewFindRequiredView4.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.wxapi.WXEntryActivity_ViewBinding.4
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                wXEntryActivity.onClick(view2);
            }
        });
        View viewFindRequiredView5 = Utils.findRequiredView(view, R.id.et_edit_new_userinfo_sex, "field 'etEditNewUserinfoSex' and method 'onClick'");
        wXEntryActivity.etEditNewUserinfoSex = (EditText) Utils.castView(viewFindRequiredView5, R.id.et_edit_new_userinfo_sex, "field 'etEditNewUserinfoSex'", EditText.class);
        this.view7f080123 = viewFindRequiredView5;
        viewFindRequiredView5.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.wxapi.WXEntryActivity_ViewBinding.5
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                wXEntryActivity.onClick(view2);
            }
        });
        View viewFindRequiredView6 = Utils.findRequiredView(view, R.id.iv_edit_new_userinfo_tick, "field 'ivEditNewUserinfoTick' and method 'onClick'");
        wXEntryActivity.ivEditNewUserinfoTick = (ImageView) Utils.castView(viewFindRequiredView6, R.id.iv_edit_new_userinfo_tick, "field 'ivEditNewUserinfoTick'", ImageView.class);
        this.view7f08019f = viewFindRequiredView6;
        viewFindRequiredView6.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.wxapi.WXEntryActivity_ViewBinding.6
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                wXEntryActivity.onClick(view2);
            }
        });
        wXEntryActivity.ivEditNewUserinfoNickname = (EditText) Utils.findRequiredViewAsType(view, R.id.iv_edit_new_userinfo_nickname, "field 'ivEditNewUserinfoNickname'", EditText.class);
        wXEntryActivity.activityEditNewUserInfo = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.activity_edit_new_user_info, "field 'activityEditNewUserInfo'", RelativeLayout.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        WXEntryActivity wXEntryActivity = this.target;
        if (wXEntryActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        wXEntryActivity.civProfileAvatar = null;
        wXEntryActivity.ivEditNewUserinfoEmail = null;
        wXEntryActivity.relEditNewUserinfoSex = null;
        wXEntryActivity.relEditNewUserinfoContry = null;
        wXEntryActivity.ivEditNewUserinfoDescription = null;
        wXEntryActivity.etEditNewUserinfoContry = null;
        wXEntryActivity.etEditNewUserinfoSex = null;
        wXEntryActivity.ivEditNewUserinfoTick = null;
        wXEntryActivity.ivEditNewUserinfoNickname = null;
        wXEntryActivity.activityEditNewUserInfo = null;
        this.view7f0800c8.setOnClickListener(null);
        this.view7f0800c8 = null;
        this.view7f08029a.setOnClickListener(null);
        this.view7f08029a = null;
        this.view7f080299.setOnClickListener(null);
        this.view7f080299 = null;
        this.view7f080122.setOnClickListener(null);
        this.view7f080122 = null;
        this.view7f080123.setOnClickListener(null);
        this.view7f080123 = null;
        this.view7f08019f.setOnClickListener(null);
        this.view7f08019f = null;
    }
}
