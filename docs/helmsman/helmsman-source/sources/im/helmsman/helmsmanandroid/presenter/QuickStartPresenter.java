package im.helmsman.helmsmanandroid.presenter;

import android.util.Log;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ArticlesResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.SectionResultModel;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.QuickStartView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class QuickStartPresenter extends BasePresenter<QuickStartView> {
    private Map<Integer, List<ArticlesResultModel.ArticlesBean>> childs = new HashMap();
    private List<SectionResultModel.SectionsBean> sectionsBeens = new ArrayList();

    public void getSectionListData(int i) {
        RequestInetUtils.instance().getSectionList(i, new Callback<SectionResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.QuickStartPresenter.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<SectionResultModel> response) {
                QuickStartPresenter.this.sectionsBeens = response.body().getSections();
                Iterator it = QuickStartPresenter.this.sectionsBeens.iterator();
                while (it.hasNext()) {
                    QuickStartPresenter.this.getArticlesList(((SectionResultModel.SectionsBean) it.next()).getId());
                    Log.e("getSectionListData", "getSectionListData");
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                Log.e("getSectionListDataerror", errorCode.getMessage());
                if (QuickStartPresenter.this.view != null) {
                    ((QuickStartView) QuickStartPresenter.this.view).loadListViewDataError(errorCode.getMessage());
                }
            }
        });
    }

    public void getArticlesList(int i) {
        RequestInetUtils.instance().getArticlesList(i, new Callback<ArticlesResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.QuickStartPresenter.2
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<ArticlesResultModel> response) {
                ArticlesResultModel articlesResultModelBody = response.body();
                if (articlesResultModelBody.getArticles().size() > 0) {
                    QuickStartPresenter.this.childs.put(Integer.valueOf(articlesResultModelBody.getArticles().get(0).getSection_id()), articlesResultModelBody.getArticles());
                    if (QuickStartPresenter.this.view != null) {
                        ((QuickStartView) QuickStartPresenter.this.view).initListViewData(QuickStartPresenter.this.sectionsBeens, QuickStartPresenter.this.childs);
                    }
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (QuickStartPresenter.this.view != null) {
                    ((QuickStartView) QuickStartPresenter.this.view).loadListViewDataError(errorCode.getMessage());
                }
            }
        });
    }
}
