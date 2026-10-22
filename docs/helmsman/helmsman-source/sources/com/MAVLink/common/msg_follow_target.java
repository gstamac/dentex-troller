package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_follow_target extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_FOLLOW_TARGET = 144;
    public static final int MAVLINK_MSG_LENGTH = 93;
    private static final long serialVersionUID = 144;
    public float[] acc;
    public float alt;
    public float[] attitude_q;
    public long custom_state;
    public short est_capabilities;
    public int lat;
    public int lon;
    public float[] position_cov;
    public float[] rates;
    public long timestamp;
    public float[] vel;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(93);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 144;
        mAVLinkPacket.payload.putUnsignedLong(this.timestamp);
        mAVLinkPacket.payload.putUnsignedLong(this.custom_state);
        mAVLinkPacket.payload.putInt(this.lat);
        mAVLinkPacket.payload.putInt(this.lon);
        mAVLinkPacket.payload.putFloat(this.alt);
        for (int i = 0; i < this.vel.length; i++) {
            mAVLinkPacket.payload.putFloat(this.vel[i]);
        }
        for (int i2 = 0; i2 < this.acc.length; i2++) {
            mAVLinkPacket.payload.putFloat(this.acc[i2]);
        }
        for (int i3 = 0; i3 < this.attitude_q.length; i3++) {
            mAVLinkPacket.payload.putFloat(this.attitude_q[i3]);
        }
        for (int i4 = 0; i4 < this.rates.length; i4++) {
            mAVLinkPacket.payload.putFloat(this.rates[i4]);
        }
        for (int i5 = 0; i5 < this.position_cov.length; i5++) {
            mAVLinkPacket.payload.putFloat(this.position_cov[i5]);
        }
        mAVLinkPacket.payload.putUnsignedByte(this.est_capabilities);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.timestamp = mAVLinkPayload.getUnsignedLong();
        this.custom_state = mAVLinkPayload.getUnsignedLong();
        this.lat = mAVLinkPayload.getInt();
        this.lon = mAVLinkPayload.getInt();
        this.alt = mAVLinkPayload.getFloat();
        int i = 0;
        int i2 = 0;
        while (true) {
            float[] fArr = this.vel;
            if (i2 >= fArr.length) {
                break;
            }
            fArr[i2] = mAVLinkPayload.getFloat();
            i2++;
        }
        int i3 = 0;
        while (true) {
            float[] fArr2 = this.acc;
            if (i3 >= fArr2.length) {
                break;
            }
            fArr2[i3] = mAVLinkPayload.getFloat();
            i3++;
        }
        int i4 = 0;
        while (true) {
            float[] fArr3 = this.attitude_q;
            if (i4 >= fArr3.length) {
                break;
            }
            fArr3[i4] = mAVLinkPayload.getFloat();
            i4++;
        }
        int i5 = 0;
        while (true) {
            float[] fArr4 = this.rates;
            if (i5 >= fArr4.length) {
                break;
            }
            fArr4[i5] = mAVLinkPayload.getFloat();
            i5++;
        }
        while (true) {
            float[] fArr5 = this.position_cov;
            if (i < fArr5.length) {
                fArr5[i] = mAVLinkPayload.getFloat();
                i++;
            } else {
                this.est_capabilities = mAVLinkPayload.getUnsignedByte();
                return;
            }
        }
    }

    public msg_follow_target() {
        this.vel = new float[3];
        this.acc = new float[3];
        this.attitude_q = new float[4];
        this.rates = new float[3];
        this.position_cov = new float[3];
        this.msgid = 144;
    }

    public msg_follow_target(MAVLinkPacket mAVLinkPacket) {
        this.vel = new float[3];
        this.acc = new float[3];
        this.attitude_q = new float[4];
        this.rates = new float[3];
        this.position_cov = new float[3];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 144;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_FOLLOW_TARGET - sysid:" + this.sysid + " compid:" + this.compid + " timestamp:" + this.timestamp + " custom_state:" + this.custom_state + " lat:" + this.lat + " lon:" + this.lon + " alt:" + this.alt + " vel:" + this.vel + " acc:" + this.acc + " attitude_q:" + this.attitude_q + " rates:" + this.rates + " position_cov:" + this.position_cov + " est_capabilities:" + ((int) this.est_capabilities) + "";
    }
}
