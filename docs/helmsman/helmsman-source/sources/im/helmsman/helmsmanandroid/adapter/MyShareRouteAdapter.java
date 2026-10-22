package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.DowloadCompleteMessage;
import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.presenter.MyRoutePresenterImp;
import im.helmsman.helmsmanandroid.ui.activity.CommentContentActivity;
import im.helmsman.helmsmanandroid.ui.activity.MessageDetailActivity;
import im.helmsman.helmsmanandroid.ui.activity.SendMessageActivity;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.CoordinateParserUtil;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.TimeUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes2.dex */
public class MyShareRouteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<LikeRouteListModel.MessagesBean> messagesBeens;
    private UserMe userMe = UserMe.getUser();

    public MyShareRouteAdapter(Context context, List<LikeRouteListModel.MessagesBean> list) {
        this.mContext = context;
        this.messagesBeens = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_my_route, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).setData(this.messagesBeens.get(i));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.messagesBeens.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button btnCount;
        private RequestOptions centerCropOptions;
        private ImageView ivCollectPortrait;
        private ImageView ivCreaterPortrait;
        private ImageView ivLikeHeart;
        private ImageView ivRoute;
        private ImageView ivShare;
        private LinearLayout linearComment;
        private LinearLayout linearDownload;
        private LinearLayout linearFavourite;
        private LikeRouteListModel.MessagesBean routeBean;
        private TextView tvCollectName;
        private TextView tvCommentCount;
        private TextView tvCreaterName;
        private TextView tvDis;
        private TextView tvDownloadCount;
        private TextView tvFavouriteCount;
        private TextView tvRouteName;
        private TextView tvTime;

        public ViewHolder(View view) {
            super(view);
            this.centerCropOptions = new RequestOptions().centerCrop();
            this.ivShare = (ImageView) view.findViewById(R.id.iv_my_route_share);
            this.ivLikeHeart = (ImageView) view.findViewById(R.id.iv_myroute_heart_like);
            this.linearFavourite = (LinearLayout) view.findViewById(R.id.linear_my_following_favourite);
            this.tvCommentCount = (TextView) view.findViewById(R.id.tv_item_my_following_comment);
            this.linearComment = (LinearLayout) view.findViewById(R.id.lin_item_my_following_comment);
            this.ivCreaterPortrait = (CircleImageView) view.findViewById(R.id.cir_myroute_creatorHead);
            this.ivCollectPortrait = (CircleImageView) view.findViewById(R.id.civ_my_following_portrait);
            this.tvCreaterName = (TextView) view.findViewById(R.id.tv_myroute_creator_name);
            this.tvCollectName = (TextView) view.findViewById(R.id.tv_item_my_following_author);
            this.ivRoute = (ImageView) view.findViewById(R.id.iv_item_my_following_route);
            this.btnCount = (Button) view.findViewById(R.id.btn_item_my_following_waypoint_count);
            this.tvRouteName = (TextView) view.findViewById(R.id.tv_myroute_route_name);
            this.tvDis = (TextView) view.findViewById(R.id.tv_item_my_following_dis);
            this.tvTime = (TextView) view.findViewById(R.id.tv_item_my_folowing_time);
            this.tvFavouriteCount = (TextView) view.findViewById(R.id.tv_item_my_folowing_favourite);
            this.tvDownloadCount = (TextView) view.findViewById(R.id.tv_item_my_following_download);
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.tlinear_item_my_following_download);
            this.linearDownload = linearLayout;
            linearLayout.setOnClickListener(this);
            this.linearComment.setOnClickListener(this);
            this.linearFavourite.setOnClickListener(this);
            this.ivRoute.setOnClickListener(this);
            this.ivShare.setVisibility(8);
        }

        public void setData(LikeRouteListModel.MessagesBean messagesBean) {
            this.routeBean = messagesBean;
            LikeRouteListModel.MessagesBean.AuthorBean author = messagesBean.getAuthor();
            LikeRouteListModel.MessagesBean.RouteBean route = messagesBean.getRoute();
            String username = author.getUsername();
            String username2 = route.getCreator().getUsername();
            String route_name = route.getRoute_name();
            float distance = route.getDistance();
            int waypoint_count = route.getWaypoint_count();
            int route_favourite_count = messagesBean.getRoute_favourite_count();
            int route_download_count = messagesBean.getRoute_download_count();
            int message_comment_count = messagesBean.getMessage_comment_count();
            boolean zIsIs_favourite = route.isIs_favourite();
            String avatar_url = MyShareRouteAdapter.this.userMe.getAvatar_url();
            String avatar_url2 = route.getCreator().getAvatar_url();
            String static_image_url = route.getStatic_image_url();
            this.tvCreaterName.setText(username2);
            this.tvCollectName.setText(username);
            this.tvRouteName.setText(route_name);
            this.tvDis.setText(CoordinateParserUtil.distanceParser(distance));
            this.btnCount.setText(String.valueOf(waypoint_count));
            this.ivLikeHeart.setImageResource(zIsIs_favourite ? R.drawable.icon_redheart : R.drawable.icon_favourite_empty_heart);
            if (route_download_count == 0) {
                this.tvDownloadCount.setText(R.string.download);
            } else {
                this.tvDownloadCount.setText(route_download_count + "");
            }
            if (route_favourite_count == 0) {
                this.tvFavouriteCount.setText(R.string.Favourite);
            } else {
                this.tvFavouriteCount.setText(route_favourite_count + "");
            }
            if (message_comment_count == 0) {
                this.tvCommentCount.setText(R.string.Comment);
            } else {
                this.tvCommentCount.setText(String.valueOf(message_comment_count));
            }
            Glide.with(MyShareRouteAdapter.this.mContext).load(avatar_url).apply((BaseRequestOptions<?>) this.centerCropOptions).into(this.ivCollectPortrait);
            Glide.with(MyShareRouteAdapter.this.mContext).load(avatar_url2).apply((BaseRequestOptions<?>) this.centerCropOptions).into(this.ivCreaterPortrait);
            Glide.with(MyShareRouteAdapter.this.mContext).load(static_image_url).into(this.ivRoute);
            String timestamp = messagesBean.getTimestamp();
            if (timestamp != null) {
                this.tvTime.setText(TimeUtils.getTimeElapse(TimeUtils.timeZoneToLocal(timestamp)));
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.equals(this.ivRoute)) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("latlngs", (ArrayList) DaoUtil.getInstance().wayPointBeanToLatLng(this.routeBean.getRoute().getWaypoints()));
                Intent intent = new Intent(MyShareRouteAdapter.this.mContext, (Class<?>) MessageDetailActivity.class);
                intent.putExtra("avatarurl", this.routeBean.getAuthor().getAvatar_url());
                intent.putExtra("islike", this.routeBean.getRoute().isIs_favourite());
                intent.putExtra("description", this.routeBean.getDescription());
                intent.putExtra("userid", this.routeBean.getAuthor().getId());
                intent.putExtra("messageid", this.routeBean.getId());
                intent.putExtra("identifier", this.routeBean.getRoute().getIdentifier());
                intent.putExtra("routename", this.routeBean.getRoute().getRoute_name());
                intent.putExtra("downloadcount", this.routeBean.getRoute_download_count());
                intent.putExtra("commentcount", this.routeBean.getMessage_comment_count());
                intent.putExtra("routeid", this.routeBean.getRoute().getRoute_id());
                intent.putExtra("shareurl", this.routeBean.getSharing_url());
                intent.putExtra("mapurl", this.routeBean.getRoute().getStatic_image_url());
                intent.putExtra("waypointcount", this.routeBean.getRoute().getWaypoint_count());
                intent.putExtra("distance", this.routeBean.getRoute().getDistance());
                intent.putExtras(bundle);
                MyShareRouteAdapter.this.mContext.startActivity(intent);
                return;
            }
            if (view.equals(this.linearFavourite)) {
                int route_id = this.routeBean.getRoute().getRoute_id();
                Intent intent2 = new Intent(MyShareRouteAdapter.this.mContext, (Class<?>) SendMessageActivity.class);
                intent2.putExtra("routeid", route_id);
                intent2.putExtra("model", (short) 1);
                MyShareRouteAdapter.this.mContext.startActivity(intent2);
                return;
            }
            if (view.equals(this.linearDownload)) {
                DaoUtil.getInstance().saveRouteByWaypointsBean(null, "NO NAME", this.ivRoute, this.routeBean.getRoute().getWaypoints());
                ViewUtils.ShowToast(R.string.myfanrouteadapter_route_download_complete);
                new MyRoutePresenterImp(null, 1).downLoadRouteById(this.routeBean.getRoute().getRoute_id(), -1);
                EventBus.getDefault().post(new DowloadCompleteMessage());
                LikeRouteListModel.MessagesBean messagesBean = this.routeBean;
                messagesBean.setRoute_download_count(messagesBean.getRoute_download_count() + 1);
                this.tvDownloadCount.setText(this.routeBean.getRoute_download_count() + "");
                return;
            }
            if (view.equals(this.linearComment)) {
                int id = this.routeBean.getId();
                int route_id2 = this.routeBean.getRoute().getRoute_id();
                Intent intent3 = new Intent(MyShareRouteAdapter.this.mContext, (Class<?>) CommentContentActivity.class);
                intent3.putExtra("messageid", id);
                intent3.putExtra("routeid", route_id2);
                MyShareRouteAdapter.this.mContext.startActivity(intent3);
            }
        }
    }

    public void backgroundAlpha(float f) {
        WindowManager.LayoutParams attributes = Base2Activity.getTopActivity().getWindow().getAttributes();
        attributes.alpha = f;
        Base2Activity.getTopActivity().getWindow().setAttributes(attributes);
    }
}
