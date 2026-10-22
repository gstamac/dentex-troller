package im.helmsman.helmsmanandroid.adapter;

import android.view.LayoutInflater;
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
import im.helmsman.helmsmanandroid.inet.model.FavouriteNotifitcationMessageListResultModel;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.TimeUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class FavouriteMessageListAdapter extends BaseAdapter {
    private List<FavouriteNotifitcationMessageListResultModel.MessagesBean> datas;

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

    public FavouriteMessageListAdapter(List<FavouriteNotifitcationMessageListResultModel.MessagesBean> list) {
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
            view = LayoutInflater.from(Base2Activity.getTopActivity()).inflate(R.layout.item_comment, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        FavouriteNotifitcationMessageListResultModel.MessagesBean messagesBean = (FavouriteNotifitcationMessageListResultModel.MessagesBean) getItem(i);
        FavouriteNotifitcationMessageListResultModel.MessagesBean.AuthorBean author = messagesBean.getAuthor();
        String avatar_url = author.getAvatar_url();
        String username = author.getUsername();
        String timeElapse = TimeUtils.getTimeElapse(TimeUtils.timeZoneToLocal(messagesBean.getTimestamp()));
        viewHolder.tvCommentlistComment.setText(R.string.favourite_notifitcation_hint);
        viewHolder.tvCommentlistTime.setText(timeElapse);
        viewHolder.tvCommentlistName.setText(username);
        Glide.with(Base2Activity.getTopActivity()).load(avatar_url).into(viewHolder.circleCommentlist);
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
