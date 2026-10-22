package im.helmsman.helmsmanandroid.ui.view;

import android.view.View;
import im.helmsman.helmsmanandroid.inet.model.ManualsListResultModel;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.io.File;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface UseManualsView extends MvpView {
    void hideProgress(View view);

    void openPDF(View view, File file);

    void setGridViewDatas(List<ManualsListResultModel.UserManualsBean> list);

    void showDownLoadImage(View view);

    void showDownloadSuccess();

    void showGetManualsErrorInfo(String str);

    void showProgress(View view);

    void updateProgress(View view, int i);
}
