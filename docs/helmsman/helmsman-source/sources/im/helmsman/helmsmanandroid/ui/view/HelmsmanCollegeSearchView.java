package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.inet.model.ZendeskManualsListModelResult;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface HelmsmanCollegeSearchView extends MvpView {
    void initHelmsmanCollege(List<ZendeskManualsListModelResult.ResultsBean> list);

    void showGetListError(String str);
}
