package com.zendesk.sdk.rating.ui;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.rating.RateMyAppButton;
import com.zendesk.sdk.util.UiUtils;
import com.zendesk.util.CollectionUtils;
import com.zendesk.util.StringUtils;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class RateMyAppButtonContainer extends LinearLayout {
    private static final int FALLBACK_DIVIDER_HEIGHT_DIP = 1;
    private static final int FALLBACK_TOP_PADDING_DIP = 16;
    private static final String LOG_TAG = "RateMyAppButtonContainer";
    private static final LinearLayout.LayoutParams TEXTVIEW_LAYOUT_PARAMS;
    private final List<RateMyAppButton> mButtons;
    private final Context mContext;
    private DismissableListener mDismissableListener;
    private RateMyAppSelectionListener mRateMyAppSelectionListener;

    interface DismissableListener {
        void dismissDialog();
    }

    public interface RateMyAppSelectionListener extends Serializable {
        void selectionMade(int i);
    }

    static {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        TEXTVIEW_LAYOUT_PARAMS = layoutParams;
        layoutParams.weight = 1.0f;
    }

    public RateMyAppButtonContainer(Context context, List<RateMyAppButton> list) {
        super(context);
        if (list == null || list.isEmpty()) {
            Logger.w(LOG_TAG, "No rate my app buttons supplied, dialog will be empty", new Object[0]);
        }
        this.mButtons = CollectionUtils.ensureEmpty(list);
        this.mContext = getContext();
        initialise();
    }

    public void setDismissableListener(DismissableListener dismissableListener) {
        this.mDismissableListener = dismissableListener;
    }

    public void setRateMyAppSelectionListener(RateMyAppSelectionListener rateMyAppSelectionListener) {
        this.mRateMyAppSelectionListener = rateMyAppSelectionListener;
    }

    private void initialise() {
        setOrientation(1);
        setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        int iRound = Math.round(getResources().getDimension(R.dimen.activity_horizontal_margin));
        int iThemeAttributeToPixels = UiUtils.themeAttributeToPixels(R.attr.RateMyAppPaddingTop, this.mContext, 1, 16.0f);
        int iThemeAttributeToPixels2 = UiUtils.themeAttributeToPixels(R.attr.RateMyAppPaddingBottom, this.mContext, 1, 16.0f);
        int iThemeAttributeToPixels3 = UiUtils.themeAttributeToPixels(R.attr.RateMyAppDividerHeight, this.mContext, 1, 1.0f);
        setWeightSum(this.mButtons.size() + 1);
        TextView textView = new TextView(getContext(), null, R.attr.RateMyAppTitleStyle);
        textView.setLayoutParams(TEXTVIEW_LAYOUT_PARAMS);
        textView.setText(R.string.rate_my_app_dialog_title_label);
        textView.setPadding(iRound, iThemeAttributeToPixels, iRound, iThemeAttributeToPixels2);
        addView(textView);
        addDividerView(iThemeAttributeToPixels3);
        Iterator<RateMyAppButton> it = this.mButtons.iterator();
        while (it.hasNext()) {
            if (addActionButton(iRound, iThemeAttributeToPixels, iThemeAttributeToPixels2, it.next())) {
                addDividerView(iThemeAttributeToPixels3);
            }
        }
    }

    private boolean addActionButton(int i, int i2, int i3, final RateMyAppButton rateMyAppButton) {
        if (rateMyAppButton == null || !StringUtils.hasLength(rateMyAppButton.getLabel()) || rateMyAppButton.getOnClickListener() == null) {
            Logger.w(LOG_TAG, "Can't add a null button or a button with no label or listener", new Object[0]);
            return false;
        }
        final TextView textView = new TextView(getContext(), null, rateMyAppButton.getStyleAttributeId());
        textView.setLayoutParams(TEXTVIEW_LAYOUT_PARAMS);
        textView.setText(rateMyAppButton.getLabel());
        textView.setPadding(i, i2, i, i3);
        textView.setId(rateMyAppButton.getId());
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.zendesk.sdk.rating.ui.RateMyAppButtonContainer.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (rateMyAppButton.getOnClickListener() != null) {
                    rateMyAppButton.getOnClickListener().onClick(textView);
                }
                if (RateMyAppButtonContainer.this.mDismissableListener != null && rateMyAppButton.shouldDismissDialog()) {
                    RateMyAppButtonContainer.this.mDismissableListener.dismissDialog();
                }
                if (RateMyAppButtonContainer.this.mRateMyAppSelectionListener == null || rateMyAppButton.getId() == -1) {
                    return;
                }
                RateMyAppButtonContainer.this.mRateMyAppSelectionListener.selectionMade(rateMyAppButton.getId());
            }
        });
        addView(textView);
        return true;
    }

    private void addDividerView(int i) {
        View view = new View(getContext(), null, R.attr.RateMyAppDividerStyle);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, i));
        addView(view);
    }
}
