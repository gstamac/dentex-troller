package im.helmsman.helmsmanandroid.utils;

/* JADX INFO: loaded from: classes2.dex */
public class UnitConvertUtil {
    public static double kmConvertMi(double d) {
        return d * 0.6213712d;
    }

    public static double kmConvertNmi(double d) {
        return d * 0.5399568d;
    }

    public static double kphConvertmph(double d) {
        return d * 0.6213711d;
    }

    public static double mConvertFT(double d) {
        return d * 3.2808399d;
    }

    public static double miConvertKm(double d) {
        return d / 0.6213712d;
    }

    public static double mConvertMi(double d) {
        return kmConvertMi(d / 1000.0d);
    }

    public static double mConvertNmi(double d) {
        return kmConvertNmi(d / 1000.0d);
    }
}
