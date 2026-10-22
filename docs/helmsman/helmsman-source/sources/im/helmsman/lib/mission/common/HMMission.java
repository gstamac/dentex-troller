package im.helmsman.lib.mission.common;

/* JADX INFO: loaded from: classes2.dex */
public abstract class HMMission {
    private MissionType type;

    public interface HMMissionDownloadProgress {
        void downloadFinish(HMMission hMMission);

        void onProgress(HMMission hMMission, float f, float f2);
    }

    public interface HMMissionProgressHandler {
        void onPrepareFinish(HMMission hMMission);

        void onProgress(HMMission hMMission, float f, float f2);
    }

    public enum MissionType {
        WAYPOINT_TYPE,
        ANCHOR_TYPE,
        RECORD_TYPE,
        NAVIGATION_TYPE,
        CRUISE_TYPE,
        TELECONTROL_SCANWIFI_TYPE,
        FIRMWAREINFO_TYPE,
        EPHEMERIS_DATA_TYPE,
        UPDATE_BOATCONTROL_FIRMWARE
    }

    public HMMission(MissionType missionType) {
        this.type = missionType;
    }

    public MissionType getMissionType() {
        return this.type;
    }
}
