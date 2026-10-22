package im.helmsman.helmsmanandroid.CustomListener;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DoubleClickListener implements View.OnClickListener {
    private long clickTime = 0;

    public abstract void onDoubleClickListener();

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (System.currentTimeMillis() - this.clickTime <= 1000) {
            onDoubleClickListener();
        }
        this.clickTime = System.currentTimeMillis();
    }
}
