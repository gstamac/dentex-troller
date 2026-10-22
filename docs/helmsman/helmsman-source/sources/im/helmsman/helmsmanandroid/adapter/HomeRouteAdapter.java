package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.ui.activity.CommentContentActivity;
import im.helmsman.helmsmanandroid.ui.activity.FriendsMessageInfoActivity;
import im.helmsman.helmsmanandroid.ui.activity.MessageDetailActivity;
import im.helmsman.helmsmanandroid.ui.activity.SendMessageActivity;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.view.MyRouteView;
import im.helmsman.helmsmanandroid.utils.CoordinateParserUtil;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.ShareUtils;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.TimeUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class HomeRouteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<LikeRouteListModel.MessagesBean> mDatas;
    private MyRouteView mRouteView;

    /* JADX WARN: Multi-variable type inference failed */
    public HomeRouteAdapter(MyRouteView myRouteView, List<LikeRouteListModel.MessagesBean> list) {
        this.mRouteView = myRouteView;
        this.mContext = ((Fragment) myRouteView).getActivity();
        this.mDatas = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_my_route, (ViewGroup) null));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).setData(this.mDatas.get(i), i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int authorId;
        private String circleHeadUrl;
        private int commentCount;
        private String creatorHeadUrl;
        private int creatorId;
        private String creatorName;
        private String description;
        private float distance;
        private int downloadCount;
        private String identifier;
        private boolean isFavourite;
        private boolean isLike;
        private ImageView ivFaceBookShare;
        private ImageButton ivShare;
        private ImageView ivWeixinShare;
        private ImageView ivWexinShareTimeLine;
        private int likeCount;
        private Button mBtnWayPointCount;
        private CircleImageView mCircleCreatorHead;
        private CircleImageView mCircleHead;
        private FrameLayout mFramLike;
        private ImageView mIvLikeHeart;
        private ImageView mIvMap;
        private LinearLayout mLinComment;
        private LinearLayout mLinDownload;
        private TextView mTvCommentCount;
        private TextView mTvCreatorName;
        private TextView mTvDescription;
        private TextView mTvDistance;
        private TextView mTvDownloadCount;
        private TextView mTvLikeCount;
        private TextView mTvRouteName;
        private TextView mTvTime;
        private TextView mTvUserName;
        private Bitmap mapBitmap;
        private String mapUrl;
        private int messageId;
        private int messageType;
        private int position;
        private int routeId;
        private String routeName;
        private String shareUrl;
        private String time;
        private String userName;
        private int wayPointCount;
        private List<LikeRouteListModel.MessagesBean.RouteBean.WaypointsBean> waypointsBeanList;

        public ViewHolder(View view) {
            super(view);
            this.isFavourite = false;
            this.ivShare = (ImageButton) view.findViewById(R.id.iv_my_route_share);
            this.mTvRouteName = (TextView) view.findViewById(R.id.tv_myroute_route_name);
            this.mIvLikeHeart = (ImageView) view.findViewById(R.id.iv_myroute_heart_like);
            this.mTvDescription = (TextView) view.findViewById(R.id.tv_item_my_following_description);
            this.mTvTime = (TextView) view.findViewById(R.id.tv_item_my_folowing_time);
            this.mCircleHead = (CircleImageView) view.findViewById(R.id.civ_my_following_portrait);
            this.mTvUserName = (TextView) view.findViewById(R.id.tv_item_my_following_author);
            this.mIvMap = (ImageView) view.findViewById(R.id.iv_item_my_following_route);
            this.mBtnWayPointCount = (Button) view.findViewById(R.id.btn_item_my_following_waypoint_count);
            this.mCircleCreatorHead = (CircleImageView) view.findViewById(R.id.cir_myroute_creatorHead);
            this.mTvCreatorName = (TextView) view.findViewById(R.id.tv_myroute_creator_name);
            this.mLinDownload = (LinearLayout) view.findViewById(R.id.tlinear_item_my_following_download);
            this.mLinComment = (LinearLayout) view.findViewById(R.id.lin_item_my_following_comment);
            this.mFramLike = (FrameLayout) view.findViewById(R.id.frame_item_my_following_favourite);
            this.mTvCommentCount = (TextView) view.findViewById(R.id.tv_item_my_following_comment);
            this.mTvDownloadCount = (TextView) view.findViewById(R.id.tv_item_my_following_download);
            this.mTvLikeCount = (TextView) view.findViewById(R.id.tv_item_my_folowing_favourite);
            this.mTvDistance = (TextView) view.findViewById(R.id.tv_item_my_following_dis);
            this.mLinDownload.setOnClickListener(this);
            this.mFramLike.setOnClickListener(this);
            this.mLinComment.setOnClickListener(this);
            this.mCircleHead.setOnClickListener(this);
            this.mIvMap.setOnClickListener(this);
            this.mCircleCreatorHead.setOnClickListener(this);
            this.ivShare.setOnClickListener(this);
        }

        /* JADX WARN: Type inference failed for: r6v34, types: [im.helmsman.helmsmanandroid.adapter.HomeRouteAdapter$ViewHolder$1] */
        public void setData(LikeRouteListModel.MessagesBean messagesBean, int i) {
            this.position = i;
            LikeRouteListModel.MessagesBean.RouteBean route = messagesBean.getRoute();
            LikeRouteListModel.MessagesBean.AuthorBean author = messagesBean.getAuthor();
            LikeRouteListModel.MessagesBean.RouteBean.CreatorBean creator = route.getCreator();
            this.routeId = route.getRoute_id();
            Log.e("HomeRouteid", this.routeId + "");
            this.routeName = route.getRoute_name();
            this.circleHeadUrl = author.getAvatar_url();
            this.userName = author.getUsername();
            this.mapUrl = route.getStatic_image_url();
            this.wayPointCount = route.getWaypoint_count();
            this.creatorHeadUrl = creator.getAvatar_url();
            this.creatorName = creator.getUsername();
            this.distance = route.getDistance();
            this.commentCount = messagesBean.getMessage_comment_count();
            this.downloadCount = messagesBean.getRoute_download_count();
            this.likeCount = messagesBean.getRoute_favourite_count();
            this.messageType = messagesBean.getType();
            this.isFavourite = route.isIs_favourite();
            this.time = TimeUtils.getTimeElapse(TimeUtils.timeZoneToLocal(messagesBean.getTimestamp()));
            this.description = messagesBean.getDescription();
            this.messageId = messagesBean.getId();
            this.identifier = route.getIdentifier();
            this.waypointsBeanList = route.getWaypoints();
            this.routeName = route.getRoute_name();
            this.authorId = author.getId();
            this.isLike = route.isIs_favourite();
            this.creatorId = creator.getId();
            this.shareUrl = messagesBean.getSharing_url();
            this.mTvRouteName.setText(this.routeName);
            Glide.with(HomeRouteAdapter.this.mContext).load(this.circleHeadUrl).into(this.mCircleHead);
            Log.e("circleHeadUrl", creator.getAvatar_url());
            this.mTvUserName.setText(this.userName);
            Glide.with(HomeRouteAdapter.this.mContext).load(this.mapUrl).into(this.mIvMap);
            this.mBtnWayPointCount.setText(this.wayPointCount + "");
            Glide.with(HomeRouteAdapter.this.mContext).load(this.creatorHeadUrl).into(this.mCircleCreatorHead);
            this.mTvCreatorName.setText(this.creatorName);
            this.mTvDistance.setText(CoordinateParserUtil.distanceParser(this.distance * 1000.0f));
            this.mTvTime.setText(this.time);
            this.mTvDescription.setText(StringUtils.userNameToBlueColor(this.description));
            this.mTvDescription.setMovementMethod(LinkMovementMethod.getInstance());
            if (this.commentCount != 0) {
                this.mTvCommentCount.setText(this.commentCount + "");
            } else {
                this.mTvCommentCount.setText(R.string.Comment);
            }
            if (this.downloadCount == 0) {
                this.mTvDownloadCount.setText(R.string.download);
            } else {
                this.mTvDownloadCount.setText(this.downloadCount + "");
            }
            if (this.likeCount == 0) {
                this.mTvLikeCount.setText(R.string.Favourite);
            } else {
                this.mTvLikeCount.setText(this.likeCount + "");
            }
            if (this.isFavourite) {
                this.mIvLikeHeart.setImageResource(R.drawable.icon_redheart);
            } else if (this.messageType == 0) {
                this.mIvLikeHeart.setImageResource(R.drawable.icon_favourite_empty_heart);
                this.mFramLike.setEnabled(true);
            }
            new Thread() { // from class: im.helmsman.helmsmanandroid.adapter.HomeRouteAdapter.ViewHolder.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    super.run();
                    try {
                        Log.e("loading", "start");
                        ViewHolder.this.mapBitmap = Picasso.with(Base2Activity.getTopActivity()).load(ViewHolder.this.mapUrl).resize(50, 50).get();
                        Log.e("loading", "end" + ViewHolder.this.mapBitmap.getByteCount());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Intent intent;
            if (view.equals(this.ivShare)) {
                View viewInflate = LayoutInflater.from(Base2Activity.getTopActivity()).inflate(R.layout.popup_shareboard, (ViewGroup) null, false);
                PopupWindow popupWindow = new PopupWindow(viewInflate, -1, ViewUtils.dpToPx(120));
                popupWindow.setContentView(viewInflate);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(this.ivShare, 80, 0, 0);
                HomeRouteAdapter.this.backgroundAlpha(0.8f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: im.helmsman.helmsmanandroid.adapter.HomeRouteAdapter.ViewHolder.2
                    @Override // android.widget.PopupWindow.OnDismissListener
                    public void onDismiss() {
                        HomeRouteAdapter.this.backgroundAlpha(1.0f);
                    }
                });
                this.ivWeixinShare = (ImageView) viewInflate.findViewById(R.id.iv_weixin_share);
                this.ivWexinShareTimeLine = (ImageView) viewInflate.findViewById(R.id.iv_weixin_share_timeline);
                this.ivFaceBookShare = (ImageView) viewInflate.findViewById(R.id.iv_facebook_share);
                this.ivWeixinShare.setOnClickListener(this);
                this.ivWexinShareTimeLine.setOnClickListener(this);
                this.ivFaceBookShare.setOnClickListener(this);
                return;
            }
            if (view.equals(this.mFramLike)) {
                if (this.isFavourite) {
                    ViewUtils.ShowToast(R.string.yououhavecollected);
                    return;
                }
                if (UserMe.isMySelef(this.creatorId)) {
                    ViewUtils.showSnackBar((View) this.ivShare, R.string.cannot_favourite_your_route, true);
                    return;
                }
                Intent intent2 = new Intent(HomeRouteAdapter.this.mContext, (Class<?>) SendMessageActivity.class);
                intent2.putExtra("routeid", this.routeId);
                intent2.putExtra("model", (short) 1);
                intent2.putExtra("likeUpdatePosition", this.position);
                HomeRouteAdapter.this.mContext.startActivity(intent2);
                return;
            }
            if (view.equals(this.mLinDownload)) {
                DaoUtil.getInstance().saveRouteByWaypointsBean(null, this.routeName, this.mIvMap, this.waypointsBeanList);
                ViewUtils.ShowToast(R.string.loading);
                HomeRouteAdapter.this.mRouteView.getPresenter().downLoadRouteById(this.routeId, this.position);
                return;
            }
            if (view.equals(this.mLinComment)) {
                if (this.commentCount > 0) {
                    intent = new Intent(HomeRouteAdapter.this.mContext, (Class<?>) CommentContentActivity.class);
                    intent.putExtra("routeid", this.routeId);
                    intent.putExtra("commentPosition", this.position);
                } else {
                    intent = new Intent(HomeRouteAdapter.this.mContext, (Class<?>) SendMessageActivity.class);
                    intent.putExtra("model", (short) 3);
                    intent.putExtra("commentPosition", this.position);
                }
                intent.putExtra("messageid", this.messageId);
                HomeRouteAdapter.this.mContext.startActivity(intent);
                return;
            }
            if (view.equals(this.mCircleHead)) {
                Intent intent3 = new Intent(HomeRouteAdapter.this.mContext, (Class<?>) FriendsMessageInfoActivity.class);
                intent3.putExtra("userId", this.authorId);
                HomeRouteAdapter.this.mContext.startActivity(intent3);
                return;
            }
            if (view.equals(this.mIvMap)) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("latlngs", (ArrayList) DaoUtil.getInstance().wayPointBeanToLatLng(this.waypointsBeanList));
                Intent intent4 = new Intent(HomeRouteAdapter.this.mContext, (Class<?>) MessageDetailActivity.class);
                intent4.putExtra("avatarurl", this.circleHeadUrl);
                intent4.putExtra("islike", this.isLike);
                intent4.putExtra("description", this.description);
                intent4.putExtra("userid", this.authorId);
                intent4.putExtra("messageid", this.messageId);
                intent4.putExtra("identifier", this.identifier);
                intent4.putExtra("routename", this.routeName);
                intent4.putExtra("downloadcount", this.downloadCount);
                intent4.putExtra("commentcount", this.commentCount);
                intent4.putExtra("routeid", this.routeId);
                intent4.putExtra("shareurl", this.shareUrl);
                intent4.putExtra("mapurl", this.mapUrl);
                intent4.putExtra("waypointcount", this.wayPointCount);
                intent4.putExtra("distance", this.distance);
                intent4.putExtras(bundle);
                HomeRouteAdapter.this.mContext.startActivity(intent4);
                return;
            }
            if (view.equals(this.mCircleCreatorHead)) {
                Intent intent5 = new Intent(HomeRouteAdapter.this.mContext, (Class<?>) FriendsMessageInfoActivity.class);
                intent5.putExtra("userId", this.creatorId);
                HomeRouteAdapter.this.mContext.startActivity(intent5);
                return;
            }
            if (view.equals(this.ivWeixinShare)) {
                if (!MyApplication.getWxApi().isWXAppInstalled()) {
                    ViewUtils.showSnackBar((View) this.ivWeixinShare, R.string.you_need_install_wx, true);
                    return;
                } else {
                    ShareUtils.shareToWx(false, this.shareUrl, this.mapBitmap, this.description, this.distance + "", this.wayPointCount);
                    return;
                }
            }
            if (view.equals(this.ivWexinShareTimeLine)) {
                if (!MyApplication.getWxApi().isWXAppInstalled()) {
                    ViewUtils.showSnackBar((View) this.ivWeixinShare, R.string.you_need_install_wx, true);
                    return;
                } else {
                    ShareUtils.shareToWx(true, this.shareUrl, this.mapBitmap, this.description, this.distance + "", this.wayPointCount);
                    return;
                }
            }
            if (view.equals(this.ivFaceBookShare)) {
                ShareUtils.shareToFaceBook(this.shareUrl, this.mapUrl, this.description, this.distance + "", this.wayPointCount);
            }
        }
    }

    public void backgroundAlpha(float f) {
        WindowManager.LayoutParams attributes = Base2Activity.getTopActivity().getWindow().getAttributes();
        attributes.alpha = f;
        Base2Activity.getTopActivity().getWindow().setAttributes(attributes);
    }
}
