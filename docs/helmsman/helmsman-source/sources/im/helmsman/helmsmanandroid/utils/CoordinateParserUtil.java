package im.helmsman.helmsmanandroid.utils;

import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.dao.WayPoint;
import java.math.BigDecimal;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CoordinateParserUtil {
    public static String longitudeParser(double d) {
        StringBuffer stringBuffer = new StringBuffer();
        String strValueOf = String.valueOf(d);
        String[] strArrSplit = strValueOf.split("\\.");
        stringBuffer.append(strArrSplit[0]);
        stringBuffer.append("°");
        if (strValueOf.length() < 13) {
            stringBuffer.append(strArrSplit[1]);
            if (d >= 0.0d) {
                stringBuffer.append("'E");
            } else {
                stringBuffer.deleteCharAt(0);
                stringBuffer.append("'W");
            }
            return stringBuffer.toString();
        }
        stringBuffer.append(strArrSplit[1].substring(0, 7));
        if (Integer.valueOf(stringBuffer.substring(stringBuffer.length() - 1).toString()).intValue() > 4) {
            stringBuffer.replace(stringBuffer.length() - 2, stringBuffer.length() - 1, String.valueOf(Integer.valueOf(stringBuffer.substring(stringBuffer.length() - 2, stringBuffer.length() - 1).toString()).intValue() + 1));
        }
        StringBuffer stringBufferDeleteCharAt = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        if (d >= 0.0d) {
            stringBufferDeleteCharAt.append("'E");
        } else {
            stringBufferDeleteCharAt.deleteCharAt(0);
            stringBufferDeleteCharAt.append("'W");
        }
        return stringBufferDeleteCharAt.toString();
    }

    public static String latitudeParser(double d) {
        StringBuffer stringBuffer = new StringBuffer();
        String strValueOf = String.valueOf(d);
        String[] strArrSplit = strValueOf.split("\\.");
        stringBuffer.append(strArrSplit[0]);
        stringBuffer.append("°");
        if (strValueOf.length() < 13) {
            stringBuffer.append(strArrSplit[1]);
            if (d >= 0.0d) {
                stringBuffer.append("'N");
            } else {
                stringBuffer.deleteCharAt(0);
                stringBuffer.append("'S");
            }
            return stringBuffer.toString();
        }
        stringBuffer.append(strArrSplit[1].substring(0, 7));
        if (Integer.valueOf(stringBuffer.substring(stringBuffer.length() - 1).toString()).intValue() > 4) {
            stringBuffer.replace(stringBuffer.length() - 2, stringBuffer.length() - 1, String.valueOf(Integer.valueOf(stringBuffer.substring(stringBuffer.length() - 2, stringBuffer.length() - 1).toString()).intValue() + 1));
        }
        StringBuffer stringBufferDeleteCharAt = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        if (d >= 0.0d) {
            stringBufferDeleteCharAt.append("'N");
        } else {
            stringBufferDeleteCharAt.deleteCharAt(0);
            stringBufferDeleteCharAt.append("'S");
        }
        return stringBufferDeleteCharAt.toString();
    }

    public static String distanceParser(double d) {
        int i = Config.DISTANCE_UNIT;
        if (i == 0) {
            return distanceParserKMOrM(d);
        }
        if (i != 1) {
            return i != 2 ? "unknow distance" : distanceParserNmOrFt(d);
        }
        return distanceParserNmOrM(d);
    }

    public static String distanceParserKMOrM(double d) {
        StringBuffer stringBuffer = new StringBuffer();
        if (d == 0.0d) {
            return "0 M";
        }
        if (d <= 1000.0d) {
            String strValueOf = String.valueOf(d);
            String[] strArrSplit = strValueOf.split("\\.");
            stringBuffer.append(strArrSplit[0]);
            stringBuffer.append(".");
            if (strValueOf.length() < 11) {
                stringBuffer.append(strArrSplit[1]);
                StringBuffer stringBufferDeleteCharAt = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                stringBufferDeleteCharAt.append(" M");
                return stringBufferDeleteCharAt.toString();
            }
            stringBuffer.append(strArrSplit[1].substring(0, 2));
            if (Integer.valueOf(stringBuffer.substring(stringBuffer.length() - 1).toString()).intValue() > 4) {
                stringBuffer.replace(stringBuffer.length() - 2, stringBuffer.length() - 1, String.valueOf(Integer.valueOf(stringBuffer.substring(stringBuffer.length() - 2, stringBuffer.length() - 1).toString()).intValue() + 1));
            }
            StringBuffer stringBufferDeleteCharAt2 = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            stringBufferDeleteCharAt2.append(" M");
            return stringBufferDeleteCharAt2.toString();
        }
        return new BigDecimal(d / 1000.0d).setScale(1, 4).doubleValue() + " KM";
    }

    public static String distanceParserNmOrM(double d) {
        if (d <= 999.0d) {
            return new BigDecimal(d).setScale(1, 4).doubleValue() + " M";
        }
        return new BigDecimal(UnitConvertUtil.mConvertNmi(d)).setScale(1, 4).doubleValue() + " nm";
    }

    public static String distanceParserNmOrFt(double d) {
        if (d <= 999.0d) {
            return new BigDecimal(UnitConvertUtil.mConvertFT(d)).setScale(1, 4).doubleValue() + " ft";
        }
        return new BigDecimal(UnitConvertUtil.mConvertNmi(d)).setScale(1, 4).doubleValue() + " nm";
    }

    public static String computerAllPolineDis(List<WayPoint> list) {
        if (list == null) {
            return "";
        }
        double dComputeDistanceBetween = 0.0d;
        int i = 0;
        while (i < list.size() - 1) {
            MLatLng mLatLng = new MLatLng((float) list.get(i).getLatitude(), (float) list.get(i).getLongitude());
            i++;
            dComputeDistanceBetween += SphericalUtil.computeDistanceBetween(mLatLng, new MLatLng((float) list.get(i).getLatitude(), (float) list.get(i).getLongitude()));
        }
        return distanceParser(dComputeDistanceBetween);
    }

    public static double computerAllPointDis(List<MLatLng> list) {
        double dComputeDistanceBetween = 0.0d;
        if (list == null) {
            return 0.0d;
        }
        int i = 0;
        while (i < list.size() - 1) {
            MLatLng mLatLng = list.get(i);
            i++;
            dComputeDistanceBetween += SphericalUtil.computeDistanceBetween(mLatLng, list.get(i));
        }
        return dComputeDistanceBetween;
    }
}
