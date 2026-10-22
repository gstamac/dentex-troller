package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.amap.api.maps.AMapException;
import com.facebook.appevents.codeless.internal.Constants;
import com.facebook.internal.ServerProtocol;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kotlinx.coroutines.DebugKt;
import okhttp3.HttpUrl;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: AuthConfigManager.java */
/* JADX INFO: loaded from: classes.dex */
public class ex {
    public static int a = -1;
    public static String b = "";

    /* JADX INFO: compiled from: AuthConfigManager.java */
    public static class a {
        public b A;
        public String a;
        public int b = -1;
        public JSONObject c;
        public JSONObject d;
        public JSONObject e;
        public JSONObject f;
        public JSONObject g;
        public JSONObject h;
        public JSONObject i;
        public JSONObject j;
        public JSONObject k;
        public JSONObject l;
        public JSONObject m;
        public JSONObject n;
        public JSONObject o;
        public JSONObject p;
        public JSONObject q;
        public JSONObject r;
        public JSONObject s;
        public JSONObject t;
        public C0015a u;
        public d v;
        public c w;
        public b x;
        public b y;
        public b z;

        /* JADX INFO: renamed from: com.amap.api.mapcore.util.ex$a$a, reason: collision with other inner class name */
        /* JADX INFO: compiled from: AuthConfigManager.java */
        public static class C0015a {
            public boolean a;
            public boolean b;
            public JSONObject c;
        }

        /* JADX INFO: compiled from: AuthConfigManager.java */
        public static class b {
            public boolean a;
            public String b;
            public String c;
            public String d;
        }

        /* JADX INFO: compiled from: AuthConfigManager.java */
        public static class c {
            public String a;
            public String b;
        }

        /* JADX INFO: compiled from: AuthConfigManager.java */
        public static class d {
            public String a;
            public String b;
            public String c;
        }
    }

    public static boolean a(String str, boolean z) {
        try {
            String[] strArrSplit = URLDecoder.decode(str).split("/");
            return strArrSplit[strArrSplit.length - 1].charAt(4) % 2 == 1;
        } catch (Throwable unused) {
            return z;
        }
    }

