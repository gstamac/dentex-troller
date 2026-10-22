package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_raw_pressure extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_RAW_PRESSURE = 28;
    public static final int MAVLINK_MSG_LENGTH = 16;
    private static final long serialVersionUID = 28;
    public short press_abs;
    public short press_diff1;
    public short press_diff2;
    public short temperature;
    public long time_usec;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(16);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 28;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putShort(this.press_abs);
        mAVLinkPacket.payload.putShort(this.press_diff1);
        mAVLinkPacket.payload.putShort(this.press_diff2);
        mAVLinkPacket.payload.putShort(this.temperature);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.press_abs = mAVLinkPayload.getShort();
        this.press_diff1 = mAVLinkPayload.getShort();
        this.press_diff2 = mAVLinkPayload.getShort();
        this.temperature = mAVLinkPayload.getShort();
    }

    public msg_raw_pressure() {
        this.msgid = 28;
    }

    public msg_raw_pressure(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 28;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_RAW_PRESSURE - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " press_abs:" + ((int) this.press_abs) + " press_diff1:" + ((int) this.press_diff1) + " press_diff2:" + ((int) this.press_diff2) + " temperature:" + ((int) this.temperature) + "";
    }
}
