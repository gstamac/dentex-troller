package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Bundle;
import com.amap.api.maps.AMap;
import java.io.IOException;

/* JADX INFO: compiled from: OfflineMapDownloadTask.java */
/* JADX INFO: loaded from: classes.dex */
public class ar extends hm implements bg.a {
    private bg a;
    private bi b;
    private bk c;
    private Context e;
    private Bundle f;
    private AMap g;
    private boolean h;

    public ar(bk bkVar, Context context) {
        this.f = new Bundle();
        this.h = false;
        this.c = bkVar;
        this.e = context;
    }

    public ar(bk bkVar, Context context, AMap aMap) {
        this(bkVar, context);
        this.g = aMap;
    }

    @Override // com.amap.api.mapcore.util.hm
    public void a() {
        if (this.c.y()) {
            this.c.a(bl.a.file_io_exception);
            return;
        }
        try {
            g();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void b() {
        this.h = true;
        bg bgVar = this.a;
        if (bgVar != null) {
            bgVar.c();
        } else {
            e();
        }
        bi biVar = this.b;
        if (biVar != null) {
            biVar.a();
        }
    }

    private String f() {
        return dw.b(this.e);
    }

    private void g() throws IOException {
        bg bgVar = new bg(new bh(this.c.getUrl(), f(), this.c.z(), 1, this.c.A()), this.c.getUrl(), this.e, this.c);
        this.a = bgVar;
        bgVar.a(this);
        bk bkVar = this.c;
        this.b = new bi(bkVar, bkVar);
        if (this.h) {
            return;
        }
        this.a.a();
    }

    public void c() {
        this.g = null;
        Bundle bundle = this.f;
        if (bundle != null) {
            bundle.clear();
            this.f = null;
        }
    }

    @Override // com.amap.api.mapcore.util.bg.a
    public void d() {
        bi biVar = this.b;
        if (biVar != null) {
            biVar.b();
        }
    }
}
