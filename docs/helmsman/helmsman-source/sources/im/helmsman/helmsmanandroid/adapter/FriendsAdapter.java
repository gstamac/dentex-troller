package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.model.GetMyFollowerResultModel;
import im.helmsman.helmsmanandroid.presenter.FriendsPersenterImp;
import im.helmsman.helmsmanandroid.ui.activity.FriendsMessageInfoActivity;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TAG_FOLLOWER = 0;
    public static final int TAG_FOLLOWING = 1;
    private List<GetMyFollowerResultModel.UsersBean> datas;
    private Context mContext;
    private FriendsPersenterImp persenter;
    private int tag;

    public FriendsAdapter(Context context, List<GetMyFollowerResultModel.UsersBean> list, FriendsPersenterImp friendsPersenterImp, int i) {
        this.mContext = context;
        this.datas = list;
        this.persenter = friendsPersenterImp;
        this.tag = i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_friends_list, (ViewGroup) null));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).setData(this.datas.get(i));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        public CircleImageView civAvata;
        public ImageView mBtnFollow;
        public GetMyFollowerResultModel.UsersBean mUsersBean;
        public TextView tvDescription;
        public TextView tvFollowerCount;
        public TextView tvUserName;
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.tvDescription = (TextView) view.findViewById(R.id.tv_friendlist_description);
            this.tvFollowerCount = (TextView) view.findViewById(R.id.tv_friendactivity_followerCount);
            this.tvUserName = (TextView) view.findViewById(R.id.tv_friendlist_name);
            this.civAvata = (CircleImageView) view.findViewById(R.id.ci_friendslist_avatar);
            this.mBtnFollow = (ImageView) view.findViewById(R.id.btn_friendsactivity_follow);
            this.view = view.findViewById(R.id.linear_itemview_layout);
            this.mBtnFollow.setOnClickListener(this);
            this.view.setOnClickListener(this);
            this.civAvata.setOnClickListener(this);
            view.setOnTouchListener(this);
        }

        public void setData(GetMyFollowerResultModel.UsersBean usersBean) {
            this.mUsersBean = usersBean;
            GetMyFollowerResultModel.UsersBean.UserBean user = usersBean.getUser();
            String about_me = user.getAbout_me();
            String username = user.getUsername();
            int followers_count = user.getFollowers_count();
            String avatar_url = user.getAvatar_url();
            boolean zIsIs_following = user.isIs_following();
            boolean zIsIs_followed_by = user.isIs_followed_by();
            this.tvDescription.setText(about_me);
            this.tvUserName.setText(username);
            this.tvFollowerCount.setText("Follower " + followers_count);
            Glide.with(FriendsAdapter.this.mContext).load(avatar_url).into(this.civAvata);
            int i = FriendsAdapter.this.tag;
            int i2 = R.drawable.icon_following;
            if (i == 0) {
                ImageView imageView = this.mBtnFollow;
                if (!zIsIs_following) {
                    i2 = R.drawable.icon_addfriend;
                }
                imageView.setImageResource(i2);
                this.mBtnFollow.setEnabled(!zIsIs_following);
                return;
            }
            if (i != 1) {
                return;
            }
            ImageView imageView2 = this.mBtnFollow;
            if (zIsIs_followed_by) {
                i2 = R.drawable.icon_each_others_fan;
            }
            imageView2.setImageResource(i2);
            this.mBtnFollow.setEnabled(false);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.equals(this.mBtnFollow)) {
                if (FriendsAdapter.this.tag == 0) {
                    FriendsAdapter.this.persenter.followerUserById(this.mUsersBean.getUser().getId());
                    return;
                }
                return;
            }
            if (view.equals(this.civAvata)) {
                Intent intent = new Intent(FriendsAdapter.this.mContext, (Class<?>) FriendsMessageInfoActivity.class);
                intent.putExtra("userId", this.mUsersBean.getUser().getId());
                FriendsAdapter.this.mContext.startActivity(intent);
            }
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (1 != motionEvent.getAction()) {
                return false;
            }
            Intent intent = new Intent(FriendsAdapter.this.mContext, (Class<?>) FriendsMessageInfoActivity.class);
            intent.putExtra("userId", this.mUsersBean.getUser().getId());
            FriendsAdapter.this.mContext.startActivity(intent);
            return true;
        }
    }
}
