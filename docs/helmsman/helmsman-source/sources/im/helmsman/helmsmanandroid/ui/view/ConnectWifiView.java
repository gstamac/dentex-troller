package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface ConnectWifiView extends MvpView {
    void connectWifiSuccess();

    void initSSIDList(List<String> list);

    void updateProgress(String str);
}
