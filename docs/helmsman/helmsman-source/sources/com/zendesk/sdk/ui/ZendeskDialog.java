package com.zendesk.sdk.ui;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import com.zendesk.sdk.util.UiUtils;
import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public class ZendeskDialog extends DialogFragment implements Serializable {
    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(1, getTheme());
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        UiUtils.sizeDialogWidthForTablets(getDialog(), 0.66f);
    }
}
