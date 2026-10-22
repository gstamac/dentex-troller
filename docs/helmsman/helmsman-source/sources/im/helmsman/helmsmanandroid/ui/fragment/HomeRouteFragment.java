package im.helmsman.helmsmanandroid.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.HomeRouteAdapter;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.DowloadCompleteMessage;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.LikeRouteSucessMessage;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.SendCommentSuccessMessage;
import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.presenter.MyRoutePresenterImp;
import im.helmsman.helmsmanandroid.ui.activity.LoginActivity;
import im.helmsman.helmsmanandroid.ui.activity.MyFollowingRouteActivity;
import im.helmsman.helmsmanandroid.ui.common.BaseV4Fragment;
import im.helmsman.helmsmanandroid.ui.view.MyRouteView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/* JADX INFO: loaded from: classes2.dex */
public class HomeRouteFragment extends BaseV4Fragment<MyRouteView, MyRoutePresenterImp> implements MyRouteView, View.OnClickListener, DialogInterface.OnDismissListener {
    private HomeRouteAdapter adapter;
    private RecyclerAdapterWithHF adapterWithHF;
    private Button btnLogin;
    private CardView cardView;
    private ImageView ivEmpyImage;
    private List<LikeRouteListModel.MessagesBean> listData = new ArrayList();
    private PtrClassicFrameLayout mRefresh;
    private RecyclerView mRvContent;
    private ProgressDialog progressDialog;
    private RelativeLayout relHint;
    private View view;

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public void dimissLoginProgress() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_friend_route, (ViewGroup) null);
        initView();
        initEvent();
        setRecycleView();
        EventBus.getDefault().register(this);
        return this.view;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (UserMe.isLogin()) {
            ((MyRoutePresenterImp) this.presenter).getMyRouteListData();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (UserMe.isLogin()) {
            this.mRefresh.setVisibility(0);
            this.relHint.setVisibility(8);
        } else {
            this.mRefresh.setVisibility(8);
            this.relHint.setVisibility(0);
            this.cardView.setVisibility(0);
        }
    }

    private void initView() {
        this.cardView = (CardView) this.view.findViewById(R.id.cardview2);
        this.ivEmpyImage = (ImageView) this.view.findViewById(R.id.iv_empy_boat);
        this.relHint = (RelativeLayout) this.view.findViewById(R.id.rel_trip);
        this.btnLogin = (Button) this.view.findViewById(R.id.btn_sinin);
        this.mRvContent = (RecyclerView) this.view.findViewById(R.id.rv_my_following_route);
        this.mRefresh = (PtrClassicFrameLayout) this.view.findViewById(R.id.srl_my_following_route);
        this.adapter = new HomeRouteAdapter(this, this.listData);
        RecyclerAdapterWithHF recyclerAdapterWithHF = new RecyclerAdapterWithHF(this.adapter);
        this.adapterWithHF = recyclerAdapterWithHF;
        this.mRvContent.setAdapter(recyclerAdapterWithHF);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseV4Fragment
    public MyRoutePresenterImp initPresenter() {
        return new MyRoutePresenterImp(this, 1);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseV4Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }

    private void initEvent() {
        this.btnLogin.setOnClickListener(this);
        this.mRefresh.setPtrHandler(new onRefreshListener());
        this.mRefresh.setOnLoadMoreListener(new onLoadMoreListener());
    }

    private void setRecycleView() {
        this.mRvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRvContent.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).size(50).color(-1).build());
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public void showGetDataErrorMsg(String str) {
        if (isAdded()) {
            ViewUtils.ShowToast(getString(R.string.loaddatafailed));
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public void enableLoadMore() {
        this.mRefresh.setLoadMoreEnable(true);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public void disableLoadMore() {
        this.mRefresh.setLoadMoreEnable(false);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public void loadmoreComplete() {
        this.mRefresh.loadMoreComplete(true);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public void refreshComplete() {
        this.mRefresh.refreshComplete();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public void setRecyclerView(List<LikeRouteListModel.MessagesBean> list) {
        this.listData.clear();
        this.listData.addAll(list);
        this.adapter.notifyDataSetChanged();
        this.cardView.setVisibility(8);
        if (list.isEmpty()) {
            this.mRefresh.setVisibility(8);
            this.relHint.setVisibility(0);
        } else {
            this.mRefresh.setVisibility(0);
            this.relHint.setVisibility(8);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public void addDataToRecycler(List<LikeRouteListModel.MessagesBean> list) {
        this.listData.addAll(list);
        this.adapter.notifyDataSetChanged();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public void showProgress() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        this.progressDialog = progressDialog;
        progressDialog.setTitle(R.string.loading);
        this.progressDialog.show();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public void hideProgress() {
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public MyRoutePresenterImp getPresenter() {
        return (MyRoutePresenterImp) this.presenter;
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public void showLoginFaileMessage(String str) {
        ViewUtils.ShowToast(R.string.loginerror);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyRouteView
    public void showEmpyBoatIcon() {
        this.mRefresh.setVisibility(8);
        this.ivEmpyImage.setVisibility(0);
        this.relHint.setVisibility(0);
        this.cardView.setVisibility(8);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.btnLogin)) {
            startActivity(new Intent(getActivity(), (Class<?>) LoginActivity.class));
        }
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        if (UserMe.isLogin()) {
            return;
        }
        ((MyFollowingRouteActivity) getActivity()).selectPageIndex(0);
    }

    class onRefreshListener extends PtrDefaultHandler {
        onRefreshListener() {
        }

        @Override // com.chanven.lib.cptr.PtrHandler
        public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
            ((MyRoutePresenterImp) HomeRouteFragment.this.presenter).getMyRouteListData();
        }
    }

    class onLoadMoreListener implements OnLoadMoreListener {
        onLoadMoreListener() {
        }

        @Override // com.chanven.lib.cptr.loadmore.OnLoadMoreListener
        public void loadMore() {
            ((MyRoutePresenterImp) HomeRouteFragment.this.presenter).loadMoreMyRouteListData();
        }
    }

    @Subscribe
    public void onEventMainThread(DowloadCompleteMessage dowloadCompleteMessage) {
        int listDataPosition = dowloadCompleteMessage.getListDataPosition();
        if (listDataPosition == -1) {
            return;
        }
        this.listData.get(listDataPosition).setRoute_download_count(dowloadCompleteMessage.getDownLoadCount());
        this.adapterWithHF.notifyDataSetChangedHF();
    }

    @Subscribe
    public void onEventMainThread(LikeRouteSucessMessage likeRouteSucessMessage) {
        int position = likeRouteSucessMessage.getPosition();
        if (position == -1) {
            return;
        }
        LikeRouteListModel.MessagesBean messagesBean = this.listData.get(position);
        messagesBean.setRoute_favourite_count(messagesBean.getRoute_download_count());
        messagesBean.getRoute().setIs_favourite(true);
        this.adapterWithHF.notifyDataSetChangedHF();
    }

    @Subscribe
    public void onEventMainThread(SendCommentSuccessMessage sendCommentSuccessMessage) {
        int listdataPositionl = sendCommentSuccessMessage.getListdataPositionl();
        if (listdataPositionl == -1) {
            return;
        }
        LikeRouteListModel.MessagesBean messagesBean = this.listData.get(listdataPositionl);
        messagesBean.setMessage_comment_count(messagesBean.getMessage_comment_count() + 1);
        this.adapterWithHF.notifyDataSetChangedHF();
    }
}
