package im.helmsman.helmsmanandroid.presenter;

import im.helmsman.helmsmanandroid.model.SendMessageModel;
import im.helmsman.helmsmanandroid.model.imp.SendMessageModelImp;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.SendMessageView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes2.dex */
public class SendMessagePresenter extends BasePresenter<SendMessageView> {
    private SendMessageModel model = new SendMessageModelImp();

    public void saveHistory(String str) {
        Matcher matcher = Pattern.compile("@[A-Za-z0-9][A-Za-z0-9_]{4,}\\s").matcher(str);
        while (matcher.find()) {
            this.model.saveSendMessgeHistory(str.substring(matcher.start() + 1, matcher.end() - 1));
        }
    }
}
