package im.helmsman.helmsmanandroid.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.requests.RequestActivity;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.StringUtils;

/* JADX INFO: loaded from: classes2.dex */
public class MyRequestActivity extends RequestActivity implements View.OnClickListener, TextWatcher, DialogInterface.OnCancelListener {
    private Button btnCancel;
    private Button btnReset;
    private AlertDialog dialog;
    private String email;
    private ImageView mIvResetMailDismiss;
    private EditText teEmail;
    private String userName = "anonymous";
    private boolean isClickOK = false;

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // com.zendesk.sdk.requests.RequestActivity, com.zendesk.sdk.ui.NetworkAwareActionbarActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getRequestInfo();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadList() {
        Logger.setLoggable(false);
        ZendeskConfig.INSTANCE.setIdentity(new AnonymousIdentity.Builder().withNameIdentifier(this.userName).withEmailIdentifier(this.email).build());
        Logger.setLoggable(true);
        onNetworkAvailable();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        Base2Activity.remove(this);
        onNetworkUnavailable();
        AlertDialog alertDialog = this.dialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        Base2Activity.finishActivity(MyRequestActivity.class);
    }

    @Override // com.zendesk.sdk.requests.RequestActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.request_list, menu);
        return true;
    }

    @Override // com.zendesk.sdk.requests.RequestActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != 16908332) {
            if (itemId != R.id.action_create_ticket) {
                return true;
            }
            startActivity(new Intent(this, (Class<?>) MyContactZendeskActivity.class));
        }
        onBackPressed();
        return true;
    }

    private void getRequestInfo() {
        loadList();
        UserMe user = UserMe.getUser();
        if (user != null && UserMe.isLogin()) {
            this.userName = user.getUsername();
            this.email = user.getEmail();
            Base2Activity.push(this);
            return;
        }
        new Handler().postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.MyRequestActivity.1
            @Override // java.lang.Runnable
            public void run() {
                MyRequestActivity.this.showInPutEmailDialog();
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showInPutEmailDialog() {
        this.dialog = new AlertDialog.Builder(this).create();
        View viewInflate = View.inflate(this, R.layout.item_reset, null);
        this.teEmail = (EditText) viewInflate.findViewById(R.id.et_reset_mail);
        this.mIvResetMailDismiss = (ImageView) viewInflate.findViewById(R.id.iv_reset_mail_dismiss);
        this.btnReset = (Button) viewInflate.findViewById(R.id.btn_dialog_reset);
        this.btnCancel = (Button) viewInflate.findViewById(R.id.btn_dialog_cancel);
        this.btnReset.setOnClickListener(this);
        this.teEmail.addTextChangedListener(this);
        this.btnCancel.setOnClickListener(this);
        this.btnCancel.setVisibility(0);
        this.btnReset.setText(getString(R.string.setting_sure));
        this.btnReset.setEnabled(false);
        this.mIvResetMailDismiss.setImageResource(R.drawable.icon_login_dismiss);
        this.mIvResetMailDismiss.setVisibility(0);
        this.mIvResetMailDismiss.setEnabled(false);
        this.dialog.setView(viewInflate);
        this.dialog.setTitle(getString(R.string.your_email));
        this.dialog.setCancelable(false);
        try {
            this.dialog.show();
        } catch (WindowManager.BadTokenException unused) {
            if (isDestroyed()) {
                Base2Activity.getTopActivity().startActivity(new Intent(Base2Activity.getTopActivity(), (Class<?>) MyRequestActivity.class));
            }
        }
        this.dialog.setOnCancelListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.btnReset)) {
            this.email = this.teEmail.getText().toString();
            loadList();
            this.dialog.dismiss();
            this.isClickOK = true;
        }
        if (view.equals(this.btnCancel)) {
            this.dialog.dismiss();
            finish();
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (StringUtils.isEmail(editable.toString())) {
            this.btnReset.setEnabled(true);
            this.mIvResetMailDismiss.setImageResource(R.drawable.icon_login_correct);
        } else {
            this.btnReset.setEnabled(false);
            this.mIvResetMailDismiss.setImageResource(R.drawable.icon_login_dismiss);
        }
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        if (this.isClickOK) {
            return;
        }
        finish();
    }
}
