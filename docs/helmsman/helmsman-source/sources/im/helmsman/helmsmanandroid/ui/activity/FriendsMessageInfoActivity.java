package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.FriendsMessageInfoAdapter;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.DowloadCompleteMessage;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.LikeRouteSucessMessage;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.SendCommentSuccessMessage;
import im.helmsman.helmsmanandroid.inet.model.GetFriendsMessageResultModel;
import im.helmsman.helmsmanandroid.presenter.FriendMessageInfoPersenterImp;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;

/* JADX INFO: loaded from: classes2.dex */
public class FriendsMessageInfoActivity extends Mvp2BaseActivity<FriendsMessageInfoView, FriendMessageInfoPersenterImp> implements AdapterView.OnItemClickListener, View.OnClickListener, FriendsMessageInfoView {
    private FriendsMessageInfoAdapter adapter;
    private String avatarUrl;
    private CircleImageView civAvatar;
    private List<GetFriendsMessageResultModel.MessagesBean> datas = new ArrayList();
    private boolean isFollow = false;
    private ImageView ivFollow;
    private ListView lvMessageList;
    private ProgressBar pbloding;
    private PtrClassicFrameLayout pcfRefresh;
    private TextView tvContent;
    private TextView tvFollower;
    private TextView tvFollowing;
    private TextView tvName;
    private int userId;
    private String userName;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_friends_message_info);
        setStatusBar(R.color.red_state);
        registerEventBus();
        Intent intent = getIntent();
        this.userName = intent.getStringExtra("username");
        this.userId = intent.getIntExtra("userId", 0);
        getDataByInet();
        initView();
        initEvent();
        this.pbloding.setVisibility(0);
    }

    private void getDataByInet() {
        if (this.userName != null) {
            ((FriendMessageInfoPersenterImp) this.presenter).getUserIdByUserName(this.userName);
        } else {
            ((FriendMessageInfoPersenterImp) this.presenter).getFriendsMessageInfo(this.userId);
            ((FriendMessageInfoPersenterImp) this.presenter).getUserInfoById(this.userId);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public FriendMessageInfoPersenterImp initPresenter() {
        return new FriendMessageInfoPersenterImp();
    }

    private void initView() {
        this.pcfRefresh = (PtrClassicFrameLayout) findViewById(R.id.ptr_friendsmessageinfo_refresh);
        this.lvMessageList = (ListView) findViewById(R.id.lv_friendmessageinfo_messagelist);
        this.lvMessageList = (ListView) findViewById(R.id.lv_friendmessageinfo_messagelist);
        FriendsMessageInfoAdapter friendsMessageInfoAdapter = new FriendsMessageInfoAdapter(this.datas, this);
        this.adapter = friendsMessageInfoAdapter;
        this.lvMessageList.setAdapter((ListAdapter) friendsMessageInfoAdapter);
        View viewInflate = LayoutInflater.from(this).inflate(R.layout.item_friends_message_info_headerview, (ViewGroup) null);
        this.pbloding = (ProgressBar) viewInflate.findViewById(R.id.pb_friendsmessageinfo_progress);
        this.ivFollow = (ImageView) viewInflate.findViewById(R.id.iv_friendsmessageinfo_follow);
        this.lvMessageList.addHeaderView(viewInflate);
        this.tvFollower = (TextView) findViewById(R.id.tv_friendmessageinfo_follower);
        this.tvFollowing = (TextView) findViewById(R.id.tv_friendmessageinfo_follow);
        this.civAvatar = (CircleImageView) findViewById(R.id.civ_friendsmessageinfo_avatar);
        this.tvName = (TextView) findViewById(R.id.tv_friendsmessageinfo_name);
        this.tvContent = (TextView) findViewById(R.id.tv_friendsmessageinfo_content);
        this.tvName.setText(this.userName);
    }

    private void initEvent() {
        this.ivFollow.setOnClickListener(this);
        this.pcfRefresh.setPtrHandler(new OnRefreshListener());
        this.pcfRefresh.setOnLoadMoreListener(new LoadMoreListener());
        this.tvFollower.setOnClickListener(this);
        this.tvFollowing.setOnClickListener(this);
        this.civAvatar.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.civAvatar)) {
            if (this.avatarUrl == null) {
                return;
            }
            Intent intent = new Intent(this, (Class<?>) AvatarActivity.class);
            intent.putExtra("avatarurl", this.avatarUrl);
            startActivity(intent);
            return;
        }
        if (view.equals(this.ivFollow)) {
            this.pbloding.setVisibility(0);
            if (this.isFollow) {
                ((FriendMessageInfoPersenterImp) this.presenter).cancelFollowUserById(this.userId);
                return;
            } else {
                ((FriendMessageInfoPersenterImp) this.presenter).followFriend(this.userId);
                return;
            }
        }
        if (view.equals(this.tvFollowing)) {
            Intent intent2 = new Intent(this, (Class<?>) FriendsActivity.class);
            intent2.putExtra("index", 1);
            intent2.putExtra("userid", this.userId);
            startActivity(intent2);
            return;
        }
        if (view.equals(this.tvFollower)) {
            Intent intent3 = new Intent(this, (Class<?>) FriendsActivity.class);
            intent3.putExtra("index", 0);
            intent3.putExtra("userid", this.userId);
            startActivity(intent3);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void setFollowerCount(int i) {
        this.tvFollower.setText(i + "\n\n" + getString(R.string.friendsactivity_Follower));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void setFollowingCount(int i) {
        this.tvFollowing.setText(i + "\n\n" + getString(R.string.friendsactivity_following));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void setContent(String str) {
        this.tvContent.setText(str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void setAvatar(String str) {
        this.avatarUrl = str;
        Glide.with((FragmentActivity) this).load(str).into(this.civAvatar);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void showNoMoreMessageMessage() {
        ViewUtils.ShowToast(getString(R.string.no_message));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void loadDataFailed(String str) {
        ViewUtils.ShowToast(getString(R.string.loaddatafailed) + str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void refreshListView(List<GetFriendsMessageResultModel.MessagesBean> list) {
        this.datas.clear();
        this.datas.addAll(list);
        this.adapter.notifyDataSetChanged();
        this.lvMessageList.setOnItemClickListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void addDataTolistView(List<GetFriendsMessageResultModel.MessagesBean> list) {
        this.datas.addAll(list);
        this.adapter.notifyDataSetChanged();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void disableLoadMore() {
        this.pcfRefresh.setLoadMoreEnable(false);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void enableLoadMore() {
        this.pcfRefresh.setLoadMoreEnable(true);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void loadmoreComplete() {
        this.pcfRefresh.loadMoreComplete(true);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void refreshComplete() {
        this.pcfRefresh.refreshComplete();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void setFriendName(String str) {
        this.tvName.setText(str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void hideProgressBar() {
        this.pbloding.setVisibility(8);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void hideFollowTextView() {
        this.ivFollow.setVisibility(8);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void showFollowTextView() {
        this.ivFollow.setVisibility(0);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void changeFollowTextViewState(String str, boolean z) {
        this.isFollow = z;
        if (z) {
            this.ivFollow.setImageResource(R.drawable.icon_userinfo_unfollow);
        } else {
            this.ivFollow.setImageResource(R.drawable.icon_userinfo_follow);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void showFollowFailedMessage(String str) {
        ViewUtils.ShowToast(getString(R.string.followfailed) + str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void showCacncelFollowUserFailedMessage(String str) {
        ViewUtils.ShowToast(getString(R.string.cancelfollowfailed));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView
    public void getUserId(int i) {
        this.userId = i;
        ((FriendMessageInfoPersenterImp) this.presenter).getFriendsMessageInfo(i);
        ((FriendMessageInfoPersenterImp) this.presenter).getUserInfoById(i);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (i == 0) {
            return;
        }
        int i2 = i - 1;
        Log.e("onItemClick", i2 + "");
        Bundle bundle = new Bundle();
        GetFriendsMessageResultModel.MessagesBean messagesBean = this.datas.get(i2);
        String avatar_url = messagesBean.getAuthor().getAvatar_url();
        boolean zIsIs_favourite = messagesBean.getRoute().isIs_favourite();
        String description = messagesBean.getDescription();
        int id = messagesBean.getAuthor().getId();
        int id2 = messagesBean.getId();
        String identifier = messagesBean.getRoute().getIdentifier();
        String route_name = messagesBean.getRoute().getRoute_name();
        int route_download_count = messagesBean.getRoute_download_count();
        int message_comment_count = messagesBean.getMessage_comment_count();
        int route_id = messagesBean.getRoute().getRoute_id();
        String static_image_url = messagesBean.getRoute().getStatic_image_url();
        int waypoint_count = messagesBean.getRoute().getWaypoint_count();
        float distance = messagesBean.getRoute().getDistance();
        List<GetFriendsMessageResultModel.MessagesBean.RouteBean.WaypointsBean> waypoints = messagesBean.getRoute().getWaypoints();
        ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
        Iterator<GetFriendsMessageResultModel.MessagesBean.RouteBean.WaypointsBean> it = waypoints.iterator();
        while (it.hasNext()) {
            GetFriendsMessageResultModel.MessagesBean.RouteBean.WaypointsBean next = it.next();
            arrayList.add(new MLatLng(next.getLatitude(), next.getLongitude()));
            it = it;
            waypoint_count = waypoint_count;
            static_image_url = static_image_url;
        }
        bundle.putParcelableArrayList("latlngs", arrayList);
        Intent intent = new Intent(this, (Class<?>) MessageDetailActivity.class);
        intent.putExtra("avatarurl", avatar_url);
        intent.putExtra("islike", zIsIs_favourite);
        intent.putExtra("description", description);
        intent.putExtra("userid", id);
        intent.putExtra("messageid", id2);
        intent.putExtra("identifier", identifier);
        intent.putExtra("routename", route_name);
        intent.putExtra("downloadcount", route_download_count);
        intent.putExtra("commentcount", message_comment_count);
        intent.putExtra("routeid", route_id);
        intent.putExtra("mapurl", static_image_url);
        intent.putExtra("waypointcount", waypoint_count);
        intent.putExtra("distance", distance);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    class OnRefreshListener extends PtrDefaultHandler {
        OnRefreshListener() {
        }

        @Override // com.chanven.lib.cptr.PtrHandler
        public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
            ((FriendMessageInfoPersenterImp) FriendsMessageInfoActivity.this.presenter).getFriendsMessageInfo(FriendsMessageInfoActivity.this.userId);
        }
    }

    class LoadMoreListener implements OnLoadMoreListener {
        LoadMoreListener() {
        }

        @Override // com.chanven.lib.cptr.loadmore.OnLoadMoreListener
        public void loadMore() {
            ((FriendMessageInfoPersenterImp) FriendsMessageInfoActivity.this.presenter).loadMoreFriendMessageInfo(FriendsMessageInfoActivity.this.userId);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(SendCommentSuccessMessage sendCommentSuccessMessage) {
        this.datas.clear();
        getDataByInet();
    }

    @Subscribe
    public void onEventMainThread(DowloadCompleteMessage dowloadCompleteMessage) {
        this.datas.clear();
        getDataByInet();
    }

    @Subscribe
    public void onEventMainThread(LikeRouteSucessMessage likeRouteSucessMessage) {
        this.datas.clear();
        getDataByInet();
    }
}
