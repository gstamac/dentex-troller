package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.autonavi.ae.gmap.style.StyleElement;
import com.autonavi.ae.gmap.style.StyleItem;
import com.autonavi.amap.mapcore.Convert;
import com.autonavi.amap.mapcore.FileUtil;
import com.qiniu.android.common.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: StyleParser.java */
/* JADX INFO: loaded from: classes.dex */
public class da {
    private Context a;
    private int b = 0;
    private char c = '#';

    public da(Context context) {
        this.a = context;
    }

    public StyleItem[] a(String str) {
        Map<Integer, StyleItem> mapB = b(str);
        if (mapB != null && mapB.size() != 0) {
            ArrayList arrayList = new ArrayList();
            for (StyleItem styleItem : mapB.values()) {
                if (styleItem.isValid()) {
                    arrayList.add(styleItem);
                }
            }
            int size = arrayList.size();
            if (size > 0) {
                return (StyleItem[]) arrayList.toArray(new StyleItem[size]);
            }
        }
        return null;
    }

    public Map<Integer, StyleItem> b(String str) {
        HashMap map = new HashMap();
        try {
            byte[] fileContents = FileUtil.readFileContents(str);
            if (!b(map, fileContents)) {
                a(map, fileContents);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return map;
    }

    private void a(Map<Integer, StyleItem> map, byte[] bArr) {
        cx cxVarA = a(bArr);
        if (cxVarA == null || cxVarA.a() == null) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(cxVarA.a());
            JSONArray jSONArrayNames = jSONObject.names();
            for (int i = 0; i < jSONArrayNames.length(); i++) {
                String string = jSONArrayNames.getString(i);
                if (string != null && string.trim().equals("background")) {
                    this.b = c("#" + jSONObject.optString("background"));
                } else {
                    JSONObject jSONObject2 = jSONObject.getJSONObject(string);
                    if (jSONObject2 != null) {
                        a(string, map, jSONObject2);
                    }
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void a(String str, Map<Integer, StyleItem> map, JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return;
        }
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("subType");
        if (jSONObjectOptJSONObject == null) {
            a((String) null, str, map, jSONObject);
            return;
        }
        JSONArray jSONArrayNames = jSONObjectOptJSONObject.names();
        for (int i = 0; i < jSONArrayNames.length(); i++) {
            String string = jSONArrayNames.getString(i);
            a(str, string, map, jSONObjectOptJSONObject.getJSONObject(string));
        }
    }

    private void a(String str, String str2, Map<Integer, StyleItem> map, JSONObject jSONObject) throws JSONException {
        if (str != null && str.equals("roads") && str2.equals("subway")) {
            str2 = "subwayline";
        }
        int iA = db.a(str2);
        if (!jSONObject.optBoolean("visible", true)) {
            a(map, iA, cy.a("visible")).textureId = -1;
            return;
        }
        if (!jSONObject.optBoolean("showIcon", true)) {
            a(map, iA, cy.a("textFillColor")).textureId = -1;
        }
        if (!jSONObject.optBoolean("showLabel", true)) {
            a(map, iA, cy.a("textFillColor")).opacity = 0.0f;
            a(map, iA, cy.a("textStrokeColor")).opacity = 0.0f;
        }
        a(map, jSONObject, TypedValues.Custom.S_COLOR, "opacity", iA);
        a(map, jSONObject, "fillColor", "fillOpacity", iA);
        a(map, jSONObject, "strokeColor", "strokeOpacity", iA);
        a(map, jSONObject, "textFillColor", "textFillOpacity", iA);
        a(map, jSONObject, "textStrokeColor", "textStrokeOpacity", iA);
        a(map, jSONObject, "backgroundColor", "backgroundOpacity", iA);
    }

    private void a(Map<Integer, StyleItem> map, JSONObject jSONObject, String str, String str2, int i) {
        int iC;
        float fOptDouble;
        try {
            String strOptString = jSONObject.optString(str, null);
            if (TextUtils.isEmpty(strOptString)) {
                fOptDouble = (float) jSONObject.optDouble(str2, 1.0d);
                iC = 0;
            } else {
                iC = c("#" + strOptString);
                fOptDouble = 1.0f;
            }
            if (iC == 0 && fOptDouble == 1.0d) {
                return;
            }
            int iA = cy.a(str);
            StyleElement styleElementA = a(map, i, iA);
            styleElementA.value = iC;
            styleElementA.opacity = fOptDouble;
            if (i == 39) {
                a(map, i, 2).opacity = 0.0f;
            } else if (i == 12 && iA == 3) {
                StyleElement styleElementA2 = a(map, i, 2);
                styleElementA2.value = iC;
                styleElementA2.opacity = fOptDouble;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private cx a(byte[] bArr) {
        cx cxVar = null;
        try {
            cx cxVar2 = new cx();
            try {
                byte[] bytes = "l".getBytes(Constants.UTF_8);
                int length = bArr.length;
                int length2 = bytes.length;
                for (int i = 0; i < length; i++) {
                    bArr[i] = (byte) (bytes[i % length2] ^ bArr[i]);
                }
                cxVar2.a(Convert.getString(bArr, 0, 4));
                cxVar2.b(Convert.getString(bArr, 4, 32));
                cxVar2.c(Convert.getString(bArr, 36, 10));
                cxVar2.d(a(Convert.getSubBytes(bArr, 78, length - 78), Convert.getSubBytes(bArr, 46, 16), Convert.getSubBytes(bArr, 62, 16)));
                return cxVar2;
            } catch (Throwable th) {
                th = th;
                cxVar = cxVar2;
                th.printStackTrace();
                return cxVar;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private String a(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr3);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(2, secretKeySpec, ivParameterSpec);
            return new String(cipher.doFinal(bArr), Constants.UTF_8);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private boolean b(Map<Integer, StyleItem> map, byte[] bArr) {
        int iA;
        int iC;
        try {
            JSONArray jSONArray = new JSONArray(new String(bArr, "UTF-8"));
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                String string = jSONObject.getString("featureType");
                if (!TextUtils.isEmpty(string)) {
                    if (string.equals("background")) {
                        JSONObject jSONObject2 = jSONObject.getJSONObject("stylers");
                        if (jSONObject2 != null && (iC = c(jSONObject2.getString(TypedValues.Custom.S_COLOR))) != 0) {
                            this.b = iC;
                        }
                    } else {
                        int[] iArrA = cz.a(string);
                        if (iArrA != null && iArrA.length != 0) {
                            String string2 = jSONObject.getString("elementType");
                            if (!TextUtils.isEmpty(string2) && (iA = cy.a(string2)) != -1) {
                                a(map, jSONObject, iArrA, iA);
                            }
                        }
                    }
                }
            }
            return true;
        } catch (JSONException unused) {
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    private void a(Map<Integer, StyleItem> map, JSONObject jSONObject, int[] iArr, int i) throws JSONException {
        for (int i2 : iArr) {
            a(map, jSONObject, i2, i);
        }
    }

    private void a(Map<Integer, StyleItem> map, JSONObject jSONObject, int i, int i2) throws JSONException {
        int iC;
        StyleElement styleElementA = a(map, i, i2);
        JSONObject jSONObject2 = jSONObject.getJSONObject("stylers");
        if (jSONObject2 == null || (iC = c(jSONObject2.getString(TypedValues.Custom.S_COLOR))) == 0) {
            return;
        }
        styleElementA.value = iC;
        styleElementA.textureId = jSONObject2.optInt("textureid", 0);
        if (i >= 30 && i <= 38) {
            a(map, i, 4).opacity = 0.1f;
        } else if (i == 12 && i2 == 3) {
            a(map, i, 2).value = iC;
        }
    }

    private StyleElement a(Map<Integer, StyleItem> map, int i, int i2) {
        StyleItem styleItem = map.get(Integer.valueOf(i));
        if (styleItem == null) {
            styleItem = new StyleItem(i);
            map.put(Integer.valueOf(i), styleItem);
        }
        StyleElement styleElement = styleItem.get(i2);
        if (styleElement != null) {
            return styleElement;
        }
        StyleElement styleElement2 = new StyleElement();
        styleElement2.styleElementType = i2;
        styleItem.put(i2, styleElement2);
        return styleElement2;
    }

    private int c(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Color.parseColor(str);
        } catch (Throwable th) {
            th.printStackTrace();
            return 0;
        }
    }

    public int a() {
        return this.b;
    }
}
