package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.inet.model.ArticlesResultModel;
import im.helmsman.helmsmanandroid.inet.model.SectionResultModel;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public interface QuickStartView extends MvpView {
    void RefreshFinish();

    void initListViewData(List<SectionResultModel.SectionsBean> list, Map<Integer, List<ArticlesResultModel.ArticlesBean>> map);

    void loadListViewDataError(String str);
}
