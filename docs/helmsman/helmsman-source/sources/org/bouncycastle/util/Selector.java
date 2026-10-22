package org.bouncycastle.util;

/* JADX INFO: loaded from: classes2.dex */
public interface Selector extends Cloneable {
    Object clone();

    boolean match(Object obj);
}
