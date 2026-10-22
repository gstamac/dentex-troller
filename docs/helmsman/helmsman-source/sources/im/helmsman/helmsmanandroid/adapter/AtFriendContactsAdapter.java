package im.helmsman.helmsmanandroid.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.dao.ContactDao;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AtFriendContactsAdapter extends BaseAdapter {
    private List<ContactDao> datas;
    private String firstItemTitle;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder_ViewBinding implements Unbinder {
        private ViewHolder target;

        public ViewHolder_ViewBinding(ViewHolder viewHolder, View view) {
            this.target = viewHolder;
            viewHolder.tvAtfriendItemContactIndex = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_atfriend_item_contact_index, "field 'tvAtfriendItemContactIndex'", TextView.class);
            viewHolder.relAtfriendItemIndexLayout = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.rel_atfriend_item_index_layout, "field 'relAtfriendItemIndexLayout'", RelativeLayout.class);
            viewHolder.civAtfriendItemContactAvatar = (CircleImageView) Utils.findRequiredViewAsType(view, R.id.civ_atfriend_item_contact_avatar, "field 'civAtfriendItemContactAvatar'", CircleImageView.class);
            viewHolder.tvAtfriendItemContactUsername = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_atfriend_item_contact_username, "field 'tvAtfriendItemContactUsername'", TextView.class);
        }

        @Override // butterknife.Unbinder
        public void unbind() {
            ViewHolder viewHolder = this.target;
            if (viewHolder == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            viewHolder.tvAtfriendItemContactIndex = null;
            viewHolder.relAtfriendItemIndexLayout = null;
            viewHolder.civAtfriendItemContactAvatar = null;
            viewHolder.tvAtfriendItemContactUsername = null;
        }
    }

    public AtFriendContactsAdapter(List<ContactDao> list) {
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
            view = LayoutInflater.from(Base2Activity.getTopActivity()).inflate(R.layout.item_atfriend_contacts, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ContactDao contactDao = (ContactDao) getItem(i);
        String username = contactDao.getUsername();
        String avatar_url = contactDao.getAvatar_url();
        String index = contactDao.getIndex();
        if (TextUtils.isEmpty(avatar_url)) {
            avatar_url = "http://oc1s6nkuq.bkt.clouddn.com/avatar/default.png?imageView2/1/w/200/h/200/q/75/format/PNG";
        }
        if (i == 0 || (index != null && !index.equals(this.datas.get(i - 1).getIndex()))) {
            if (i == 0) {
                viewHolder.tvAtfriendItemContactIndex.setText(this.firstItemTitle);
            } else {
                viewHolder.tvAtfriendItemContactIndex.setText(index);
            }
            viewHolder.relAtfriendItemIndexLayout.setVisibility(0);
        } else {
            viewHolder.relAtfriendItemIndexLayout.setVisibility(8);
        }
        viewHolder.tvAtfriendItemContactUsername.setText(username);
        Glide.with(Base2Activity.getTopActivity()).load(avatar_url).into(viewHolder.civAtfriendItemContactAvatar);
        return view;
    }

    static class ViewHolder {

        @BindView(R.id.civ_atfriend_item_contact_avatar)
        CircleImageView civAtfriendItemContactAvatar;

        @BindView(R.id.rel_atfriend_item_index_layout)
        RelativeLayout relAtfriendItemIndexLayout;

        @BindView(R.id.tv_atfriend_item_contact_index)
        TextView tvAtfriendItemContactIndex;

        @BindView(R.id.tv_atfriend_item_contact_username)
        TextView tvAtfriendItemContactUsername;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setFirstItemTitle(String str) {
        this.firstItemTitle = str;
    }
}
