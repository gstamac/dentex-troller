package im.helmsman.helmsmanandroid.presenter;

import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ArticlesResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.HelmsmanCollegeView;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class HelmsmanCollegePresenter extends BasePresenter<HelmsmanCollegeView> {
    public void getHelmsmanCollegeList() {
        RequestInetUtils.instance().getArticlesList(206471047, new Callback<ArticlesResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.HelmsmanCollegePresenter.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<ArticlesResultModel> response) {
                if (HelmsmanCollegePresenter.this.view != null) {
                    ((HelmsmanCollegeView) HelmsmanCollegePresenter.this.view).initHelmsmanCollege(response.body().getArticles());
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (HelmsmanCollegePresenter.this.view != null) {
                    ((HelmsmanCollegeView) HelmsmanCollegePresenter.this.view).showGetListError(errorCode.getMessage());
                }
            }
        });
    }
}
