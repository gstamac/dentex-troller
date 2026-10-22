package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_landing_target extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_LANDING_TARGET = 149;
    public static final int MAVLINK_MSG_LENGTH = 30;
    private static final long serialVersionUID = 149;
    public float angle_x;
    public float angle_y;
    public float distance;
    public short frame;
    public float size_x;
    public float size_y;
    public short target_num;
    public long time_usec;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(30);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 149;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putFloat(this.angle_x);
        mAVLinkPacket.payload.putFloat(this.angle_y);
        mAVLinkPacket.payload.putFloat(this.distance);
        mAVLinkPacket.payload.putFloat(this.size_x);
        mAVLinkPacket.payload.putFloat(this.size_y);
        mAVLinkPacket.payload.putUnsignedByte(this.target_num);
        mAVLinkPacket.payload.putUnsignedByte(this.frame);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.angle_x = mAVLinkPayload.getFloat();
        this.angle_y = mAVLinkPayload.getFloat();
        this.distance = mAVLinkPayload.getFloat();
        this.size_x = mAVLinkPayload.getFloat();
        this.size_y = mAVLinkPayload.getFloat();
        this.target_num = mAVLinkPayload.getUnsignedByte();
        this.frame = mAVLinkPayload.getUnsignedByte();
    }

    public msg_landing_target() {
        this.msgid = 149;
    }

    public msg_landing_target(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 149;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_LANDING_TARGET - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " angle_x:" + this.angle_x + " angle_y:" + this.angle_y + " distance:" + this.distance + " size_x:" + this.size_x + " size_y:" + this.size_y + " target_num:" + ((int) this.target_num) + " frame:" + ((int) this.frame) + "";
    }
}
