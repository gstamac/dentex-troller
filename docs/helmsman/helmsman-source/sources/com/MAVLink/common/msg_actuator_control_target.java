package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_actuator_control_target extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_ACTUATOR_CONTROL_TARGET = 140;
    public static final int MAVLINK_MSG_LENGTH = 41;
    private static final long serialVersionUID = 140;
    public float[] controls;
    public short group_mlx;
    public long time_usec;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(41);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 140;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        for (int i = 0; i < this.controls.length; i++) {
            mAVLinkPacket.payload.putFloat(this.controls[i]);
        }
        mAVLinkPacket.payload.putUnsignedByte(this.group_mlx);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        int i = 0;
        while (true) {
            float[] fArr = this.controls;
            if (i < fArr.length) {
                fArr[i] = mAVLinkPayload.getFloat();
                i++;
            } else {
                this.group_mlx = mAVLinkPayload.getUnsignedByte();
                return;
            }
        }
    }

    public msg_actuator_control_target() {
        this.controls = new float[8];
        this.msgid = 140;
    }

    public msg_actuator_control_target(MAVLinkPacket mAVLinkPacket) {
        this.controls = new float[8];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 140;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_ACTUATOR_CONTROL_TARGET - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " controls:" + this.controls + " group_mlx:" + ((int) this.group_mlx) + "";
    }
}
