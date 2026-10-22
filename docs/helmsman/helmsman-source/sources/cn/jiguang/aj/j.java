package cn.jiguang.aj;

import android.os.Bundle;
import android.os.Message;

/* JADX INFO: loaded from: classes.dex */
final class j extends cn.jiguang.an.a {
    final /* synthetic */ i a;

    j(i iVar) {
        this.a = iVar;
    }

    @Override // cn.jiguang.an.a
    public final void a(Message message) {
        if (message != null) {
            int i = message.what;
            if (i == 1011) {
                i iVar = this.a;
                iVar.a(iVar.k, "tcp_a14", null);
                return;
            }
            if (i == 1022) {
                i iVar2 = this.a;
                iVar2.a(iVar2.k, "tcp_a17", null);
                return;
            }
            if (i == 2000) {
                q.a().a(this.a.k);
                return;
            }
            switch (i) {
                case 1004:
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("force", true);
                    i iVar3 = this.a;
                    iVar3.a(iVar3.k, "tcp_a16", bundle);
                    break;
                case 1005:
                    Bundle bundle2 = new Bundle();
                    bundle2.putBoolean("force", false);
                    i iVar4 = this.a;
                    iVar4.a(iVar4.k, "tcp_a16", bundle2);
                    break;
                case 1006:
                case 1007:
                    i iVar5 = this.a;
                    iVar5.a(iVar5.k, "tcp_a2", null);
                    break;
            }
        }
    }
}
