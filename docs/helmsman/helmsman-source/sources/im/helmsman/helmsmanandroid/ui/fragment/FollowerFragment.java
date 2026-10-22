package im.helmsman.helmsmanandroid.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.MAVLink.enums.MAV_MODE;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.FriendsAdapter;
import im.helmsman.helmsmanandroid.inet.model.GetMyFollowerResultModel;
import im.helmsman.helmsmanandroid.presenter.FriendsPersenterImp;
import im.helmsman.helmsmanandroid.ui.activity.FriendsActivity;
import im.helmsman.helmsmanandroid.ui.view.MyFollowerView;
import im.helmsman.helmsmanandroid.utils.AutoLayoutExtendsUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class FollowerFragment extends Fragment implements MyFollowerView {
    private FriendsAdapter adapter;
    private RecyclerAdapterWithHF adapterWithHF;
    private List<GetMyFollowerResultModel.UsersBean> datas = new ArrayList();
    private FriendsPersenterImp friendsPersenter;
    private RecyclerView mRvFollowerList;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private int userid;
    private View view;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = View.inflate(getActivity(), R.layout.fragment_follower, null);
        this.userid = ((FriendsActivity) getActivity()).userId;
        FriendsPersenterImp friendsPersenterImp = new FriendsPersenterImp(this, this.userid);
        this.friendsPersenter = friendsPersenterImp;
        friendsPersenterImp.getMyFollowerList(this.userid);
        initView();
        initEvent();
        return this.view;
    }

    private void initView() {
        this.ptrClassicFrameLayout = (PtrClassicFrameLayout) this.view.findViewById(R.id.ptr_friendslist_refresh);
        RecyclerView recyclerView = (RecyclerView) this.view.findViewById(R.id.rv_follower_list);
        this.mRvFollowerList = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.adapter = new FriendsAdapter(getActivity(), this.datas, this.friendsPersenter, 0);
        RecyclerAdapterWithHF recyclerAdapterWithHF = new RecyclerAdapterWithHF(this.adapter);
        this.adapterWithHF = recyclerAdapterWithHF;
        this.mRvFollowerList.setAdapter(recyclerAdapterWithHF);
        this.mRvFollowerList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).size(4).margin(AutoLayoutExtendsUtils.widthConvert(MAV_MODE.MAV_MODE_GUIDED_ARMED), 0).build());
    }

    private void initEvent() {
        this.ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMore());
        this.ptrClassicFrameLayout.setPtrHandler(new OnRereshListener());
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyFollowerView
    public void refreshRecyclerView(List<GetMyFollowerResultModel.UsersBean> list) {
        this.datas.clear();
        this.datas.addAll(list);
        this.adapterWithHF.notifyDataSetChanged();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyFollowerView
    public void addDataToRecycler(List<GetMyFollowerResultModel.UsersBean> list) {
        this.datas.addAll(list);
        this.adapterWithHF.notifyDataSetChanged();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyFollowerView
    public void showNoMoreDataMessage() {
        ViewUtils.ShowToast(R.string.no_message);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyFollowerView
    public void showGetDataFailedMessage(String str) {
        ViewUtils.ShowToast(MyApplication.getInstance().getString(R.string.loaddatafailed));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyFollowerView
    public void refreshComplete() {
        this.ptrClassicFrameLayout.refreshComplete();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyFollowerView
    public void enableLoadMore() {
        this.ptrClassicFrameLayout.setLoadMoreEnable(true);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyFollowerView
    public void disableLoadMore() {
        this.ptrClassicFrameLayout.setLoadMoreEnable(false);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyFollowerView
    public void loadMoreComplete() {
        if (this.ptrClassicFrameLayout.isLoadingMore()) {
            this.ptrClassicFrameLayout.loadMoreComplete(true);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MyFollowerView
    public void showFollowFailedMessage(String str) {
        ViewUtils.ShowToast(MyApplication.getInstance().getString(R.string.followfailed));
    }

    class OnRereshListener extends PtrDefaultHandler {
        OnRereshListener() {
        }

        @Override // com.chanven.lib.cptr.PtrHandler
        public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
            FollowerFragment.this.friendsPersenter.getMyFollowerList(FollowerFragment.this.userid);
        }
    }

    class OnLoadMore implements OnLoadMoreListener {
        OnLoadMore() {
        }

        @Override // com.chanven.lib.cptr.loadmore.OnLoadMoreListener
        public void loadMore() {
            FollowerFragment.this.friendsPersenter.loadMoreMyFollower(FollowerFragment.this.userid);
        }
    }
}
