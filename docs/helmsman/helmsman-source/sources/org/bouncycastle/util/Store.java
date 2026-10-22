package org.bouncycastle.util;

import java.util.Collection;

/* JADX INFO: loaded from: classes2.dex */
public interface Store {
    Collection getMatches(Selector selector) throws StoreException;
}
