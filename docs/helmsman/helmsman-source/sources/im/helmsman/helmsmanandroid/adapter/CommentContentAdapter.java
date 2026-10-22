package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
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
import im.helmsman.helmsmanandroid.inet.model.CommentListModel;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.TimeUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CommentContentAdapter extends BaseAdapter {
    private List<CommentListModel.CommentsBean> datas;
    private Context mContext;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder_ViewBinding implements Unbinder {
        private ViewHolder target;

        public ViewHolder_ViewBinding(ViewHolder viewHolder, View view) {
            this.target = viewHolder;
            viewHolder.circleCommentlist = (CircleImageView) Utils.findRequiredViewAsType(view, R.id.circle_commentlist, "field 'circleCommentlist'", CircleImageView.class);
            viewHolder.tvCommentlistName = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_commentlist_name, "field 'tvCommentlistName'", TextView.class);
            viewHolder.tvCommentlistTime = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_commentlist_time, "field 'tvCommentlistTime'", TextView.class);
            viewHolder.tvCommentlistComment = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_commentlist_comment, "field 'tvCommentlistComment'", TextView.class);
        }

        @Override // butterknife.Unbinder
        public void unbind() {
            ViewHolder viewHolder = this.target;
            if (viewHolder == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            viewHolder.circleCommentlist = null;
            viewHolder.tvCommentlistName = null;
            viewHolder.tvCommentlistTime = null;
            viewHolder.tvCommentlistComment = null;
        }
    }

    public CommentContentAdapter(Context context, List<CommentListModel.CommentsBean> list) {
        this.datas = list;
        this.mContext = context;
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
            view = LayoutInflater.from(Base2Activity.getTopActivity()).inflate(R.layout.item_comment, (ViewGroup) null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CommentListModel.CommentsBean commentsBean = (CommentListModel.CommentsBean) getItem(i);
        CommentListModel.CommentsBean.AuthorBean author = commentsBean.getAuthor();
        String avatar_url = author.getAvatar_url();
        String username = author.getUsername();
        String comment_text = commentsBean.getComment_text();
        String timeElapse = TimeUtils.getTimeElapse(TimeUtils.timeZoneToLocal(commentsBean.getTimestamp()));
        Glide.with(this.mContext).load(avatar_url).into(viewHolder.circleCommentlist);
        viewHolder.tvCommentlistComment.setText(StringUtils.userNameToBlueColor(comment_text));
        viewHolder.tvCommentlistComment.setOnTouchListener(new View.OnTouchListener() { // from class: im.helmsman.helmsmanandroid.adapter.CommentContentAdapter.1
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
        viewHolder.tvCommentlistName.setText(username);
        viewHolder.tvCommentlistTime.setText(timeElapse);
        return view;
    }

    static class ViewHolder {

        @BindView(R.id.circle_commentlist)
        CircleImageView circleCommentlist;

        @BindView(R.id.tv_commentlist_comment)
        TextView tvCommentlistComment;

        @BindView(R.id.tv_commentlist_name)
        TextView tvCommentlistName;

        @BindView(R.id.tv_commentlist_time)
        TextView tvCommentlistTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
