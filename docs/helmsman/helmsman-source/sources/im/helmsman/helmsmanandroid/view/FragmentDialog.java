package im.helmsman.helmsmanandroid.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

/* JADX INFO: loaded from: classes2.dex */
public class FragmentDialog extends DialogFragment {
    private final String defaultTag = "FRAGMENT_DIALOG";
    private FragmentActivity fragmentActivity;
    private DialogParams params;

    public FragmentDialog() {
    }

    public FragmentDialog(DialogParams dialogParams, FragmentActivity fragmentActivity) {
        this.params = dialogParams;
        this.fragmentActivity = fragmentActivity;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(this.params.mTitle).setMessage(this.params.mMessage).setPositiveButton(this.params.mPositiveButtonText, this.params.mPositiveButtonListener).setNegativeButton(this.params.mNegativeButtonText, this.params.mNegativeButtonListener).setCancelable(this.params.mCancelable).setView(this.params.mView);
        return builder.create();
    }

    public void show() {
        show(this.fragmentActivity, "FRAGMENT_DIALOG");
    }

    private void show(FragmentActivity fragmentActivity, String str) {
        if (fragmentActivity == null) {
            Log.w("FragmentDialog", "FragmentActivity  is null!");
        } else {
            super.show(fragmentActivity.getSupportFragmentManager(), str);
        }
    }

    public void show(String str) {
        show(this.fragmentActivity, str);
    }

    public static class Builder {
        private FragmentActivity fragmentActivity;
        private DialogParams p = new DialogParams();

        public Builder(FragmentActivity fragmentActivity) {
            this.fragmentActivity = fragmentActivity;
        }

        public FragmentDialog create() {
            FragmentDialog fragmentDialog = new FragmentDialog(this.p, this.fragmentActivity);
            this.p = null;
            this.fragmentActivity = null;
            return fragmentDialog;
        }

        public Builder setView(View view) {
            this.p.mView = view;
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.p.mTitle = charSequence;
            return this;
        }

        public Builder setTitle(int i) {
            this.p.mTitle = this.fragmentActivity.getString(i);
            return this;
        }

        public Builder setMessage(CharSequence charSequence) {
            this.p.mMessage = charSequence;
            return this;
        }

        public Builder setMessage(int i) {
            this.p.mMessage = this.fragmentActivity.getString(i);
            return this;
        }

        public Builder setCancelable(boolean z) {
            this.p.mCancelable = z;
            return this;
        }

        public Builder setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            this.p.mPositiveButtonText = charSequence;
            this.p.mPositiveButtonListener = onClickListener;
            return this;
        }

        public Builder setPositiveButton(int i, DialogInterface.OnClickListener onClickListener) {
            this.p.mPositiveButtonText = this.fragmentActivity.getString(i);
            this.p.mPositiveButtonListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            this.p.mNegativeButtonListener = onClickListener;
            this.p.mNegativeButtonText = charSequence;
            return this;
        }

        public Builder setNegativeButton(int i, DialogInterface.OnClickListener onClickListener) {
            this.p.mNegativeButtonListener = onClickListener;
            this.p.mNegativeButtonText = this.fragmentActivity.getString(i);
            return this;
        }
    }
}
