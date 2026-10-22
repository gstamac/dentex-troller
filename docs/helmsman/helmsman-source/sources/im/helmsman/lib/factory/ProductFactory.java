package im.helmsman.lib.factory;

import android.util.Log;
import im.helmsman.lib.ABaseProduct;
import im.helmsman.lib.product.HMMotor50736;
import im.helmsman.lib.product.HMMotorH1;

/* JADX INFO: loaded from: classes2.dex */
public class ProductFactory {
    public static final int TYPE50736 = 110;
    public static final int TYPE_H1 = 11;

    public static ABaseProduct createProduct(int i, int i2) {
        ABaseProduct hMMotorH1;
        if (i == 11) {
            hMMotorH1 = new HMMotorH1(i, i2);
            hMMotorH1.initComponent();
        } else if (i != 110) {
            hMMotorH1 = null;
        } else {
            hMMotorH1 = new HMMotor50736(i, i2);
            hMMotorH1.initComponent();
        }
        Log.d("creeateProduct", "type:" + i + " firwareVersion:" + i2);
        return hMMotorH1;
    }
}
