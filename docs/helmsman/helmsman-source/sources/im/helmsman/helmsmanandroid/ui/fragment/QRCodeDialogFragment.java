package im.helmsman.helmsmanandroid.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.utils.ViewUtils;

/* JADX INFO: loaded from: classes2.dex */
public class QRCodeDialogFragment extends DialogFragment {
    public static final String QRCodeUrl = "QR_CODE_URL";
    private View view;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().requestWindowFeature(1);
        this.view = layoutInflater.inflate(R.layout.item_dialog_qrcode, (ViewGroup) null);
        createEnglishQRcode();
        return this.view;
    }

    private void createEnglishQRcode() {
        if (isDetached()) {
            return;
        }
        createQRcode();
    }

    private void createQRcode() {
        Bitmap bitmapSyncEncodeQRCode = QRCodeEncoder.syncEncodeQRCode(getArguments().getString(QRCodeUrl), ViewUtils.dpToPx(300), Color.parseColor("#000000"));
        if (bitmapSyncEncodeQRCode != null) {
            ((ImageView) this.view.findViewById(R.id.iv_item_dialog_qrcode)).setImageBitmap(bitmapSyncEncodeQRCode);
        } else {
            Toast.makeText(getActivity(), "生成英文二维码失败", 0).show();
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    public int show(FragmentTransaction fragmentTransaction, String str) {
        return super.show(fragmentTransaction, str);
    }

    @Override // androidx.fragment.app.DialogFragment
    public void show(FragmentManager fragmentManager, String str) {
        super.show(fragmentManager, str);
    }
}
