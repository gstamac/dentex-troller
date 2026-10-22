package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.DowloadCompleteMessage;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.GetFriendsMessageResultModel;
import im.helmsman.helmsmanandroid.ui.activity.CommentContentActivity;
import im.helmsman.helmsmanandroid.ui.activity.SendMessageActivity;
import im.helmsman.helmsmanandroid.utils.CoordinateParserUtil;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.TimeUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes2.dex */
public class FriendsMessageInfoAdapter extends BaseAdapter {
    public List<GetFriendsMessageResultModel.MessagesBean> datas;
    public Context mContext;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder_ViewBinding implements Unbinder {
        private ViewHolder target;
        private View view7f08014d;
        private View view7f0801fa;
        private View view7f08034a;

        public ViewHolder_ViewBinding(final ViewHolder viewHolder, View view) {
            this.target = viewHolder;
            viewHolder.ibShare = (ImageButton) Utils.findRequiredViewAsType(view, R.id.iv_my_route_share, "field 'ibShare'", ImageButton.class);
            viewHolder.civMyFollowingPortrait = (CircleImageView) Utils.findRequiredViewAsType(view, R.id.civ_my_following_portrait, "field 'civMyFollowingPortrait'", CircleImageView.class);
            viewHolder.tvItemMyFollowingAuthor = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_item_my_following_author, "field 'tvItemMyFollowingAuthor'", TextView.class);
            viewHolder.tvItemMyFolowingTime = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_item_my_folowing_time, "field 'tvItemMyFolowingTime'", TextView.class);
            viewHolder.tvItemMyFollowingDescription = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_item_my_following_description, "field 'tvItemMyFollowingDescription'", TextView.class);
            viewHolder.ivItemMyFollowingRoute = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_item_my_following_route, "field 'ivItemMyFollowingRoute'", ImageView.class);
            viewHolder.btnItemMyFollowingWaypointCount = (Button) Utils.findRequiredViewAsType(view, R.id.btn_item_my_following_waypoint_count, "field 'btnItemMyFollowingWaypointCount'", Button.class);
            viewHolder.cirMyrouteCreatorHead = (CircleImageView) Utils.findRequiredViewAsType(view, R.id.cir_myroute_creatorHead, "field 'cirMyrouteCreatorHead'", CircleImageView.class);
            viewHolder.tvMyrouteCreatorName = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_myroute_creator_name, "field 'tvMyrouteCreatorName'", TextView.class);
            viewHolder.tvMyrouteRouteName = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_myroute_route_name, "field 'tvMyrouteRouteName'", TextView.class);
            viewHolder.tvItemMyFollowingDis = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_item_my_following_dis, "field 'tvItemMyFollowingDis'", TextView.class);
            viewHolder.ivMyrouteHeartLike = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_myroute_heart_like, "field 'ivMyrouteHeartLike'", ImageView.class);
            viewHolder.tvItemMyFolowingFavourite = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_item_my_folowing_favourite, "field 'tvItemMyFolowingFavourite'", TextView.class);
            View viewFindRequiredView = Utils.findRequiredView(view, R.id.frame_item_my_following_favourite, "field 'frameItemMyFollowingFavourite' and method 'onClick'");
            viewHolder.frameItemMyFollowingFavourite = (FrameLayout) Utils.castView(viewFindRequiredView, R.id.frame_item_my_following_favourite, "field 'frameItemMyFollowingFavourite'", FrameLayout.class);
            this.view7f08014d = viewFindRequiredView;
            viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.adapter.FriendsMessageInfoAdapter.ViewHolder_ViewBinding.1
                @Override // butterknife.internal.DebouncingOnClickListener
                public void doClick(View view2) {
                    viewHolder.onClick(view2);
                }
            });
            viewHolder.tvItemMyFollowingDownload = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_item_my_following_download, "field 'tvItemMyFollowingDownload'", TextView.class);
            View viewFindRequiredView2 = Utils.findRequiredView(view, R.id.tlinear_item_my_following_download, "field 'tlinearItemMyFollowingDownload' and method 'onClick'");
            viewHolder.tlinearItemMyFollowingDownload = (LinearLayout) Utils.castView(viewFindRequiredView2, R.id.tlinear_item_my_following_download, "field 'tlinearItemMyFollowingDownload'", LinearLayout.class);
            this.view7f08034a = viewFindRequiredView2;
            viewFindRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.adapter.FriendsMessageInfoAdapter.ViewHolder_ViewBinding.2
                @Override // butterknife.internal.DebouncingOnClickListener
                public void doClick(View view2) {
                    viewHolder.onClick(view2);
                }
            });
            viewHolder.tvItemMyFollowingComment = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_item_my_following_comment, "field 'tvItemMyFollowingComment'", TextView.class);
            View viewFindRequiredView3 = Utils.findRequiredView(view, R.id.lin_item_my_following_comment, "field 'linItemMyFollowingComment' and method 'onClick'");
            viewHolder.linItemMyFollowingComment = (LinearLayout) Utils.castView(viewFindRequiredView3, R.id.lin_item_my_following_comment, "field 'linItemMyFollowingComment'", LinearLayout.class);
            this.view7f0801fa = viewFindRequiredView3;
            viewFindRequiredView3.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.adapter.FriendsMessageInfoAdapter.ViewHolder_ViewBinding.3
                @Override // butterknife.internal.DebouncingOnClickListener
                public void doClick(View view2) {
                    viewHolder.onClick(view2);
                }
            });
        }

        @Override // butterknife.Unbinder
        public void unbind() {
            ViewHolder viewHolder = this.target;
            if (viewHolder == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            viewHolder.ibShare = null;
            viewHolder.civMyFollowingPortrait = null;
            viewHolder.tvItemMyFollowingAuthor = null;
            viewHolder.tvItemMyFolowingTime = null;
            viewHolder.tvItemMyFollowingDescription = null;
            viewHolder.ivItemMyFollowingRoute = null;
            viewHolder.btnItemMyFollowingWaypointCount = null;
            viewHolder.cirMyrouteCreatorHead = null;
            viewHolder.tvMyrouteCreatorName = null;
            viewHolder.tvMyrouteRouteName = null;
            viewHolder.tvItemMyFollowingDis = null;
            viewHolder.ivMyrouteHeartLike = null;
            viewHolder.tvItemMyFolowingFavourite = null;
            viewHolder.frameItemMyFollowingFavourite = null;
            viewHolder.tvItemMyFollowingDownload = null;
            viewHolder.tlinearItemMyFollowingDownload = null;
            viewHolder.tvItemMyFollowingComment = null;
            viewHolder.linItemMyFollowingComment = null;
            this.view7f08014d.setOnClickListener(null);
            this.view7f08014d = null;
            this.view7f08034a.setOnClickListener(null);
            this.view7f08034a = null;
            this.view7f0801fa.setOnClickListener(null);
            this.view7f0801fa = null;
        }
    }

    public FriendsMessageInfoAdapter(List<GetFriendsMessageResultModel.MessagesBean> list, Context context) {
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
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_my_route, (ViewGroup) null);
            view.setTag(new ViewHolder(view));
        }
        ((ViewHolder) view.getTag()).setData(this.mContext, (GetFriendsMessageResultModel.MessagesBean) getItem(i));
        return view;
    }

    static class ViewHolder {

        @BindView(R.id.btn_item_my_following_waypoint_count)
        Button btnItemMyFollowingWaypointCount;

        @BindView(R.id.cir_myroute_creatorHead)
        CircleImageView cirMyrouteCreatorHead;

        @BindView(R.id.civ_my_following_portrait)
        CircleImageView civMyFollowingPortrait;
        private Context context;

        @BindView(R.id.frame_item_my_following_favourite)
        FrameLayout frameItemMyFollowingFavourite;

        @BindView(R.id.iv_my_route_share)
        ImageButton ibShare;
        boolean isFavourite;

        @BindView(R.id.iv_item_my_following_route)
        ImageView ivItemMyFollowingRoute;

        @BindView(R.id.iv_myroute_heart_like)
        ImageView ivMyrouteHeartLike;

        @BindView(R.id.lin_item_my_following_comment)
        LinearLayout linItemMyFollowingComment;
        private int messageId;
        private int routeId;
        private String routeName;

        @BindView(R.id.tlinear_item_my_following_download)
        LinearLayout tlinearItemMyFollowingDownload;

        @BindView(R.id.tv_item_my_following_author)
        TextView tvItemMyFollowingAuthor;

        @BindView(R.id.tv_item_my_following_comment)
        TextView tvItemMyFollowingComment;

        @BindView(R.id.tv_item_my_following_description)
        TextView tvItemMyFollowingDescription;

        @BindView(R.id.tv_item_my_following_dis)
        TextView tvItemMyFollowingDis;

        @BindView(R.id.tv_item_my_following_download)
        TextView tvItemMyFollowingDownload;

        @BindView(R.id.tv_item_my_folowing_favourite)
        TextView tvItemMyFolowingFavourite;

        @BindView(R.id.tv_item_my_folowing_time)
        TextView tvItemMyFolowingTime;

        @BindView(R.id.tv_myroute_creator_name)
        TextView tvMyrouteCreatorName;

        @BindView(R.id.tv_myroute_route_name)
        TextView tvMyrouteRouteName;
        private List<GetFriendsMessageResultModel.MessagesBean.RouteBean.WaypointsBean> waypointsBeens;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setData(Context context, GetFriendsMessageResultModel.MessagesBean messagesBean) {
            this.context = context;
            this.ibShare.setVisibility(8);
            GetFriendsMessageResultModel.MessagesBean.AuthorBean author = messagesBean.getAuthor();
            GetFriendsMessageResultModel.MessagesBean.RouteBean route = messagesBean.getRoute();
            GetFriendsMessageResultModel.MessagesBean.RouteBean.CreatorBean creator = route.getCreator();
            String avatar_url = author.getAvatar_url();
            String avatar_url2 = creator.getAvatar_url();
            String username = author.getUsername();
            String username2 = creator.getUsername();
            String timeElapse = TimeUtils.getTimeElapse(TimeUtils.timeZoneToLocal(messagesBean.getTimestamp()));
            String description = messagesBean.getDescription();
            String static_image_url = route.getStatic_image_url();
            this.isFavourite = route.isIs_favourite();
            this.routeName = route.getRoute_name();
            this.waypointsBeens = route.getWaypoints();
            float distance = route.getDistance();
            int waypoint_count = route.getWaypoint_count();
            this.messageId = messagesBean.getId();
            this.routeId = route.getRoute_id();
            int route_favourite_count = messagesBean.getRoute_favourite_count();
            int route_download_count = messagesBean.getRoute_download_count();
            int message_comment_count = messagesBean.getMessage_comment_count();
            this.tvItemMyFolowingFavourite.setText(route_favourite_count + "");
            this.tvItemMyFollowingComment.setText(message_comment_count + "");
            this.tvItemMyFollowingDownload.setText(route_download_count + "");
            Glide.with(context).load(avatar_url).into(this.civMyFollowingPortrait);
            this.tvMyrouteRouteName.setText(this.routeName);
            SpannableString spannableStringUserNameToBlueColor = StringUtils.userNameToBlueColor(description);
            this.tvItemMyFollowingDescription.setMovementMethod(LinkMovementMethod.getInstance());
            this.tvItemMyFollowingDescription.setText(spannableStringUserNameToBlueColor);
            this.tvItemMyFolowingTime.setText(timeElapse);
            Glide.with(context).load(static_image_url).into(this.ivItemMyFollowingRoute);
            this.btnItemMyFollowingWaypointCount.setText(waypoint_count + "");
            this.tvMyrouteCreatorName.setText(username2);
            this.tvItemMyFollowingDis.setText(CoordinateParserUtil.distanceParser(distance));
            this.tvItemMyFollowingAuthor.setText(username);
            if (this.isFavourite) {
                this.ivMyrouteHeartLike.setImageResource(R.drawable.icon_redheart);
            } else {
                this.ivMyrouteHeartLike.setImageResource(R.drawable.icon_favourite_empty_heart);
            }
            Glide.with(context).load(avatar_url2).into(this.cirMyrouteCreatorHead);
        }

        @OnClick({R.id.frame_item_my_following_favourite, R.id.tlinear_item_my_following_download, R.id.lin_item_my_following_comment})
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.frame_item_my_following_favourite) {
                if (this.isFavourite) {
                    ViewUtils.showSnackBar(view, R.string.yououhavecollected, true);
                    return;
                }
                Intent intent = new Intent(this.context, (Class<?>) SendMessageActivity.class);
                intent.putExtra("routeid", this.routeId);
                intent.putExtra("model", (short) 1);
                this.context.startActivity(intent);
                return;
            }
            if (id == R.id.lin_item_my_following_comment) {
                Intent intent2 = new Intent(this.context, (Class<?>) CommentContentActivity.class);
                intent2.putExtra("messageid", this.messageId);
                this.context.startActivity(intent2);
            } else {
                if (id != R.id.tlinear_item_my_following_download) {
                    return;
                }
                DaoUtil.getInstance().saveRouteByWaypointsBean(this.routeName, this.waypointsBeens);
                ViewUtils.ShowToast(R.string.myfanrouteadapter_route_download_complete);
                EventBus.getDefault().post(new DowloadCompleteMessage());
                RequestInetUtils.instance().downLoadRouteById(this.routeId, -1, null);
            }
        }
    }
}
