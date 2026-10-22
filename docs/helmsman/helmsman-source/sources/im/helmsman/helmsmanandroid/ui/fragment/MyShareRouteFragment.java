package im.helmsman.helmsmanandroid.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.MyShareRouteAdapter;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.LikeRouteSucessMessage;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.SendCommentSuccessMessage;
import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.presenter.MyShareRoutePresenter;
import im.helmsman.helmsmanandroid.ui.common.BaseV4Fragment;
import im.helmsman.helmsmanandroid.ui.view.MyShareRouteRouteView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/* JADX INFO: loaded from: classes2.dex */
public class MyShareRouteFragment extends BaseV4Fragment<MyShareRouteRouteView, MyShareRoutePresenter> implements MyShareRouteRouteView {
    private MyShareRouteAdapter adapter;
    private RecyclerAdapterWithHF adapterWithHF;
    private List<LikeRouteListModel.MessagesBean> datas = new ArrayList();
    private RecyclerView mRvContent;
    private View mViewParent;
    private PtrClassicFrameLayout ptrClassicFrameLayout;

    @Override // im.helmsman.helmsmanandroid.ui.view.MyShareRouteRouteView
    public void enableLoadMore() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mViewParent = layoutInflater.inflate(R.layout.fragment_myshare_route, (ViewGroup) null);
        initView();
        setRecycleViewAdapter();
        initEvent();
        EventBus.getDefault().register(this);
        return this.mViewParent;
    }

    private void initView() {
        this.ptrClassicFrameLayout = (PtrClassicFrameLayout) this.mViewParent.findViewById(R.id.ptr_userfavourite);
        this.mRvContent = (RecyclerView) this.mViewParent.findViewById(R.id.rv_favourite_route);
    }

    private void initEvent() {
        this.ptrClassicFrameLayout.setOnLoadMoreListener(new onRecyclerViewLoadMorelistener());
        this.ptrClassicFrameLayout.setPtrHandler(new onRecyclerViewRefreshListener());
    }

    private void setRecycleViewAdapter() {
        this.adapter = new MyShareRouteAdapter(getActivity(), this.datas);
        this.adapterWithHF = new RecyclerAdapterWithHF(this.adapter);
        ((MyShareRoutePresenter) this.presenter).getMyFavouriteRoute();
        this.mRvContent.setAdapter(this.adapterWithHF);
        this.mRvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRvContent.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).size(50).build());
        new Handler().postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.fragment.MyShareRouteFragment.1
            @Override // java.lang.Runnable
            public void run() {
                MyShareRouteFragment.this.ptrClassicFrameLayout.autoRefresh();
            }
        }, 200L);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseV4Fragment
    public MyShareRoutePresenter initPresenter() {
        return new MyShareRoutePresenter();
    }

    class onRecyclerViewRefreshListener extends PtrDefaultHandler {
        onRecyclerViewRefreshListener() {
        }

        @Override // com.chanven.lib.cptr.PtrHandler
        public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
            ((MyShareRoutePresenter) MyShareRouteFragment.this.presenter).getMyFavouriteRoute();
        }
    }

    class onRecyclerViewLoadMorelistener implements OnLoadMoreListener {
        onRecyclerViewLoadMorelistener() {
        }

        @Override // com.chanven.lib.cptr.loadmore.OnLoadMoreListener
        public void loadMore() {
            ((MyShareRoutePresenter) MyShareRouteFragment.this.presenter).loadMore();
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyShareRouteRouteView
    public void showRefreshSuccessMessage() {
        this.ptrClassicFrameLayout.setLoadMoreEnable(true);
        ViewUtils.ShowToast(R.string.RefreshSuccessMessage);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyShareRouteRouteView
    public void showRefreshFailedMessage(String str) {
        ViewUtils.ShowToast(getString(R.string.RefreshFailedMessage) + str);
        this.ptrClassicFrameLayout.setLoadMoreEnable(false);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyShareRouteRouteView
    public void RefreshComplete() {
        this.ptrClassicFrameLayout.refreshComplete();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyShareRouteRouteView
    public void LoadMoreComplete() {
        if (this.ptrClassicFrameLayout.isLoadingMore()) {
            this.ptrClassicFrameLayout.loadMoreComplete(true);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyShareRouteRouteView
    public void noMessage() {
        ViewUtils.ShowToast(R.string.no_message);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyShareRouteRouteView
    public void setRecyclerViewData(List<LikeRouteListModel.MessagesBean> list) {
        this.datas.clear();
        this.datas.addAll(list);
        this.adapterWithHF.notifyDataSetChangedHF();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyShareRouteRouteView
    public void addRecyclerViewData(List<LikeRouteListModel.MessagesBean> list) {
        this.datas.addAll(list);
        this.adapterWithHF.notifyDataSetChangedHF();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyShareRouteRouteView
    public void dismissDiaLog() {
        this.ptrClassicFrameLayout.refreshComplete();
    }

    @Subscribe
    public void onEventMainThread(SendCommentSuccessMessage sendCommentSuccessMessage) {
        this.ptrClassicFrameLayout.autoRefresh();
    }

    @Subscribe
    public void onEventMainThread(LikeRouteSucessMessage likeRouteSucessMessage) {
        this.ptrClassicFrameLayout.autoRefresh();
    }
}
