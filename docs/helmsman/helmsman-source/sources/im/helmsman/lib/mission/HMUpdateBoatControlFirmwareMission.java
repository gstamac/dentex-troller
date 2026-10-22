package im.helmsman.lib.mission;

import im.helmsman.lib.mission.common.HMMission;
import kotlin.UByte;

/* JADX INFO: loaded from: classes2.dex */
public class HMUpdateBoatControlFirmwareMission extends HMMission {
    private int build;
    private short[] firmwaredatas;
    private int version;

    public HMUpdateBoatControlFirmwareMission() {
        super(HMMission.MissionType.UPDATE_BOATCONTROL_FIRMWARE);
    }

    public short[] getFirmwaredatas() {
        return this.firmwaredatas;
    }

    public void setFirmwaredatas(short[] sArr) {
        this.firmwaredatas = sArr;
    }

    public void setFirmwaredatas(byte[] bArr) {
        int length = bArr.length;
        short[] sArr = new short[length];
        for (int i = 0; i < length; i++) {
            sArr[i] = (short) (bArr[i] & UByte.MAX_VALUE);
        }
        this.firmwaredatas = sArr;
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
