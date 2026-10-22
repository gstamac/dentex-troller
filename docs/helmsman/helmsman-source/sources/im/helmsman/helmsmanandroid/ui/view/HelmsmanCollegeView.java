package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.inet.model.ArticlesResultModel;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface HelmsmanCollegeView extends MvpView {
    void initHelmsmanCollege(List<ArticlesResultModel.ArticlesBean> list);

    void showGetListError(String str);
}
