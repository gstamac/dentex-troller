package org.bouncycastle.x509.util;

import java.util.Collection;

/* JADX INFO: loaded from: classes2.dex */
public interface StreamParser {
    Object read() throws StreamParsingException;

    Collection readAll() throws StreamParsingException;
}
