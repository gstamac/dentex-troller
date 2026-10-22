package com.zendesk.sdk.ui;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;

/* JADX INFO: loaded from: classes2.dex */
public class ToolbarSherlock {
    private static final String LOG_TAG = "ToolbarSherlock";

    public static void installToolBar(AppCompatActivity appCompatActivity) {
        if (appCompatActivity.getSupportActionBar() != null) {
            Logger.d(LOG_TAG, "ActionBar available.", new Object[0]);
            return;
        }
        Logger.d(LOG_TAG, "No Actionbar detected. Installing Toolbar", new Object[0]);
        View viewFindViewById = appCompatActivity.findViewById(R.id.zd_toolbar_container);
        if (viewFindViewById == null) {
            Logger.d(LOG_TAG, "Unable to find toolbar in Activity.", new Object[0]);
            return;
        }
        viewFindViewById.setVisibility(0);
        appCompatActivity.setSupportActionBar((Toolbar) appCompatActivity.findViewById(R.id.zd_toolbar));
        appCompatActivity.getSupportActionBar().setElevation(appCompatActivity.getResources().getDimension(R.dimen.zd_toolbar_elevation));
    }
}
