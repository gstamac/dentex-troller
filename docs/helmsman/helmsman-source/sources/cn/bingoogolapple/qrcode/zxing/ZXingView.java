package cn.bingoogolapple.qrcode.zxing;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

/* JADX INFO: loaded from: classes.dex */
public class ZXingView extends QRCodeView {
    private MultiFormatReader mMultiFormatReader;

    public ZXingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZXingView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initMultiFormatReader();
    }

    private void initMultiFormatReader() {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        this.mMultiFormatReader = multiFormatReader;
        multiFormatReader.setHints(QRCodeDecoder.HINTS);
    }

    @Override // cn.bingoogolapple.qrcode.core.ProcessDataTask.Delegate
    public String processData(byte[] bArr, int i, int i2, boolean z) {
        Result resultDecodeWithState;
        PlanarYUVLuminanceSource planarYUVLuminanceSource;
        try {
            try {
                Rect scanBoxAreaRect = this.mScanBoxView.getScanBoxAreaRect(i2);
                if (scanBoxAreaRect != null) {
                    planarYUVLuminanceSource = new PlanarYUVLuminanceSource(bArr, i, i2, scanBoxAreaRect.left, scanBoxAreaRect.top, scanBoxAreaRect.width(), scanBoxAreaRect.height(), false);
                } else {
                    planarYUVLuminanceSource = new PlanarYUVLuminanceSource(bArr, i, i2, 0, 0, i, i2, false);
                }
                resultDecodeWithState = this.mMultiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(planarYUVLuminanceSource)));
                this.mMultiFormatReader.reset();
            } catch (Exception e) {
                e.printStackTrace();
                this.mMultiFormatReader.reset();
                resultDecodeWithState = null;
            }
            if (resultDecodeWithState != null) {
                return resultDecodeWithState.getText();
            }
            return null;
        } catch (Throwable th) {
            this.mMultiFormatReader.reset();
            throw th;
        }
    }
}
