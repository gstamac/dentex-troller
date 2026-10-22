package im.helmsman.helmsmanandroid.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.CommentContentAdapter;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.DowloadCompleteMessage;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.LikeRouteSucessMessage;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.SendCommentSuccessMessage;
import im.helmsman.helmsmanandroid.inet.model.CommentContentResultModel;
import im.helmsman.helmsmanandroid.inet.model.CommentListModel;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.presenter.CommentContentPresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.CommentContentView;
import im.helmsman.helmsmanandroid.utils.AutoLayoutExtendsUtils;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.TimeUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.i18n.TextBundle;
import org.greenrobot.eventbus.Subscribe;

/* JADX INFO: loaded from: classes2.dex */
public class CommentContentActivity extends Mvp2BaseActivity<CommentContentView, CommentContentPresenter> implements CommentContentView, AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
    private CommentContentAdapter adapter;
    private String commentItemusername;
    private String copyStr;
    private int delCommentId;
    private FrameLayout frameFavouritel;
    private View headerview;

    @BindView(R.id.iv_comment_content_map)
    ImageView ivCommentContentMap;
    private ImageView ivFavourite;
    private LinearLayout linearComment;
    private LinearLayout linearDownload;
    ListView lvCommentContent;
    private int messageid;
    private PtrClassicFrameLayout pcfRefresh;
    private PopupWindow popupWindownew;
    private View popupWindows;

    @BindView(R.id.tv_comment_content_content)
    TextView tvCommentContentContent;

    @BindView(R.id.tv_comment_content_time)
    TextView tvCommentContentTime;

    @BindView(R.id.tv_comment_content_username)
    TextView tvCommentContentUsername;
    private TextView tvCommentCount;
    private TextView tvCopy;
    private TextView tvDel;
    private TextView tvDownLoadCount;
    private TextView tvFavouriteCount;
    private TextView tvReply;
    private List<CommentListModel.CommentsBean> datas = new ArrayList();
    private int routeid = -1;
    private boolean isFavourite = false;
    private int commentPosition = -1;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public CommentContentPresenter initPresenter() {
        return new CommentContentPresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        registerEventBus();
        this.isBind = false;
        setContentView(R.layout.activity_comment_content);
        setStatusBar(R.color.red_state);
        Intent intent = getIntent();
        this.commentPosition = intent.getIntExtra("commentPosition", -1);
        this.messageid = intent.getIntExtra("messageid", 0);
        int intExtra = intent.getIntExtra("sinceid", 0);
        ((CommentContentPresenter) this.presenter).getMessageById(this.messageid);
        ((CommentContentPresenter) this.presenter).getCommentLists(this.messageid, intExtra, 1);
        initView();
    }

    private void initView() {
        PtrClassicFrameLayout ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.pcfl_refresh);
        this.pcfRefresh = ptrClassicFrameLayout;
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() { // from class: im.helmsman.helmsmanandroid.ui.activity.CommentContentActivity.1
            @Override // com.chanven.lib.cptr.PtrHandler
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                CommentContentActivity.this.pcfRefresh.refreshComplete();
            }
        });
        this.ivFavourite = (ImageView) findViewById(R.id.iv_myroute_heart_like);
        this.tvCommentCount = (TextView) findViewById(R.id.tv_item_my_following_comment);
        this.tvDownLoadCount = (TextView) findViewById(R.id.tv_item_my_following_download);
        this.tvFavouriteCount = (TextView) findViewById(R.id.tv_item_my_folowing_favourite);
        this.frameFavouritel = (FrameLayout) findViewById(R.id.frame_item_my_following_favourite);
        this.linearDownload = (LinearLayout) findViewById(R.id.tlinear_item_my_following_download);
        this.linearComment = (LinearLayout) findViewById(R.id.lin_item_my_following_comment);
        this.frameFavouritel.setEnabled(false);
        this.linearComment.setOnClickListener(this);
        this.linearDownload.setOnClickListener(this);
        this.frameFavouritel.setOnClickListener(this);
        this.lvCommentContent = (ListView) findViewById(R.id.lv_comment_content);
        CommentContentAdapter commentContentAdapter = new CommentContentAdapter(this, this.datas);
        this.adapter = commentContentAdapter;
        this.lvCommentContent.setAdapter((ListAdapter) commentContentAdapter);
        this.lvCommentContent.setOnItemClickListener(this);
        this.lvCommentContent.setOnItemLongClickListener(this);
        this.pcfRefresh.setOnLoadMoreListener(new MyOnLoadMoreListener());
        View viewInflate = LayoutInflater.from(this).inflate(R.layout.heade_comment_content, (ViewGroup) this.lvCommentContent, false);
        this.headerview = viewInflate;
        ButterKnife.bind(this, viewInflate);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentContentView
    public void getMessageSuccess(CommentContentResultModel commentContentResultModel) {
        this.lvCommentContent.removeHeaderView(this.headerview);
        this.lvCommentContent.addHeaderView(this.headerview);
        CommentContentResultModel.RouteBean route = commentContentResultModel.getRoute();
        String username = commentContentResultModel.getAuthor().getUsername();
        String static_image_url = route.getStatic_image_url();
        String timeElapse = TimeUtils.getTimeElapse(TimeUtils.timeZoneToLocal(commentContentResultModel.getTimestamp()));
        String description = commentContentResultModel.getDescription();
        int route_download_count = commentContentResultModel.getRoute_download_count();
        int route_favourite_count = commentContentResultModel.getRoute_favourite_count();
        this.isFavourite = commentContentResultModel.getRoute().isIs_favourite();
        Glide.with((FragmentActivity) this).load(static_image_url).into(this.ivCommentContentMap);
        SpannableString spannableStringUserNameToBlueColor = StringUtils.userNameToBlueColor(description);
        this.tvCommentContentContent.setMovementMethod(LinkMovementMethod.getInstance());
        this.tvCommentContentContent.setText(spannableStringUserNameToBlueColor);
        this.tvCommentContentTime.setText(timeElapse);
        this.tvCommentContentUsername.setText(username);
        this.tvFavouriteCount.setText(route_favourite_count > 0 ? route_favourite_count + "" : getString(R.string.Favourite));
        this.tvDownLoadCount.setText(route_download_count > 0 ? route_download_count + "" : getString(R.string.download));
        this.routeid = commentContentResultModel.getRoute().getRoute_id();
        if (this.isFavourite) {
            this.ivFavourite.setImageResource(R.drawable.icon_redheart);
            this.frameFavouritel.setEnabled(false);
        } else {
            this.ivFavourite.setImageResource(R.drawable.icon_favourite_empty_heart);
            this.frameFavouritel.setEnabled(true);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentContentView
    public void showGetDatasFaileMessage(String str) {
        ViewUtils.ShowToast(str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentContentView
    public void initListView(CommentListModel commentListModel) {
        this.datas.addAll(commentListModel.getComments());
        this.adapter.notifyDataSetChanged();
        this.tvCommentCount.setText(commentListModel.getCount() + "");
        if (this.pcfRefresh.isLoadingMore()) {
            this.pcfRefresh.loadMoreComplete(true);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentContentView
    public void showDeleteCommentFailed(String str) {
        ViewUtils.ShowToast(R.string.deleteCommentFailed);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentContentView
    public void refreshListView() {
        this.datas.clear();
        ((CommentContentPresenter) this.presenter).getCommentLists(this.messageid, 0, 1);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentContentView
    public void enableLoadMore() {
        this.pcfRefresh.setLoadMoreEnable(true);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentContentView
    public void diableLoadMore() {
        this.pcfRefresh.setLoadMoreEnable(false);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentContentView
    public void showDownLoadRouteSuccessMessage() {
        ViewUtils.ShowToast(R.string.myfanrouteadapter_route_download_complete);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentContentView
    public void showDownLoadRouteFailedMessage(String str) {
        ViewUtils.ShowToast(getString(R.string.download_route_failed));
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (i == 0) {
            return;
        }
        String username = this.datas.get(i - 1).getAuthor().getUsername();
        Intent intent = new Intent(this, (Class<?>) SendMessageActivity.class);
        intent.putExtra("model", (short) 4);
        intent.putExtra("messageid", this.messageid);
        intent.putExtra("username", username);
        intent.putExtra("commentPosition", this.commentPosition);
        startActivity(intent);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.lvCommentContent.removeHeaderView(this.headerview);
        this.lvCommentContent.addHeaderView(this.headerview);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (activityStack.size() == 0) {
            startActivity(new Intent(this, (Class<?>) MainActivity.class));
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        PopupWindow popupWindow = this.popupWindownew;
        if (popupWindow != null && popupWindow.isShowing()) {
            this.popupWindownew.dismiss();
        }
        if (view.equals(this.linearComment)) {
            Intent intent = new Intent(this, (Class<?>) SendMessageActivity.class);
            intent.putExtra("model", (short) 3);
            intent.putExtra("messageid", this.messageid);
            intent.putExtra("commentPosition", this.commentPosition);
            startActivity(intent);
            return;
        }
        if (view.equals(this.linearDownload)) {
            ((CommentContentPresenter) this.presenter).downLoadWayPointToPhone(this.ivCommentContentMap, this.routeid);
            ViewUtils.ShowToast(R.string.loading);
            return;
        }
        if (view.equals(this.frameFavouritel)) {
            Intent intent2 = new Intent(this, (Class<?>) SendMessageActivity.class);
            intent2.putExtra("model", (short) 1);
            intent2.putExtra("routeid", this.routeid);
            startActivity(intent2);
            return;
        }
        if (view.equals(this.tvCopy)) {
            ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(TextBundle.TEXT_ENTRY, this.copyStr));
            return;
        }
        if (view.equals(this.tvReply)) {
            Intent intent3 = new Intent(this, (Class<?>) SendMessageActivity.class);
            intent3.putExtra("model", (short) 4);
            intent3.putExtra("messageid", this.messageid);
            intent3.putExtra("username", this.commentItemusername);
            startActivity(intent3);
            return;
        }
        if (view.equals(this.tvDel)) {
            ((CommentContentPresenter) this.presenter).deleteComment(this.delCommentId);
        }
    }

    @Subscribe
    public void onEventMainThread(SendCommentSuccessMessage sendCommentSuccessMessage) {
        this.datas.clear();
        ((CommentContentPresenter) this.presenter).getCommentLists(this.messageid, 0, 1);
    }

    @Subscribe
    public void onEventMainThread(DowloadCompleteMessage dowloadCompleteMessage) {
        ((CommentContentPresenter) this.presenter).getMessageById(this.messageid);
    }

    @Subscribe
    public void onEventMainThread(LikeRouteSucessMessage likeRouteSucessMessage) {
        ((CommentContentPresenter) this.presenter).getMessageById(this.messageid);
    }

    @Override // android.widget.AdapterView.OnItemLongClickListener
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        CommentListModel.CommentsBean commentsBean = this.datas.get(i - 1);
        CommentListModel.CommentsBean.AuthorBean author = commentsBean.getAuthor();
        this.delCommentId = commentsBean.getId();
        this.commentItemusername = author.getUsername();
        this.copyStr = ((TextView) view.findViewById(R.id.tv_commentlist_comment)).getText().toString();
        showPopupMenu(view);
        return true;
    }

    private void showPopupMenu(View view) {
        if (this.popupWindows == null) {
            View viewInflate = getLayoutInflater().inflate(R.layout.popup_comment_menu, (ViewGroup) null);
            this.popupWindows = viewInflate;
            this.tvReply = (TextView) viewInflate.findViewById(R.id.tv_popup_comment_reply);
            this.tvCopy = (TextView) this.popupWindows.findViewById(R.id.tv_popup_comment_copy);
            this.tvDel = (TextView) this.popupWindows.findViewById(R.id.tv_popup_comment_del);
            this.tvReply.setOnClickListener(this);
            this.tvCopy.setOnClickListener(this);
            this.tvDel.setOnClickListener(this);
        }
        if (this.commentItemusername.equals(UserMe.getUser().getUsername())) {
            this.tvDel.setVisibility(0);
        } else {
            this.tvDel.setVisibility(8);
        }
        PopupWindow popupWindow = new PopupWindow(this.popupWindows, AutoLayoutExtendsUtils.widthConvert(984), -2);
        this.popupWindownew = popupWindow;
        popupWindow.setTouchable(true);
        this.popupWindownew.setFocusable(true);
        this.popupWindownew.setOutsideTouchable(true);
        this.popupWindownew.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = 0.5f;
        getWindow().setAttributes(attributes);
        this.popupWindownew.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.CommentContentActivity.2
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                WindowManager.LayoutParams attributes2 = CommentContentActivity.this.getWindow().getAttributes();
                attributes2.alpha = 1.0f;
                CommentContentActivity.this.getWindow().setAttributes(attributes2);
            }
        });
        this.popupWindownew.showAtLocation(view, 17, 0, 0);
    }

    class MyOnLoadMoreListener implements OnLoadMoreListener {
        MyOnLoadMoreListener() {
        }

        @Override // com.chanven.lib.cptr.loadmore.OnLoadMoreListener
        public void loadMore() {
            ((CommentContentPresenter) CommentContentActivity.this.presenter).loadMore();
        }
    }
}
