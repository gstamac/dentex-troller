package androidx.transition;

import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes.dex */
class GhostViewUtils {
    static GhostViewImpl addGhost(View view, ViewGroup viewGroup, Matrix matrix) {
        return GhostViewApi21.addGhost(view, viewGroup, matrix);
    }

    static void removeGhost(View view) {
        GhostViewApi21.removeGhost(view);
    }

    private GhostViewUtils() {
    }
}
