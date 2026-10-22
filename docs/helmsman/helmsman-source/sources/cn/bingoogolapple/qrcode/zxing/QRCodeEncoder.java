package cn.bingoogolapple.qrcode.zxing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.view.ViewCompat;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.qiniu.android.common.Constants;
import java.util.EnumMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class QRCodeEncoder {
    public static final Map<EncodeHintType, Object> HINTS;

    static {
        EnumMap enumMap = new EnumMap(EncodeHintType.class);
        HINTS = enumMap;
        enumMap.put(EncodeHintType.CHARACTER_SET, Constants.UTF_8);
        enumMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        enumMap.put(EncodeHintType.MARGIN, 0);
    }

    private QRCodeEncoder() {
    }

    public static Bitmap syncEncodeQRCode(String str, int i) {
        return syncEncodeQRCode(str, i, ViewCompat.MEASURED_STATE_MASK, -1, null);
    }

    public static Bitmap syncEncodeQRCode(String str, int i, int i2) {
        return syncEncodeQRCode(str, i, i2, -1, null);
    }

    public static Bitmap syncEncodeQRCode(String str, int i, int i2, Bitmap bitmap) {
        return syncEncodeQRCode(str, i, i2, -1, bitmap);
    }

    public static Bitmap syncEncodeQRCode(String str, int i, int i2, int i3, Bitmap bitmap) {
        try {
            BitMatrix bitMatrixEncode = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, i, i, HINTS);
            int[] iArr = new int[i * i];
            for (int i4 = 0; i4 < i; i4++) {
                for (int i5 = 0; i5 < i; i5++) {
                    if (bitMatrixEncode.get(i5, i4)) {
                        iArr[(i4 * i) + i5] = i2;
                    } else {
                        iArr[(i4 * i) + i5] = i3;
                    }
                }
            }
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
            bitmapCreateBitmap.setPixels(iArr, 0, i, 0, 0, i, i);
            return addLogoToQRCode(bitmapCreateBitmap, bitmap);
        } catch (Exception unused) {
            return null;
        }
    }

    private static Bitmap addLogoToQRCode(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null || bitmap2 == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int width2 = bitmap2.getWidth();
        int height2 = bitmap2.getHeight();
        float f = ((width * 1.0f) / 5.0f) / width2;
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            canvas.scale(f, f, width / 2, height / 2);
            canvas.drawBitmap(bitmap2, (width - width2) / 2, (height - height2) / 2, (Paint) null);
            canvas.save(31);
            canvas.restore();
            return bitmapCreateBitmap;
        } catch (Exception unused) {
            return null;
        }
    }
}
