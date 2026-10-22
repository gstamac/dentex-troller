package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_protocol_att_ekf_states extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_STATES = 165;
    public static final int MAVLINK_MSG_LENGTH = 36;
    private static final long serialVersionUID = 165;
    public int accel_bias_x;
    public int accel_bias_y;
    public int accel_bias_z;
    public int gyro_bias_x;
    public int gyro_bias_y;
    public int gyro_bias_z;
    public int mag_d;
    public int mag_e;
    public int mag_n;
    public int mag_x;
    public int mag_y;
    public int mag_z;
    public int pos_x;
    public int pos_y;
    public int pos_z;
    public int vel_x;
    public int vel_y;
    public int vel_z;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(36);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_STATES;
        mAVLinkPacket.payload.putUnsignedShort(this.pos_x);
        mAVLinkPacket.payload.putUnsignedShort(this.pos_y);
        mAVLinkPacket.payload.putUnsignedShort(this.pos_z);
        mAVLinkPacket.payload.putUnsignedShort(this.vel_x);
        mAVLinkPacket.payload.putUnsignedShort(this.vel_y);
        mAVLinkPacket.payload.putUnsignedShort(this.vel_z);
        mAVLinkPacket.payload.putUnsignedShort(this.gyro_bias_x);
        mAVLinkPacket.payload.putUnsignedShort(this.gyro_bias_y);
        mAVLinkPacket.payload.putUnsignedShort(this.gyro_bias_z);
        mAVLinkPacket.payload.putUnsignedShort(this.accel_bias_x);
        mAVLinkPacket.payload.putUnsignedShort(this.accel_bias_y);
        mAVLinkPacket.payload.putUnsignedShort(this.accel_bias_z);
        mAVLinkPacket.payload.putUnsignedShort(this.mag_n);
        mAVLinkPacket.payload.putUnsignedShort(this.mag_e);
        mAVLinkPacket.payload.putUnsignedShort(this.mag_d);
        mAVLinkPacket.payload.putUnsignedShort(this.mag_x);
        mAVLinkPacket.payload.putUnsignedShort(this.mag_y);
        mAVLinkPacket.payload.putUnsignedShort(this.mag_z);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.pos_x = mAVLinkPayload.getUnsignedShort();
        this.pos_y = mAVLinkPayload.getUnsignedShort();
        this.pos_z = mAVLinkPayload.getUnsignedShort();
        this.vel_x = mAVLinkPayload.getUnsignedShort();
        this.vel_y = mAVLinkPayload.getUnsignedShort();
        this.vel_z = mAVLinkPayload.getUnsignedShort();
        this.gyro_bias_x = mAVLinkPayload.getUnsignedShort();
        this.gyro_bias_y = mAVLinkPayload.getUnsignedShort();
        this.gyro_bias_z = mAVLinkPayload.getUnsignedShort();
        this.accel_bias_x = mAVLinkPayload.getUnsignedShort();
        this.accel_bias_y = mAVLinkPayload.getUnsignedShort();
        this.accel_bias_z = mAVLinkPayload.getUnsignedShort();
        this.mag_n = mAVLinkPayload.getUnsignedShort();
        this.mag_e = mAVLinkPayload.getUnsignedShort();
        this.mag_d = mAVLinkPayload.getUnsignedShort();
        this.mag_x = mAVLinkPayload.getUnsignedShort();
        this.mag_y = mAVLinkPayload.getUnsignedShort();
        this.mag_z = mAVLinkPayload.getUnsignedShort();
    }

    public msg_protocol_att_ekf_states() {
        this.msgid = MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_STATES;
    }

    public msg_protocol_att_ekf_states(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_STATES;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_STATES - sysid:" + this.sysid + " compid:" + this.compid + " pos_x:" + this.pos_x + " pos_y:" + this.pos_y + " pos_z:" + this.pos_z + " vel_x:" + this.vel_x + " vel_y:" + this.vel_y + " vel_z:" + this.vel_z + " gyro_bias_x:" + this.gyro_bias_x + " gyro_bias_y:" + this.gyro_bias_y + " gyro_bias_z:" + this.gyro_bias_z + " accel_bias_x:" + this.accel_bias_x + " accel_bias_y:" + this.accel_bias_y + " accel_bias_z:" + this.accel_bias_z + " mag_n:" + this.mag_n + " mag_e:" + this.mag_e + " mag_d:" + this.mag_d + " mag_x:" + this.mag_x + " mag_y:" + this.mag_y + " mag_z:" + this.mag_z + "";
    }
}
