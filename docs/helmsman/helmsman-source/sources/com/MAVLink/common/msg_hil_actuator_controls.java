package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_hil_actuator_controls extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_HIL_ACTUATOR_CONTROLS = 93;
    public static final int MAVLINK_MSG_LENGTH = 81;
    private static final long serialVersionUID = 93;
    public float[] controls;
    public long flags;
    public short mode;
    public long time_usec;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(81);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 93;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putUnsignedLong(this.flags);
        for (int i = 0; i < this.controls.length; i++) {
            mAVLinkPacket.payload.putFloat(this.controls[i]);
        }
        mAVLinkPacket.payload.putUnsignedByte(this.mode);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.flags = mAVLinkPayload.getUnsignedLong();
        int i = 0;
        while (true) {
            float[] fArr = this.controls;
            if (i < fArr.length) {
                fArr[i] = mAVLinkPayload.getFloat();
                i++;
            } else {
                this.mode = mAVLinkPayload.getUnsignedByte();
                return;
            }
        }
    }

    public msg_hil_actuator_controls() {
        this.controls = new float[16];
        this.msgid = 93;
    }

    public msg_hil_actuator_controls(MAVLinkPacket mAVLinkPacket) {
        this.controls = new float[16];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 93;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_HIL_ACTUATOR_CONTROLS - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " flags:" + this.flags + " controls:" + this.controls + " mode:" + ((int) this.mode) + "";
    }
}
