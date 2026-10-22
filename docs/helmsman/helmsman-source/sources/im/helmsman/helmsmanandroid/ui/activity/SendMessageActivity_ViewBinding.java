package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.view.TitleBar;

/* JADX INFO: loaded from: classes2.dex */
public class SendMessageActivity_ViewBinding implements Unbinder {
    private SendMessageActivity target;
    private View view7f0801d8;
    private View view7f0802ab;

    public SendMessageActivity_ViewBinding(SendMessageActivity sendMessageActivity) {
        this(sendMessageActivity, sendMessageActivity.getWindow().getDecorView());
    }

    public SendMessageActivity_ViewBinding(final SendMessageActivity sendMessageActivity, View view) {
        this.target = sendMessageActivity;
        sendMessageActivity.etShareDescription = (EditText) Utils.findRequiredViewAsType(view, R.id.et_share_description, "field 'etShareDescription'", EditText.class);
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.iv_send, "field 'tvSend' and method 'onClick'");
        sendMessageActivity.tvSend = (ImageView) Utils.castView(viewFindRequiredView, R.id.iv_send, "field 'tvSend'", ImageView.class);
        this.view7f0801d8 = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.SendMessageActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                sendMessageActivity.onClick(view2);
            }
        });
        sendMessageActivity.titleBar = (TitleBar) Utils.findRequiredViewAsType(view, R.id.titleBar, "field 'titleBar'", TitleBar.class);
        sendMessageActivity.tvSendMessageTextCount = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_send_message_text_count, "field 'tvSendMessageTextCount'", TextView.class);
        View viewFindRequiredView2 = Utils.findRequiredView(view, R.id.rel_send_message_at, "field 'relSendMessageAt' and method 'onClick'");
        sendMessageActivity.relSendMessageAt = (RelativeLayout) Utils.castView(viewFindRequiredView2, R.id.rel_send_message_at, "field 'relSendMessageAt'", RelativeLayout.class);
        this.view7f0802ab = viewFindRequiredView2;
        viewFindRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.SendMessageActivity_ViewBinding.2
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                sendMessageActivity.onClick(view2);
            }
        });
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        SendMessageActivity sendMessageActivity = this.target;
        if (sendMessageActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        sendMessageActivity.etShareDescription = null;
        sendMessageActivity.tvSend = null;
        sendMessageActivity.titleBar = null;
        sendMessageActivity.tvSendMessageTextCount = null;
        sendMessageActivity.relSendMessageAt = null;
        this.view7f0801d8.setOnClickListener(null);
        this.view7f0801d8 = null;
        this.view7f0802ab.setOnClickListener(null);
        this.view7f0802ab = null;
    }
}
