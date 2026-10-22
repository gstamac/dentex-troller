package im.helmsman.helmsmanandroid.ui.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class RegisterSuccessInfoDialogFragment extends DialogFragment implements View.OnClickListener {
    private Button btnButton;
    private View view;

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().requestWindowFeature(1);
        this.view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_register_success_info, (ViewGroup) null);
        initView();
        return this.view;
    }

    private void initView() {
        Button button = (Button) this.view.findViewById(R.id.btn_registersuccess_ok);
        this.btnButton = button;
        button.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.btnButton)) {
            dismiss();
        }
    }
}
