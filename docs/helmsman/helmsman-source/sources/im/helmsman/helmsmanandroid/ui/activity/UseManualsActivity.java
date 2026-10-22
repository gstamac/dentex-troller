package im.helmsman.helmsmanandroid.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.UseManualsListAdapter;
import im.helmsman.helmsmanandroid.inet.model.ManualsListResultModel;
import im.helmsman.helmsmanandroid.presenter.UseManualsPresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.UseManualsView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.io.File;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class UseManualsActivity extends Mvp2BaseActivity<UseManualsView, UseManualsPresenter> implements UseManualsView, AdapterView.OnItemClickListener {
    private List<ManualsListResultModel.UserManualsBean> datas;

    @BindView(R.id.gv_manuals_list)
    GridView gvManualsList;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public UseManualsPresenter initPresenter() {
        return null;
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_use_manuals);
        new UseManualsPresenter(this).getManualsList(1);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.UseManualsView
    public void setGridViewDatas(List<ManualsListResultModel.UserManualsBean> list) {
        this.gvManualsList.setAdapter((ListAdapter) new UseManualsListAdapter(this, list));
        this.gvManualsList.setOnItemClickListener(this);
        this.datas = list;
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.UseManualsView
    public void showGetManualsErrorInfo(String str) {
        ViewUtils.ShowToast(getString(R.string.getManualError) + str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.UseManualsView
    public void hideProgress(View view) {
        getPb(view).setVisibility(8);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.UseManualsView
    public void showProgress(View view) {
        getPb(view).setVisibility(0);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.UseManualsView
    public void updateProgress(View view, int i) {
        getPb(view).setProgress(i);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.UseManualsView
    public void openPDF(View view, File file) {
        hideProgress(view);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(1);
        intent.setDataAndType(FileProvider.getUriForFile(MyApplication.getContext(), "im.helmsman.helmsmanandroid.fileProvider", file), "application/pdf");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            ViewUtils.ShowToast(R.string.cannot_open_usemanuals);
        } catch (Exception e) {
            Log.w("openPDF", e.getMessage() + "");
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.UseManualsView
    public void showDownLoadImage(View view) {
        getIv(view).setVisibility(0);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.UseManualsView
    public void showDownloadSuccess() {
        ViewUtils.ShowToast(R.string.route_download_success);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (getPb(view).getVisibility() == 0) {
            return;
        }
        showProgress(view);
        ManualsListResultModel.UserManualsBean userManualsBean = this.datas.get(i);
        new UseManualsPresenter(this).openManuals(userManualsBean.getId(), userManualsBean.getDownload_url(), view);
        getIv(view).setVisibility(8);
    }

    private ProgressBar getPb(View view) {
        return (ProgressBar) view.findViewById(R.id.pb_manuals);
    }

    private ImageView getIv(View view) {
        return (ImageView) view.findViewById(R.id.iv_manuals_download);
    }
}
