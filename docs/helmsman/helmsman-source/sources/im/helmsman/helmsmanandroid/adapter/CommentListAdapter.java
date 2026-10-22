package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.model.CommentListModel;
import im.helmsman.helmsmanandroid.utils.TimeUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CommentListModel.CommentsBean> datas;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClickListener(CommentListModel.CommentsBean commentsBean);
    }

    public CommentListAdapter(Context context, List<CommentListModel.CommentsBean> list) {
        this.datas = list;
        this.context = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_comment, (ViewGroup) null));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).setData(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        CircleImageView ciAvatar;
        private CommentListModel.CommentsBean commentsBean;
        TextView tvCommentText;
        TextView tvName;
        TextView tvTime;

        public ViewHolder(View view) {
            super(view);
            view.setOnTouchListener(this);
            this.tvName = (TextView) view.findViewById(R.id.tv_commentlist_name);
            this.tvTime = (TextView) view.findViewById(R.id.tv_commentlist_time);
            this.ciAvatar = (CircleImageView) view.findViewById(R.id.circle_commentlist);
            TextView textView = (TextView) view.findViewById(R.id.tv_commentlist_comment);
            this.tvCommentText = textView;
            textView.setOnClickListener(this);
            this.ciAvatar.setOnClickListener(this);
            this.tvName.setOnClickListener(this);
            this.tvTime.setOnClickListener(this);
        }

        public void setData(int i) {
            CommentListModel.CommentsBean commentsBean = (CommentListModel.CommentsBean) CommentListAdapter.this.datas.get(i);
            this.commentsBean = commentsBean;
            CommentListModel.CommentsBean.AuthorBean author = commentsBean.getAuthor();
            String username = author.getUsername();
            String avatar_url = author.getAvatar_url();
            String timeElapse = TimeUtils.getTimeElapse(TimeUtils.timeZoneToLocal(this.commentsBean.getTimestamp()));
            String comment_text = this.commentsBean.getComment_text();
            this.tvName.setText(username);
            Glide.with(CommentListAdapter.this.context).load(avatar_url).into(this.ciAvatar);
            this.tvTime.setText(timeElapse);
            this.tvCommentText.setText(comment_text);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (CommentListAdapter.this.listener != null) {
                CommentListAdapter.this.listener.onItemClickListener(this.commentsBean);
            }
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (CommentListAdapter.this.listener != null && motionEvent.getAction() == 1) {
                CommentListAdapter.this.listener.onItemClickListener(this.commentsBean);
            }
            return true;
        }
    }

    public void setOnItemClickListener(onItemClickListener onitemclicklistener) {
        this.listener = onitemclicklistener;
    }
}
