package com.tencent.wxop.stat;

import android.content.Context;
import com.tencent.wxop.stat.common.StatLogger;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes2.dex */
class ap implements Runnable {
    private Context a;
    private Map<String, Integer> b;
    private StatSpecifyReportedInfo c;

    public ap(Context context, Map<String, Integer> map, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.b = null;
        this.a = context;
        this.c = statSpecifyReportedInfo;
        if (map != null) {
            this.b = map;
        }
    }

    private NetworkMonitor a(String str, int i) {
        int i2;
        NetworkMonitor networkMonitor = new NetworkMonitor();
        Socket socket = new Socket();
        try {
            try {
                networkMonitor.setDomain(str);
                networkMonitor.setPort(i);
                long jCurrentTimeMillis = System.currentTimeMillis();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(str, i);
                socket.connect(inetSocketAddress, 30000);
                networkMonitor.setMillisecondsConsume(System.currentTimeMillis() - jCurrentTimeMillis);
                networkMonitor.setRemoteIp(inetSocketAddress.getAddress().getHostAddress());
                socket.close();
                try {
                    socket.close();
                } catch (Throwable th) {
                    StatServiceImpl.q.e(th);
                }
                i2 = 0;
            } catch (IOException e) {
                StatServiceImpl.q.e((Throwable) e);
                try {
                    socket.close();
                } catch (Throwable th2) {
                    StatServiceImpl.q.e(th2);
                }
                i2 = -1;
            }
            networkMonitor.setStatusCode(i2);
            return networkMonitor;
        } catch (Throwable th3) {
            try {
                socket.close();
            } catch (Throwable th4) {
                StatServiceImpl.q.e(th4);
            }
            throw th3;
        }
    }

    private Map<String, Integer> a() {
        String str;
        HashMap map = new HashMap();
        String strA = StatConfig.a("__MTA_TEST_SPEED__", (String) null);
        if (strA != null && strA.trim().length() != 0) {
            for (String str2 : strA.split(";")) {
                String[] strArrSplit = str2.split(",");
                if (strArrSplit != null && strArrSplit.length == 2 && (str = strArrSplit[0]) != null && str.trim().length() != 0) {
                    try {
                        map.put(str, Integer.valueOf(Integer.valueOf(strArrSplit[1]).intValue()));
                    } catch (NumberFormatException e) {
                        StatServiceImpl.q.e((Throwable) e);
                    }
                }
            }
        }
        return map;
    }

    @Override // java.lang.Runnable
    public void run() {
        StatLogger statLogger;
        String str;
        try {
            if (this.b == null) {
                this.b = a();
            }
            Map<String, Integer> map = this.b;
            if (map != null && map.size() != 0) {
                JSONArray jSONArray = new JSONArray();
                for (Map.Entry<String, Integer> entry : this.b.entrySet()) {
                    String key = entry.getKey();
                    if (key == null || key.length() == 0) {
                        statLogger = StatServiceImpl.q;
                        str = "empty domain name.";
                    } else if (entry.getValue() == null) {
                        statLogger = StatServiceImpl.q;
                        str = "port is null for " + key;
                    } else {
                        jSONArray.put(a(entry.getKey(), entry.getValue().intValue()).toJSONObject());
                    }
                    statLogger.w(str);
                }
                if (jSONArray.length() == 0) {
                    return;
                }
                Context context = this.a;
                com.tencent.wxop.stat.a.j jVar = new com.tencent.wxop.stat.a.j(context, StatServiceImpl.a(context, false, this.c), this.c);
                jVar.a(jSONArray.toString());
                new aq(jVar).a();
                return;
            }
            StatServiceImpl.q.i("empty domain list.");
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
        }
    }
}
