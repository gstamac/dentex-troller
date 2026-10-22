package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_vision_speed_estimate extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_VISION_SPEED_ESTIMATE = 103;
    public static final int MAVLINK_MSG_LENGTH = 20;
    private static final long serialVersionUID = 103;
    public long usec;
    public float x;
    public float y;
    public float z;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(20);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 103;
        mAVLinkPacket.payload.putUnsignedLong(this.usec);
        mAVLinkPacket.payload.putFloat(this.x);
        mAVLinkPacket.payload.putFloat(this.y);
        mAVLinkPacket.payload.putFloat(this.z);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.usec = mAVLinkPayload.getUnsignedLong();
        this.x = mAVLinkPayload.getFloat();
        this.y = mAVLinkPayload.getFloat();
        this.z = mAVLinkPayload.getFloat();
    }

    public msg_vision_speed_estimate() {
        this.msgid = 103;
    }

    public msg_vision_speed_estimate(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 103;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_VISION_SPEED_ESTIMATE - sysid:" + this.sysid + " compid:" + this.compid + " usec:" + this.usec + " x:" + this.x + " y:" + this.y + " z:" + this.z + "";
    }
}
