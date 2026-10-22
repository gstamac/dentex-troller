package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.view.TitleBar;

/* JADX INFO: loaded from: classes2.dex */
public class MessageDetailActivity_ViewBinding implements Unbinder {
    private MessageDetailActivity target;
    private View view7f08014d;
    private View view7f0801fa;
    private View view7f08034a;

    public MessageDetailActivity_ViewBinding(MessageDetailActivity messageDetailActivity) {
        this(messageDetailActivity, messageDetailActivity.getWindow().getDecorView());
    }

    public MessageDetailActivity_ViewBinding(final MessageDetailActivity messageDetailActivity, View view) {
        this.target = messageDetailActivity;
        messageDetailActivity.tvMessagedetailDescription = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_messagedetail_description, "field 'tvMessagedetailDescription'", TextView.class);
        messageDetailActivity.titleBar = (TitleBar) Utils.findRequiredViewAsType(view, R.id.titleBar, "field 'titleBar'", TitleBar.class);
        messageDetailActivity.vsGoogleMap = (ViewStub) Utils.findRequiredViewAsType(view, R.id.vs_google_map, "field 'vsGoogleMap'", ViewStub.class);
        messageDetailActivity.vsGaodeMap = (ViewStub) Utils.findRequiredViewAsType(view, R.id.vs_gaode_map, "field 'vsGaodeMap'", ViewStub.class);
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.frame_item_my_following_favourite, "field 'frameItemMyFollowingFavourite' and method 'onClick'");
        messageDetailActivity.frameItemMyFollowingFavourite = (FrameLayout) Utils.castView(viewFindRequiredView, R.id.frame_item_my_following_favourite, "field 'frameItemMyFollowingFavourite'", FrameLayout.class);
        this.view7f08014d = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.MessageDetailActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                messageDetailActivity.onClick(view2);
            }
        });
        View viewFindRequiredView2 = Utils.findRequiredView(view, R.id.tlinear_item_my_following_download, "field 'tlinearItemMyFollowingDownload' and method 'onClick'");
        messageDetailActivity.tlinearItemMyFollowingDownload = (LinearLayout) Utils.castView(viewFindRequiredView2, R.id.tlinear_item_my_following_download, "field 'tlinearItemMyFollowingDownload'", LinearLayout.class);
        this.view7f08034a = viewFindRequiredView2;
        viewFindRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.MessageDetailActivity_ViewBinding.2
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                messageDetailActivity.onClick(view2);
            }
        });
        View viewFindRequiredView3 = Utils.findRequiredView(view, R.id.lin_item_my_following_comment, "field 'linItemMyFollowingComment' and method 'onClick'");
        messageDetailActivity.linItemMyFollowingComment = (LinearLayout) Utils.castView(viewFindRequiredView3, R.id.lin_item_my_following_comment, "field 'linItemMyFollowingComment'", LinearLayout.class);
        this.view7f0801fa = viewFindRequiredView3;
        viewFindRequiredView3.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.MessageDetailActivity_ViewBinding.3
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                messageDetailActivity.onClick(view2);
            }
        });
        messageDetailActivity.tvItemMyFollowingDownload = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_item_my_following_download, "field 'tvItemMyFollowingDownload'", TextView.class);
        messageDetailActivity.tvItemMyFollowingComment = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_item_my_following_comment, "field 'tvItemMyFollowingComment'", TextView.class);
        messageDetailActivity.ivMyrouteHeartLike = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_myroute_heart_like, "field 'ivMyrouteHeartLike'", ImageView.class);
        messageDetailActivity.civMessagedetail = (CircleImageView) Utils.findRequiredViewAsType(view, R.id.civ_messagedetail, "field 'civMessagedetail'", CircleImageView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        MessageDetailActivity messageDetailActivity = this.target;
        if (messageDetailActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        messageDetailActivity.tvMessagedetailDescription = null;
        messageDetailActivity.titleBar = null;
        messageDetailActivity.vsGoogleMap = null;
        messageDetailActivity.vsGaodeMap = null;
        messageDetailActivity.frameItemMyFollowingFavourite = null;
        messageDetailActivity.tlinearItemMyFollowingDownload = null;
        messageDetailActivity.linItemMyFollowingComment = null;
        messageDetailActivity.tvItemMyFollowingDownload = null;
        messageDetailActivity.tvItemMyFollowingComment = null;
        messageDetailActivity.ivMyrouteHeartLike = null;
        messageDetailActivity.civMessagedetail = null;
        this.view7f08014d.setOnClickListener(null);
        this.view7f08014d = null;
        this.view7f08034a.setOnClickListener(null);
        this.view7f08034a = null;
        this.view7f0801fa.setOnClickListener(null);
        this.view7f0801fa = null;
    }
}
