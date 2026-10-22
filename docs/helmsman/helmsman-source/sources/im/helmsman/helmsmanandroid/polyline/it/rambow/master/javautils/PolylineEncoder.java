package im.helmsman.helmsmanandroid.polyline.it.rambow.master.javautils;

import im.helmsman.helmsmanandroid.dao.MLatLng;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

/* JADX INFO: loaded from: classes2.dex */
public class PolylineEncoder {
    private HashMap<String, Double> bounds;
    private boolean forceEndpoints;
    private int numLevels;
    private double verySmall;
    private int zoomFactor;
    private double[] zoomLevelBreaks;

    public PolylineEncoder(int i, int i2, double d, boolean z) {
        this.numLevels = i;
        this.zoomFactor = i2;
        this.verySmall = d;
        this.forceEndpoints = z;
        this.zoomLevelBreaks = new double[i];
        for (int i3 = 0; i3 < i; i3++) {
            this.zoomLevelBreaks[i3] = Math.pow(this.zoomFactor, (i - i3) - 1) * d;
        }
    }

    public PolylineEncoder() {
        this.numLevels = 18;
        this.zoomFactor = 2;
        this.verySmall = 1.0E-5d;
        this.forceEndpoints = true;
        this.zoomLevelBreaks = new double[18];
        int i = 0;
        while (true) {
            int i2 = this.numLevels;
            if (i >= i2) {
                return;
            }
            this.zoomLevelBreaks[i] = this.verySmall * Math.pow(this.zoomFactor, (i2 - i) - 1);
            i++;
        }
    }

    public String dpEncode(List<MLatLng> list) {
        double d;
        char c;
        Track track = new Track();
        track.setTrackpoints(list);
        Stack stack = new Stack();
        double[] dArr = new double[track.getTrackpoints().size()];
        if (track.getTrackpoints().size() > 2) {
            char c2 = 0;
            stack.push(new int[]{0, track.getTrackpoints().size() - 1});
            int i = 0;
            double d2 = 0.0d;
            while (stack.size() > 0) {
                int[] iArr = (int[]) stack.pop();
                int i2 = iArr[c2] + 1;
                double d3 = 0.0d;
                while (i2 < iArr[1]) {
                    double dDistance = distance(track.getTrackpoints().get(i2), track.getTrackpoints().get(iArr[c2]), track.getTrackpoints().get(iArr[1]));
                    if (dDistance > d3) {
                        if (dDistance > d2) {
                            d2 = dDistance;
                            d3 = d2;
                        } else {
                            d3 = dDistance;
                        }
                        i = i2;
                    }
                    i2++;
                    c2 = 0;
                }
                if (d3 > this.verySmall) {
                    dArr[i] = d3;
                    c = 0;
                    stack.push(new int[]{iArr[0], i});
                    stack.push(new int[]{i, iArr[1]});
                } else {
                    c = 0;
                }
                c2 = c;
            }
            d = d2;
        } else {
            d = 0.0d;
        }
        String strReplace = replace(createEncodings(track.getTrackpoints(), dArr), "\\", "\\\\");
        encodeLevels(track.getTrackpoints(), dArr, d);
        return strReplace;
    }

    public String replace(String str, String str2, String str3) {
        String str4 = "";
        if (str.equals("")) {
            return "";
        }
        int length = 0;
        int iIndexOf = str.indexOf(str2, 0);
        while (iIndexOf != -1) {
            str4 = str4 + str.substring(length, iIndexOf) + str3;
            length = str2.length() + iIndexOf;
            iIndexOf = str.indexOf(str2, length);
        }
        return str4 + str.substring(length);
    }

    public double distance(MLatLng mLatLng, MLatLng mLatLng2, MLatLng mLatLng3) {
        if (mLatLng2.latitude == mLatLng3.latitude && mLatLng2.longitude == mLatLng3.longitude) {
            return Math.sqrt(Math.pow(mLatLng3.latitude - mLatLng.latitude, 2.0d) + Math.pow(mLatLng3.longitude - mLatLng.longitude, 2.0d));
        }
        double dPow = ((double) (((mLatLng.latitude - mLatLng2.latitude) * (mLatLng3.latitude - mLatLng2.latitude)) + ((mLatLng.longitude - mLatLng2.longitude) * (mLatLng3.longitude - mLatLng2.longitude)))) / (Math.pow(mLatLng3.latitude - mLatLng2.latitude, 2.0d) + Math.pow(mLatLng3.longitude - mLatLng2.longitude, 2.0d));
        double dSqrt = dPow <= 0.0d ? Math.sqrt(Math.pow(mLatLng.latitude - mLatLng2.latitude, 2.0d) + Math.pow(mLatLng.longitude - mLatLng2.longitude, 2.0d)) : 0.0d;
        if (dPow >= 1.0d) {
            dSqrt = Math.sqrt(Math.pow(mLatLng.latitude - mLatLng3.latitude, 2.0d) + Math.pow(mLatLng.longitude - mLatLng3.longitude, 2.0d));
        }
        return (0.0d >= dPow || dPow >= 1.0d) ? dSqrt : Math.sqrt(Math.pow(((double) (mLatLng.latitude - mLatLng2.latitude)) - (((double) (mLatLng3.latitude - mLatLng2.latitude)) * dPow), 2.0d) + Math.pow(((double) (mLatLng.longitude - mLatLng2.longitude)) - (dPow * ((double) (mLatLng3.longitude - mLatLng2.longitude))), 2.0d));
    }

