package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_attitude_quaternion_cov extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_ATTITUDE_QUATERNION_COV = 61;
    public static final int MAVLINK_MSG_LENGTH = 72;
    private static final long serialVersionUID = 61;
    public float[] covariance;
    public float pitchspeed;
    public float[] q;
    public float rollspeed;
    public long time_usec;
    public float yawspeed;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(72);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 61;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        for (int i = 0; i < this.q.length; i++) {
            mAVLinkPacket.payload.putFloat(this.q[i]);
        }
        mAVLinkPacket.payload.putFloat(this.rollspeed);
        mAVLinkPacket.payload.putFloat(this.pitchspeed);
        mAVLinkPacket.payload.putFloat(this.yawspeed);
        for (int i2 = 0; i2 < this.covariance.length; i2++) {
            mAVLinkPacket.payload.putFloat(this.covariance[i2]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        int i = 0;
        int i2 = 0;
        while (true) {
            float[] fArr = this.q;
            if (i2 >= fArr.length) {
                break;
            }
            fArr[i2] = mAVLinkPayload.getFloat();
            i2++;
        }
        this.rollspeed = mAVLinkPayload.getFloat();
        this.pitchspeed = mAVLinkPayload.getFloat();
        this.yawspeed = mAVLinkPayload.getFloat();
        while (true) {
            float[] fArr2 = this.covariance;
            if (i >= fArr2.length) {
                return;
            }
            fArr2[i] = mAVLinkPayload.getFloat();
            i++;
        }
    }

    public msg_attitude_quaternion_cov() {
        this.q = new float[4];
        this.covariance = new float[9];
        this.msgid = 61;
    }

    public msg_attitude_quaternion_cov(MAVLinkPacket mAVLinkPacket) {
        this.q = new float[4];
        this.covariance = new float[9];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 61;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_ATTITUDE_QUATERNION_COV - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " q:" + this.q + " rollspeed:" + this.rollspeed + " pitchspeed:" + this.pitchspeed + " yawspeed:" + this.yawspeed + " covariance:" + this.covariance + "";
    }
}
