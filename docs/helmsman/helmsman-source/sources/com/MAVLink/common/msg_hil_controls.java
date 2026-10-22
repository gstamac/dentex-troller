package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_hil_controls extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_HIL_CONTROLS = 91;
    public static final int MAVLINK_MSG_LENGTH = 42;
    private static final long serialVersionUID = 91;
    public float aux1;
    public float aux2;
    public float aux3;
    public float aux4;
    public short mode;
    public short nav_mode;
    public float pitch_elevator;
    public float roll_ailerons;
    public float throttle;
    public long time_usec;
    public float yaw_rudder;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(42);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 91;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putFloat(this.roll_ailerons);
        mAVLinkPacket.payload.putFloat(this.pitch_elevator);
        mAVLinkPacket.payload.putFloat(this.yaw_rudder);
        mAVLinkPacket.payload.putFloat(this.throttle);
        mAVLinkPacket.payload.putFloat(this.aux1);
        mAVLinkPacket.payload.putFloat(this.aux2);
        mAVLinkPacket.payload.putFloat(this.aux3);
        mAVLinkPacket.payload.putFloat(this.aux4);
        mAVLinkPacket.payload.putUnsignedByte(this.mode);
        mAVLinkPacket.payload.putUnsignedByte(this.nav_mode);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.roll_ailerons = mAVLinkPayload.getFloat();
        this.pitch_elevator = mAVLinkPayload.getFloat();
        this.yaw_rudder = mAVLinkPayload.getFloat();
        this.throttle = mAVLinkPayload.getFloat();
        this.aux1 = mAVLinkPayload.getFloat();
        this.aux2 = mAVLinkPayload.getFloat();
        this.aux3 = mAVLinkPayload.getFloat();
        this.aux4 = mAVLinkPayload.getFloat();
        this.mode = mAVLinkPayload.getUnsignedByte();
        this.nav_mode = mAVLinkPayload.getUnsignedByte();
    }

    public msg_hil_controls() {
        this.msgid = 91;
    }

    public msg_hil_controls(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 91;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_HIL_CONTROLS - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " roll_ailerons:" + this.roll_ailerons + " pitch_elevator:" + this.pitch_elevator + " yaw_rudder:" + this.yaw_rudder + " throttle:" + this.throttle + " aux1:" + this.aux1 + " aux2:" + this.aux2 + " aux3:" + this.aux3 + " aux4:" + this.aux4 + " mode:" + ((int) this.mode) + " nav_mode:" + ((int) this.nav_mode) + "";
    }
}
