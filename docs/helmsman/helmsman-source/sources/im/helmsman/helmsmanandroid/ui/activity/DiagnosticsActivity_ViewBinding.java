package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.view.SwitchView;

/* JADX INFO: loaded from: classes2.dex */
public class DiagnosticsActivity_ViewBinding implements Unbinder {
    private DiagnosticsActivity target;
    private View view7f080297;
    private View view7f080298;

    public DiagnosticsActivity_ViewBinding(DiagnosticsActivity diagnosticsActivity) {
        this(diagnosticsActivity, diagnosticsActivity.getWindow().getDecorView());
    }

    public DiagnosticsActivity_ViewBinding(final DiagnosticsActivity diagnosticsActivity, View view) {
        this.target = diagnosticsActivity;
        diagnosticsActivity.switchDiagnosticsAutosend = (SwitchView) Utils.findRequiredViewAsType(view, R.id.switch_diagnostics_autosend, "field 'switchDiagnosticsAutosend'", SwitchView.class);
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.rel_diagnostics_sendImmediately, "field 'relDiagnosticsSendImmediately' and method 'onClick'");
        diagnosticsActivity.relDiagnosticsSendImmediately = (RelativeLayout) Utils.castView(viewFindRequiredView, R.id.rel_diagnostics_sendImmediately, "field 'relDiagnosticsSendImmediately'", RelativeLayout.class);
        this.view7f080298 = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.DiagnosticsActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                diagnosticsActivity.onClick(view2);
            }
        });
        View viewFindRequiredView2 = Utils.findRequiredView(view, R.id.rel_diagnostics_clear, "field 'relDiagnosticsClear' and method 'onClick'");
        diagnosticsActivity.relDiagnosticsClear = (RelativeLayout) Utils.castView(viewFindRequiredView2, R.id.rel_diagnostics_clear, "field 'relDiagnosticsClear'", RelativeLayout.class);
        this.view7f080297 = viewFindRequiredView2;
        viewFindRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.DiagnosticsActivity_ViewBinding.2
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                diagnosticsActivity.onClick(view2);
            }
        });
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        DiagnosticsActivity diagnosticsActivity = this.target;
        if (diagnosticsActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        diagnosticsActivity.switchDiagnosticsAutosend = null;
        diagnosticsActivity.relDiagnosticsSendImmediately = null;
        diagnosticsActivity.relDiagnosticsClear = null;
        this.view7f080298.setOnClickListener(null);
        this.view7f080298 = null;
        this.view7f080297.setOnClickListener(null);
        this.view7f080297 = null;
    }
}
