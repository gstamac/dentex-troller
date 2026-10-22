package im.helmsman.helmsmanandroid.ui.activity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.FollowerUserModel;
import im.helmsman.helmsmanandroid.model.imp.StartModelImp;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.RSAUtils;
import im.helmsman.helmsmanandroid.utils.VibratorUtil;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class CaptureActivityVertical extends Base2Activity implements QRCodeView.Delegate {
    private QRCodeView qrCodeView;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_capture);
        QRCodeView qRCodeView = (QRCodeView) findViewById(R.id.zxingview);
        this.qrCodeView = qRCodeView;
        qRCodeView.setDelegate(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        this.qrCodeView.startCamera();
        this.qrCodeView.startSpot();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.qrCodeView.onDestroy();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        this.qrCodeView.stopCamera();
        super.onStop();
    }

    @Override // cn.bingoogolapple.qrcode.core.QRCodeView.Delegate
    public void onScanQRCodeSuccess(String str) {
        VibratorUtil.vibrator(this, new long[]{200, 200});
        if (str.indexOf("http://helmsman.im/users/me/follow?id=") < 0) {
            ViewUtils.ShowToast("无效的二维码...");
            this.qrCodeView.stopSpot();
            this.qrCodeView.startSpot();
        } else {
            int i = Integer.parseInt(str.substring(38));
            ViewUtils.ShowToast("正在关注...");
            finish();
            RequestInetUtils.instance().followUser(i, new Callback<FollowerUserModel>() { // from class: im.helmsman.helmsmanandroid.ui.activity.CaptureActivityVertical.1
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<FollowerUserModel> response) {
                    ViewUtils.ShowToast("关注成功");
                    new StartModelImp().downloadContacts();
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    ViewUtils.ShowToast(errorCode.getMessage());
                }
            });
        }
    }

    @Override // cn.bingoogolapple.qrcode.core.QRCodeView.Delegate
    public void onScanQRCodeOpenCameraError() {
        Log.i("qrcode", "result:error");
    }

    public int getPrivateKeyFromFile(String str) {
        InputStream inputStreamOpen;
        PrivateKey privateKeyLoadPrivateKey = null;
        try {
            inputStreamOpen = getResources().getAssets().open("rsa_private_key.pem");
        } catch (IOException e) {
            e.printStackTrace();
            inputStreamOpen = null;
        }
        try {
            privateKeyLoadPrivateKey = RSAUtils.loadPrivateKey(inputStreamOpen);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Log.e("decode", Base64.decode(str.getBytes(), 0).length + "");
        String str2 = new String(RSAUtils.decryptData(Base64.decode(str.getBytes(), 0), privateKeyLoadPrivateKey));
        return Integer.valueOf(str2.substring(str2.lastIndexOf("=") + 1)).intValue();
    }
}
