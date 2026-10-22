package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.facebook.appevents.AppEventsConstants;
import java.nio.ByteBuffer;
import kotlin.UByte;
import org.json.JSONObject;

/* JADX INFO: compiled from: MapParser.java */
/* JADX INFO: loaded from: classes.dex */
public final class hy {
    private StringBuilder a = new StringBuilder();

    public final hp a(String str, Context context, he heVar) {
        hp hpVar = new hp("");
        hpVar.setErrorCode(7);
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("status") || !jSONObject.has("info")) {
                this.a.append("json is error " + str);
            }
            String string = jSONObject.getString("status");
            String string2 = jSONObject.getString("info");
            if (string.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                this.a.append("auth fail:" + string2);
            }
        } catch (Throwable th) {
            this.a.append("json exception error:" + th.getMessage());
            ij.a(th, "parser", "paseAuthFailurJson");
        }
        try {
            this.a.append("#SHA1AndPackage#").append(ew.e(context));
            String str2 = heVar.b.get("gsid").get(0);
            if (!TextUtils.isEmpty(str2)) {
                this.a.append(" #gsid#").append(str2);
            }
            String str3 = heVar.c;
            if (!TextUtils.isEmpty(str3)) {
                this.a.append(" #csid#" + str3);
            }
        } catch (Throwable unused) {
        }
        hpVar.setLocationDetail(this.a.toString());
        if (this.a.length() > 0) {
            StringBuilder sb = this.a;
            sb.delete(0, sb.length());
        }
        return hpVar;
    }

    public final hp a(byte[] bArr) {
        hp hpVar;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        ByteBuffer byteBuffer = null;
        try {
            hpVar = new hp("");
            if (bArr == null) {
                hpVar.setErrorCode(5);
                this.a.append("byte[] is null");
                hpVar.setLocationDetail(this.a.toString());
                StringBuilder sb = this.a;
                sb.delete(0, sb.length());
                return hpVar;
            }
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            if (byteBufferWrap.get() == 0) {
                hpVar.b(String.valueOf((int) byteBufferWrap.getShort()));
                byteBufferWrap.clear();
                if (byteBufferWrap != null) {
                    byteBufferWrap.clear();
                }
                return hpVar;
            }
            hpVar.setLongitude(im.a(((double) byteBufferWrap.getInt()) / 1000000.0d));
            hpVar.setLatitude(im.a(((double) byteBufferWrap.getInt()) / 1000000.0d));
            hpVar.setAccuracy(byteBufferWrap.getShort());
            hpVar.c(String.valueOf((int) byteBufferWrap.get()));
            hpVar.d(String.valueOf((int) byteBufferWrap.get()));
            if (byteBufferWrap.get() == 1) {
                byte[] bArr2 = new byte[byteBufferWrap.get() & UByte.MAX_VALUE];
                byteBufferWrap.get(bArr2);
                try {
                    hpVar.setCountry(new String(bArr2, "UTF-8"));
                } catch (Throwable unused) {
                }
                byte[] bArr3 = new byte[byteBufferWrap.get() & UByte.MAX_VALUE];
                byteBufferWrap.get(bArr3);
                try {
                    str = new String(bArr3, "UTF-8");
                    try {
                        hpVar.setProvince(str);
                    } catch (Throwable unused2) {
                    }
                } catch (Throwable unused3) {
                    str = "";
                }
                byte[] bArr4 = new byte[byteBufferWrap.get() & UByte.MAX_VALUE];
                byteBufferWrap.get(bArr4);
                try {
                    str2 = new String(bArr4, "UTF-8");
                    try {
                        hpVar.setCity(str2);
                    } catch (Throwable unused4) {
                    }
                } catch (Throwable unused5) {
                    str2 = "";
                }
                byte[] bArr5 = new byte[byteBufferWrap.get() & UByte.MAX_VALUE];
                byteBufferWrap.get(bArr5);
                try {
                    str3 = new String(bArr5, "UTF-8");
                    try {
                        hpVar.setDistrict(str3);
                    } catch (Throwable unused6) {
                    }
                } catch (Throwable unused7) {
                    str3 = "";
                }
                byte[] bArr6 = new byte[byteBufferWrap.get() & UByte.MAX_VALUE];
                byteBufferWrap.get(bArr6);
                try {
                    str4 = new String(bArr6, "UTF-8");
                    try {
                        hpVar.setStreet(str4);
                        hpVar.setRoad(str4);
                    } catch (Throwable unused8) {
                    }
                } catch (Throwable unused9) {
                    str4 = "";
                }
                byte[] bArr7 = new byte[byteBufferWrap.get() & UByte.MAX_VALUE];
                byteBufferWrap.get(bArr7);
                try {
                    hpVar.setNumber(new String(bArr7, "UTF-8"));
                } catch (Throwable unused10) {
                }
                byte[] bArr8 = new byte[byteBufferWrap.get() & UByte.MAX_VALUE];
                byteBufferWrap.get(bArr8);
                try {
                    str5 = new String(bArr8, "UTF-8");
                    try {
                        hpVar.setPoiName(str5);
                    } catch (Throwable unused11) {
                    }
                } catch (Throwable unused12) {
                    str5 = "";
                }
                byte[] bArr9 = new byte[byteBufferWrap.get() & UByte.MAX_VALUE];
                byteBufferWrap.get(bArr9);
                try {
                    hpVar.setAoiName(new String(bArr9, "UTF-8"));
                } catch (Throwable unused13) {
                }
                byte[] bArr10 = new byte[byteBufferWrap.get() & UByte.MAX_VALUE];
                byteBufferWrap.get(bArr10);
                try {
                    str6 = new String(bArr10, "UTF-8");
                    try {
                        hpVar.setAdCode(str6);
                    } catch (Throwable unused14) {
                    }
                } catch (Throwable unused15) {
                    str6 = "";
                }
                byte[] bArr11 = new byte[byteBufferWrap.get() & UByte.MAX_VALUE];
                byteBufferWrap.get(bArr11);
                try {
                    hpVar.setCityCode(new String(bArr11, "UTF-8"));
                } catch (Throwable unused16) {
                }
                StringBuilder sb2 = new StringBuilder();
                if (!TextUtils.isEmpty(str)) {
                    sb2.append(str).append(" ");
                }
                if (!TextUtils.isEmpty(str2) && (!str.contains("市") || !str.equals(str2))) {
                    sb2.append(str2).append(" ");
                }
                if (!TextUtils.isEmpty(str3)) {
                    sb2.append(str3).append(" ");
                }
                if (!TextUtils.isEmpty(str4)) {
                    sb2.append(str4).append(" ");
                }
                if (!TextUtils.isEmpty(str5)) {
                    if (!TextUtils.isEmpty(str6)) {
                        sb2.append("靠近");
                    }
                    sb2.append(str5).append(" ");
                }
                Bundle bundle = new Bundle();
                bundle.putString("citycode", hpVar.getCityCode());
                bundle.putString("desc", sb2.toString());
                bundle.putString("adcode", hpVar.getAdCode());
                hpVar.setExtras(bundle);
                hpVar.e(sb2.toString());
                String adCode = hpVar.getAdCode();
                hpVar.setAddress((adCode == null || adCode.trim().length() <= 0) ? sb2.toString() : sb2.toString().replace(" ", ""));
            }
            byteBufferWrap.get(new byte[byteBufferWrap.get() & UByte.MAX_VALUE]);
            if (byteBufferWrap.get() == 1) {
                byteBufferWrap.getInt();
                byteBufferWrap.getInt();
                byteBufferWrap.getShort();
            }
            if (byteBufferWrap.get() == 1) {
                byte[] bArr12 = new byte[byteBufferWrap.get() & UByte.MAX_VALUE];
                byteBufferWrap.get(bArr12);
                try {
                    hpVar.setBuildingId(new String(bArr12, "UTF-8"));
                } catch (Throwable unused17) {
                }
                byte[] bArr13 = new byte[byteBufferWrap.get() & UByte.MAX_VALUE];
                byteBufferWrap.get(bArr13);
                try {
                    hpVar.setFloor(new String(bArr13, "UTF-8"));
                } catch (Throwable unused18) {
                }
            }
            if (byteBufferWrap.get() == 1) {
                byteBufferWrap.get();
                byteBufferWrap.getInt();
                byteBufferWrap.get();
            }
            if (byteBufferWrap.get() == 1) {
                hpVar.setTime(byteBufferWrap.getLong());
            }
            byte[] bArr14 = new byte[byteBufferWrap.getShort()];
            byteBufferWrap.get(bArr14);
            try {
                hpVar.a(new String(bArr14, "UTF-8"));
            } catch (Throwable unused19) {
            }
            if (byteBufferWrap != null) {
                byteBufferWrap.clear();
            }
            if (this.a.length() > 0) {
                StringBuilder sb3 = this.a;
                sb3.delete(0, sb3.length());
            }
            return hpVar;
        } catch (Throwable th) {
            try {
                hp hpVar2 = new hp("");
                hpVar2.setErrorCode(5);
                this.a.append("parser data error:" + th.getMessage());
                hpVar2.setLocationDetail(this.a.toString());
                if (0 != 0) {
                    byteBuffer.clear();
                }
                hpVar = hpVar2;
            } catch (Throwable th2) {
                if (0 != 0) {
                    byteBuffer.clear();
                }
                throw th2;
            }
        }
    }
}
