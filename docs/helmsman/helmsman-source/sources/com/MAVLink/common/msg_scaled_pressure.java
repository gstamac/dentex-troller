package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_scaled_pressure extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_SCALED_PRESSURE = 29;
    public static final int MAVLINK_MSG_LENGTH = 14;
    private static final long serialVersionUID = 29;
    public float press_abs;
    public float press_diff;
    public short temperature;
    public long time_boot_ms;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(14);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 29;
        mAVLinkPacket.payload.putUnsignedInt(this.time_boot_ms);
        mAVLinkPacket.payload.putFloat(this.press_abs);
        mAVLinkPacket.payload.putFloat(this.press_diff);
        mAVLinkPacket.payload.putShort(this.temperature);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_boot_ms = mAVLinkPayload.getUnsignedInt();
        this.press_abs = mAVLinkPayload.getFloat();
        this.press_diff = mAVLinkPayload.getFloat();
        this.temperature = mAVLinkPayload.getShort();
    }

    public msg_scaled_pressure() {
        this.msgid = 29;
    }

    public msg_scaled_pressure(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 29;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_SCALED_PRESSURE - sysid:" + this.sysid + " compid:" + this.compid + " time_boot_ms:" + this.time_boot_ms + " press_abs:" + this.press_abs + " press_diff:" + this.press_diff + " temperature:" + ((int) this.temperature) + "";
    }
}
