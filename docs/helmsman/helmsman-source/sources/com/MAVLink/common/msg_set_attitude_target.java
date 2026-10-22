package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_set_attitude_target extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_SET_ATTITUDE_TARGET = 82;
    public static final int MAVLINK_MSG_LENGTH = 39;
    private static final long serialVersionUID = 82;
    public float body_pitch_rate;
    public float body_roll_rate;
    public float body_yaw_rate;
    public float[] q;
    public short target_component;
    public short target_system;
    public float thrust;
    public long time_boot_ms;
    public short type_mask;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(39);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 82;
        mAVLinkPacket.payload.putUnsignedInt(this.time_boot_ms);
        for (int i = 0; i < this.q.length; i++) {
            mAVLinkPacket.payload.putFloat(this.q[i]);
        }
        mAVLinkPacket.payload.putFloat(this.body_roll_rate);
        mAVLinkPacket.payload.putFloat(this.body_pitch_rate);
        mAVLinkPacket.payload.putFloat(this.body_yaw_rate);
        mAVLinkPacket.payload.putFloat(this.thrust);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        mAVLinkPacket.payload.putUnsignedByte(this.type_mask);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_boot_ms = mAVLinkPayload.getUnsignedInt();
        int i = 0;
        while (true) {
            float[] fArr = this.q;
            if (i < fArr.length) {
                fArr[i] = mAVLinkPayload.getFloat();
                i++;
            } else {
                this.body_roll_rate = mAVLinkPayload.getFloat();
                this.body_pitch_rate = mAVLinkPayload.getFloat();
                this.body_yaw_rate = mAVLinkPayload.getFloat();
                this.thrust = mAVLinkPayload.getFloat();
                this.target_system = mAVLinkPayload.getUnsignedByte();
                this.target_component = mAVLinkPayload.getUnsignedByte();
                this.type_mask = mAVLinkPayload.getUnsignedByte();
                return;
            }
        }
    }

    public msg_set_attitude_target() {
        this.q = new float[4];
        this.msgid = 82;
    }

    public msg_set_attitude_target(MAVLinkPacket mAVLinkPacket) {
        this.q = new float[4];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 82;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_SET_ATTITUDE_TARGET - sysid:" + this.sysid + " compid:" + this.compid + " time_boot_ms:" + this.time_boot_ms + " q:" + this.q + " body_roll_rate:" + this.body_roll_rate + " body_pitch_rate:" + this.body_pitch_rate + " body_yaw_rate:" + this.body_yaw_rate + " thrust:" + this.thrust + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " type_mask:" + ((int) this.type_mask) + "";
    }
}
