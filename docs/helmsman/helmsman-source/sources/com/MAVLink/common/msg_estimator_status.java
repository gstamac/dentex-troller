package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_estimator_status extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_ESTIMATOR_STATUS = 230;
    public static final int MAVLINK_MSG_LENGTH = 42;
    private static final long serialVersionUID = 230;
    public int flags;
    public float hagl_ratio;
    public float mag_ratio;
    public float pos_horiz_accuracy;
    public float pos_horiz_ratio;
    public float pos_vert_accuracy;
    public float pos_vert_ratio;
    public float tas_ratio;
    public long time_usec;
    public float vel_ratio;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(42);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 230;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putFloat(this.vel_ratio);
        mAVLinkPacket.payload.putFloat(this.pos_horiz_ratio);
        mAVLinkPacket.payload.putFloat(this.pos_vert_ratio);
        mAVLinkPacket.payload.putFloat(this.mag_ratio);
        mAVLinkPacket.payload.putFloat(this.hagl_ratio);
        mAVLinkPacket.payload.putFloat(this.tas_ratio);
        mAVLinkPacket.payload.putFloat(this.pos_horiz_accuracy);
        mAVLinkPacket.payload.putFloat(this.pos_vert_accuracy);
        mAVLinkPacket.payload.putUnsignedShort(this.flags);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.vel_ratio = mAVLinkPayload.getFloat();
        this.pos_horiz_ratio = mAVLinkPayload.getFloat();
        this.pos_vert_ratio = mAVLinkPayload.getFloat();
        this.mag_ratio = mAVLinkPayload.getFloat();
        this.hagl_ratio = mAVLinkPayload.getFloat();
        this.tas_ratio = mAVLinkPayload.getFloat();
        this.pos_horiz_accuracy = mAVLinkPayload.getFloat();
        this.pos_vert_accuracy = mAVLinkPayload.getFloat();
        this.flags = mAVLinkPayload.getUnsignedShort();
    }

    public msg_estimator_status() {
        this.msgid = 230;
    }

    public msg_estimator_status(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 230;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_ESTIMATOR_STATUS - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " vel_ratio:" + this.vel_ratio + " pos_horiz_ratio:" + this.pos_horiz_ratio + " pos_vert_ratio:" + this.pos_vert_ratio + " mag_ratio:" + this.mag_ratio + " hagl_ratio:" + this.hagl_ratio + " tas_ratio:" + this.tas_ratio + " pos_horiz_accuracy:" + this.pos_horiz_accuracy + " pos_vert_accuracy:" + this.pos_vert_accuracy + " flags:" + this.flags + "";
    }
}
