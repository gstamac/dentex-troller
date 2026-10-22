package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_control_system_state extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_CONTROL_SYSTEM_STATE = 146;
    public static final int MAVLINK_MSG_LENGTH = 100;
    private static final long serialVersionUID = 146;
    public float airspeed;
    public float pitch_rate;
    public float[] pos_variance;
    public float[] q;
    public float roll_rate;
    public long time_usec;
    public float[] vel_variance;
    public float x_acc;
    public float x_pos;
    public float x_vel;
    public float y_acc;
    public float y_pos;
    public float y_vel;
    public float yaw_rate;
    public float z_acc;
    public float z_pos;
    public float z_vel;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(100);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 146;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putFloat(this.x_acc);
        mAVLinkPacket.payload.putFloat(this.y_acc);
        mAVLinkPacket.payload.putFloat(this.z_acc);
        mAVLinkPacket.payload.putFloat(this.x_vel);
        mAVLinkPacket.payload.putFloat(this.y_vel);
        mAVLinkPacket.payload.putFloat(this.z_vel);
        mAVLinkPacket.payload.putFloat(this.x_pos);
        mAVLinkPacket.payload.putFloat(this.y_pos);
        mAVLinkPacket.payload.putFloat(this.z_pos);
        mAVLinkPacket.payload.putFloat(this.airspeed);
        for (int i = 0; i < this.vel_variance.length; i++) {
            mAVLinkPacket.payload.putFloat(this.vel_variance[i]);
        }
        for (int i2 = 0; i2 < this.pos_variance.length; i2++) {
            mAVLinkPacket.payload.putFloat(this.pos_variance[i2]);
        }
        for (int i3 = 0; i3 < this.q.length; i3++) {
            mAVLinkPacket.payload.putFloat(this.q[i3]);
        }
        mAVLinkPacket.payload.putFloat(this.roll_rate);
        mAVLinkPacket.payload.putFloat(this.pitch_rate);
        mAVLinkPacket.payload.putFloat(this.yaw_rate);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.x_acc = mAVLinkPayload.getFloat();
        this.y_acc = mAVLinkPayload.getFloat();
        this.z_acc = mAVLinkPayload.getFloat();
        this.x_vel = mAVLinkPayload.getFloat();
        this.y_vel = mAVLinkPayload.getFloat();
        this.z_vel = mAVLinkPayload.getFloat();
        this.x_pos = mAVLinkPayload.getFloat();
        this.y_pos = mAVLinkPayload.getFloat();
        this.z_pos = mAVLinkPayload.getFloat();
        this.airspeed = mAVLinkPayload.getFloat();
        int i = 0;
        int i2 = 0;
        while (true) {
            float[] fArr = this.vel_variance;
            if (i2 >= fArr.length) {
                break;
            }
            fArr[i2] = mAVLinkPayload.getFloat();
            i2++;
        }
        int i3 = 0;
        while (true) {
            float[] fArr2 = this.pos_variance;
            if (i3 >= fArr2.length) {
                break;
            }
            fArr2[i3] = mAVLinkPayload.getFloat();
            i3++;
        }
        while (true) {
            float[] fArr3 = this.q;
            if (i < fArr3.length) {
                fArr3[i] = mAVLinkPayload.getFloat();
                i++;
            } else {
                this.roll_rate = mAVLinkPayload.getFloat();
                this.pitch_rate = mAVLinkPayload.getFloat();
                this.yaw_rate = mAVLinkPayload.getFloat();
                return;
            }
        }
    }

    public msg_control_system_state() {
        this.vel_variance = new float[3];
        this.pos_variance = new float[3];
        this.q = new float[4];
        this.msgid = 146;
    }

    public msg_control_system_state(MAVLinkPacket mAVLinkPacket) {
        this.vel_variance = new float[3];
        this.pos_variance = new float[3];
        this.q = new float[4];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 146;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_CONTROL_SYSTEM_STATE - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " x_acc:" + this.x_acc + " y_acc:" + this.y_acc + " z_acc:" + this.z_acc + " x_vel:" + this.x_vel + " y_vel:" + this.y_vel + " z_vel:" + this.z_vel + " x_pos:" + this.x_pos + " y_pos:" + this.y_pos + " z_pos:" + this.z_pos + " airspeed:" + this.airspeed + " vel_variance:" + this.vel_variance + " pos_variance:" + this.pos_variance + " q:" + this.q + " roll_rate:" + this.roll_rate + " pitch_rate:" + this.pitch_rate + " yaw_rate:" + this.yaw_rate + "";
    }
}
