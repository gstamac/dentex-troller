package im.helmsman.helmsmanandroid.presenter;

import im.helmsman.helmsmanandroid.model.StartModel;
import im.helmsman.helmsmanandroid.model.imp.StartModelImp;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.StartView;

/* JADX INFO: loaded from: classes2.dex */
public class StartPresenter extends BasePresenter<StartView> {
    private StartModel startModel = new StartModelImp();

    public void updateContact() {
        this.startModel.downloadContacts();
    }
}
