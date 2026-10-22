package com.tencent.wxop.stat.a;

import android.content.Context;
import com.tencent.wxop.stat.StatGameUser;
import com.tencent.wxop.stat.StatSpecifyReportedInfo;
import com.tencent.wxop.stat.common.q;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class g extends e {
    private StatGameUser a;

    public g(Context context, int i, StatGameUser statGameUser, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.a = null;
        this.a = statGameUser.m54clone();
    }

    @Override // com.tencent.wxop.stat.a.e
    public f a() {
        return f.MTA_GAME_USER;
    }

    @Override // com.tencent.wxop.stat.a.e
    public boolean a(JSONObject jSONObject) {
        StatGameUser statGameUser = this.a;
        if (statGameUser == null) {
            return false;
        }
        q.a(jSONObject, "wod", statGameUser.getWorldName());
        q.a(jSONObject, "gid", this.a.getAccount());
        q.a(jSONObject, "lev", this.a.getLevel());
        return true;
    }
}
