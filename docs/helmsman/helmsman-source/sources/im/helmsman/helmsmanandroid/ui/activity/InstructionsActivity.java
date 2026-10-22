package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import javy.lib.jsave.JSave;

/* JADX INFO: loaded from: classes2.dex */
public class InstructionsActivity extends Base2Activity implements View.OnClickListener {
    private RelativeLayout mRelCollege;
    private RelativeLayout mRelCustomerService;
    private RelativeLayout mRelManuals;
    private RelativeLayout mRelQuickStart;
    private TextView tvPrivatePolicy;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_instructions);
        initView();
        initEvent();
    }

    protected void initView() {
        this.mRelQuickStart = (RelativeLayout) findViewById(R.id.rel_instruction_quick_start);
        this.mRelManuals = (RelativeLayout) findViewById(R.id.rel_instruction_manuals);
        this.mRelCustomerService = (RelativeLayout) findViewById(R.id.rel_instruction_customer_service);
        this.mRelCollege = (RelativeLayout) findViewById(R.id.rel_instruction_college);
        this.tvPrivatePolicy = (TextView) findViewById(R.id.tv_private_policy);
    }

    protected void initEvent() {
        this.mRelQuickStart.setOnClickListener(this);
        this.mRelManuals.setOnClickListener(this);
        this.mRelCustomerService.setOnClickListener(this);
        this.mRelCollege.setOnClickListener(this);
        this.tvPrivatePolicy.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.mRelCollege)) {
            startActivity(new Intent(this, (Class<?>) HelmsmanCollegeActivity.class));
            return;
        }
        if (view.equals(this.mRelQuickStart)) {
            startActivity(new Intent(this, (Class<?>) QuickStartActivity.class));
            return;
        }
        if (view.equals(this.mRelManuals)) {
            startActivity(new Intent(this, (Class<?>) UseManualsActivity.class));
            return;
        }
        if (view.equals(this.mRelCustomerService)) {
            Intent intent = new Intent(this, (Class<?>) MyRequestActivity.class);
            intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL);
            startActivity(intent);
            new Handler().postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.InstructionsActivity.1
                @Override // java.lang.Runnable
                public void run() {
                    if (UserMe.isLogin()) {
                        Intent intent2 = new Intent(Base2Activity.getTopActivity(), (Class<?>) MyRequestActivity.class);
                        intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL);
                        InstructionsActivity.this.startActivity(intent2);
                    }
                }
            }, 50L);
            return;
        }
        if (view.equals(this.tvPrivatePolicy)) {
            String str = (String) JSave.get("private_policy_url");
            Intent intent2 = new Intent(this, (Class<?>) WebViewActivity.class);
            if (TextUtils.isEmpty(str)) {
                str = "http://qnimg.helmsman.im/agreement/helmsman-pp.html";
            }
            intent2.putExtra("weburl", str);
            intent2.putExtra("title", getString(R.string.private_policy));
            startActivity(intent2);
        }
    }
}