    public static Track pointsToTrack(String str) {
        Track track = new Track();
        StringTokenizer stringTokenizer = new StringTokenizer(str, "\n");
        while (stringTokenizer.hasMoreTokens()) {
            String[] strArrSplit = stringTokenizer.nextToken().split(", ");
            track.addTrackpoint(new MLatLng(new Float(strArrSplit[0]).floatValue(), new Float(strArrSplit[1]).floatValue()));
        }
        return track;
    }

    public static Track kmlLineStringToTrack(String str) {
        Track track = new Track();
        new StringTokenizer(str, " ");
        return track;
    }

    public static Track pointsAndAltitudeToTrack(String str) {
        System.out.println("pointsAndAltitudeToTrack");
        Track track = new Track();
        StringTokenizer stringTokenizer = new StringTokenizer(str, "\n");
        while (stringTokenizer.hasMoreTokens()) {
            String[] strArrSplit = stringTokenizer.nextToken().split(",");
            track.addTrackpoint(new MLatLng(new Float(strArrSplit[1]).floatValue(), new Float(strArrSplit[0]).floatValue()));
            System.out.println(new Double(strArrSplit[1]).toString() + ", " + new Double(strArrSplit[0]).toString());
        }
        return track;
    }

    private static int floor1e5(double d) {
        return (int) Math.floor(d * 100000.0d);
    }

    private static String encodeSignedNumber(int i) {
        int i2 = i << 1;
        if (i < 0) {
            i2 = ~i2;
        }
        return encodeNumber(i2);
    }

    private static String encodeNumber(int i) {
        StringBuffer stringBuffer = new StringBuffer();
        while (i >= 32) {
            stringBuffer.append((char) ((32 | (i & 31)) + 63));
            i >>= 5;
        }
        stringBuffer.append((char) (i + 63));
        return stringBuffer.toString();
    }

    private String encodeLevels(List<MLatLng> list, double[] dArr, double d) {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.forceEndpoints) {
            stringBuffer.append(encodeNumber(this.numLevels - 1));
        } else {
            stringBuffer.append(encodeNumber((this.numLevels - computeLevel(d)) - 1));
        }
        for (int i = 1; i < list.size() - 1; i++) {
            double d2 = dArr[i];
            if (d2 != 0.0d) {
                stringBuffer.append(encodeNumber((this.numLevels - computeLevel(d2)) - 1));
            }
        }
        if (this.forceEndpoints) {
            stringBuffer.append(encodeNumber(this.numLevels - 1));
        } else {
            stringBuffer.append(encodeNumber((this.numLevels - computeLevel(d)) - 1));
        }
        return stringBuffer.toString();
    }

    private int computeLevel(double d) {
        int i = 0;
        if (d > this.verySmall) {
            while (d < this.zoomLevelBreaks[i]) {
                i++;
            }
        }
        return i;
    }

    private String createEncodings(List<MLatLng> list, double[] dArr) {
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        double d = 0.0d;
        double d2 = 0.0d;
        double d3 = 0.0d;
        double d4 = 0.0d;
        while (i < list.size()) {
            if (i == 0) {
                d = list.get(i).latitude;
                d3 = list.get(i).longitude;
                d2 = d;
                d4 = d3;
            } else if (list.get(i).latitude > d) {
                d = list.get(i).latitude;
            } else if (list.get(i).latitude < d2) {
                d2 = list.get(i).latitude;
            } else if (list.get(i).longitude > d3) {
                d3 = list.get(i).longitude;
            } else if (list.get(i).longitude < d4) {
                d4 = list.get(i).longitude;
            }
            if (dArr[i] != 0.0d || i == 0 || i == list.size() - 1) {
                MLatLng mLatLng = list.get(i);
                int iFloor1e5 = floor1e5(mLatLng.latitude);
                int iFloor1e6 = floor1e5(mLatLng.longitude);
                stringBuffer.append(encodeSignedNumber(iFloor1e5 - i2));
                stringBuffer.append(encodeSignedNumber(iFloor1e6 - i3));
                i3 = iFloor1e6;
                i2 = iFloor1e5;
            }
            i++;
            d = d;
        }
        HashMap<String, Double> map = new HashMap<>();
        map.put("maxlat", new Double(d));
        map.put("minlat", new Double(d2));
        map.put("maxlon", new Double(d3));
        map.put("minlon", new Double(d4));
        setBounds(map);
        return stringBuffer.toString();
    }

    private void setBounds(HashMap<String, Double> map) {
        this.bounds = map;
    }

    public static HashMap createEncodings(Track track, int i, int i2) {
        HashMap map = new HashMap();
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        ArrayList arrayList = (ArrayList) track.getTrackpoints();
        int size = arrayList.size();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i3 < size) {
            i4++;
            Trackpoint trackpoint = (Trackpoint) arrayList.get(i3);
            int iFloor1e5 = floor1e5(trackpoint.getLatDouble());
            int iFloor1e6 = floor1e5(trackpoint.getLonDouble());
            stringBuffer.append(encodeSignedNumber(iFloor1e5 - i5)).append(encodeSignedNumber(iFloor1e6 - i6));
            stringBuffer2.append(encodeNumber(i));
            i3 += i2;
            i6 = iFloor1e6;
            i5 = iFloor1e5;
        }
        System.out.println("listSize: " + size + " step: " + i2 + " counter: " + i4);
        map.put("encodedPoints", stringBuffer.toString());
        map.put("encodedLevels", stringBuffer2.toString());
        return map;
    }

    public HashMap<String, Double> getBounds() {
        return this.bounds;
    }
}