    /* JADX WARN: Code duplicated, block: B:105:0x01b8 A[Catch: all -> 0x0397, TRY_LEAVE, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:109:0x01e9 A[Catch: all -> 0x01f0, TRY_LEAVE, TryCatch #4 {all -> 0x01f0, blocks: (B:107:0x01d0, B:109:0x01e9), top: B:195:0x01d0, outer: #22 }] */
    /* JADX WARN: Code duplicated, block: B:115:0x01fe A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:118:0x020c A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:121:0x021a A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:124:0x0228 A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:127:0x0236 A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:130:0x0244 A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:133:0x0252 A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:136:0x0260 A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:139:0x026e A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:142:0x027e A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:145:0x0296 A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:148:0x02ae A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:151:0x02be A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:154:0x02ce A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:157:0x02e6 A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:160:0x02f6 A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:163:0x0306 A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:166:0x031e A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:169:0x0336 A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:172:0x034e A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:175:0x035e A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:178:0x036e A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:181:0x037e A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:184:0x038e A[Catch: all -> 0x0397, TRY_LEAVE, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:195:0x01d0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:73:0x0143 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:74:0x0144  */
    /* JADX WARN: Code duplicated, block: B:76:0x014a  */
    /* JADX WARN: Code duplicated, block: B:80:0x015b A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:82:0x0162 A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:83:0x0165 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:84:0x0167  */
    /* JADX WARN: Code duplicated, block: B:86:0x016d A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:89:0x017d A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:92:0x018b A[Catch: all -> 0x0397, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:95:0x0198 A[Catch: all -> 0x0397, TRY_LEAVE, TryCatch #22 {all -> 0x0397, blocks: (B:78:0x0150, B:80:0x015b, B:82:0x0162, B:103:0x01b0, B:105:0x01b8, B:113:0x01f6, B:115:0x01fe, B:116:0x0204, B:118:0x020c, B:119:0x0212, B:121:0x021a, B:122:0x0220, B:124:0x0228, B:125:0x022e, B:127:0x0236, B:128:0x023c, B:130:0x0244, B:131:0x024a, B:133:0x0252, B:134:0x0258, B:136:0x0260, B:137:0x0266, B:139:0x026e, B:140:0x0276, B:142:0x027e, B:143:0x028e, B:145:0x0296, B:146:0x02a6, B:148:0x02ae, B:149:0x02b6, B:151:0x02be, B:152:0x02c6, B:154:0x02ce, B:155:0x02de, B:157:0x02e6, B:158:0x02ee, B:160:0x02f6, B:161:0x02fe, B:163:0x0306, B:164:0x0316, B:166:0x031e, B:167:0x032e, B:169:0x0336, B:170:0x0346, B:172:0x034e, B:173:0x0356, B:175:0x035e, B:176:0x0366, B:178:0x036e, B:179:0x0376, B:181:0x037e, B:182:0x0386, B:184:0x038e, B:112:0x01f1, B:102:0x01ab, B:86:0x016d, B:87:0x0171, B:89:0x017d, B:90:0x0183, B:92:0x018b, B:93:0x018f, B:95:0x0198, B:97:0x019d, B:99:0x01a3, B:107:0x01d0, B:109:0x01e9), top: B:197:0x0150, inners: #3, #4 }] */
    /* JADX WARN: Code duplicated, block: B:99:0x01a3 A[Catch: all -> 0x01aa, TRY_LEAVE, TryCatch #3 {all -> 0x01aa, blocks: (B:97:0x019d, B:99:0x01a3), top: B:193:0x019d, outer: #22 }] */
    public static a a(Context context, ff ffVar, String str, Map<String, String> map) {
        String str2;
        String str3;
        String str4;
        String str5;
        he heVarA;
        byte[] bArr;
        JSONObject jSONObject;
        int i;
        String str6;
        String str7;
        String str8;
        a.C0015a c0015a;
        JSONObject jSONObject2;
        String str9;
        JSONObject jSONObject3;
        String str10;
        String str11;
        a aVar = new a();
        String strA = null;
        try {
            try {
                gw gwVar = new gw();
                try {
                    str2 = DebugKt.DEBUG_PROPERTY_VALUE_OFF;
                    str3 = "11B";
                    try {
                        b bVar = new b(context, ffVar, str, map);
                        heVarA = gwVar.a(bVar, bVar.d());
                        if (heVarA != null) {
                            try {
                                bArr = heVarA.a;
                            } catch (ev e) {
                                e = e;
                                str4 = "11K";
                                str5 = "result";
                                bArr = null;
                                aVar.a = e.a();
                                fn.a(ffVar, "/v3/iasdkauth", e);
                                if (bArr == null) {
                                    return aVar;
                                }
                                if (TextUtils.isEmpty(strA)) {
                                    strA = fg.a(bArr);
                                }
                                try {
                                    jSONObject = new JSONObject(strA);
                                    if (jSONObject.has("status")) {
                                        i = jSONObject.getInt("status");
                                        if (i == 1) {
                                            a = 1;
                                        } else if (i == 0) {
                                            str6 = "authcsid";
                                            str7 = "authgsid";
                                            if (heVarA != null) {
                                                str6 = heVarA.c;
                                                str7 = heVarA.d;
                                            }
                                            fg.a(context, str6, str7, jSONObject);
                                            a = 0;
                                            if (jSONObject.has("info")) {
                                                b = jSONObject.getString("info");
                                            }
                                            fn.a(ffVar, "/v3/iasdkauth", b, str7, jSONObject.has("infocode") ? jSONObject.getString("infocode") : "");
                                            if (a == 0) {
                                                aVar.a = b;
                                                return aVar;
                                            }
                                        }
                                        try {
                                            if (jSONObject.has("ver")) {
                                                aVar.b = jSONObject.getInt("ver");
                                            }
                                        } catch (Throwable th) {
                                            fk.a(th, "AuthConfigManager", "loadConfigVer");
                                        }
                                        str8 = str5;
                                        if (fg.a(jSONObject, str8)) {
                                            c0015a = new a.C0015a();
                                            c0015a.a = false;
                                            c0015a.b = false;
                                            aVar.u = c0015a;
                                            jSONObject2 = jSONObject.getJSONObject(str8);
                                            str9 = str4;
                                            if (fg.a(jSONObject2, str9)) {
                                                try {
                                                    jSONObject3 = jSONObject2.getJSONObject(str9);
                                                    c0015a.a = a(jSONObject3.getString("able"), false);
                                                    str10 = str2;
                                                    if (jSONObject3.has(str10)) {
                                                        c0015a.c = jSONObject3.getJSONObject(str10);
                                                    }
                                                } catch (Throwable th2) {
                                                    fk.a(th2, "AuthConfigManager", "loadException");
                                                }
                                            }
                                            str11 = str3;
                                            if (fg.a(jSONObject2, str11)) {
                                                aVar.h = jSONObject2.getJSONObject(str11);
                                            }
                                            if (fg.a(jSONObject2, "11C")) {
                                                aVar.k = jSONObject2.getJSONObject("11C");
                                            }
                                            if (fg.a(jSONObject2, "11I")) {
                                                aVar.l = jSONObject2.getJSONObject("11I");
                                            }
                                            if (fg.a(jSONObject2, "11H")) {
                                                aVar.m = jSONObject2.getJSONObject("11H");
                                            }
                                            if (fg.a(jSONObject2, "11E")) {
                                                aVar.n = jSONObject2.getJSONObject("11E");
                                            }
                                            if (fg.a(jSONObject2, "11F")) {
                                                aVar.o = jSONObject2.getJSONObject("11F");
                                            }
                                            if (fg.a(jSONObject2, "13A")) {
                                                aVar.q = jSONObject2.getJSONObject("13A");
                                            }
                                            if (fg.a(jSONObject2, "13J")) {
                                                aVar.i = jSONObject2.getJSONObject("13J");
                                            }
                                            if (fg.a(jSONObject2, "11G")) {
                                                aVar.p = jSONObject2.getJSONObject("11G");
                                            }
                                            if (fg.a(jSONObject2, "001")) {
                                                JSONObject jSONObject4 = jSONObject2.getJSONObject("001");
                                                a.d dVar = new a.d();
                                                a(jSONObject4, dVar);
                                                aVar.v = dVar;
                                            }
                                            if (fg.a(jSONObject2, "002")) {
                                                JSONObject jSONObject5 = jSONObject2.getJSONObject("002");
                                                a.c cVar = new a.c();
                                                a(jSONObject5, cVar);
                                                aVar.w = cVar;
                                            }
                                            if (fg.a(jSONObject2, "006")) {
                                                aVar.r = jSONObject2.getJSONObject("006");
                                            }
                                            if (fg.a(jSONObject2, "010")) {
                                                aVar.s = jSONObject2.getJSONObject("010");
                                            }
                                            if (fg.a(jSONObject2, "11Z")) {
                                                JSONObject jSONObject6 = jSONObject2.getJSONObject("11Z");
                                                a.b bVar2 = new a.b();
                                                a(jSONObject6, bVar2);
                                                aVar.x = bVar2;
                                            }
                                            if (fg.a(jSONObject2, "135")) {
                                                aVar.j = jSONObject2.getJSONObject("135");
                                            }
                                            if (fg.a(jSONObject2, "13S")) {
                                                aVar.g = jSONObject2.getJSONObject("13S");
                                            }
                                            if (fg.a(jSONObject2, "121")) {
                                                JSONObject jSONObject7 = jSONObject2.getJSONObject("121");
                                                a.b bVar3 = new a.b();
                                                a(jSONObject7, bVar3);
                                                aVar.y = bVar3;
                                            }
                                            if (fg.a(jSONObject2, "122")) {
                                                JSONObject jSONObject8 = jSONObject2.getJSONObject("122");
                                                a.b bVar4 = new a.b();
                                                a(jSONObject8, bVar4);
                                                aVar.z = bVar4;
                                            }
                                            if (fg.a(jSONObject2, "123")) {
                                                JSONObject jSONObject9 = jSONObject2.getJSONObject("123");
                                                a.b bVar5 = new a.b();
                                                a(jSONObject9, bVar5);
                                                aVar.A = bVar5;
                                            }
                                            if (fg.a(jSONObject2, "011")) {
                                                aVar.c = jSONObject2.getJSONObject("011");
                                            }
                                            if (fg.a(jSONObject2, "012")) {
                                                aVar.d = jSONObject2.getJSONObject("012");
                                            }
                                            if (fg.a(jSONObject2, "013")) {
                                                aVar.e = jSONObject2.getJSONObject("013");
                                            }
                                            if (fg.a(jSONObject2, "014")) {
                                                aVar.f = jSONObject2.getJSONObject("014");
                                            }
                                            if (fg.a(jSONObject2, "145")) {
                                                aVar.t = jSONObject2.getJSONObject("145");
                                            }
                                        }
                                        return aVar;
                                    }
                                } catch (Throwable th3) {
                                    fk.a(th3, "AuthConfigManager", "loadConfig");
                                }
                                return aVar;
                            } catch (IllegalBlockSizeException unused) {
                                str4 = "11K";
                                str5 = "result";
                                bArr = null;
                            } catch (Throwable th4) {
                                th = th4;
                                str4 = "11K";
                                str5 = "result";
                                bArr = null;
                                fk.a(th, "ConfigManager", "loadConfig");
                                if (bArr == null) {
                                    return aVar;
                                }
                                if (TextUtils.isEmpty(strA)) {
                                    strA = fg.a(bArr);
                                }
                                jSONObject = new JSONObject(strA);
                                if (jSONObject.has("status")) {
                                    i = jSONObject.getInt("status");
                                    if (i == 1) {
                                        a = 1;
                                    } else if (i == 0) {
                                        str6 = "authcsid";
                                        str7 = "authgsid";
                                        if (heVarA != null) {
                                            str6 = heVarA.c;
                                            str7 = heVarA.d;
                                        }
                                        fg.a(context, str6, str7, jSONObject);
                                        a = 0;
                                        if (jSONObject.has("info")) {
                                            b = jSONObject.getString("info");
                                        }
                                        fn.a(ffVar, "/v3/iasdkauth", b, str7, jSONObject.has("infocode") ? jSONObject.getString("infocode") : "");
                                        if (a == 0) {
                                            aVar.a = b;
                                            return aVar;
                                        }
                                    }
                                    if (jSONObject.has("ver")) {
                                        aVar.b = jSONObject.getInt("ver");
                                    }
                                    str8 = str5;
                                    if (fg.a(jSONObject, str8)) {
                                        c0015a = new a.C0015a();
                                        c0015a.a = false;
                                        c0015a.b = false;
                                        aVar.u = c0015a;
                                        jSONObject2 = jSONObject.getJSONObject(str8);
                                        str9 = str4;
                                        if (fg.a(jSONObject2, str9)) {
                                            jSONObject3 = jSONObject2.getJSONObject(str9);
                                            c0015a.a = a(jSONObject3.getString("able"), false);
                                            str10 = str2;
                                            if (jSONObject3.has(str10)) {
                                                c0015a.c = jSONObject3.getJSONObject(str10);
                                            }
                                        }
                                        str11 = str3;
                                        if (fg.a(jSONObject2, str11)) {
                                            aVar.h = jSONObject2.getJSONObject(str11);
                                        }
                                        if (fg.a(jSONObject2, "11C")) {
                                            aVar.k = jSONObject2.getJSONObject("11C");
                                        }
                                        if (fg.a(jSONObject2, "11I")) {
                                            aVar.l = jSONObject2.getJSONObject("11I");
                                        }
                                        if (fg.a(jSONObject2, "11H")) {
                                            aVar.m = jSONObject2.getJSONObject("11H");
                                        }
                                        if (fg.a(jSONObject2, "11E")) {
                                            aVar.n = jSONObject2.getJSONObject("11E");
                                        }
                                        if (fg.a(jSONObject2, "11F")) {
                                            aVar.o = jSONObject2.getJSONObject("11F");
                                        }
                                        if (fg.a(jSONObject2, "13A")) {
                                            aVar.q = jSONObject2.getJSONObject("13A");
                                        }
                                        if (fg.a(jSONObject2, "13J")) {
                                            aVar.i = jSONObject2.getJSONObject("13J");
                                        }
                                        if (fg.a(jSONObject2, "11G")) {
                                            aVar.p = jSONObject2.getJSONObject("11G");
                                        }
                                        if (fg.a(jSONObject2, "001")) {
                                            JSONObject jSONObject10 = jSONObject2.getJSONObject("001");
                                            a.d dVar2 = new a.d();
                                            a(jSONObject10, dVar2);
                                            aVar.v = dVar2;
                                        }
                                        if (fg.a(jSONObject2, "002")) {
                                            JSONObject jSONObject11 = jSONObject2.getJSONObject("002");
                                            a.c cVar2 = new a.c();
                                            a(jSONObject11, cVar2);
                                            aVar.w = cVar2;
                                        }
                                        if (fg.a(jSONObject2, "006")) {
                                            aVar.r = jSONObject2.getJSONObject("006");
                                        }
                                        if (fg.a(jSONObject2, "010")) {
                                            aVar.s = jSONObject2.getJSONObject("010");
                                        }
                                        if (fg.a(jSONObject2, "11Z")) {
                                            JSONObject jSONObject12 = jSONObject2.getJSONObject("11Z");
                                            a.b bVar6 = new a.b();
                                            a(jSONObject12, bVar6);
                                            aVar.x = bVar6;
                                        }
                                        if (fg.a(jSONObject2, "135")) {
                                            aVar.j = jSONObject2.getJSONObject("135");
                                        }
                                        if (fg.a(jSONObject2, "13S")) {
                                            aVar.g = jSONObject2.getJSONObject("13S");
                                        }
                                        if (fg.a(jSONObject2, "121")) {
                                            JSONObject jSONObject13 = jSONObject2.getJSONObject("121");
                                            a.b bVar7 = new a.b();
                                            a(jSONObject13, bVar7);
                                            aVar.y = bVar7;
                                        }
                                        if (fg.a(jSONObject2, "122")) {
                                            JSONObject jSONObject14 = jSONObject2.getJSONObject("122");
                                            a.b bVar8 = new a.b();
                                            a(jSONObject14, bVar8);
                                            aVar.z = bVar8;
                                        }
                                        if (fg.a(jSONObject2, "123")) {
                                            JSONObject jSONObject15 = jSONObject2.getJSONObject("123");
                                            a.b bVar9 = new a.b();
                                            a(jSONObject15, bVar9);
                                            aVar.A = bVar9;
                                        }
                                        if (fg.a(jSONObject2, "011")) {
                                            aVar.c = jSONObject2.getJSONObject("011");
                                        }
                                        if (fg.a(jSONObject2, "012")) {
                                            aVar.d = jSONObject2.getJSONObject("012");
                                        }
                                        if (fg.a(jSONObject2, "013")) {
                                            aVar.e = jSONObject2.getJSONObject("013");
                                        }
                                        if (fg.a(jSONObject2, "014")) {
                                            aVar.f = jSONObject2.getJSONObject("014");
                                        }
                                        if (fg.a(jSONObject2, "145")) {
                                            aVar.t = jSONObject2.getJSONObject("145");
                                        }
                                    }
                                    return aVar;
                                }
                                return aVar;
                            }
                        } else {
                            bArr = null;
                        }
                        try {
                            byte[] bArr2 = new byte[16];
                            try {
                                byte[] bArr3 = new byte[bArr.length - 16];
                                str4 = "11K";
                                try {
                                    System.arraycopy(bArr, 0, bArr2, 0, 16);
                                    str5 = "result";
                                    try {
                                        System.arraycopy(bArr, 16, bArr3, 0, bArr.length - 16);
                                        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
                                        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                                        cipher.init(2, secretKeySpec, new IvParameterSpec(fg.b()));
                                        strA = fg.a(cipher.doFinal(bArr3));
                                    } catch (ev e2) {
                                        e = e2;
                                        heVarA = heVarA;
                                        aVar.a = e.a();
                                        fn.a(ffVar, "/v3/iasdkauth", e);
                                        if (bArr == null) {
                                            return aVar;
                                        }
                                        if (TextUtils.isEmpty(strA)) {
                                            strA = fg.a(bArr);
                                        }
                                        jSONObject = new JSONObject(strA);
                                        if (jSONObject.has("status")) {
                                            i = jSONObject.getInt("status");
                                            if (i == 1) {
                                                a = 1;
                                            } else if (i == 0) {
                                                str6 = "authcsid";
                                                str7 = "authgsid";
                                                if (heVarA != null) {
                                                    str6 = heVarA.c;
                                                    str7 = heVarA.d;
                                                }
                                                fg.a(context, str6, str7, jSONObject);
                                                a = 0;
                                                if (jSONObject.has("info")) {
                                                    b = jSONObject.getString("info");
                                                }
                                                fn.a(ffVar, "/v3/iasdkauth", b, str7, jSONObject.has("infocode") ? jSONObject.getString("infocode") : "");
                                                if (a == 0) {
                                                    aVar.a = b;
                                                    return aVar;
                                                }
                                            }
                                            if (jSONObject.has("ver")) {
                                                aVar.b = jSONObject.getInt("ver");
                                            }
                                            str8 = str5;
                                            if (fg.a(jSONObject, str8)) {
                                                c0015a = new a.C0015a();
                                                c0015a.a = false;
                                                c0015a.b = false;
                                                aVar.u = c0015a;
                                                jSONObject2 = jSONObject.getJSONObject(str8);
                                                str9 = str4;
                                                if (fg.a(jSONObject2, str9)) {
                                                    jSONObject3 = jSONObject2.getJSONObject(str9);
                                                    c0015a.a = a(jSONObject3.getString("able"), false);
                                                    str10 = str2;
                                                    if (jSONObject3.has(str10)) {
                                                        c0015a.c = jSONObject3.getJSONObject(str10);
                                                    }
                                                }
                                                str11 = str3;
                                                if (fg.a(jSONObject2, str11)) {
                                                    aVar.h = jSONObject2.getJSONObject(str11);
                                                }
                                                if (fg.a(jSONObject2, "11C")) {
                                                    aVar.k = jSONObject2.getJSONObject("11C");
                                                }
                                                if (fg.a(jSONObject2, "11I")) {
                                                    aVar.l = jSONObject2.getJSONObject("11I");
                                                }
                                                if (fg.a(jSONObject2, "11H")) {
                                                    aVar.m = jSONObject2.getJSONObject("11H");
                                                }
                                                if (fg.a(jSONObject2, "11E")) {
                                                    aVar.n = jSONObject2.getJSONObject("11E");
                                                }
                                                if (fg.a(jSONObject2, "11F")) {
                                                    aVar.o = jSONObject2.getJSONObject("11F");
                                                }
                                                if (fg.a(jSONObject2, "13A")) {
                                                    aVar.q = jSONObject2.getJSONObject("13A");
                                                }
                                                if (fg.a(jSONObject2, "13J")) {
                                                    aVar.i = jSONObject2.getJSONObject("13J");
                                                }
                                                if (fg.a(jSONObject2, "11G")) {
                                                    aVar.p = jSONObject2.getJSONObject("11G");
                                                }
                                                if (fg.a(jSONObject2, "001")) {
                                                    JSONObject jSONObject16 = jSONObject2.getJSONObject("001");
                                                    a.d dVar3 = new a.d();
                                                    a(jSONObject16, dVar3);
                                                    aVar.v = dVar3;
                                                }
                                                if (fg.a(jSONObject2, "002")) {
                                                    JSONObject jSONObject17 = jSONObject2.getJSONObject("002");
                                                    a.c cVar3 = new a.c();
                                                    a(jSONObject17, cVar3);
                                                    aVar.w = cVar3;
                                                }
                                                if (fg.a(jSONObject2, "006")) {
                                                    aVar.r = jSONObject2.getJSONObject("006");
                                                }
                                                if (fg.a(jSONObject2, "010")) {
                                                    aVar.s = jSONObject2.getJSONObject("010");
                                                }
                                                if (fg.a(jSONObject2, "11Z")) {
                                                    JSONObject jSONObject18 = jSONObject2.getJSONObject("11Z");
                                                    a.b bVar10 = new a.b();
                                                    a(jSONObject18, bVar10);
                                                    aVar.x = bVar10;
                                                }
                                                if (fg.a(jSONObject2, "135")) {
                                                    aVar.j = jSONObject2.getJSONObject("135");
                                                }
                                                if (fg.a(jSONObject2, "13S")) {
                                                    aVar.g = jSONObject2.getJSONObject("13S");
                                                }
                                                if (fg.a(jSONObject2, "121")) {
                                                    JSONObject jSONObject19 = jSONObject2.getJSONObject("121");
                                                    a.b bVar11 = new a.b();
                                                    a(jSONObject19, bVar11);
                                                    aVar.y = bVar11;
                                                }
                                                if (fg.a(jSONObject2, "122")) {
                                                    JSONObject jSONObject110 = jSONObject2.getJSONObject("122");
                                                    a.b bVar12 = new a.b();
                                                    a(jSONObject110, bVar12);
                                                    aVar.z = bVar12;
                                                }
                                                if (fg.a(jSONObject2, "123")) {
                                                    JSONObject jSONObject111 = jSONObject2.getJSONObject("123");
                                                    a.b bVar13 = new a.b();
                                                    a(jSONObject111, bVar13);
                                                    aVar.A = bVar13;
                                                }
                                                if (fg.a(jSONObject2, "011")) {
                                                    aVar.c = jSONObject2.getJSONObject("011");
                                                }
                                                if (fg.a(jSONObject2, "012")) {
                                                    aVar.d = jSONObject2.getJSONObject("012");
                                                }
                                                if (fg.a(jSONObject2, "013")) {
                                                    aVar.e = jSONObject2.getJSONObject("013");
                                                }
                                                if (fg.a(jSONObject2, "014")) {
                                                    aVar.f = jSONObject2.getJSONObject("014");
                                                }
                                                if (fg.a(jSONObject2, "145")) {
                                                    aVar.t = jSONObject2.getJSONObject("145");
                                                }
                                            }
                                            return aVar;
                                        }
                                        return aVar;
                                    } catch (IllegalBlockSizeException unused2) {
                                    } catch (Throwable th5) {
                                        th = th5;
                                        heVarA = heVarA;
                                        fk.a(th, "ConfigManager", "loadConfig");
                                        if (bArr == null) {
                                            return aVar;
                                        }
                                        if (TextUtils.isEmpty(strA)) {
                                            strA = fg.a(bArr);
                                        }
                                        jSONObject = new JSONObject(strA);
                                        if (jSONObject.has("status")) {
                                            i = jSONObject.getInt("status");
                                            if (i == 1) {
                                                a = 1;
                                            } else if (i == 0) {
                                                str6 = "authcsid";
                                                str7 = "authgsid";
                                                if (heVarA != null) {
                                                    str6 = heVarA.c;
                                                    str7 = heVarA.d;
                                                }
                                                fg.a(context, str6, str7, jSONObject);
                                                a = 0;
                                                if (jSONObject.has("info")) {
                                                    b = jSONObject.getString("info");
                                                }
                                                fn.a(ffVar, "/v3/iasdkauth", b, str7, jSONObject.has("infocode") ? jSONObject.getString("infocode") : "");
                                                if (a == 0) {
                                                    aVar.a = b;
                                                    return aVar;
                                                }
                                            }
                                            if (jSONObject.has("ver")) {
                                                aVar.b = jSONObject.getInt("ver");
                                            }
                                            str8 = str5;
                                            if (fg.a(jSONObject, str8)) {
                                                c0015a = new a.C0015a();
                                                c0015a.a = false;
                                                c0015a.b = false;
                                                aVar.u = c0015a;
                                                jSONObject2 = jSONObject.getJSONObject(str8);
                                                str9 = str4;
                                                if (fg.a(jSONObject2, str9)) {
                                                    jSONObject3 = jSONObject2.getJSONObject(str9);
                                                    c0015a.a = a(jSONObject3.getString("able"), false);
                                                    str10 = str2;
                                                    if (jSONObject3.has(str10)) {
                                                        c0015a.c = jSONObject3.getJSONObject(str10);
                                                    }
                                                }
                                                str11 = str3;
                                                if (fg.a(jSONObject2, str11)) {
                                                    aVar.h = jSONObject2.getJSONObject(str11);
                                                }
                                                if (fg.a(jSONObject2, "11C")) {
                                                    aVar.k = jSONObject2.getJSONObject("11C");
                                                }
                                                if (fg.a(jSONObject2, "11I")) {
                                                    aVar.l = jSONObject2.getJSONObject("11I");
                                                }
                                                if (fg.a(jSONObject2, "11H")) {
                                                    aVar.m = jSONObject2.getJSONObject("11H");
                                                }
                                                if (fg.a(jSONObject2, "11E")) {
                                                    aVar.n = jSONObject2.getJSONObject("11E");
                                                }
                                                if (fg.a(jSONObject2, "11F")) {
                                                    aVar.o = jSONObject2.getJSONObject("11F");
                                                }
                                                if (fg.a(jSONObject2, "13A")) {
                                                    aVar.q = jSONObject2.getJSONObject("13A");
                                                }
                                                if (fg.a(jSONObject2, "13J")) {
                                                    aVar.i = jSONObject2.getJSONObject("13J");
                                                }
                                                if (fg.a(jSONObject2, "11G")) {
                                                    aVar.p = jSONObject2.getJSONObject("11G");
                                                }
                                                if (fg.a(jSONObject2, "001")) {
                                                    JSONObject jSONObject112 = jSONObject2.getJSONObject("001");
                                                    a.d dVar4 = new a.d();
                                                    a(jSONObject112, dVar4);
                                                    aVar.v = dVar4;
                                                }
                                                if (fg.a(jSONObject2, "002")) {
                                                    JSONObject jSONObject113 = jSONObject2.getJSONObject("002");
                                                    a.c cVar4 = new a.c();
                                                    a(jSONObject113, cVar4);
                                                    aVar.w = cVar4;
                                                }
                                                if (fg.a(jSONObject2, "006")) {
                                                    aVar.r = jSONObject2.getJSONObject("006");
                                                }
                                                if (fg.a(jSONObject2, "010")) {
                                                    aVar.s = jSONObject2.getJSONObject("010");
                                                }
                                                if (fg.a(jSONObject2, "11Z")) {
                                                    JSONObject jSONObject114 = jSONObject2.getJSONObject("11Z");
                                                    a.b bVar14 = new a.b();
                                                    a(jSONObject114, bVar14);
                                                    aVar.x = bVar14;
                                                }
                                                if (fg.a(jSONObject2, "135")) {
                                                    aVar.j = jSONObject2.getJSONObject("135");
                                                }
                                                if (fg.a(jSONObject2, "13S")) {
                                                    aVar.g = jSONObject2.getJSONObject("13S");
                                                }
                                                if (fg.a(jSONObject2, "121")) {
                                                    JSONObject jSONObject115 = jSONObject2.getJSONObject("121");
                                                    a.b bVar15 = new a.b();
                                                    a(jSONObject115, bVar15);
                                                    aVar.y = bVar15;
                                                }
                                                if (fg.a(jSONObject2, "122")) {
                                                    JSONObject jSONObject116 = jSONObject2.getJSONObject("122");
                                                    a.b bVar16 = new a.b();
                                                    a(jSONObject116, bVar16);
                                                    aVar.z = bVar16;
                                                }
                                                if (fg.a(jSONObject2, "123")) {
                                                    JSONObject jSONObject117 = jSONObject2.getJSONObject("123");
                                                    a.b bVar17 = new a.b();
                                                    a(jSONObject117, bVar17);
                                                    aVar.A = bVar17;
                                                }
                                                if (fg.a(jSONObject2, "011")) {
                                                    aVar.c = jSONObject2.getJSONObject("011");
                                                }
                                                if (fg.a(jSONObject2, "012")) {
                                                    aVar.d = jSONObject2.getJSONObject("012");
                                                }
                                                if (fg.a(jSONObject2, "013")) {
                                                    aVar.e = jSONObject2.getJSONObject("013");
                                                }
                                                if (fg.a(jSONObject2, "014")) {
                                                    aVar.f = jSONObject2.getJSONObject("014");
                                                }
                                                if (fg.a(jSONObject2, "145")) {
                                                    aVar.t = jSONObject2.getJSONObject("145");
                                                }
                                            }
                                            return aVar;
                                        }
                                        return aVar;
                                    }
                                } catch (ev e3) {
                                    e = e3;
                                    str5 = "result";
                                    heVarA = heVarA;
                                    aVar.a = e.a();
                                    fn.a(ffVar, "/v3/iasdkauth", e);
                                    if (bArr == null) {
                                        return aVar;
                                    }
                                    if (TextUtils.isEmpty(strA)) {
                                        strA = fg.a(bArr);
                                    }
                                    jSONObject = new JSONObject(strA);
                                    if (jSONObject.has("status")) {
                                        i = jSONObject.getInt("status");
                                        if (i == 1) {
                                            a = 1;
                                        } else if (i == 0) {
                                            str6 = "authcsid";
                                            str7 = "authgsid";
                                            if (heVarA != null) {
                                                str6 = heVarA.c;
                                                str7 = heVarA.d;
                                            }
                                            fg.a(context, str6, str7, jSONObject);
                                            a = 0;
                                            if (jSONObject.has("info")) {
                                                b = jSONObject.getString("info");
                                            }
                                            fn.a(ffVar, "/v3/iasdkauth", b, str7, jSONObject.has("infocode") ? jSONObject.getString("infocode") : "");
                                            if (a == 0) {
                                                aVar.a = b;
                                                return aVar;
                                            }
                                        }
                                        if (jSONObject.has("ver")) {
                                            aVar.b = jSONObject.getInt("ver");
                                        }
                                        str8 = str5;
                                        if (fg.a(jSONObject, str8)) {
                                            c0015a = new a.C0015a();
                                            c0015a.a = false;
                                            c0015a.b = false;
                                            aVar.u = c0015a;
                                            jSONObject2 = jSONObject.getJSONObject(str8);
                                            str9 = str4;
                                            if (fg.a(jSONObject2, str9)) {
                                                jSONObject3 = jSONObject2.getJSONObject(str9);
                                                c0015a.a = a(jSONObject3.getString("able"), false);
                                                str10 = str2;
                                                if (jSONObject3.has(str10)) {
                                                    c0015a.c = jSONObject3.getJSONObject(str10);
                                                }
                                            }
                                            str11 = str3;
                                            if (fg.a(jSONObject2, str11)) {
                                                aVar.h = jSONObject2.getJSONObject(str11);
                                            }
                                            if (fg.a(jSONObject2, "11C")) {
                                                aVar.k = jSONObject2.getJSONObject("11C");
                                            }
                                            if (fg.a(jSONObject2, "11I")) {
                                                aVar.l = jSONObject2.getJSONObject("11I");
                                            }
                                            if (fg.a(jSONObject2, "11H")) {
                                                aVar.m = jSONObject2.getJSONObject("11H");
                                            }
                                            if (fg.a(jSONObject2, "11E")) {
                                                aVar.n = jSONObject2.getJSONObject("11E");
                                            }
                                            if (fg.a(jSONObject2, "11F")) {
                                                aVar.o = jSONObject2.getJSONObject("11F");
                                            }
                                            if (fg.a(jSONObject2, "13A")) {
                                                aVar.q = jSONObject2.getJSONObject("13A");
                                            }
                                            if (fg.a(jSONObject2, "13J")) {
                                                aVar.i = jSONObject2.getJSONObject("13J");
                                            }
                                            if (fg.a(jSONObject2, "11G")) {
                                                aVar.p = jSONObject2.getJSONObject("11G");
                                            }
                                            if (fg.a(jSONObject2, "001")) {
                                                JSONObject jSONObject118 = jSONObject2.getJSONObject("001");
                                                a.d dVar5 = new a.d();
                                                a(jSONObject118, dVar5);
                                                aVar.v = dVar5;
                                            }
                                            if (fg.a(jSONObject2, "002")) {
                                                JSONObject jSONObject119 = jSONObject2.getJSONObject("002");
                                                a.c cVar5 = new a.c();
                                                a(jSONObject119, cVar5);
                                                aVar.w = cVar5;
                                            }
                                            if (fg.a(jSONObject2, "006")) {
                                                aVar.r = jSONObject2.getJSONObject("006");
                                            }
                                            if (fg.a(jSONObject2, "010")) {
                                                aVar.s = jSONObject2.getJSONObject("010");
                                            }
                                            if (fg.a(jSONObject2, "11Z")) {
                                                JSONObject jSONObject1110 = jSONObject2.getJSONObject("11Z");
                                                a.b bVar18 = new a.b();
                                                a(jSONObject1110, bVar18);
                                                aVar.x = bVar18;
                                            }
                                            if (fg.a(jSONObject2, "135")) {
                                                aVar.j = jSONObject2.getJSONObject("135");
                                            }
                                            if (fg.a(jSONObject2, "13S")) {
                                                aVar.g = jSONObject2.getJSONObject("13S");
                                            }
                                            if (fg.a(jSONObject2, "121")) {
                                                JSONObject jSONObject1111 = jSONObject2.getJSONObject("121");
                                                a.b bVar19 = new a.b();
                                                a(jSONObject1111, bVar19);
                                                aVar.y = bVar19;
                                            }
                                            if (fg.a(jSONObject2, "122")) {
                                                JSONObject jSONObject1112 = jSONObject2.getJSONObject("122");
                                                a.b bVar110 = new a.b();
                                                a(jSONObject1112, bVar110);
                                                aVar.z = bVar110;
                                            }
                                            if (fg.a(jSONObject2, "123")) {
                                                JSONObject jSONObject1113 = jSONObject2.getJSONObject("123");
                                                a.b bVar111 = new a.b();
                                                a(jSONObject1113, bVar111);
                                                aVar.A = bVar111;
                                            }
                                            if (fg.a(jSONObject2, "011")) {
                                                aVar.c = jSONObject2.getJSONObject("011");
                                            }
                                            if (fg.a(jSONObject2, "012")) {
                                                aVar.d = jSONObject2.getJSONObject("012");
                                            }
                                            if (fg.a(jSONObject2, "013")) {
                                                aVar.e = jSONObject2.getJSONObject("013");
                                            }
                                            if (fg.a(jSONObject2, "014")) {
                                                aVar.f = jSONObject2.getJSONObject("014");
                                            }
                                            if (fg.a(jSONObject2, "145")) {
                                                aVar.t = jSONObject2.getJSONObject("145");
                                            }
                                        }
                                        return aVar;
                                    }
                                    return aVar;
                                } catch (IllegalBlockSizeException unused3) {
                                    str5 = "result";
                                } catch (Throwable th6) {
                                    th = th6;
                                    str5 = "result";
                                    heVarA = heVarA;
                                    fk.a(th, "ConfigManager", "loadConfig");
                                    if (bArr == null) {
                                        return aVar;
                                    }
                                    if (TextUtils.isEmpty(strA)) {
                                        strA = fg.a(bArr);
                                    }
                                    jSONObject = new JSONObject(strA);
                                    if (jSONObject.has("status")) {
                                        i = jSONObject.getInt("status");
                                        if (i == 1) {
                                            a = 1;
                                        } else if (i == 0) {
                                            str6 = "authcsid";
                                            str7 = "authgsid";
                                            if (heVarA != null) {
                                                str6 = heVarA.c;
                                                str7 = heVarA.d;
                                            }
                                            fg.a(context, str6, str7, jSONObject);
                                            a = 0;
                                            if (jSONObject.has("info")) {
                                                b = jSONObject.getString("info");
                                            }
                                            fn.a(ffVar, "/v3/iasdkauth", b, str7, jSONObject.has("infocode") ? jSONObject.getString("infocode") : "");
                                            if (a == 0) {
                                                aVar.a = b;
                                                return aVar;
                                            }
                                        }
                                        if (jSONObject.has("ver")) {
                                            aVar.b = jSONObject.getInt("ver");
                                        }
                                        str8 = str5;
                                        if (fg.a(jSONObject, str8)) {
                                            c0015a = new a.C0015a();
                                            c0015a.a = false;
                                            c0015a.b = false;
                                            aVar.u = c0015a;
                                            jSONObject2 = jSONObject.getJSONObject(str8);
                                            str9 = str4;
                                            if (fg.a(jSONObject2, str9)) {
                                                jSONObject3 = jSONObject2.getJSONObject(str9);
                                                c0015a.a = a(jSONObject3.getString("able"), false);
                                                str10 = str2;
                                                if (jSONObject3.has(str10)) {
                                                    c0015a.c = jSONObject3.getJSONObject(str10);
                                                }
                                            }
                                            str11 = str3;
                                            if (fg.a(jSONObject2, str11)) {
                                                aVar.h = jSONObject2.getJSONObject(str11);
                                            }
                                            if (fg.a(jSONObject2, "11C")) {
                                                aVar.k = jSONObject2.getJSONObject("11C");
                                            }
                                            if (fg.a(jSONObject2, "11I")) {
                                                aVar.l = jSONObject2.getJSONObject("11I");
                                            }
                                            if (fg.a(jSONObject2, "11H")) {
                                                aVar.m = jSONObject2.getJSONObject("11H");
                                            }
                                            if (fg.a(jSONObject2, "11E")) {
                                                aVar.n = jSONObject2.getJSONObject("11E");
                                            }
                                            if (fg.a(jSONObject2, "11F")) {
                                                aVar.o = jSONObject2.getJSONObject("11F");
                                            }
                                            if (fg.a(jSONObject2, "13A")) {
                                                aVar.q = jSONObject2.getJSONObject("13A");
                                            }
                                            if (fg.a(jSONObject2, "13J")) {
                                                aVar.i = jSONObject2.getJSONObject("13J");
                                            }
                                            if (fg.a(jSONObject2, "11G")) {
                                                aVar.p = jSONObject2.getJSONObject("11G");
                                            }
                                            if (fg.a(jSONObject2, "001")) {
                                                JSONObject jSONObject1114 = jSONObject2.getJSONObject("001");
                                                a.d dVar6 = new a.d();
                                                a(jSONObject1114, dVar6);
                                                aVar.v = dVar6;
                                            }
                                            if (fg.a(jSONObject2, "002")) {
                                                JSONObject jSONObject1115 = jSONObject2.getJSONObject("002");
                                                a.c cVar6 = new a.c();
                                                a(jSONObject1115, cVar6);
                                                aVar.w = cVar6;
                                            }
                                            if (fg.a(jSONObject2, "006")) {
                                                aVar.r = jSONObject2.getJSONObject("006");
                                            }
                                            if (fg.a(jSONObject2, "010")) {
                                                aVar.s = jSONObject2.getJSONObject("010");
                                            }
                                            if (fg.a(jSONObject2, "11Z")) {
                                                JSONObject jSONObject1116 = jSONObject2.getJSONObject("11Z");
                                                a.b bVar112 = new a.b();
                                                a(jSONObject1116, bVar112);
                                                aVar.x = bVar112;
                                            }
                                            if (fg.a(jSONObject2, "135")) {
                                                aVar.j = jSONObject2.getJSONObject("135");
                                            }
                                            if (fg.a(jSONObject2, "13S")) {
                                                aVar.g = jSONObject2.getJSONObject("13S");
                                            }
                                            if (fg.a(jSONObject2, "121")) {
                                                JSONObject jSONObject1117 = jSONObject2.getJSONObject("121");
                                                a.b bVar113 = new a.b();
                                                a(jSONObject1117, bVar113);
                                                aVar.y = bVar113;
                                            }
                                            if (fg.a(jSONObject2, "122")) {
                                                JSONObject jSONObject1118 = jSONObject2.getJSONObject("122");
                                                a.b bVar114 = new a.b();
                                                a(jSONObject1118, bVar114);
                                                aVar.z = bVar114;
                                            }
                                            if (fg.a(jSONObject2, "123")) {
                                                JSONObject jSONObject1119 = jSONObject2.getJSONObject("123");
                                                a.b bVar115 = new a.b();
                                                a(jSONObject1119, bVar115);
                                                aVar.A = bVar115;
                                            }
                                            if (fg.a(jSONObject2, "011")) {
                                                aVar.c = jSONObject2.getJSONObject("011");
                                            }
                                            if (fg.a(jSONObject2, "012")) {
                                                aVar.d = jSONObject2.getJSONObject("012");
                                            }
                                            if (fg.a(jSONObject2, "013")) {
                                                aVar.e = jSONObject2.getJSONObject("013");
                                            }
                                            if (fg.a(jSONObject2, "014")) {
                                                aVar.f = jSONObject2.getJSONObject("014");
                                            }
                                            if (fg.a(jSONObject2, "145")) {
                                                aVar.t = jSONObject2.getJSONObject("145");
                                            }
                                        }
                                        return aVar;
                                    }
                                    return aVar;
                                }
                            } catch (ev e4) {
                                e = e4;
                                str4 = "11K";
                            } catch (IllegalBlockSizeException unused4) {
                                str4 = "11K";
                            } catch (Throwable th7) {
                                th = th7;
                                str4 = "11K";
                            }
                            heVarA = heVarA;
                        } catch (ev e5) {
                            e = e5;
                            str4 = "11K";
                            str5 = "result";
                        } catch (IllegalBlockSizeException unused5) {
                            str4 = "11K";
                            str5 = "result";
                        } catch (Throwable th8) {
                            th = th8;
                            str4 = "11K";
                            str5 = "result";
                        }
                    } catch (ev e6) {
                        e = e6;
                        throw e;
                    } catch (Throwable unused6) {
                        throw new ev(AMapException.ERROR_UNKNOWN);
                    }
                } catch (ev e7) {
                    e = e7;
                } catch (Throwable unused7) {
                }
            } catch (ev e8) {
                e = e8;
                heVarA = null;
                bArr = null;
                aVar.a = e.a();
                fn.a(ffVar, "/v3/iasdkauth", e);
                if (bArr == null) {
                    return aVar;
                }
                if (TextUtils.isEmpty(strA)) {
                    strA = fg.a(bArr);
                }
                jSONObject = new JSONObject(strA);
                if (jSONObject.has("status")) {
                    i = jSONObject.getInt("status");
                    if (i == 1) {
                        a = 1;
                    } else if (i == 0) {
                        str6 = "authcsid";
                        str7 = "authgsid";
                        if (heVarA != null) {
                            str6 = heVarA.c;
                            str7 = heVarA.d;
                        }
                        fg.a(context, str6, str7, jSONObject);
                        a = 0;
                        if (jSONObject.has("info")) {
                            b = jSONObject.getString("info");
                        }
                        fn.a(ffVar, "/v3/iasdkauth", b, str7, jSONObject.has("infocode") ? jSONObject.getString("infocode") : "");
                        if (a == 0) {
                            aVar.a = b;
                            return aVar;
                        }
                    }
                    if (jSONObject.has("ver")) {
                        aVar.b = jSONObject.getInt("ver");
                    }
                    str8 = str5;
                    if (fg.a(jSONObject, str8)) {
                        c0015a = new a.C0015a();
                        c0015a.a = false;
                        c0015a.b = false;
                        aVar.u = c0015a;
                        jSONObject2 = jSONObject.getJSONObject(str8);
                        str9 = str4;
                        if (fg.a(jSONObject2, str9)) {
                            jSONObject3 = jSONObject2.getJSONObject(str9);
                            c0015a.a = a(jSONObject3.getString("able"), false);
                            str10 = str2;
                            if (jSONObject3.has(str10)) {
                                c0015a.c = jSONObject3.getJSONObject(str10);
                            }
                        }
                        str11 = str3;
                        if (fg.a(jSONObject2, str11)) {
                            aVar.h = jSONObject2.getJSONObject(str11);
                        }
                        if (fg.a(jSONObject2, "11C")) {
                            aVar.k = jSONObject2.getJSONObject("11C");
                        }
                        if (fg.a(jSONObject2, "11I")) {
                            aVar.l = jSONObject2.getJSONObject("11I");
                        }
                        if (fg.a(jSONObject2, "11H")) {
                            aVar.m = jSONObject2.getJSONObject("11H");
                        }
                        if (fg.a(jSONObject2, "11E")) {
                            aVar.n = jSONObject2.getJSONObject("11E");
                        }
                        if (fg.a(jSONObject2, "11F")) {
                            aVar.o = jSONObject2.getJSONObject("11F");
                        }
                        if (fg.a(jSONObject2, "13A")) {
                            aVar.q = jSONObject2.getJSONObject("13A");
                        }
                        if (fg.a(jSONObject2, "13J")) {
                            aVar.i = jSONObject2.getJSONObject("13J");
                        }
                        if (fg.a(jSONObject2, "11G")) {
                            aVar.p = jSONObject2.getJSONObject("11G");
                        }
                        if (fg.a(jSONObject2, "001")) {
                            JSONObject jSONObject11110 = jSONObject2.getJSONObject("001");
                            a.d dVar7 = new a.d();
                            a(jSONObject11110, dVar7);
                            aVar.v = dVar7;
                        }
                        if (fg.a(jSONObject2, "002")) {
                            JSONObject jSONObject11111 = jSONObject2.getJSONObject("002");
                            a.c cVar7 = new a.c();
                            a(jSONObject11111, cVar7);
                            aVar.w = cVar7;
                        }
                        if (fg.a(jSONObject2, "006")) {
                            aVar.r = jSONObject2.getJSONObject("006");
                        }
                        if (fg.a(jSONObject2, "010")) {
                            aVar.s = jSONObject2.getJSONObject("010");
                        }
                        if (fg.a(jSONObject2, "11Z")) {
                            JSONObject jSONObject11112 = jSONObject2.getJSONObject("11Z");
                            a.b bVar116 = new a.b();
                            a(jSONObject11112, bVar116);
                            aVar.x = bVar116;
                        }
                        if (fg.a(jSONObject2, "135")) {
                            aVar.j = jSONObject2.getJSONObject("135");
                        }
                        if (fg.a(jSONObject2, "13S")) {
                            aVar.g = jSONObject2.getJSONObject("13S");
                        }
                        if (fg.a(jSONObject2, "121")) {
                            JSONObject jSONObject11113 = jSONObject2.getJSONObject("121");
                            a.b bVar117 = new a.b();
                            a(jSONObject11113, bVar117);
                            aVar.y = bVar117;
                        }
                        if (fg.a(jSONObject2, "122")) {
                            JSONObject jSONObject11114 = jSONObject2.getJSONObject("122");
                            a.b bVar118 = new a.b();
                            a(jSONObject11114, bVar118);
                            aVar.z = bVar118;
                        }
                        if (fg.a(jSONObject2, "123")) {
                            JSONObject jSONObject11115 = jSONObject2.getJSONObject("123");
                            a.b bVar119 = new a.b();
                            a(jSONObject11115, bVar119);
                            aVar.A = bVar119;
                        }
                        if (fg.a(jSONObject2, "011")) {
                            aVar.c = jSONObject2.getJSONObject("011");
                        }
                        if (fg.a(jSONObject2, "012")) {
                            aVar.d = jSONObject2.getJSONObject("012");
                        }
                        if (fg.a(jSONObject2, "013")) {
                            aVar.e = jSONObject2.getJSONObject("013");
                        }
                        if (fg.a(jSONObject2, "014")) {
                            aVar.f = jSONObject2.getJSONObject("014");
                        }
                        if (fg.a(jSONObject2, "145")) {
                            aVar.t = jSONObject2.getJSONObject("145");
                        }
                    }
                    return aVar;
                }
                return aVar;
            } catch (IllegalBlockSizeException unused8) {
                heVarA = null;
                bArr = null;
            } catch (Throwable th9) {
                th = th9;
                heVarA = null;
                bArr = null;
                fk.a(th, "ConfigManager", "loadConfig");
                if (bArr == null) {
                    return aVar;
                }
                if (TextUtils.isEmpty(strA)) {
                    strA = fg.a(bArr);
                }
                jSONObject = new JSONObject(strA);
                if (jSONObject.has("status")) {
                    i = jSONObject.getInt("status");
                    if (i == 1) {
                        a = 1;
                    } else if (i == 0) {
                        str6 = "authcsid";
                        str7 = "authgsid";
                        if (heVarA != null) {
                            str6 = heVarA.c;
                            str7 = heVarA.d;
                        }
                        fg.a(context, str6, str7, jSONObject);
                        a = 0;
                        if (jSONObject.has("info")) {
                            b = jSONObject.getString("info");
                        }
                        fn.a(ffVar, "/v3/iasdkauth", b, str7, jSONObject.has("infocode") ? jSONObject.getString("infocode") : "");
                        if (a == 0) {
                            aVar.a = b;
                            return aVar;
                        }
                    }
                    if (jSONObject.has("ver")) {
                        aVar.b = jSONObject.getInt("ver");
                    }
                    str8 = str5;
                    if (fg.a(jSONObject, str8)) {
                        c0015a = new a.C0015a();
                        c0015a.a = false;
                        c0015a.b = false;
                        aVar.u = c0015a;
                        jSONObject2 = jSONObject.getJSONObject(str8);
                        str9 = str4;
                        if (fg.a(jSONObject2, str9)) {
                            jSONObject3 = jSONObject2.getJSONObject(str9);
                            c0015a.a = a(jSONObject3.getString("able"), false);
                            str10 = str2;
                            if (jSONObject3.has(str10)) {
                                c0015a.c = jSONObject3.getJSONObject(str10);
                            }
                        }
                        str11 = str3;
                        if (fg.a(jSONObject2, str11)) {
                            aVar.h = jSONObject2.getJSONObject(str11);
                        }
                        if (fg.a(jSONObject2, "11C")) {
                            aVar.k = jSONObject2.getJSONObject("11C");
                        }
                        if (fg.a(jSONObject2, "11I")) {
                            aVar.l = jSONObject2.getJSONObject("11I");
                        }
                        if (fg.a(jSONObject2, "11H")) {
                            aVar.m = jSONObject2.getJSONObject("11H");
                        }
                        if (fg.a(jSONObject2, "11E")) {
                            aVar.n = jSONObject2.getJSONObject("11E");
                        }
                        if (fg.a(jSONObject2, "11F")) {
                            aVar.o = jSONObject2.getJSONObject("11F");
                        }
                        if (fg.a(jSONObject2, "13A")) {
                            aVar.q = jSONObject2.getJSONObject("13A");
                        }
                        if (fg.a(jSONObject2, "13J")) {
                            aVar.i = jSONObject2.getJSONObject("13J");
                        }
                        if (fg.a(jSONObject2, "11G")) {
                            aVar.p = jSONObject2.getJSONObject("11G");
                        }
                        if (fg.a(jSONObject2, "001")) {
                            JSONObject jSONObject11116 = jSONObject2.getJSONObject("001");
                            a.d dVar8 = new a.d();
                            a(jSONObject11116, dVar8);
                            aVar.v = dVar8;
                        }
                        if (fg.a(jSONObject2, "002")) {
                            JSONObject jSONObject11117 = jSONObject2.getJSONObject("002");
                            a.c cVar8 = new a.c();
                            a(jSONObject11117, cVar8);
                            aVar.w = cVar8;
                        }
                        if (fg.a(jSONObject2, "006")) {
                            aVar.r = jSONObject2.getJSONObject("006");
                        }
                        if (fg.a(jSONObject2, "010")) {
                            aVar.s = jSONObject2.getJSONObject("010");
                        }
                        if (fg.a(jSONObject2, "11Z")) {
                            JSONObject jSONObject11118 = jSONObject2.getJSONObject("11Z");
                            a.b bVar1110 = new a.b();
                            a(jSONObject11118, bVar1110);
                            aVar.x = bVar1110;
                        }
                        if (fg.a(jSONObject2, "135")) {
                            aVar.j = jSONObject2.getJSONObject("135");
                        }
                        if (fg.a(jSONObject2, "13S")) {
                            aVar.g = jSONObject2.getJSONObject("13S");
                        }
                        if (fg.a(jSONObject2, "121")) {
                            JSONObject jSONObject11119 = jSONObject2.getJSONObject("121");
                            a.b bVar1111 = new a.b();
                            a(jSONObject11119, bVar1111);
                            aVar.y = bVar1111;
                        }
                        if (fg.a(jSONObject2, "122")) {
                            JSONObject jSONObject111110 = jSONObject2.getJSONObject("122");
                            a.b bVar1112 = new a.b();
                            a(jSONObject111110, bVar1112);
                            aVar.z = bVar1112;
                        }
                        if (fg.a(jSONObject2, "123")) {
                            JSONObject jSONObject111111 = jSONObject2.getJSONObject("123");
                            a.b bVar1113 = new a.b();
                            a(jSONObject111111, bVar1113);
                            aVar.A = bVar1113;
                        }
                        if (fg.a(jSONObject2, "011")) {
                            aVar.c = jSONObject2.getJSONObject("011");
                        }
                        if (fg.a(jSONObject2, "012")) {
                            aVar.d = jSONObject2.getJSONObject("012");
                        }
                        if (fg.a(jSONObject2, "013")) {
                            aVar.e = jSONObject2.getJSONObject("013");
                        }
                        if (fg.a(jSONObject2, "014")) {
                            aVar.f = jSONObject2.getJSONObject("014");
                        }
                        if (fg.a(jSONObject2, "145")) {
                            aVar.t = jSONObject2.getJSONObject("145");
                        }
                    }
                    return aVar;
                }
                return aVar;
            }
        } catch (ev e9) {
            e = e9;
            str2 = DebugKt.DEBUG_PROPERTY_VALUE_OFF;
            str3 = "11B";
            str4 = "11K";
            str5 = "result";
            heVarA = null;
            bArr = null;
            aVar.a = e.a();
            fn.a(ffVar, "/v3/iasdkauth", e);
            if (bArr == null) {
                return aVar;
            }
            if (TextUtils.isEmpty(strA)) {
                strA = fg.a(bArr);
            }
            jSONObject = new JSONObject(strA);
            if (jSONObject.has("status")) {
                i = jSONObject.getInt("status");
                if (i == 1) {
                    a = 1;
                } else if (i == 0) {
                    str6 = "authcsid";
                    str7 = "authgsid";
                    if (heVarA != null) {
                        str6 = heVarA.c;
                        str7 = heVarA.d;
                    }
                    fg.a(context, str6, str7, jSONObject);
                    a = 0;
                    if (jSONObject.has("info")) {
                        b = jSONObject.getString("info");
                    }
                    fn.a(ffVar, "/v3/iasdkauth", b, str7, jSONObject.has("infocode") ? jSONObject.getString("infocode") : "");
                    if (a == 0) {
                        aVar.a = b;
                        return aVar;
                    }
                }
                if (jSONObject.has("ver")) {
                    aVar.b = jSONObject.getInt("ver");
                }
                str8 = str5;
                if (fg.a(jSONObject, str8)) {
                    c0015a = new a.C0015a();
                    c0015a.a = false;
                    c0015a.b = false;
                    aVar.u = c0015a;
                    jSONObject2 = jSONObject.getJSONObject(str8);
                    str9 = str4;
                    if (fg.a(jSONObject2, str9)) {
                        jSONObject3 = jSONObject2.getJSONObject(str9);
                        c0015a.a = a(jSONObject3.getString("able"), false);
                        str10 = str2;
                        if (jSONObject3.has(str10)) {
                            c0015a.c = jSONObject3.getJSONObject(str10);
                        }
                    }
                    str11 = str3;
                    if (fg.a(jSONObject2, str11)) {
                        aVar.h = jSONObject2.getJSONObject(str11);
                    }
                    if (fg.a(jSONObject2, "11C")) {
                        aVar.k = jSONObject2.getJSONObject("11C");
                    }
                    if (fg.a(jSONObject2, "11I")) {
                        aVar.l = jSONObject2.getJSONObject("11I");
                    }
                    if (fg.a(jSONObject2, "11H")) {
                        aVar.m = jSONObject2.getJSONObject("11H");
                    }
                    if (fg.a(jSONObject2, "11E")) {
                        aVar.n = jSONObject2.getJSONObject("11E");
                    }
                    if (fg.a(jSONObject2, "11F")) {
                        aVar.o = jSONObject2.getJSONObject("11F");
                    }
                    if (fg.a(jSONObject2, "13A")) {
                        aVar.q = jSONObject2.getJSONObject("13A");
                    }
                    if (fg.a(jSONObject2, "13J")) {
                        aVar.i = jSONObject2.getJSONObject("13J");
                    }
                    if (fg.a(jSONObject2, "11G")) {
                        aVar.p = jSONObject2.getJSONObject("11G");
                    }
                    if (fg.a(jSONObject2, "001")) {
                        JSONObject jSONObject111112 = jSONObject2.getJSONObject("001");
                        a.d dVar9 = new a.d();
                        a(jSONObject111112, dVar9);
                        aVar.v = dVar9;
                    }
                    if (fg.a(jSONObject2, "002")) {
                        JSONObject jSONObject111113 = jSONObject2.getJSONObject("002");
                        a.c cVar9 = new a.c();
                        a(jSONObject111113, cVar9);
                        aVar.w = cVar9;
                    }
                    if (fg.a(jSONObject2, "006")) {
                        aVar.r = jSONObject2.getJSONObject("006");
                    }
                    if (fg.a(jSONObject2, "010")) {
                        aVar.s = jSONObject2.getJSONObject("010");
                    }
                    if (fg.a(jSONObject2, "11Z")) {
                        JSONObject jSONObject111114 = jSONObject2.getJSONObject("11Z");
                        a.b bVar1114 = new a.b();
                        a(jSONObject111114, bVar1114);
                        aVar.x = bVar1114;
                    }
                    if (fg.a(jSONObject2, "135")) {
                        aVar.j = jSONObject2.getJSONObject("135");
                    }
                    if (fg.a(jSONObject2, "13S")) {
                        aVar.g = jSONObject2.getJSONObject("13S");
                    }
                    if (fg.a(jSONObject2, "121")) {
                        JSONObject jSONObject111115 = jSONObject2.getJSONObject("121");
                        a.b bVar1115 = new a.b();
                        a(jSONObject111115, bVar1115);
                        aVar.y = bVar1115;
                    }
                    if (fg.a(jSONObject2, "122")) {
                        JSONObject jSONObject111116 = jSONObject2.getJSONObject("122");
                        a.b bVar1116 = new a.b();
                        a(jSONObject111116, bVar1116);
                        aVar.z = bVar1116;
                    }
                    if (fg.a(jSONObject2, "123")) {
                        JSONObject jSONObject111117 = jSONObject2.getJSONObject("123");
                        a.b bVar1117 = new a.b();
                        a(jSONObject111117, bVar1117);
                        aVar.A = bVar1117;
                    }
                    if (fg.a(jSONObject2, "011")) {
                        aVar.c = jSONObject2.getJSONObject("011");
                    }
                    if (fg.a(jSONObject2, "012")) {
                        aVar.d = jSONObject2.getJSONObject("012");
                    }
                    if (fg.a(jSONObject2, "013")) {
                        aVar.e = jSONObject2.getJSONObject("013");
                    }
                    if (fg.a(jSONObject2, "014")) {
                        aVar.f = jSONObject2.getJSONObject("014");
                    }
                    if (fg.a(jSONObject2, "145")) {
                        aVar.t = jSONObject2.getJSONObject("145");
                    }
                }
                return aVar;
            }
            return aVar;
        } catch (IllegalBlockSizeException unused9) {
            str2 = DebugKt.DEBUG_PROPERTY_VALUE_OFF;
            str3 = "11B";
            str4 = "11K";
            str5 = "result";
            heVarA = null;
            bArr = null;
        } catch (Throwable th10) {
            th = th10;
            str2 = DebugKt.DEBUG_PROPERTY_VALUE_OFF;
            str3 = "11B";
            str4 = "11K";
            str5 = "result";
            heVarA = null;
            bArr = null;
            fk.a(th, "ConfigManager", "loadConfig");
            if (bArr == null) {
                return aVar;
            }
            if (TextUtils.isEmpty(strA)) {
                strA = fg.a(bArr);
            }
            jSONObject = new JSONObject(strA);
            if (jSONObject.has("status")) {
                i = jSONObject.getInt("status");
                if (i == 1) {
                    a = 1;
                } else if (i == 0) {
                    str6 = "authcsid";
                    str7 = "authgsid";
                    if (heVarA != null) {
                        str6 = heVarA.c;
                        str7 = heVarA.d;
                    }
                    fg.a(context, str6, str7, jSONObject);
                    a = 0;
                    if (jSONObject.has("info")) {
                        b = jSONObject.getString("info");
                    }
                    fn.a(ffVar, "/v3/iasdkauth", b, str7, jSONObject.has("infocode") ? jSONObject.getString("infocode") : "");
                    if (a == 0) {
                        aVar.a = b;
                        return aVar;
                    }
                }
                if (jSONObject.has("ver")) {
                    aVar.b = jSONObject.getInt("ver");
                }
                str8 = str5;
                if (fg.a(jSONObject, str8)) {
                    c0015a = new a.C0015a();
                    c0015a.a = false;
                    c0015a.b = false;
                    aVar.u = c0015a;
                    jSONObject2 = jSONObject.getJSONObject(str8);
                    str9 = str4;
                    if (fg.a(jSONObject2, str9)) {
                        jSONObject3 = jSONObject2.getJSONObject(str9);
                        c0015a.a = a(jSONObject3.getString("able"), false);
                        str10 = str2;
                        if (jSONObject3.has(str10)) {
                            c0015a.c = jSONObject3.getJSONObject(str10);
                        }
                    }
                    str11 = str3;
                    if (fg.a(jSONObject2, str11)) {
                        aVar.h = jSONObject2.getJSONObject(str11);
                    }
                    if (fg.a(jSONObject2, "11C")) {
                        aVar.k = jSONObject2.getJSONObject("11C");
                    }
                    if (fg.a(jSONObject2, "11I")) {
                        aVar.l = jSONObject2.getJSONObject("11I");
                    }
                    if (fg.a(jSONObject2, "11H")) {
                        aVar.m = jSONObject2.getJSONObject("11H");
                    }
                    if (fg.a(jSONObject2, "11E")) {
                        aVar.n = jSONObject2.getJSONObject("11E");
                    }
                    if (fg.a(jSONObject2, "11F")) {
                        aVar.o = jSONObject2.getJSONObject("11F");
                    }
                    if (fg.a(jSONObject2, "13A")) {
                        aVar.q = jSONObject2.getJSONObject("13A");
                    }
                    if (fg.a(jSONObject2, "13J")) {
                        aVar.i = jSONObject2.getJSONObject("13J");
                    }
                    if (fg.a(jSONObject2, "11G")) {
                        aVar.p = jSONObject2.getJSONObject("11G");
                    }
                    if (fg.a(jSONObject2, "001")) {
                        JSONObject jSONObject111118 = jSONObject2.getJSONObject("001");
                        a.d dVar10 = new a.d();
                        a(jSONObject111118, dVar10);
                        aVar.v = dVar10;
                    }
                    if (fg.a(jSONObject2, "002")) {
                        JSONObject jSONObject111119 = jSONObject2.getJSONObject("002");
                        a.c cVar10 = new a.c();
                        a(jSONObject111119, cVar10);
                        aVar.w = cVar10;
                    }
                    if (fg.a(jSONObject2, "006")) {
                        aVar.r = jSONObject2.getJSONObject("006");
                    }
                    if (fg.a(jSONObject2, "010")) {
                        aVar.s = jSONObject2.getJSONObject("010");
                    }
                    if (fg.a(jSONObject2, "11Z")) {
                        JSONObject jSONObject1111110 = jSONObject2.getJSONObject("11Z");
                        a.b bVar1118 = new a.b();
                        a(jSONObject1111110, bVar1118);
                        aVar.x = bVar1118;
                    }
                    if (fg.a(jSONObject2, "135")) {
                        aVar.j = jSONObject2.getJSONObject("135");
                    }
                    if (fg.a(jSONObject2, "13S")) {
                        aVar.g = jSONObject2.getJSONObject("13S");
                    }
                    if (fg.a(jSONObject2, "121")) {
                        JSONObject jSONObject1111111 = jSONObject2.getJSONObject("121");
                        a.b bVar1119 = new a.b();
                        a(jSONObject1111111, bVar1119);
                        aVar.y = bVar1119;
                    }
                    if (fg.a(jSONObject2, "122")) {
                        JSONObject jSONObject1111112 = jSONObject2.getJSONObject("122");
                        a.b bVar11110 = new a.b();
                        a(jSONObject1111112, bVar11110);
                        aVar.z = bVar11110;
                    }
                    if (fg.a(jSONObject2, "123")) {
                        JSONObject jSONObject1111113 = jSONObject2.getJSONObject("123");
                        a.b bVar11111 = new a.b();
                        a(jSONObject1111113, bVar11111);
                        aVar.A = bVar11111;
                    }
                    if (fg.a(jSONObject2, "011")) {
                        aVar.c = jSONObject2.getJSONObject("011");
                    }
                    if (fg.a(jSONObject2, "012")) {
                        aVar.d = jSONObject2.getJSONObject("012");
                    }
                    if (fg.a(jSONObject2, "013")) {
                        aVar.e = jSONObject2.getJSONObject("013");
                    }
                    if (fg.a(jSONObject2, "014")) {
                        aVar.f = jSONObject2.getJSONObject("014");
                    }
                    if (fg.a(jSONObject2, "145")) {
                        aVar.t = jSONObject2.getJSONObject("145");
                    }
                }
                return aVar;
            }
            return aVar;
        }
        if (bArr == null) {
            return aVar;
        }
        if (TextUtils.isEmpty(strA)) {
            strA = fg.a(bArr);
        }
        jSONObject = new JSONObject(strA);
        if (jSONObject.has("status")) {
            i = jSONObject.getInt("status");
            if (i == 1) {
                a = 1;
            } else if (i == 0) {
                str6 = "authcsid";
                str7 = "authgsid";
                if (heVarA != null) {
                    str6 = heVarA.c;
                    str7 = heVarA.d;
                }
                fg.a(context, str6, str7, jSONObject);
                a = 0;
                if (jSONObject.has("info")) {
                    b = jSONObject.getString("info");
                }
                fn.a(ffVar, "/v3/iasdkauth", b, str7, jSONObject.has("infocode") ? jSONObject.getString("infocode") : "");
                if (a == 0) {
                    aVar.a = b;
                    return aVar;
                }
            }
            if (jSONObject.has("ver")) {
                aVar.b = jSONObject.getInt("ver");
            }
            str8 = str5;
            if (fg.a(jSONObject, str8)) {
                c0015a = new a.C0015a();
                c0015a.a = false;
                c0015a.b = false;
                aVar.u = c0015a;
                jSONObject2 = jSONObject.getJSONObject(str8);
                str9 = str4;
                if (fg.a(jSONObject2, str9)) {
                    jSONObject3 = jSONObject2.getJSONObject(str9);
                    c0015a.a = a(jSONObject3.getString("able"), false);
                    str10 = str2;
                    if (jSONObject3.has(str10)) {
                        c0015a.c = jSONObject3.getJSONObject(str10);
                    }
                }
                str11 = str3;
                if (fg.a(jSONObject2, str11)) {
                    aVar.h = jSONObject2.getJSONObject(str11);
                }
                if (fg.a(jSONObject2, "11C")) {
                    aVar.k = jSONObject2.getJSONObject("11C");
                }
                if (fg.a(jSONObject2, "11I")) {
                    aVar.l = jSONObject2.getJSONObject("11I");
                }
                if (fg.a(jSONObject2, "11H")) {
                    aVar.m = jSONObject2.getJSONObject("11H");
                }
                if (fg.a(jSONObject2, "11E")) {
                    aVar.n = jSONObject2.getJSONObject("11E");
                }
                if (fg.a(jSONObject2, "11F")) {
                    aVar.o = jSONObject2.getJSONObject("11F");
                }
                if (fg.a(jSONObject2, "13A")) {
                    aVar.q = jSONObject2.getJSONObject("13A");
                }
                if (fg.a(jSONObject2, "13J")) {
                    aVar.i = jSONObject2.getJSONObject("13J");
                }
                if (fg.a(jSONObject2, "11G")) {
                    aVar.p = jSONObject2.getJSONObject("11G");
                }
                if (fg.a(jSONObject2, "001")) {
                    JSONObject jSONObject1111114 = jSONObject2.getJSONObject("001");
                    a.d dVar11 = new a.d();
                    a(jSONObject1111114, dVar11);
                    aVar.v = dVar11;
                }
                if (fg.a(jSONObject2, "002")) {
                    JSONObject jSONObject1111115 = jSONObject2.getJSONObject("002");
                    a.c cVar11 = new a.c();
                    a(jSONObject1111115, cVar11);
                    aVar.w = cVar11;
                }
                if (fg.a(jSONObject2, "006")) {
                    aVar.r = jSONObject2.getJSONObject("006");
                }
                if (fg.a(jSONObject2, "010")) {
                    aVar.s = jSONObject2.getJSONObject("010");
                }
                if (fg.a(jSONObject2, "11Z")) {
                    JSONObject jSONObject1111116 = jSONObject2.getJSONObject("11Z");
                    a.b bVar11112 = new a.b();
                    a(jSONObject1111116, bVar11112);
                    aVar.x = bVar11112;
                }
                if (fg.a(jSONObject2, "135")) {
                    aVar.j = jSONObject2.getJSONObject("135");
                }
                if (fg.a(jSONObject2, "13S")) {
                    aVar.g = jSONObject2.getJSONObject("13S");
                }
                if (fg.a(jSONObject2, "121")) {
                    JSONObject jSONObject1111117 = jSONObject2.getJSONObject("121");
                    a.b bVar11113 = new a.b();
                    a(jSONObject1111117, bVar11113);
                    aVar.y = bVar11113;
                }
                if (fg.a(jSONObject2, "122")) {
                    JSONObject jSONObject1111118 = jSONObject2.getJSONObject("122");
                    a.b bVar11114 = new a.b();
                    a(jSONObject1111118, bVar11114);
                    aVar.z = bVar11114;
                }
                if (fg.a(jSONObject2, "123")) {
                    JSONObject jSONObject1111119 = jSONObject2.getJSONObject("123");
                    a.b bVar11115 = new a.b();
                    a(jSONObject1111119, bVar11115);
                    aVar.A = bVar11115;
                }
                if (fg.a(jSONObject2, "011")) {
                    aVar.c = jSONObject2.getJSONObject("011");
                }
                if (fg.a(jSONObject2, "012")) {
                    aVar.d = jSONObject2.getJSONObject("012");
                }
                if (fg.a(jSONObject2, "013")) {
                    aVar.e = jSONObject2.getJSONObject("013");
                }
                if (fg.a(jSONObject2, "014")) {
                    aVar.f = jSONObject2.getJSONObject("014");
                }
                if (fg.a(jSONObject2, "145")) {
                    aVar.t = jSONObject2.getJSONObject("145");
                }
            }
            return aVar;
        }
        return aVar;
    }

