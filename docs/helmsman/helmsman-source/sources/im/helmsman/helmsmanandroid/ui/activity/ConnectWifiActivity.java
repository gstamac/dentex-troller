package im.helmsman.helmsmanandroid.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.ControlSSIDListAdapter;
import im.helmsman.helmsmanandroid.presenter.ConnectWifiPresenter;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.ConnectWifiView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ConnectWifiActivity extends Mvp2BaseActivity<ConnectWifiView, ConnectWifiPresenter> implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, ConnectWifiView {
    private Button btnCancel;
    private Button btnConnect;
    private AlertDialog dialog;
    private View dialogLayoutView;
    private ControlSSIDListAdapter listViewAdapter;
    private EditText mEtPassword;
    private ListView mLvWifiList;
    private SwipeRefreshLayout mSwipe;
    private ProgressDialog progressDialog;
    private Runnable refreshTimeRunnable;
    private String ssid;
    private Runnable timeOutRunnable;
    private List<String> datas = new ArrayList();
    private Handler handler = new Handler();

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public ConnectWifiPresenter initPresenter() {
        return new ConnectWifiPresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_connect_wifi);
        setStatusBar(R.color.red_state);
        initView();
        initEvent();
        this.handler.postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.ConnectWifiActivity.1
            @Override // java.lang.Runnable
            public void run() {
                ConnectWifiActivity.this.refreshListView();
            }
        }, 1000L);
    }

    private void initView() {
        View viewInflate = View.inflate(this, R.layout.dialog_wifi_password, null);
        this.dialogLayoutView = viewInflate;
        this.btnConnect = (Button) viewInflate.findViewById(R.id.btn_dialog_ssid_connect);
        this.btnCancel = (Button) this.dialogLayoutView.findViewById(R.id.btn_dialog_ssid_cancel);
        this.mEtPassword = (EditText) this.dialogLayoutView.findViewById(R.id.et_dialog_ssid_password);
        this.mLvWifiList = (ListView) findViewById(R.id.lv_connectwifi_list);
        this.mSwipe = (SwipeRefreshLayout) findViewById(R.id.swipe_connectwifi);
        ControlSSIDListAdapter controlSSIDListAdapter = new ControlSSIDListAdapter(this, this.datas);
        this.listViewAdapter = controlSSIDListAdapter;
        this.mLvWifiList.setAdapter((ListAdapter) controlSSIDListAdapter);
        this.listViewAdapter.notifyDataSetChanged();
    }

    private void initEvent() {
        this.btnCancel.setOnClickListener(this);
        this.btnConnect.setOnClickListener(this);
        this.mSwipe.setOnRefreshListener(this);
        this.mLvWifiList.setOnItemClickListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.timeOutRunnable);
        this.handler.removeCallbacks(this.refreshTimeRunnable);
        this.refreshTimeRunnable = null;
        this.timeOutRunnable = null;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.btnCancel)) {
            this.dialog.dismiss();
            return;
        }
        if (view.equals(this.btnConnect)) {
            ((ConnectWifiPresenter) this.presenter).connectWifi(this.ssid, this.mEtPassword.getText().toString());
            this.dialog.dismiss();
            showProgressDialog();
            ConnectWifiTimeOutRunnable connectWifiTimeOutRunnable = new ConnectWifiTimeOutRunnable();
            this.timeOutRunnable = connectWifiTimeOutRunnable;
            this.handler.postDelayed(connectWifiTimeOutRunnable, 30000L);
        }
    }

    private void showProgressDialog() {
        if (this.progressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            this.progressDialog = progressDialog;
            progressDialog.setTitle(getString(R.string.pulltorefreshlistadapter_pleasewait));
            this.progressDialog.setMessage(getString(R.string.connectwifiactivity_connecting));
            this.progressDialog.setCancelable(false);
        }
        this.progressDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshListView() {
        this.mSwipe.setRefreshing(true);
        onRefresh();
        ViewUtils.showProgressDialog(R.string.loading);
    }

    @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
    public void onRefresh() {
        if (this.refreshTimeRunnable != null) {
            return;
        }
        this.datas.clear();
        this.listViewAdapter.notifyDataSetChanged();
        Runnable runnable = new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.ConnectWifiActivity.2
            @Override // java.lang.Runnable
            public void run() {
                ConnectWifiActivity.this.mSwipe.setRefreshing(false);
                ConnectWifiActivity.this.refreshTimeRunnable = null;
            }
        };
        this.refreshTimeRunnable = runnable;
        this.handler.postDelayed(runnable, OkHttpUtils.DEFAULT_MILLISECONDS);
        ((ConnectWifiPresenter) this.presenter).scanWifi();
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        this.ssid = ((TextView) view.findViewById(R.id.tv_controlssid_ssid)).getText().toString();
        if (this.dialog == null) {
            AlertDialog alertDialogCreate = new AlertDialog.Builder(this).create();
            this.dialog = alertDialogCreate;
            alertDialogCreate.setView(this.dialogLayoutView);
        }
        this.dialog.setTitle(getString(R.string.connect_wifi) + ":" + this.ssid);
        this.dialog.show();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.ConnectWifiView
    public void connectWifiSuccess() {
        ViewUtils.ShowToast(getString(R.string.connect_success));
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        this.handler.removeCallbacks(this.timeOutRunnable);
        this.handler.postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.ConnectWifiActivity.3
            @Override // java.lang.Runnable
            public void run() {
                ConnectWifiActivity.this.finish();
                Base2Activity.finishActivity(BleBlueToothActivity.class);
                Base2Activity.finishActivity(AutoConnectBleActivity.class);
            }
        }, 500L);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.ConnectWifiView
    public void updateProgress(String str) {
        ViewUtils.setProgressDialogText(str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.ConnectWifiView
    public void initSSIDList(List<String> list) {
        this.datas.clear();
        this.datas.addAll(list);
        this.listViewAdapter.notifyDataSetChanged();
        this.mSwipe.setRefreshing(false);
        this.refreshTimeRunnable = null;
    }

    class ConnectWifiTimeOutRunnable implements Runnable {
        ConnectWifiTimeOutRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ConnectWifiActivity.this.progressDialog.dismiss();
            ViewUtils.ShowToast(R.string.connectwifiactivity_connectfailed);
        }
    }
}
