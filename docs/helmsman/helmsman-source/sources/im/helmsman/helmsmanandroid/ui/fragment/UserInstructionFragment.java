package im.helmsman.helmsmanandroid.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class UserInstructionFragment extends Fragment implements View.OnClickListener, TextWatcher {
    protected static String MY_INSTRUCTION_CONTENT = "my_instruction_content";
    private InputMethodManager inputMethodManager;
    private Button mBtnFinish;
    private EditText mEtInstructionContent;
    private String mFirstText;
    private RelativeLayout mRelMyInstructionDismiss;
    private View mViewParent;

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mViewParent = layoutInflater.inflate(R.layout.fragment_user_instruction, (ViewGroup) null);
        initView();
        initEvent();
        setEtContent();
        return this.mViewParent;
    }

    private void initView() {
        this.mRelMyInstructionDismiss = (RelativeLayout) this.mViewParent.findViewById(R.id.rel_my_instruction_dismiss);
        this.mEtInstructionContent = (EditText) this.mViewParent.findViewById(R.id.tv_account_instruction_content);
        this.mBtnFinish = (Button) this.mViewParent.findViewById(R.id.btn_my_instruction_finish);
        FragmentActivity activity = getActivity();
        getContext();
        this.inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
    }

    private void initEvent() {
        this.mRelMyInstructionDismiss.setOnClickListener(this);
        this.mEtInstructionContent.addTextChangedListener(this);
        this.mBtnFinish.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.mRelMyInstructionDismiss)) {
            this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().show(UserInfoFragment.getInstance()).hide(this).commit();
            return;
        }
        if (view.equals(this.mBtnFinish)) {
            this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            setResult();
            FragmentManager fragmentManager2 = getFragmentManager();
            fragmentManager2.beginTransaction().show(UserInfoFragment.getInstance()).remove(this).commit();
        }
    }

    protected void setResult() {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(MY_INSTRUCTION_CONTENT, this.mEtInstructionContent.getText().toString());
        Fragment targetFragment = getTargetFragment();
        getActivity();
        targetFragment.onActivityResult(2, -1, intent);
    }

    private void setEtContent() {
        String string = getArguments().getString(UserInfoFragment.ACCONT_INSTRUCTION);
        this.mFirstText = string;
        if (string != null) {
            this.mEtInstructionContent.setText(string);
            this.mEtInstructionContent.setSelection(this.mFirstText.length());
        } else {
            this.mFirstText = "";
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (this.mFirstText.equals(this.mEtInstructionContent.getText().toString())) {
            this.mBtnFinish.setEnabled(false);
        } else {
            this.mBtnFinish.setEnabled(true);
        }
    }
}
