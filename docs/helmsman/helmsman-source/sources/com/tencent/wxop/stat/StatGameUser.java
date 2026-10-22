package com.tencent.wxop.stat;

/* JADX INFO: loaded from: classes2.dex */
public class StatGameUser implements Cloneable {
    private String a;
    private String b;
    private String c;

    public StatGameUser() {
        this.a = "";
        this.b = "";
        this.c = "";
    }

    public StatGameUser(String str, String str2, String str3) {
        this.b = str;
        this.a = str2;
        this.c = str3;
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public StatGameUser m54clone() {
        try {
            return (StatGameUser) super.clone();
        } catch (CloneNotSupportedException unused) {
            return null;
        }
    }

    public String getAccount() {
        return this.b;
    }

    public String getLevel() {
        return this.c;
    }

    public String getWorldName() {
        return this.a;
    }

    public void setAccount(String str) {
        this.b = str;
    }

    public void setLevel(String str) {
        this.c = str;
    }

    public void setWorldName(String str) {
        this.a = str;
    }

    public String toString() {
        return "StatGameUser [worldName=" + this.a + ", account=" + this.b + ", level=" + this.c + "]";
    }
}
