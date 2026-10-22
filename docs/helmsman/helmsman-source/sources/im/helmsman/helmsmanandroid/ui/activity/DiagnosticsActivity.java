package im.helmsman.helmsmanandroid.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.helmsmanandroid.view.SwitchView;

/* JADX INFO: loaded from: classes2.dex */
public class DiagnosticsActivity extends Base2Activity implements SwitchView.OnSwitchStateChangedListener {

    @BindView(R.id.rel_diagnostics_clear)
    RelativeLayout relDiagnosticsClear;

    @BindView(R.id.rel_diagnostics_sendImmediately)
    RelativeLayout relDiagnosticsSendImmediately;

    @BindView(R.id.switch_diagnostics_autosend)
    SwitchView switchDiagnosticsAutosend;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_diagnostics);
        ButterKnife.bind(this);
        this.switchDiagnosticsAutosend.setState(Config.isUpLoadLog);
        this.switchDiagnosticsAutosend.setOnStateChangedListener(this);
    }

    @OnClick({R.id.rel_diagnostics_sendImmediately, R.id.rel_diagnostics_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rel_diagnostics_clear /* 2131231383 */:
                DaoUtil.getInstance().deleteAllLogFile();
                ViewUtils.ShowToast(R.string.deleteComplete);
                break;
            case R.id.rel_diagnostics_sendImmediately /* 2131231384 */:
                RequestInetUtils.instance().upLogFile();
                ViewUtils.ShowToast(R.string.route_uploading);
                break;
        }
    }

    @Override // im.helmsman.helmsmanandroid.view.SwitchView.OnSwitchStateChangedListener
    public void stateChangeListener(boolean z, int i) {
        Config.isUpLoadLog = z;
        Config.saveConfig();
    }
}
