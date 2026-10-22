package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_global_position_int_cov extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GLOBAL_POSITION_INT_COV = 63;
    public static final int MAVLINK_MSG_LENGTH = 181;
    private static final long serialVersionUID = 63;
    public int alt;
    public float[] covariance;
    public short estimator_type;
    public int lat;
    public int lon;
    public int relative_alt;
    public long time_usec;
    public float vx;
    public float vy;
    public float vz;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(181);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 63;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putInt(this.lat);
        mAVLinkPacket.payload.putInt(this.lon);
        mAVLinkPacket.payload.putInt(this.alt);
        mAVLinkPacket.payload.putInt(this.relative_alt);
        mAVLinkPacket.payload.putFloat(this.vx);
        mAVLinkPacket.payload.putFloat(this.vy);
        mAVLinkPacket.payload.putFloat(this.vz);
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
        this.lat = mAVLinkPayload.getInt();
        this.lon = mAVLinkPayload.getInt();
        this.alt = mAVLinkPayload.getInt();
        this.relative_alt = mAVLinkPayload.getInt();
        this.vx = mAVLinkPayload.getFloat();
        this.vy = mAVLinkPayload.getFloat();
        this.vz = mAVLinkPayload.getFloat();
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

    public msg_global_position_int_cov() {
        this.covariance = new float[36];
        this.msgid = 63;
    }

    public msg_global_position_int_cov(MAVLinkPacket mAVLinkPacket) {
        this.covariance = new float[36];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 63;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_GLOBAL_POSITION_INT_COV - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " lat:" + this.lat + " lon:" + this.lon + " alt:" + this.alt + " relative_alt:" + this.relative_alt + " vx:" + this.vx + " vy:" + this.vy + " vz:" + this.vz + " covariance:" + this.covariance + " estimator_type:" + ((int) this.estimator_type) + "";
    }
}
