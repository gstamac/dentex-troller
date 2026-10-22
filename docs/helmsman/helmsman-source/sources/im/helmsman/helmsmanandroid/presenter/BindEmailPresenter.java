package im.helmsman.helmsmanandroid.presenter;

import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.BindEmailResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.BindEmailView;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class BindEmailPresenter extends BasePresenter<BindEmailView> {
    public void bindEmail(String str) {
        if (StringUtils.isEmail(str)) {
            RequestInetUtils.instance().bindEmail(str, new Callback<BindEmailResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.BindEmailPresenter.1
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<BindEmailResultModel> response) {
                    if (BindEmailPresenter.this.view != null) {
                        ((BindEmailView) BindEmailPresenter.this.view).bindEmailSuccess();
                        ((BindEmailView) BindEmailPresenter.this.view).dismissDialog();
                    }
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    if (BindEmailPresenter.this.view != null) {
                        ((BindEmailView) BindEmailPresenter.this.view).bindEmailError(errorCode.getMessage());
                        ((BindEmailView) BindEmailPresenter.this.view).dismissDialog();
                    }
                }
            });
        } else if (this.view != 0) {
            ((BindEmailView) this.view).emailFormatError();
            ((BindEmailView) this.view).dismissDialog();
        }
    }
}
