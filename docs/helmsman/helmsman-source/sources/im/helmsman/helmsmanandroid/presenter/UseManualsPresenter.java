package im.helmsman.helmsmanandroid.presenter;

import android.view.View;
import im.helmsman.helmsmanandroid.dao.UserManualsDao;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.ManualsListResultModel;
import im.helmsman.helmsmanandroid.model.UseManualsModel;
import im.helmsman.helmsmanandroid.model.imp.UseManualsModelImp;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.presenter.listener.OnDownLoadManualsListener;
import im.helmsman.helmsmanandroid.ui.view.UseManualsView;
import im.helmsman.helmsmanandroid.utils.FileUtils;
import java.io.File;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class UseManualsPresenter extends BasePresenter<UseManualsView> implements OnDownLoadManualsListener {
    public static final String MANUAL_PATH = FileUtils.getInnerSDCardPath() + "/Helmsman_Manuals/";
    private View itemView;
    private UseManualsModel model = new UseManualsModelImp(this);

    public UseManualsPresenter(UseManualsView useManualsView) {
        this.view = useManualsView;
    }

    public void getManualsList(int i) {
        RequestInetUtils.instance().getManualsList(i, new Callback<ManualsListResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.UseManualsPresenter.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<ManualsListResultModel> response) {
                ((UseManualsView) UseManualsPresenter.this.view).setGridViewDatas(response.body().getUser_manuals());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                ((UseManualsView) UseManualsPresenter.this.view).showGetManualsErrorInfo(errorCode.getMessage());
            }
        });
    }

    public void openManuals(int i, String str, View view) {
        File file = new File(MANUAL_PATH, i + ".pdf");
        if (file.exists()) {
            ((UseManualsView) this.view).openPDF(view, file);
        } else {
            this.model.downloadManual(i, file.getName(), str);
        }
        this.itemView = view;
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnDownLoadManualsListener
    public void onDownLoadFinish(int i, String str) {
        new UserManualsDao(str, i).save();
        ((UseManualsView) this.view).showDownloadSuccess();
        ((UseManualsView) this.view).hideProgress(this.itemView);
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnDownLoadManualsListener
    public void onDownLoadError(ErrorCode errorCode) {
        ((UseManualsView) this.view).showGetManualsErrorInfo(errorCode.getMessage());
        ((UseManualsView) this.view).showDownLoadImage(this.itemView);
        ((UseManualsView) this.view).hideProgress(this.itemView);
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnDownLoadManualsListener
    public void onDownLoadProgress(int i) {
        ((UseManualsView) this.view).updateProgress(this.itemView, i);
    }
}
