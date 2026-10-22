package com.autonavi.ae.gmap.glanimation;

import com.autonavi.ae.gmap.GLMapState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ADGLMapAnimationMgr {
    private List<ADGLAnimation> list = Collections.synchronizedList(new ArrayList());

    public void setMapCoreListener() {
    }

    public synchronized void clearAnimations() {
        this.list.clear();
    }

    public synchronized int getAnimationsCount() {
        return this.list.size();
    }

    public synchronized void doAnimations(GLMapState gLMapState) {
        if (gLMapState == null) {
            return;
        }
        if (this.list.size() <= 0) {
            return;
        }
        ADGLAnimation aDGLAnimation = this.list.get(0);
        if (aDGLAnimation == null) {
            return;
        }
        if (aDGLAnimation.isOver()) {
            this.list.remove(aDGLAnimation);
        } else {
            aDGLAnimation.doAnimation(gLMapState);
        }
    }

    public void addAnimation(ADGLAnimation aDGLAnimation) {
        if (aDGLAnimation == null) {
            return;
        }
        synchronized (this.list) {
            if (!aDGLAnimation.isOver() && this.list.size() > 0) {
                List<ADGLAnimation> list = this.list;
                ADGLAnimation aDGLAnimation2 = list.get(list.size() - 1);
                if (aDGLAnimation2 != null && (aDGLAnimation instanceof ADGLMapAnimGroup) && (aDGLAnimation2 instanceof ADGLMapAnimGroup) && ((ADGLMapAnimGroup) aDGLAnimation).typeEqueal((ADGLMapAnimGroup) aDGLAnimation2) && !((ADGLMapAnimGroup) aDGLAnimation)._needMove) {
                    this.list.remove(aDGLAnimation2);
                }
            }
            this.list.add(aDGLAnimation);
        }
    }
}
