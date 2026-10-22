package com.tencent.mm.sdk.modelbiz;

import com.tencent.mm.sdk.modelbase.BaseReq;

/* JADX INFO: loaded from: classes2.dex */
public class OpenRankList {

    public static class Req extends BaseReq {
        @Override // com.tencent.mm.sdk.modelbase.BaseReq
        public boolean checkArgs() {
            return true;
        }

        @Override // com.tencent.mm.sdk.modelbase.BaseReq
        public int getType() {
            return 11;
        }
    }
}
