package im.helmsman.helmsmanandroid.polyline.it.rambow.master.javautils;

import im.helmsman.helmsmanandroid.dao.MLatLng;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class Track {
    private List<MLatLng> trackpoints = new ArrayList();

    public List<MLatLng> getTrackpoints() {
        return this.trackpoints;
    }

    public void setTrackpoints(List<MLatLng> list) {
        this.trackpoints = list;
    }

    public void addTrackpoint(MLatLng mLatLng) {
        this.trackpoints.add(mLatLng);
    }
}
