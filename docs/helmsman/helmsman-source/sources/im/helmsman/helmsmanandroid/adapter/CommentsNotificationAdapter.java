package im.helmsman.helmsmanandroid.adapter;

import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.model.CommentNotificationResultModel;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.TimeUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CommentsNotificationAdapter extends BaseAdapter {
    private List<CommentNotificationResultModel.CommentsBean> datas;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder_ViewBinding implements Unbinder {
        private ViewHolder target;

        public ViewHolder_ViewBinding(ViewHolder viewHolder, View view) {
            this.target = viewHolder;
            viewHolder.civCommentNotificationAvatar = (CircleImageView) Utils.findRequiredViewAsType(view, R.id.civ_comment_notification_avatar, "field 'civCommentNotificationAvatar'", CircleImageView.class);
            viewHolder.tvCommentNotificationUsername = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_comment_notification_username, "field 'tvCommentNotificationUsername'", TextView.class);
            viewHolder.tvCommentNotificationContent = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_comment_notification_content, "field 'tvCommentNotificationContent'", TextView.class);
            viewHolder.tvCommentNotificationTime = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_comment_notification_time, "field 'tvCommentNotificationTime'", TextView.class);
        }

        @Override // butterknife.Unbinder
        public void unbind() {
            ViewHolder viewHolder = this.target;
            if (viewHolder == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            viewHolder.civCommentNotificationAvatar = null;
            viewHolder.tvCommentNotificationUsername = null;
            viewHolder.tvCommentNotificationContent = null;
            viewHolder.tvCommentNotificationTime = null;
        }
    }

    public CommentsNotificationAdapter(List<CommentNotificationResultModel.CommentsBean> list) {
        this.datas = list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.datas.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.datas.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(Base2Activity.getTopActivity()).inflate(R.layout.item_comment_notification, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CommentNotificationResultModel.CommentsBean commentsBean = (CommentNotificationResultModel.CommentsBean) getItem(i);
        CommentNotificationResultModel.CommentsBean.AuthorBean author = commentsBean.getAuthor();
        String username = author.getUsername();
        String comment_text = commentsBean.getComment_text();
        String avatar_url = author.getAvatar_url();
        String timeElapse = TimeUtils.getTimeElapse(TimeUtils.timeZoneToLocal(commentsBean.getTimestamp()));
        SpannableString spannableStringUserNameToBlueColor = StringUtils.userNameToBlueColor(comment_text);
        Glide.with(Base2Activity.getTopActivity()).load(avatar_url).into(viewHolder.civCommentNotificationAvatar);
        viewHolder.tvCommentNotificationContent.setText(spannableStringUserNameToBlueColor);
        viewHolder.tvCommentNotificationTime.setText(timeElapse);
        viewHolder.tvCommentNotificationUsername.setText(username);
        viewHolder.tvCommentNotificationContent.setOnTouchListener(new View.OnTouchListener() { // from class: im.helmsman.helmsmanandroid.adapter.CommentsNotificationAdapter.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                TextView textView = (TextView) view2;
                Spannable spannableNewSpannable = Spannable.Factory.getInstance().newSpannable(textView.getText());
                int action = motionEvent.getAction();
                if (action != 1 && action != 0) {
                    return false;
                }
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                int totalPaddingLeft = x - textView.getTotalPaddingLeft();
                int totalPaddingTop = y - textView.getTotalPaddingTop();
                int scrollX = totalPaddingLeft + textView.getScrollX();
                int scrollY = totalPaddingTop + textView.getScrollY();
                Layout layout = textView.getLayout();
                int offsetForHorizontal = layout.getOffsetForHorizontal(layout.getLineForVertical(scrollY), scrollX);
                ClickableSpan[] clickableSpanArr = (ClickableSpan[]) spannableNewSpannable.getSpans(offsetForHorizontal, offsetForHorizontal, ClickableSpan.class);
                if (clickableSpanArr.length == 0) {
                    return false;
                }
                if (action == 1) {
                    clickableSpanArr[0].onClick(textView);
                }
                return true;
            }
        });
        return view;
    }

    static class ViewHolder {

        @BindView(R.id.civ_comment_notification_avatar)
        CircleImageView civCommentNotificationAvatar;

        @BindView(R.id.tv_comment_notification_content)
        TextView tvCommentNotificationContent;

        @BindView(R.id.tv_comment_notification_time)
        TextView tvCommentNotificationTime;

        @BindView(R.id.tv_comment_notification_username)
        TextView tvCommentNotificationUsername;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
