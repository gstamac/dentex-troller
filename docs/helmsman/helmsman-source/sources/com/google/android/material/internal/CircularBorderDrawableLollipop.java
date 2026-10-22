package com.google.android.material.internal;

import android.graphics.Outline;

/* JADX INFO: loaded from: classes2.dex */
public class CircularBorderDrawableLollipop extends CircularBorderDrawable {
    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        copyBounds(this.rect);
        outline.setOval(this.rect);
    }
}
