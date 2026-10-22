package im.helmsman.lib.mission;

import im.helmsman.lib.mission.common.HMMission;

/* JADX INFO: loaded from: classes2.dex */
public class HMFirmwareInfoMission extends HMMission {
    private int build;
    private int version;

    public HMFirmwareInfoMission() {
        super(HMMission.MissionType.FIRMWAREINFO_TYPE);
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int i) {
        this.version = i;
    }

    public int getBuild() {
        return this.build;
    }

    public void setBuild(int i) {
        this.build = i;
    }
}
