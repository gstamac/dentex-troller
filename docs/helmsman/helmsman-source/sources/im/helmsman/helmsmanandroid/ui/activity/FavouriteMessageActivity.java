package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.FavouriteMessageListAdapter;
import im.helmsman.helmsmanandroid.inet.model.FavouriteNotifitcationMessageListResultModel;
import im.helmsman.helmsmanandroid.presenter.FavouriteMessagePresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.FavouriteMessageView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class FavouriteMessageActivity extends Mvp2BaseActivity<FavouriteMessageView, FavouriteMessagePresenter> implements OnLoadMoreListener, FavouriteMessageView, AdapterView.OnItemClickListener {
    private FavouriteMessageListAdapter adapter;
    private List<FavouriteNotifitcationMessageListResultModel.MessagesBean> datas = new ArrayList();

    @BindView(R.id.lv_favourite_message_list)
    ListView lvFavouriteMessageList;

    @BindView(R.id.swipe_refresh)
    PtrClassicFrameLayout swipeRefresh;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public FavouriteMessagePresenter initPresenter() {
        return new FavouriteMessagePresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_favourite_message);
        ButterKnife.bind(this);
        setStatusBar(R.color.red_state);
        FavouriteMessageListAdapter favouriteMessageListAdapter = new FavouriteMessageListAdapter(this.datas);
        this.adapter = favouriteMessageListAdapter;
        this.lvFavouriteMessageList.setAdapter((ListAdapter) favouriteMessageListAdapter);
        this.lvFavouriteMessageList.setOnItemClickListener(this);
        ((FavouriteMessagePresenter) this.presenter).getFravouriteMessageNotifitcations();
        this.swipeRefresh.setPtrHandler(new OnRefreshListener());
        this.swipeRefresh.setOnLoadMoreListener(this);
        new Handler().postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.FavouriteMessageActivity.1
            @Override // java.lang.Runnable
            public void run() {
                FavouriteMessageActivity.this.swipeRefresh.autoRefresh();
            }
        }, 200L);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FavouriteMessageView
    public void initListView(List<FavouriteNotifitcationMessageListResultModel.MessagesBean> list) {
        this.datas.addAll(list);
        this.adapter.notifyDataSetChanged();
        this.swipeRefresh.refreshComplete();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FavouriteMessageView
    public void showLoadDataFaileMessage(String str) {
        ViewUtils.ShowToast(R.string.loaddatafailed);
        this.swipeRefresh.refreshComplete();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FavouriteMessageView
    public void enableLoadMore() {
        this.swipeRefresh.setLoadMoreEnable(true);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.FavouriteMessageView
    public void disableLoadMore() {
        this.swipeRefresh.setLoadMoreEnable(false);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        FavouriteNotifitcationMessageListResultModel.MessagesBean messagesBean = this.datas.get(i);
        int id = messagesBean.getId();
        int route_id = messagesBean.getRoute_id();
        Intent intent = new Intent(this, (Class<?>) CommentContentActivity.class);
        intent.putExtra("messageid", id);
        intent.putExtra("sinceid", 0);
        intent.putExtra("routeid", route_id);
        startActivity(intent);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (activityStack.size() == 0) {
            startActivity(new Intent(this, (Class<?>) MainActivity.class));
        }
    }

    @Override // com.chanven.lib.cptr.loadmore.OnLoadMoreListener
    public void loadMore() {
        ((FavouriteMessagePresenter) this.presenter).loadMore();
    }

    class OnRefreshListener extends PtrDefaultHandler {
        OnRefreshListener() {
        }

        @Override // com.chanven.lib.cptr.PtrHandler
        public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
            FavouriteMessageActivity.this.datas.clear();
            ((FavouriteMessagePresenter) FavouriteMessageActivity.this.presenter).refresh();
            FavouriteMessageActivity.this.adapter.notifyDataSetChanged();
            FavouriteMessageActivity.this.disableLoadMore();
        }
    }
}