    public static String a(JSONObject jSONObject, String str) throws JSONException {
        return (jSONObject == null || !jSONObject.has(str) || jSONObject.getString(str).equals(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI)) ? "" : jSONObject.optString(str);
    }

    private static void a(JSONObject jSONObject, a.b bVar) {
        if (bVar != null) {
            try {
                String strA = a(jSONObject, "m");
                String strA2 = a(jSONObject, "u");
                String strA3 = a(jSONObject, "v");
                String strA4 = a(jSONObject, "able");
                bVar.c = strA;
                bVar.b = strA2;
                bVar.d = strA3;
                bVar.a = a(strA4, false);
            } catch (Throwable th) {
                fk.a(th, "ConfigManager", "parsePluginEntity");
            }
        }
    }

    private static void a(JSONObject jSONObject, a.c cVar) {
        if (jSONObject != null) {
            try {
                String strA = a(jSONObject, "md5");
                String strA2 = a(jSONObject, "url");
                cVar.b = strA;
                cVar.a = strA2;
            } catch (Throwable th) {
                fk.a(th, "ConfigManager", "parseSDKCoordinate");
            }
        }
    }

    private static void a(JSONObject jSONObject, a.d dVar) {
        if (jSONObject != null) {
            try {
                String strA = a(jSONObject, "md5");
                String strA2 = a(jSONObject, "url");
                String strA3 = a(jSONObject, "sdkversion");
                if (!TextUtils.isEmpty(strA) && !TextUtils.isEmpty(strA2) && !TextUtils.isEmpty(strA3)) {
                    dVar.a = strA2;
                    dVar.b = strA;
                    dVar.c = strA3;
                }
            } catch (Throwable th) {
                fk.a(th, "ConfigManager", "parseSDKUpdate");
            }
        }
    }

