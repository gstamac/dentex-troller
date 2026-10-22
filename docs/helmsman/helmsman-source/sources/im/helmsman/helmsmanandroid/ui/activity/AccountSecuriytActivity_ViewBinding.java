package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class AccountSecuriytActivity_ViewBinding implements Unbinder {
    private AccountSecuriytActivity target;

    public AccountSecuriytActivity_ViewBinding(AccountSecuriytActivity accountSecuriytActivity) {
        this(accountSecuriytActivity, accountSecuriytActivity.getWindow().getDecorView());
    }

    public AccountSecuriytActivity_ViewBinding(AccountSecuriytActivity accountSecuriytActivity, View view) {
        this.target = accountSecuriytActivity;
        accountSecuriytActivity.lvAccountSecuriytList = (ListView) Utils.findRequiredViewAsType(view, R.id.lv_account_securiyt_list, "field 'lvAccountSecuriytList'", ListView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        AccountSecuriytActivity accountSecuriytActivity = this.target;
        if (accountSecuriytActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        accountSecuriytActivity.lvAccountSecuriytList = null;
    }
}
