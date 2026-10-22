package im.helmsman.lib.mission;

import im.helmsman.lib.mission.common.HMMission;

/* JADX INFO: loaded from: classes2.dex */
public class HMEphemerisdataMission extends HMMission {
    private short[] ephemerisdata;

    public HMEphemerisdataMission() {
        super(HMMission.MissionType.EPHEMERIS_DATA_TYPE);
        initTempData();
    }

    private void initTempData() {
        this.ephemerisdata = new short[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 1, 12, 2, 2};
    }

    public short[] getEphemerisdata() {
        return this.ephemerisdata;
    }

    public void setEphemerisdata(short[] sArr) {
        this.ephemerisdata = sArr;
    }
}
