package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_local_position_ned extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_LOCAL_POSITION_NED = 32;
    public static final int MAVLINK_MSG_LENGTH = 28;
    private static final long serialVersionUID = 32;
    public long time_boot_ms;
    public float vx;
    public float vy;
    public float vz;
    public float x;
    public float y;
    public float z;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(28);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 32;
        mAVLinkPacket.payload.putUnsignedInt(this.time_boot_ms);
        mAVLinkPacket.payload.putFloat(this.x);
        mAVLinkPacket.payload.putFloat(this.y);
        mAVLinkPacket.payload.putFloat(this.z);
        mAVLinkPacket.payload.putFloat(this.vx);
        mAVLinkPacket.payload.putFloat(this.vy);
        mAVLinkPacket.payload.putFloat(this.vz);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_boot_ms = mAVLinkPayload.getUnsignedInt();
        this.x = mAVLinkPayload.getFloat();
        this.y = mAVLinkPayload.getFloat();
        this.z = mAVLinkPayload.getFloat();
        this.vx = mAVLinkPayload.getFloat();
        this.vy = mAVLinkPayload.getFloat();
        this.vz = mAVLinkPayload.getFloat();
    }

    public msg_local_position_ned() {
        this.msgid = 32;
    }

    public msg_local_position_ned(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 32;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_LOCAL_POSITION_NED - sysid:" + this.sysid + " compid:" + this.compid + " time_boot_ms:" + this.time_boot_ms + " x:" + this.x + " y:" + this.y + " z:" + this.z + " vx:" + this.vx + " vy:" + this.vy + " vz:" + this.vz + "";
    }
}
