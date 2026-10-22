package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.tt.whorlviewlibrary.WhorlView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.AddFriendsListAdapter;
import im.helmsman.helmsmanandroid.inet.model.FindFriendsResultModel;
import im.helmsman.helmsmanandroid.presenter.AddFriendsPresenterImp;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.AddFriendsView;
import im.helmsman.helmsmanandroid.view.SearchView;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AddFrinedsActivity extends Mvp2BaseActivity<AddFriendsView, AddFriendsPresenterImp> implements AddFriendsView, SearchView.OnSearchViewTextChangeListener, OnLoadMoreListener, AddFriendsListAdapter.OnFollowOnClickListener, AdapterView.OnItemClickListener {
    private AddFriendsListAdapter adapter;
    private List<FindFriendsResultModel.UsersBean> datas = new ArrayList();
    private boolean isLoadMore = false;

    @BindView(R.id.lv_addfriend_friendlist)
    ListView lvFriendList;

    @BindView(R.id.whorlview_addfriend_progressbar)
    WhorlView progressbar;

    @BindView(R.id.ptr_addfriends_refresh)
    PtrClassicFrameLayout ptrAddfriendsRefresh;

    @BindView(R.id.seachview_addFriends_searchfiends_)
    SearchView searchView;

    @Override // im.helmsman.helmsmanandroid.ui.view.AddFriendsView
    public void dismissFollowingDialog() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddFriendsView
    public void showFollowingDialog() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_frineds);
        ButterKnife.bind(this);
        this.adapter = new AddFriendsListAdapter(this, this.datas);
        this.searchView.setOnSearchViewTextChangeListener(this);
        this.ptrAddfriendsRefresh.setOnLoadMoreListener(this);
        this.adapter.setOnFollowClickListener(this);
        this.lvFriendList.setAdapter((ListAdapter) this.adapter);
        this.lvFriendList.setOnItemClickListener(this);
        this.ptrAddfriendsRefresh.setPtrHandler(new PtrDefaultHandler() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddFrinedsActivity.1
            @Override // com.chanven.lib.cptr.PtrHandler
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                AddFrinedsActivity.this.ptrAddfriendsRefresh.refreshComplete();
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public AddFriendsPresenterImp initPresenter() {
        return new AddFriendsPresenterImp();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddFriendsView
    public void loadDataToList(FindFriendsResultModel findFriendsResultModel) {
        if (!this.isLoadMore) {
            this.datas.clear();
        }
        this.datas.addAll(findFriendsResultModel.getUsers());
        this.adapter.notifyDataSetChanged();
        if (this.progressbar.isCircling()) {
            this.progressbar.stop();
            this.progressbar.setVisibility(8);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddFriendsView
    public void showLoadDataErrorMsg(String str) {
        if (this.progressbar.isCircling()) {
            this.progressbar.stop();
            this.progressbar.setVisibility(8);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddFriendsView
    public void changeFollowBtnState(int i, boolean z) {
        this.adapter.changeFollowBtnState(i, z);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddFriendsView
    public void closeLoadMore() {
        this.ptrAddfriendsRefresh.setLoadMoreEnable(false);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddFriendsView
    public void openLoadMode() {
        this.ptrAddfriendsRefresh.setLoadMoreEnable(true);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddFriendsView
    public void loadMoreComplete() {
        if (this.ptrAddfriendsRefresh.isLoadingMore()) {
            this.ptrAddfriendsRefresh.loadMoreComplete(true);
        }
    }

    @Override // im.helmsman.helmsmanandroid.view.SearchView.OnSearchViewTextChangeListener
    public void onTextChange(String str) {
        ((AddFriendsPresenterImp) this.presenter).searchFriendsByUserName(str);
        this.isLoadMore = false;
        if (TextUtils.isEmpty(str) || this.progressbar.isCircling()) {
            return;
        }
        this.progressbar.start();
        this.progressbar.setVisibility(0);
    }

    @Override // com.chanven.lib.cptr.loadmore.OnLoadMoreListener
    public void loadMore() {
        ((AddFriendsPresenterImp) this.presenter).loadMoreFriendsByUserName();
        this.isLoadMore = true;
    }

    @Override // im.helmsman.helmsmanandroid.adapter.AddFriendsListAdapter.OnFollowOnClickListener
    public void onClick(int i) {
        ((AddFriendsPresenterImp) this.presenter).followFriendById(i);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.datas.size() > i) {
            int id = this.datas.get(i).getId();
            Intent intent = new Intent(this, (Class<?>) FriendsMessageInfoActivity.class);
            intent.putExtra("userId", id);
            startActivity(intent);
        }
    }
}
