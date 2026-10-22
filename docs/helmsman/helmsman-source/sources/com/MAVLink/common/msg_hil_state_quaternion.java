package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_hil_state_quaternion extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_HIL_STATE_QUATERNION = 115;
    public static final int MAVLINK_MSG_LENGTH = 64;
    private static final long serialVersionUID = 115;
    public int alt;
    public float[] attitude_quaternion;
    public int ind_airspeed;
    public int lat;
    public int lon;
    public float pitchspeed;
    public float rollspeed;
    public long time_usec;
    public int true_airspeed;
    public short vx;
    public short vy;
    public short vz;
    public short xacc;
    public short yacc;
    public float yawspeed;
    public short zacc;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(64);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 115;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        for (int i = 0; i < this.attitude_quaternion.length; i++) {
            mAVLinkPacket.payload.putFloat(this.attitude_quaternion[i]);
        }
        mAVLinkPacket.payload.putFloat(this.rollspeed);
        mAVLinkPacket.payload.putFloat(this.pitchspeed);
        mAVLinkPacket.payload.putFloat(this.yawspeed);
        mAVLinkPacket.payload.putInt(this.lat);
        mAVLinkPacket.payload.putInt(this.lon);
        mAVLinkPacket.payload.putInt(this.alt);
        mAVLinkPacket.payload.putShort(this.vx);
        mAVLinkPacket.payload.putShort(this.vy);
        mAVLinkPacket.payload.putShort(this.vz);
        mAVLinkPacket.payload.putUnsignedShort(this.ind_airspeed);
        mAVLinkPacket.payload.putUnsignedShort(this.true_airspeed);
        mAVLinkPacket.payload.putShort(this.xacc);
        mAVLinkPacket.payload.putShort(this.yacc);
        mAVLinkPacket.payload.putShort(this.zacc);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        int i = 0;
        while (true) {
            float[] fArr = this.attitude_quaternion;
            if (i < fArr.length) {
                fArr[i] = mAVLinkPayload.getFloat();
                i++;
            } else {
                this.rollspeed = mAVLinkPayload.getFloat();
                this.pitchspeed = mAVLinkPayload.getFloat();
                this.yawspeed = mAVLinkPayload.getFloat();
                this.lat = mAVLinkPayload.getInt();
                this.lon = mAVLinkPayload.getInt();
                this.alt = mAVLinkPayload.getInt();
                this.vx = mAVLinkPayload.getShort();
                this.vy = mAVLinkPayload.getShort();
                this.vz = mAVLinkPayload.getShort();
                this.ind_airspeed = mAVLinkPayload.getUnsignedShort();
                this.true_airspeed = mAVLinkPayload.getUnsignedShort();
                this.xacc = mAVLinkPayload.getShort();
                this.yacc = mAVLinkPayload.getShort();
                this.zacc = mAVLinkPayload.getShort();
                return;
            }
        }
    }

    public msg_hil_state_quaternion() {
        this.attitude_quaternion = new float[4];
        this.msgid = 115;
    }

    public msg_hil_state_quaternion(MAVLinkPacket mAVLinkPacket) {
        this.attitude_quaternion = new float[4];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 115;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_HIL_STATE_QUATERNION - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " attitude_quaternion:" + this.attitude_quaternion + " rollspeed:" + this.rollspeed + " pitchspeed:" + this.pitchspeed + " yawspeed:" + this.yawspeed + " lat:" + this.lat + " lon:" + this.lon + " alt:" + this.alt + " vx:" + ((int) this.vx) + " vy:" + ((int) this.vy) + " vz:" + ((int) this.vz) + " ind_airspeed:" + this.ind_airspeed + " true_airspeed:" + this.true_airspeed + " xacc:" + ((int) this.xacc) + " yacc:" + ((int) this.yacc) + " zacc:" + ((int) this.zacc) + "";
    }
}
