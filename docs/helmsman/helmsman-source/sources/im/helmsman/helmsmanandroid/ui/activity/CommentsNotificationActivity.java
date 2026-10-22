package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import butterknife.BindView;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.CommentsNotificationAdapter;
import im.helmsman.helmsmanandroid.inet.model.CommentNotificationResultModel;
import im.helmsman.helmsmanandroid.presenter.CommentNotificationPresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.CommentNotificationView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CommentsNotificationActivity extends Mvp2BaseActivity<CommentNotificationView, CommentNotificationPresenter> implements AdapterView.OnItemClickListener, OnLoadMoreListener, CommentNotificationView {
    private CommentsNotificationAdapter adapter;
    private List<CommentNotificationResultModel.CommentsBean> datas = new ArrayList();

    @BindView(R.id.lv_comment_notif)
    ListView lvCommentNotif;

    @BindView(R.id.swipe_refresh)
    PtrClassicFrameLayout swipeRefresh;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public CommentNotificationPresenter initPresenter() {
        return new CommentNotificationPresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_comments_notification);
        setStatusBar(R.color.red_state);
        CommentsNotificationAdapter commentsNotificationAdapter = new CommentsNotificationAdapter(this.datas);
        this.adapter = commentsNotificationAdapter;
        this.lvCommentNotif.setAdapter((ListAdapter) commentsNotificationAdapter);
        ((CommentNotificationPresenter) this.presenter).refresh();
        disableLoadMore();
        initEvent();
        new Handler().postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.CommentsNotificationActivity.1
            @Override // java.lang.Runnable
            public void run() {
                CommentsNotificationActivity.this.swipeRefresh.autoRefresh();
            }
        }, 200L);
    }

    private void initEvent() {
        this.swipeRefresh.setPtrHandler(new OnRefreshListener());
        this.swipeRefresh.setOnLoadMoreListener(this);
        this.lvCommentNotif.setOnItemClickListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentNotificationView
    public void loadDataFaile(String str) {
        this.swipeRefresh.refreshComplete();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentNotificationView
    public void showLoadDataFaileMessage(String str) {
        ViewUtils.ShowToast(str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentNotificationView
    public void initListView(List<CommentNotificationResultModel.CommentsBean> list) {
        this.datas.addAll(list);
        this.adapter.notifyDataSetChanged();
        this.swipeRefresh.refreshComplete();
        if (this.swipeRefresh.isLoadMoreEnable()) {
            this.swipeRefresh.loadMoreComplete(true);
        }
        list.size();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentNotificationView
    public void enableLoadMore() {
        this.swipeRefresh.setLoadMoreEnable(true);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentNotificationView
    public void disableLoadMore() {
        this.swipeRefresh.setLoadMoreEnable(false);
    }

    @Override // com.chanven.lib.cptr.loadmore.OnLoadMoreListener
    public void loadMore() {
        ((CommentNotificationPresenter) this.presenter).loadMore();
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        CommentNotificationResultModel.CommentsBean commentsBean = this.datas.get(i);
        int id = commentsBean.getMessage().getId();
        int id2 = commentsBean.getId();
        int route_id = commentsBean.getMessage().getRoute().getRoute_id();
        Intent intent = new Intent(this, (Class<?>) CommentContentActivity.class);
        intent.putExtra("messageid", id);
        intent.putExtra("routeid", route_id);
        intent.putExtra("sinceid", id2);
        startActivity(intent);
    }

    class OnRefreshListener extends PtrDefaultHandler {
        OnRefreshListener() {
        }

        @Override // com.chanven.lib.cptr.PtrHandler
        public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
            ((CommentNotificationPresenter) CommentsNotificationActivity.this.presenter).refresh();
            CommentsNotificationActivity.this.datas.clear();
            CommentsNotificationActivity.this.adapter.notifyDataSetChanged();
            CommentsNotificationActivity.this.disableLoadMore();
        }
    }
}