    /* JADX INFO: compiled from: AuthConfigManager.java */
    static class b extends gx {
        private String c;
        private Map<String, String> d;
        private boolean e;

        @Override // com.amap.api.mapcore.util.hc
        public Map<String, String> a() {
            return null;
        }

        @Override // com.amap.api.mapcore.util.gx
        public byte[] e() {
            return null;
        }

        @Override // com.amap.api.mapcore.util.gx
        protected String h() {
            return "3.0";
        }

        b(Context context, ff ffVar, String str, Map<String, String> map) {
            super(context, ffVar);
            this.c = str;
            this.d = map;
            this.e = true;
        }

        public boolean d() {
            return this.e;
        }

        @Override // com.amap.api.mapcore.util.hc
        public String c() {
            return this.e ? "https://restapi.amap.com/v3/iasdkauth" : "http://restapi.amap.com/v3/iasdkauth";
        }

        @Override // com.amap.api.mapcore.util.gx
        public byte[] f() {
            return fg.a(fg.a(o()));
        }

        private Map<String, String> o() {
            String strQ = fa.q(this.a);
            if (TextUtils.isEmpty(strQ)) {
                strQ = fa.c();
            }
            if (!TextUtils.isEmpty(strQ)) {
                strQ = fc.b(new StringBuilder(strQ).reverse().toString());
            }
            HashMap map = new HashMap();
            map.put("authkey", this.c);
            map.put("plattype", Constants.PLATFORM);
            map.put("product", this.b.a());
            map.put(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION, this.b.b());
            map.put("output", "json");
            map.put("androidversion", Build.VERSION.SDK_INT + "");
            map.put("deviceId", strQ);
            Map<String, String> map2 = this.d;
            if (map2 != null && !map2.isEmpty()) {
                map.putAll(this.d);
            }
            map.put("abitype", fg.a(this.a));
            map.put("ext", this.b.e());
            return map;
        }
    }
}
