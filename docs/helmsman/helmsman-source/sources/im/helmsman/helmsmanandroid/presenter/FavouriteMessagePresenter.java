package im.helmsman.helmsmanandroid.presenter;

import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.FavouriteNotifitcationMessageListResultModel;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.FavouriteMessageView;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class FavouriteMessagePresenter extends BasePresenter<FavouriteMessageView> {
    private int page = 1;
    private int sinceId = 0;
    private int nextPage = 1;

    public void getFravouriteMessageNotifitcations() {
        RequestInetUtils.instance().getFravouriteMessageNotifitcations(this.sinceId, this.page, new Callback<FavouriteNotifitcationMessageListResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.FavouriteMessagePresenter.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<FavouriteNotifitcationMessageListResultModel> response) {
                FavouriteMessagePresenter.this.nextPage = response.body().getNext_page();
                FavouriteMessagePresenter.this.sinceId = response.body().getSince_id();
                if (FavouriteMessagePresenter.this.view != null) {
                    ((FavouriteMessageView) FavouriteMessagePresenter.this.view).initListView(response.body().getMessages());
                    if (FavouriteMessagePresenter.this.nextPage > FavouriteMessagePresenter.this.page) {
                        ((FavouriteMessageView) FavouriteMessagePresenter.this.view).enableLoadMore();
                    } else {
                        ((FavouriteMessageView) FavouriteMessagePresenter.this.view).disableLoadMore();
                    }
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (FavouriteMessagePresenter.this.view != null) {
                    ((FavouriteMessageView) FavouriteMessagePresenter.this.view).showLoadDataFaileMessage(errorCode.getMessage());
                }
            }
        });
    }

    public void refresh() {
        this.page = 1;
        this.sinceId = 0;
        this.nextPage = 1;
        getFravouriteMessageNotifitcations();
    }

    public void loadMore() {
        this.page++;
        getFravouriteMessageNotifitcations();
    }
}
