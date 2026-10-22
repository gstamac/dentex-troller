package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.bumptech.glide.Glide;
import com.tt.whorlviewlibrary.WhorlView;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.model.FindFriendsResultModel;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import org.reactivestreams.Publisher;

/* JADX INFO: loaded from: classes2.dex */
public class AddFriendsListAdapter extends BaseAdapter {
    private OnFollowOnClickListener listener;
    private Context mContext;
    private List<FindFriendsResultModel.UsersBean> usersBeanList;

    public interface OnFollowOnClickListener {
        void onClick(int i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder_ViewBinding implements Unbinder {
        private ViewHolder target;

        public ViewHolder_ViewBinding(ViewHolder viewHolder, View view) {
            this.target = viewHolder;
            viewHolder.ciFriendslistAvatar = (CircleImageView) Utils.findRequiredViewAsType(view, R.id.ci_friendslist_avatar, "field 'ciFriendslistAvatar'", CircleImageView.class);
            viewHolder.tvFriendlistName = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_friendlist_name, "field 'tvFriendlistName'", TextView.class);
            viewHolder.tvFriendlistDescription = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_friendlist_description, "field 'tvFriendlistDescription'", TextView.class);
            viewHolder.tvFriendactivityFollowerCount = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_friendactivity_followerCount, "field 'tvFriendactivityFollowerCount'", TextView.class);
            viewHolder.btnFriendsactivityFollow = (ImageView) Utils.findRequiredViewAsType(view, R.id.btn_friendsactivity_follow, "field 'btnFriendsactivityFollow'", ImageView.class);
            viewHolder.linearItemviewLayout = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.linear_itemview_layout, "field 'linearItemviewLayout'", RelativeLayout.class);
            viewHolder.progressBar = (WhorlView) Utils.findRequiredViewAsType(view, R.id.progress_friendsactivity_follow, "field 'progressBar'", WhorlView.class);
        }

        @Override // butterknife.Unbinder
        public void unbind() {
            ViewHolder viewHolder = this.target;
            if (viewHolder == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            viewHolder.ciFriendslistAvatar = null;
            viewHolder.tvFriendlistName = null;
            viewHolder.tvFriendlistDescription = null;
            viewHolder.tvFriendactivityFollowerCount = null;
            viewHolder.btnFriendsactivityFollow = null;
            viewHolder.linearItemviewLayout = null;
            viewHolder.progressBar = null;
        }
    }

    public AddFriendsListAdapter(Context context, List<FindFriendsResultModel.UsersBean> list) {
        this.mContext = context;
        this.usersBeanList = list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.usersBeanList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.usersBeanList.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_friends_list, (ViewGroup) null);
            view.setTag(new ViewHolder(view));
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        FindFriendsResultModel.UsersBean usersBean = (FindFriendsResultModel.UsersBean) getItem(i);
        String avatar_url = usersBean.getAvatar_url();
        String username = usersBean.getUsername();
        String about_me = usersBean.getAbout_me();
        String str = usersBean.getFollowers_count() + "";
        boolean zIsIs_following = usersBean.isIs_following();
        boolean zIsloading = usersBean.isloading();
        Glide.with(this.mContext).load(avatar_url).into(viewHolder.ciFriendslistAvatar);
        viewHolder.tvFriendlistName.setText(username);
        viewHolder.tvFriendlistDescription.setText(about_me);
        viewHolder.tvFriendactivityFollowerCount.setText("Follower " + str);
        viewHolder.btnFriendsactivityFollow.setImageResource(zIsIs_following ? R.drawable.icon_following : R.drawable.icon_addfriend);
        viewHolder.btnFriendsactivityFollow.setEnabled(!zIsIs_following);
        viewHolder.setOnFollowBtnClickListener(i);
        if (zIsloading) {
            viewHolder.btnFriendsactivityFollow.setVisibility(8);
            viewHolder.progressBar.setVisibility(0);
            if (!viewHolder.progressBar.isCircling()) {
                viewHolder.progressBar.start();
            }
        } else {
            viewHolder.btnFriendsactivityFollow.setVisibility(0);
            viewHolder.progressBar.setVisibility(8);
            if (viewHolder.progressBar.isCircling()) {
                viewHolder.progressBar.stop();
            }
        }
        return view;
    }

    public void setOnFollowClickListener(OnFollowOnClickListener onFollowOnClickListener) {
        this.listener = onFollowOnClickListener;
    }

    public void changeFollowBtnState(final int i, final boolean z) {
        Log.e("changeFollowBtnState4", this.usersBeanList.size() + "");
        Flowable.just(this.usersBeanList).flatMap(new Function<List<FindFriendsResultModel.UsersBean>, Publisher<FindFriendsResultModel.UsersBean>>() { // from class: im.helmsman.helmsmanandroid.adapter.AddFriendsListAdapter.4
            @Override // io.reactivex.functions.Function
            public Publisher<FindFriendsResultModel.UsersBean> apply(List<FindFriendsResultModel.UsersBean> list) throws Exception {
                return Flowable.fromIterable(list);
            }
        }).filter(new Predicate<FindFriendsResultModel.UsersBean>() { // from class: im.helmsman.helmsmanandroid.adapter.AddFriendsListAdapter.3
            @Override // io.reactivex.functions.Predicate
            public boolean test(FindFriendsResultModel.UsersBean usersBean) throws Exception {
                return usersBean.getId() == i;
            }
        }).flatMap(new Function<FindFriendsResultModel.UsersBean, Publisher<FindFriendsResultModel.UsersBean>>() { // from class: im.helmsman.helmsmanandroid.adapter.AddFriendsListAdapter.2
            @Override // io.reactivex.functions.Function
            public Publisher<FindFriendsResultModel.UsersBean> apply(FindFriendsResultModel.UsersBean usersBean) throws Exception {
                usersBean.setIsloading(z);
                if (!z) {
                    usersBean.setIs_following(true);
                }
                return Flowable.just(usersBean);
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<FindFriendsResultModel.UsersBean>() { // from class: im.helmsman.helmsmanandroid.adapter.AddFriendsListAdapter.1
            @Override // io.reactivex.functions.Consumer
            public void accept(FindFriendsResultModel.UsersBean usersBean) throws Exception {
                if (usersBean == null) {
                    return;
                }
                AddFriendsListAdapter.this.notifyDataSetChanged();
                Log.e("changeFollowBtnState5", AddFriendsListAdapter.this.usersBeanList.size() + "");
            }
        });
    }

    class ViewHolder implements View.OnClickListener {

        @BindView(R.id.btn_friendsactivity_follow)
        ImageView btnFriendsactivityFollow;

        @BindView(R.id.ci_friendslist_avatar)
        CircleImageView ciFriendslistAvatar;

        @BindView(R.id.linear_itemview_layout)
        RelativeLayout linearItemviewLayout;
        private int position = -1;

        @BindView(R.id.progress_friendsactivity_follow)
        WhorlView progressBar;

        @BindView(R.id.tv_friendactivity_followerCount)
        TextView tvFriendactivityFollowerCount;

        @BindView(R.id.tv_friendlist_description)
        TextView tvFriendlistDescription;

        @BindView(R.id.tv_friendlist_name)
        TextView tvFriendlistName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            this.btnFriendsactivityFollow.setOnClickListener(this);
        }

        public void setOnFollowBtnClickListener(int i) {
            this.position = i;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (this.position == -1 || AddFriendsListAdapter.this.listener == null) {
                return;
            }
            AddFriendsListAdapter.this.listener.onClick(((FindFriendsResultModel.UsersBean) AddFriendsListAdapter.this.getItem(this.position)).getId());
        }
    }
}
