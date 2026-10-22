package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_hil_state extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_HIL_STATE = 90;
    public static final int MAVLINK_MSG_LENGTH = 56;
    private static final long serialVersionUID = 90;
    public int alt;
    public int lat;
    public int lon;
    public float pitch;
    public float pitchspeed;
    public float roll;
    public float rollspeed;
    public long time_usec;
    public short vx;
    public short vy;
    public short vz;
    public short xacc;
    public short yacc;
    public float yaw;
    public float yawspeed;
    public short zacc;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(56);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 90;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putFloat(this.roll);
        mAVLinkPacket.payload.putFloat(this.pitch);
        mAVLinkPacket.payload.putFloat(this.yaw);
        mAVLinkPacket.payload.putFloat(this.rollspeed);
        mAVLinkPacket.payload.putFloat(this.pitchspeed);
        mAVLinkPacket.payload.putFloat(this.yawspeed);
        mAVLinkPacket.payload.putInt(this.lat);
        mAVLinkPacket.payload.putInt(this.lon);
        mAVLinkPacket.payload.putInt(this.alt);
        mAVLinkPacket.payload.putShort(this.vx);
        mAVLinkPacket.payload.putShort(this.vy);
        mAVLinkPacket.payload.putShort(this.vz);
        mAVLinkPacket.payload.putShort(this.xacc);
        mAVLinkPacket.payload.putShort(this.yacc);
        mAVLinkPacket.payload.putShort(this.zacc);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.roll = mAVLinkPayload.getFloat();
        this.pitch = mAVLinkPayload.getFloat();
        this.yaw = mAVLinkPayload.getFloat();
        this.rollspeed = mAVLinkPayload.getFloat();
        this.pitchspeed = mAVLinkPayload.getFloat();
        this.yawspeed = mAVLinkPayload.getFloat();
        this.lat = mAVLinkPayload.getInt();
        this.lon = mAVLinkPayload.getInt();
        this.alt = mAVLinkPayload.getInt();
        this.vx = mAVLinkPayload.getShort();
        this.vy = mAVLinkPayload.getShort();
        this.vz = mAVLinkPayload.getShort();
        this.xacc = mAVLinkPayload.getShort();
        this.yacc = mAVLinkPayload.getShort();
        this.zacc = mAVLinkPayload.getShort();
    }

    public msg_hil_state() {
        this.msgid = 90;
    }

    public msg_hil_state(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 90;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_HIL_STATE - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " roll:" + this.roll + " pitch:" + this.pitch + " yaw:" + this.yaw + " rollspeed:" + this.rollspeed + " pitchspeed:" + this.pitchspeed + " yawspeed:" + this.yawspeed + " lat:" + this.lat + " lon:" + this.lon + " alt:" + this.alt + " vx:" + ((int) this.vx) + " vy:" + ((int) this.vy) + " vz:" + ((int) this.vz) + " xacc:" + ((int) this.xacc) + " yacc:" + ((int) this.yacc) + " zacc:" + ((int) this.zacc) + "";
    }
}
