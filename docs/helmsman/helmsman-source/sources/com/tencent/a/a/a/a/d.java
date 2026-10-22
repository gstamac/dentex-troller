package com.tencent.a.a.a.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/* JADX INFO: loaded from: classes2.dex */
final class d extends f {
    public d(Context context) {
        super(context);
    }

    @Override // com.tencent.a.a.a.a.f
    protected final void a(String str) {
        synchronized (this) {
            Log.i("MID", "write mid to sharedPreferences");
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.a).edit();
            editorEdit.putString(h.f("4kU71lN96TJUomD1vOU9lgj9Tw=="), str);
            editorEdit.commit();
        }
    }

    @Override // com.tencent.a.a.a.a.f
    protected final boolean a() {
        return true;
    }

    @Override // com.tencent.a.a.a.a.f
    protected final String b() {
        String string;
        synchronized (this) {
            Log.i("MID", "read mid from sharedPreferences");
            string = PreferenceManager.getDefaultSharedPreferences(this.a).getString(h.f("4kU71lN96TJUomD1vOU9lgj9Tw=="), null);
        }
        return string;
    }
}
