package im.helmsman.helmsmanandroid.presenter;

import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.ZendeskManualsListModelResult;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.HelmsmanCollegeSearchView;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class HelmsmanCollegeSearchPresenter extends BasePresenter<HelmsmanCollegeSearchView> {
    public void getHelmsmanCollegeList(String str) {
        RequestInetUtils.instance().searchZendeskManualsList(str, new Callback<ZendeskManualsListModelResult>() { // from class: im.helmsman.helmsmanandroid.presenter.HelmsmanCollegeSearchPresenter.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<ZendeskManualsListModelResult> response) {
                if (HelmsmanCollegeSearchPresenter.this.view != null) {
                    ((HelmsmanCollegeSearchView) HelmsmanCollegeSearchPresenter.this.view).initHelmsmanCollege(response.body().getResults());
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (HelmsmanCollegeSearchPresenter.this.view != null) {
                    ((HelmsmanCollegeSearchView) HelmsmanCollegeSearchPresenter.this.view).showGetListError(errorCode.getMessage());
                }
            }
        });
    }
}
