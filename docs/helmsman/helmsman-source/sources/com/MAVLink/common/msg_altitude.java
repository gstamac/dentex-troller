package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_altitude extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_ALTITUDE = 141;
    public static final int MAVLINK_MSG_LENGTH = 32;
    private static final long serialVersionUID = 141;
    public float altitude_amsl;
    public float altitude_local;
    public float altitude_monotonic;
    public float altitude_relative;
    public float altitude_terrain;
    public float bottom_clearance;
    public long time_usec;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(32);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 141;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putFloat(this.altitude_monotonic);
        mAVLinkPacket.payload.putFloat(this.altitude_amsl);
        mAVLinkPacket.payload.putFloat(this.altitude_local);
        mAVLinkPacket.payload.putFloat(this.altitude_relative);
        mAVLinkPacket.payload.putFloat(this.altitude_terrain);
        mAVLinkPacket.payload.putFloat(this.bottom_clearance);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.altitude_monotonic = mAVLinkPayload.getFloat();
        this.altitude_amsl = mAVLinkPayload.getFloat();
        this.altitude_local = mAVLinkPayload.getFloat();
        this.altitude_relative = mAVLinkPayload.getFloat();
        this.altitude_terrain = mAVLinkPayload.getFloat();
        this.bottom_clearance = mAVLinkPayload.getFloat();
    }

    public msg_altitude() {
        this.msgid = 141;
    }

    public msg_altitude(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 141;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_ALTITUDE - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " altitude_monotonic:" + this.altitude_monotonic + " altitude_amsl:" + this.altitude_amsl + " altitude_local:" + this.altitude_local + " altitude_relative:" + this.altitude_relative + " altitude_terrain:" + this.altitude_terrain + " bottom_clearance:" + this.bottom_clearance + "";
    }
}
