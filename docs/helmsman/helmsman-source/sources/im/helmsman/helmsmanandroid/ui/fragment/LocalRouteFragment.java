package im.helmsman.helmsmanandroid.ui.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.LocalRouteListAdapter;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.dao.RouteModel;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.DowloadCompleteMessage;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.SaveLocalRouteCompleteMessage;
import im.helmsman.helmsmanandroid.presenter.LocalRoutePresenter;
import im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity;
import im.helmsman.helmsmanandroid.ui.activity.EditRouteActivity;
import im.helmsman.helmsmanandroid.ui.activity.MyFollowingRouteActivity;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.common.BaseV4Fragment;
import im.helmsman.helmsmanandroid.ui.view.LocalRouteView;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.helmsmanandroid.view.SearchView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;

/* JADX INFO: loaded from: classes2.dex */
public class LocalRouteFragment extends BaseV4Fragment<LocalRouteView, LocalRoutePresenter> implements SearchView.OnSearchViewTextChangeListener, View.OnClickListener, AdapterView.OnItemClickListener, LocalRouteView, LocalRouteListAdapter.OnListButtonClickListener, AdapterView.OnItemLongClickListener {
    private MyFollowingRouteActivity activity;
    private Handler handler;

    @BindView(R.id.iv_addroute)
    ImageView ivAddroute;

    @BindView(R.id.lv_edit)
    ListView lvEdit;
    private LocalRouteListAdapter mAdapter;
    ProgressDialog mPDLoading;
    private ProgressDialog progressDialog;

    @BindView(R.id.swipe_refresh)
    PtrClassicFrameLayout swipeRefresh;
    private View view;
    private List<RouteModel> datas = new ArrayList();
    private List<RouteModel> checkList = new ArrayList();

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseV4Fragment
    public LocalRoutePresenter initPresenter() {
        return new LocalRoutePresenter();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        registerEventBus();
        View viewInflate = layoutInflater.inflate(R.layout.activity_route, viewGroup, false);
        this.view = viewInflate;
        ButterKnife.bind(this, viewInflate);
        LocalRouteListAdapter localRouteListAdapter = new LocalRouteListAdapter(getActivity(), this.datas, this.lvEdit);
        this.mAdapter = localRouteListAdapter;
        this.lvEdit.setAdapter((ListAdapter) localRouteListAdapter);
        initEvent();
        this.activity = (MyFollowingRouteActivity) getActivity();
        initActivityViewEvent();
        this.handler = new Handler();
        return this.view;
    }

    private void initActivityViewEvent() {
        this.activity.ivMyfollowingRouteDelete.setOnClickListener(this);
        this.activity.ivMyfollowingRouteEdit.setOnClickListener(this);
        this.activity.ivMyfollowingRouteCopy.setOnClickListener(this);
        this.activity.searchLocalrouteSearch.setOnSearchViewTextChangeListener(this);
    }

