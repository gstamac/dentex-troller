package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_local_position_ned_cov extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_LOCAL_POSITION_NED_COV = 64;
    public static final int MAVLINK_MSG_LENGTH = 225;
    private static final long serialVersionUID = 64;
    public float ax;
    public float ay;
    public float az;
    public float[] covariance;
    public short estimator_type;
    public long time_usec;
    public float vx;
    public float vy;
    public float vz;
    public float x;
    public float y;
    public float z;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 64;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putFloat(this.x);
        mAVLinkPacket.payload.putFloat(this.y);
        mAVLinkPacket.payload.putFloat(this.z);
        mAVLinkPacket.payload.putFloat(this.vx);
        mAVLinkPacket.payload.putFloat(this.vy);
        mAVLinkPacket.payload.putFloat(this.vz);
        mAVLinkPacket.payload.putFloat(this.ax);
        mAVLinkPacket.payload.putFloat(this.ay);
        mAVLinkPacket.payload.putFloat(this.az);
        for (int i = 0; i < this.covariance.length; i++) {
            mAVLinkPacket.payload.putFloat(this.covariance[i]);
        }
        mAVLinkPacket.payload.putUnsignedByte(this.estimator_type);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.x = mAVLinkPayload.getFloat();
        this.y = mAVLinkPayload.getFloat();
        this.z = mAVLinkPayload.getFloat();
        this.vx = mAVLinkPayload.getFloat();
        this.vy = mAVLinkPayload.getFloat();
        this.vz = mAVLinkPayload.getFloat();
        this.ax = mAVLinkPayload.getFloat();
        this.ay = mAVLinkPayload.getFloat();
        this.az = mAVLinkPayload.getFloat();
        int i = 0;
        while (true) {
            float[] fArr = this.covariance;
            if (i < fArr.length) {
                fArr[i] = mAVLinkPayload.getFloat();
                i++;
            } else {
                this.estimator_type = mAVLinkPayload.getUnsignedByte();
                return;
            }
        }
    }

    public msg_local_position_ned_cov() {
        this.covariance = new float[45];
        this.msgid = 64;
    }

    public msg_local_position_ned_cov(MAVLinkPacket mAVLinkPacket) {
        this.covariance = new float[45];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 64;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_LOCAL_POSITION_NED_COV - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " x:" + this.x + " y:" + this.y + " z:" + this.z + " vx:" + this.vx + " vy:" + this.vy + " vz:" + this.vz + " ax:" + this.ax + " ay:" + this.ay + " az:" + this.az + " covariance:" + this.covariance + " estimator_type:" + ((int) this.estimator_type) + "";
    }
}
