package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.facebook.internal.NativeProtocol;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.fragment.MyShareRouteFragment;
import im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment;
import im.helmsman.helmsmanandroid.utils.ViewUtils;

/* JADX INFO: loaded from: classes2.dex */
public class AccountActivity extends Base2Activity {
    public static final int ACTION_START_ROUTE_FRAGMENT = 1;
    private FragmentManager mFragManger;
    private Fragment mUserInfoFragment;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_account);
        this.mFragManger = getSupportFragmentManager();
        if (getIntent().getIntExtra(NativeProtocol.WEB_DIALOG_ACTION, 0) == 1) {
            Fragment fragmentFindFragmentById = this.mFragManger.findFragmentById(R.id.frame_account_base);
            this.mUserInfoFragment = fragmentFindFragmentById;
            if (fragmentFindFragmentById == null) {
                this.mFragManger.beginTransaction().add(R.id.frame_account_base, new MyShareRouteFragment()).commit();
                return;
            }
            return;
        }
        Fragment fragmentFindFragmentById2 = this.mFragManger.findFragmentById(R.id.frame_account_base);
        this.mUserInfoFragment = fragmentFindFragmentById2;
        if (fragmentFindFragmentById2 == null) {
            this.mUserInfoFragment = UserInfoFragment.getInstance();
            this.mFragManger.beginTransaction().add(R.id.frame_account_base, this.mUserInfoFragment).commit();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 0) {
            if (iArr != null && iArr[0] == 0) {
                startActivity(new Intent(this, (Class<?>) CaptureActivityVertical.class));
            } else {
                ViewUtils.ShowToast(R.string.camera_permission);
            }
        }
    }
}