    private void initEvent() {
        this.mAdapter.setOnListButtonClickListener(this);
        this.swipeRefresh.setPtrHandler(new OnRefreshListener());
        this.lvEdit.setOnItemLongClickListener(this);
        this.lvEdit.setOnItemClickListener(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        reloadData();
        this.swipeRefresh.setHeaderText(R.string.route_pull_to_download, R.string.route_release_to_download);
    }

    private void reloadData() {
        this.checkList.clear();
        this.datas.clear();
        this.datas.addAll(DaoUtil.getInstance().getAllWayPointList());
        this.mAdapter.notifyDataSetChanged();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseV4Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(SaveLocalRouteCompleteMessage saveLocalRouteCompleteMessage) {
        reloadData();
    }

    @Override // im.helmsman.helmsmanandroid.adapter.LocalRouteListAdapter.OnListButtonClickListener
    public void onButtonClickListener(int i, View view, RouteModel routeModel) {
        if (view.getId() != R.id.tv_localroute_navi) {
            return;
        }
        ((LocalRoutePresenter) this.presenter).sendWayPointToBoat(routeModel);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void showNoWifiMessage() {
        ViewUtils.ShowToast(R.string.main_no_boat_connected_message);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void showTooFarMessage() {
        ViewUtils.ShowToast(R.string.pulltorefreshlistadapter_toofar);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void showProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(Base2Activity.getTopActivity());
        this.progressDialog = progressDialog;
        progressDialog.setTitle(Base2Activity.getTopActivity().getString(R.string.pulltorefreshlistadapter_pleasewait));
        this.progressDialog.setMessage(Base2Activity.getTopActivity().getString(R.string.route_uploading));
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void dimissDownloadRouteProgress() {
        ProgressDialog progressDialog = this.mPDLoading;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.mPDLoading.cancel();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void updateProgress(final String str) {
        this.handler.post(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.fragment.LocalRouteFragment.1
            @Override // java.lang.Runnable
            public void run() {
                if (LocalRouteFragment.this.progressDialog != null) {
                    LocalRouteFragment.this.progressDialog.setMessage(str);
                }
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void uploadSuccess() {
        dismissProgress();
        ViewUtils.ShowToast(R.string.main_success);
        Base2Activity.getTopActivity().finish();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void uploadFailed(String str) {
        dismissProgress();
        ViewUtils.ShowToast(str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void showdownloadRouteProgress(int i, int i2) {
        ProgressDialog progressDialog = this.mPDLoading;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.mPDLoading.setMessage(getString(R.string.route_downloading) + i + "/" + i2);
    }

    @OnClick({R.id.iv_addroute})
    public void onClick() {
        startActivity(new Intent(getActivity(), (Class<?>) AddRouteActivity.class));
    }

    @Override // android.widget.AdapterView.OnItemLongClickListener
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        reloadData();
        ((MyFollowingRouteActivity) getActivity()).changeState(4);
        return true;
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (LocalRouteListAdapter.visiblity == 0) {
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_localroute_check_route);
            checkBox.setChecked(!checkBox.isChecked());
            RouteModel routeModel = this.datas.get(i);
            if (checkBox.isChecked()) {
                this.checkList.add(routeModel);
            } else {
                this.checkList.remove(routeModel);
            }
            routeModel.setCheck(checkBox.isChecked());
            if (this.checkList.size() > 1) {
                this.activity.ivMyfollowingRouteEdit.setVisibility(8);
                this.activity.ivMyfollowingRouteDelete.setVisibility(0);
                this.activity.ivMyfollowingRouteCopy.setVisibility(0);
            } else if (this.checkList.size() == 1) {
                this.activity.ivMyfollowingRouteCopy.setVisibility(0);
                this.activity.ivMyfollowingRouteDelete.setVisibility(0);
                this.activity.ivMyfollowingRouteEdit.setVisibility(0);
            } else {
                this.activity.ivMyfollowingRouteDelete.setVisibility(8);
                this.activity.ivMyfollowingRouteEdit.setVisibility(8);
                this.activity.ivMyfollowingRouteCopy.setVisibility(8);
            }
        } else {
            Intent intent = new Intent(getActivity(), (Class<?>) AddRouteActivity.class);
            intent.putExtra("id", this.datas.get(i).getId());
            startActivity(intent);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.activity.ivMyfollowingRouteDelete)) {
            for (RouteModel routeModel : this.checkList) {
                this.datas.remove(routeModel);
                DaoUtil.getInstance().deleteWayPointList(routeModel);
                this.mAdapter.setCheckBoxVisiblity(8);
                this.activity.ivMyfollowingRouteSearch.setVisibility(0);
                this.activity.ivMyfollowingRouteDelete.setVisibility(8);
                this.activity.ivMyfollowingRouteEdit.setVisibility(8);
                this.activity.ivMyfollowingRouteCopy.setVisibility(8);
                this.mAdapter.notifyDataSetChanged();
            }
        }
        if (view.equals(this.activity.ivMyfollowingRouteEdit)) {
            if (this.checkList.isEmpty()) {
                return;
            }
            RouteModel routeModel2 = this.checkList.get(0);
            this.activity.ivMyfollowingRouteSearch.setVisibility(0);
            this.activity.ivMyfollowingRouteDelete.setVisibility(8);
            this.activity.ivMyfollowingRouteEdit.setVisibility(8);
            this.activity.ivMyfollowingRouteCopy.setVisibility(8);
            this.mAdapter.setCheckBoxVisiblity(8);
            Intent intent = new Intent(getActivity(), (Class<?>) EditRouteActivity.class);
            intent.putExtra("id", routeModel2.getId());
            startActivity(intent);
        }
        if (view.equals(this.activity.ivMyfollowingRouteCopy)) {
            this.mAdapter.setCheckBoxVisiblity(8);
            this.activity.ivMyfollowingRouteSearch.setVisibility(0);
            this.activity.ivMyfollowingRouteDelete.setVisibility(8);
            this.activity.ivMyfollowingRouteEdit.setVisibility(8);
            this.activity.ivMyfollowingRouteCopy.setVisibility(8);
            Iterator<RouteModel> it = this.checkList.iterator();
            while (it.hasNext()) {
                DaoUtil.getInstance().copyRoute(it.next());
            }
        }
        reloadData();
    }

    @Override // im.helmsman.helmsmanandroid.view.SearchView.OnSearchViewTextChangeListener
    public void onTextChange(String str) {
        this.datas.clear();
        if (TextUtils.isEmpty(str)) {
            this.datas.addAll(DaoUtil.getInstance().getAllWayPointList());
            this.mAdapter.notifyDataSetChanged();
        } else {
            this.datas.addAll(RouteModel.where("name like ?", "%" + str + "%").find(RouteModel.class, true));
        }
        this.mAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveRoute(List<MLatLng> list) {
        if (list != null) {
            ViewUtils.ShowToast(R.string.route_download_success);
            DaoUtil.getInstance().saveWayPointList(list);
            List<RouteModel> allWayPointList = DaoUtil.getInstance().getAllWayPointList();
            this.datas.clear();
            this.datas.addAll(allWayPointList);
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        ViewUtils.ShowToast(R.string.route_no_route);
    }

    public void refreshListView() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        this.handler.postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.fragment.LocalRouteFragment.2
            @Override // java.lang.Runnable
            public void run() {
                LocalRouteFragment.this.swipeRefresh.autoRefresh();
            }
        }, 2000L);
    }

    class OnRefreshListener extends PtrDefaultHandler {
        OnRefreshListener() {
        }

        @Override // com.chanven.lib.cptr.PtrHandler
        public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
            LocalRouteFragment.this.swipeRefresh.refreshComplete();
            if (MyApplication.getInstance().wifiState) {
                ((LocalRoutePresenter) LocalRouteFragment.this.presenter).requestDownloadRoute();
                FragmentActivity activity = LocalRouteFragment.this.getActivity();
                if (activity == null || activity.isDestroyed()) {
                    return;
                }
                LocalRouteFragment.this.mPDLoading = new ProgressDialog(LocalRouteFragment.this.getActivity());
                LocalRouteFragment.this.mPDLoading.setMessage(LocalRouteFragment.this.getString(R.string.route_downloading));
                LocalRouteFragment.this.mPDLoading.setCancelable(false);
                LocalRouteFragment.this.mPDLoading.show();
                return;
            }
            ViewUtils.ShowToast(R.string.main_no_boat_connected_message);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void onDownloadProgress(int i, int i2) {
        this.mPDLoading.setMessage(getString(R.string.route_downloading) + ": " + i + "/" + i2);
        this.mPDLoading.setTitle(R.string.route_downloading);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void uploadFailureAndRetry() {
        ViewUtils.showAlertDialog(R.string.timeout, R.string.faile_and_retry, R.string.app_ok, 0, (DialogInterface.OnClickListener) null);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void dismissProgress() {
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void downloadFaile() {
        if (this.mPDLoading.isShowing() && getActivity() != null && !getActivity().isDestroyed()) {
            this.mPDLoading.cancel();
        }
        ViewUtils.ShowToast(R.string.route_failed);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void notRoute() {
        ViewUtils.ShowToast(R.string.not_routre);
        if (!this.mPDLoading.isShowing() || getActivity() == null || getActivity().isDestroyed()) {
            return;
        }
        this.mPDLoading.cancel();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LocalRouteView
    public void onDownloadFinish(final List<MLatLng> list) {
        this.handler.removeCallbacksAndMessages(null);
        ProgressDialog progressDialog = this.mPDLoading;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.mPDLoading.cancel();
        }
        if (DaoUtil.getInstance().isRepeatRoute(list)) {
            new AlertDialog.Builder(getActivity()).setTitle(R.string.routeactivity_isSave).setNegativeButton(R.string.routeactivity_no, (DialogInterface.OnClickListener) null).setPositiveButton(R.string.routeactivity_yes, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.LocalRouteFragment.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    LocalRouteFragment.this.saveRoute(list);
                }
            }).show();
        } else {
            saveRoute(list);
        }
    }

    @Subscribe
    public void onEventMainThread(DowloadCompleteMessage dowloadCompleteMessage) {
        this.handler.postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.fragment.LocalRouteFragment.4
            @Override // java.lang.Runnable
            public void run() {
                LocalRouteFragment.this.datas.clear();
                LocalRouteFragment.this.datas.addAll(DaoUtil.getInstance().getAllWayPointList());
                LocalRouteFragment.this.mAdapter.notifyDataSetChanged();
            }
        }, 1200L);
    }
}
