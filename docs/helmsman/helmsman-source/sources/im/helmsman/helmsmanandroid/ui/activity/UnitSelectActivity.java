package im.helmsman.helmsmanandroid.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.ui.common.BaseActivity;
import im.helmsman.helmsmanandroid.view.FragmentDialog;
import im.helmsman.helmsmanandroid.view.TitleBar;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: UnitSelectActivity.kt */
/* JADX INFO: loaded from: classes2.dex */
@Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\b\u0010\u0014\u001a\u00020\u0013H\u0002J\b\u0010\u0015\u001a\u00020\u0013H\u0002J\u0012\u0010\u0016\u001a\u00020\u00132\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\u0012\u0010\u0019\u001a\u00020\u00132\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u0082.¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"Lim/helmsman/helmsmanandroid/ui/activity/UnitSelectActivity;", "Lim/helmsman/helmsmanandroid/ui/common/BaseActivity;", "Landroid/view/View$OnClickListener;", "()V", "distanceUnit", "", "relDistanceConvert", "Landroid/widget/RelativeLayout;", "relSpeedConvert", "rgDistanceUnit", "Landroid/widget/RadioGroup;", "rgSpeedUnit", "speedUnit", "titleBar", "Lim/helmsman/helmsmanandroid/view/TitleBar;", "tvDistanceUnitState", "Landroid/widget/TextView;", "tvSpeedUnitState", "changeStateTextView", "", "initEvent", "initView", "onClick", "v", "Landroid/view/View;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "app_googleplayRelease"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class UnitSelectActivity extends BaseActivity implements View.OnClickListener {
    private int distanceUnit;
    private RelativeLayout relDistanceConvert;
    private RelativeLayout relSpeedConvert;
    private RadioGroup rgDistanceUnit;
    private RadioGroup rgSpeedUnit;
    private int speedUnit;
    private TitleBar titleBar;
    private TextView tvDistanceUnitState;
    private TextView tvSpeedUnitState;

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_select);
        initView();
        initEvent();
        changeStateTextView();
    }

    private final void initView() {
        View viewFindViewById = findViewById(R.id.tv_speed_unit);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById, "findViewById(...)");
        this.tvSpeedUnitState = (TextView) viewFindViewById;
        View viewFindViewById2 = findViewById(R.id.tv_distance_unit);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById2, "findViewById(...)");
        this.tvDistanceUnitState = (TextView) viewFindViewById2;
        View viewFindViewById3 = findViewById(R.id.titleBar);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById3, "findViewById(...)");
        this.titleBar = (TitleBar) viewFindViewById3;
        View viewFindViewById4 = findViewById(R.id.rel_unitconvert_speed);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById4, "findViewById(...)");
        this.relSpeedConvert = (RelativeLayout) viewFindViewById4;
        View viewFindViewById5 = findViewById(R.id.rel_unitconvert_distance);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById5, "findViewById(...)");
        this.relDistanceConvert = (RelativeLayout) viewFindViewById5;
    }

    private final void initEvent() {
        TitleBar titleBar = this.titleBar;
        RelativeLayout relativeLayout = null;
        if (titleBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("titleBar");
            titleBar = null;
        }
        titleBar.setLeftViewOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.UnitSelectActivity.initEvent.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                UnitSelectActivity.this.finish();
            }
        });
        RelativeLayout relativeLayout2 = this.relSpeedConvert;
        if (relativeLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("relSpeedConvert");
            relativeLayout2 = null;
        }
        UnitSelectActivity unitSelectActivity = this;
        relativeLayout2.setOnClickListener(unitSelectActivity);
        RelativeLayout relativeLayout3 = this.relDistanceConvert;
        if (relativeLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("relDistanceConvert");
        } else {
            relativeLayout = relativeLayout3;
        }
        relativeLayout.setOnClickListener(unitSelectActivity);
    }

    private final void changeStateTextView() {
        int i = Config.SPEED_UNIT;
        TextView textView = null;
        if (i == 0) {
            TextView textView2 = this.tvSpeedUnitState;
            if (textView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tvSpeedUnitState");
                textView2 = null;
            }
            textView2.setText("KPH");
        } else if (i == 1) {
            TextView textView3 = this.tvSpeedUnitState;
            if (textView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tvSpeedUnitState");
                textView3 = null;
            }
            textView3.setText("MPH");
        }
        int i2 = Config.DISTANCE_UNIT;
        if (i2 == 0) {
            TextView textView4 = this.tvDistanceUnitState;
            if (textView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tvDistanceUnitState");
            } else {
                textView = textView4;
            }
            textView.setText("M or KM");
            return;
        }
        if (i2 == 1) {
            TextView textView5 = this.tvDistanceUnitState;
            if (textView5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tvDistanceUnitState");
            } else {
                textView = textView5;
            }
            textView.setText("M or NM");
            return;
        }
        if (i2 != 2) {
            return;
        }
        TextView textView6 = this.tvDistanceUnitState;
        if (textView6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tvDistanceUnitState");
        } else {
            textView = textView6;
        }
        textView.setText("FT or NM");
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        Intrinsics.checkNotNull(v);
        RelativeLayout relativeLayout = this.relSpeedConvert;
        RadioGroup radioGroup = null;
        if (relativeLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("relSpeedConvert");
            relativeLayout = null;
        }
        if (v.equals(relativeLayout)) {
            this.speedUnit = Config.SPEED_UNIT;
            View viewInflate = LayoutInflater.from(this).inflate(R.layout.dialog_speed_unit_select, (ViewGroup) null, false);
            new FragmentDialog.Builder(this).setTitle(R.string.speed_unit).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.UnitSelectActivity$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    UnitSelectActivity.onClick$lambda$0(this.f$0, dialogInterface, i);
                }
            }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).setView(viewInflate).create().show();
            View viewFindViewById = viewInflate.findViewById(R.id.rg_speed_unit_select);
            Intrinsics.checkNotNullExpressionValue(viewFindViewById, "findViewById(...)");
            this.rgSpeedUnit = (RadioGroup) viewFindViewById;
            int i = Config.SPEED_UNIT;
            if (i == 0) {
                RadioGroup radioGroup2 = this.rgSpeedUnit;
                if (radioGroup2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rgSpeedUnit");
                    radioGroup2 = null;
                }
                radioGroup2.check(R.id.rb_speed_kph);
            } else if (i == 1) {
                RadioGroup radioGroup3 = this.rgSpeedUnit;
                if (radioGroup3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rgSpeedUnit");
                    radioGroup3 = null;
                }
                radioGroup3.check(R.id.rb_speed_mph);
            }
            RadioGroup radioGroup4 = this.rgSpeedUnit;
            if (radioGroup4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("rgSpeedUnit");
            } else {
                radioGroup = radioGroup4;
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.UnitSelectActivity$$ExternalSyntheticLambda1
                @Override // android.widget.RadioGroup.OnCheckedChangeListener
                public final void onCheckedChanged(RadioGroup radioGroup5, int i2) {
                    UnitSelectActivity.onClick$lambda$1(radioGroup5, i2);
                }
            });
            return;
        }
        RelativeLayout relativeLayout2 = this.relDistanceConvert;
        if (relativeLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("relDistanceConvert");
            relativeLayout2 = null;
        }
        if (v.equals(relativeLayout2)) {
            this.distanceUnit = Config.DISTANCE_UNIT;
            View viewInflate2 = LayoutInflater.from(this).inflate(R.layout.dialog_distance_unit_select, (ViewGroup) null, false);
            new FragmentDialog.Builder(this).setTitle(R.string.distance_unit).setPositiveButton(R.string.app_ok, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.UnitSelectActivity$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    UnitSelectActivity.onClick$lambda$2(this.f$0, dialogInterface, i2);
                }
            }).setNegativeButton(R.string.cancle, (DialogInterface.OnClickListener) null).setView(viewInflate2).create().show();
            View viewFindViewById2 = viewInflate2.findViewById(R.id.rg_distance_unit_select);
            Intrinsics.checkNotNullExpressionValue(viewFindViewById2, "findViewById(...)");
            this.rgDistanceUnit = (RadioGroup) viewFindViewById2;
            int i2 = Config.DISTANCE_UNIT;
            if (i2 == 0) {
                RadioGroup radioGroup5 = this.rgDistanceUnit;
                if (radioGroup5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rgDistanceUnit");
                    radioGroup5 = null;
                }
                radioGroup5.check(R.id.rb_distance_m_km);
            } else if (i2 == 1) {
                RadioGroup radioGroup6 = this.rgDistanceUnit;
                if (radioGroup6 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rgDistanceUnit");
                    radioGroup6 = null;
                }
                radioGroup6.check(R.id.rb_distance_M_NM);
            } else if (i2 == 2) {
                RadioGroup radioGroup7 = this.rgDistanceUnit;
                if (radioGroup7 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rgDistanceUnit");
                    radioGroup7 = null;
                }
                radioGroup7.check(R.id.rb_distance_FT_NM);
            }
            RadioGroup radioGroup8 = this.rgDistanceUnit;
            if (radioGroup8 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("rgDistanceUnit");
            } else {
                radioGroup = radioGroup8;
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.UnitSelectActivity$$ExternalSyntheticLambda3
                @Override // android.widget.RadioGroup.OnCheckedChangeListener
                public final void onCheckedChanged(RadioGroup radioGroup9, int i3) {
                    UnitSelectActivity.onClick$lambda$3(radioGroup9, i3);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onClick$lambda$0(UnitSelectActivity this$0, DialogInterface dialogInterface, int i) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Config.saveConfig();
        this$0.changeStateTextView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onClick$lambda$1(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_speed_kph /* 2131231369 */:
                Config.SPEED_UNIT = 0;
                break;
            case R.id.rb_speed_mph /* 2131231370 */:
                Config.SPEED_UNIT = 1;
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onClick$lambda$2(UnitSelectActivity this$0, DialogInterface dialogInterface, int i) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Config.saveConfig();
        this$0.changeStateTextView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onClick$lambda$3(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_distance_FT_NM /* 2131231366 */:
                Config.DISTANCE_UNIT = 2;
                break;
            case R.id.rb_distance_M_NM /* 2131231367 */:
                Config.DISTANCE_UNIT = 1;
                break;
            case R.id.rb_distance_m_km /* 2131231368 */:
                Config.DISTANCE_UNIT = 0;
                break;
        }
    }
}
