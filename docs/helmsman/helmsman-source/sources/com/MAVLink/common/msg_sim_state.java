package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_sim_state extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_SIM_STATE = 108;
    public static final int MAVLINK_MSG_LENGTH = 84;
    private static final long serialVersionUID = 108;
    public float alt;
    public float lat;
    public float lon;
    public float pitch;
    public float q1;
    public float q2;
    public float q3;
    public float q4;
    public float roll;
    public float std_dev_horz;
    public float std_dev_vert;
    public float vd;
    public float ve;
    public float vn;
    public float xacc;
    public float xgyro;
    public float yacc;
    public float yaw;
    public float ygyro;
    public float zacc;
    public float zgyro;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(84);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 108;
        mAVLinkPacket.payload.putFloat(this.q1);
        mAVLinkPacket.payload.putFloat(this.q2);
        mAVLinkPacket.payload.putFloat(this.q3);
        mAVLinkPacket.payload.putFloat(this.q4);
        mAVLinkPacket.payload.putFloat(this.roll);
        mAVLinkPacket.payload.putFloat(this.pitch);
        mAVLinkPacket.payload.putFloat(this.yaw);
        mAVLinkPacket.payload.putFloat(this.xacc);
        mAVLinkPacket.payload.putFloat(this.yacc);
        mAVLinkPacket.payload.putFloat(this.zacc);
        mAVLinkPacket.payload.putFloat(this.xgyro);
        mAVLinkPacket.payload.putFloat(this.ygyro);
        mAVLinkPacket.payload.putFloat(this.zgyro);
        mAVLinkPacket.payload.putFloat(this.lat);
        mAVLinkPacket.payload.putFloat(this.lon);
        mAVLinkPacket.payload.putFloat(this.alt);
        mAVLinkPacket.payload.putFloat(this.std_dev_horz);
        mAVLinkPacket.payload.putFloat(this.std_dev_vert);
        mAVLinkPacket.payload.putFloat(this.vn);
        mAVLinkPacket.payload.putFloat(this.ve);
        mAVLinkPacket.payload.putFloat(this.vd);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.q1 = mAVLinkPayload.getFloat();
        this.q2 = mAVLinkPayload.getFloat();
        this.q3 = mAVLinkPayload.getFloat();
        this.q4 = mAVLinkPayload.getFloat();
        this.roll = mAVLinkPayload.getFloat();
        this.pitch = mAVLinkPayload.getFloat();
        this.yaw = mAVLinkPayload.getFloat();
        this.xacc = mAVLinkPayload.getFloat();
        this.yacc = mAVLinkPayload.getFloat();
        this.zacc = mAVLinkPayload.getFloat();
        this.xgyro = mAVLinkPayload.getFloat();
        this.ygyro = mAVLinkPayload.getFloat();
        this.zgyro = mAVLinkPayload.getFloat();
        this.lat = mAVLinkPayload.getFloat();
        this.lon = mAVLinkPayload.getFloat();
        this.alt = mAVLinkPayload.getFloat();
        this.std_dev_horz = mAVLinkPayload.getFloat();
        this.std_dev_vert = mAVLinkPayload.getFloat();
        this.vn = mAVLinkPayload.getFloat();
        this.ve = mAVLinkPayload.getFloat();
        this.vd = mAVLinkPayload.getFloat();
    }

    public msg_sim_state() {
        this.msgid = 108;
    }

    public msg_sim_state(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 108;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_SIM_STATE - sysid:" + this.sysid + " compid:" + this.compid + " q1:" + this.q1 + " q2:" + this.q2 + " q3:" + this.q3 + " q4:" + this.q4 + " roll:" + this.roll + " pitch:" + this.pitch + " yaw:" + this.yaw + " xacc:" + this.xacc + " yacc:" + this.yacc + " zacc:" + this.zacc + " xgyro:" + this.xgyro + " ygyro:" + this.ygyro + " zgyro:" + this.zgyro + " lat:" + this.lat + " lon:" + this.lon + " alt:" + this.alt + " std_dev_horz:" + this.std_dev_horz + " std_dev_vert:" + this.std_dev_vert + " vn:" + this.vn + " ve:" + this.ve + " vd:" + this.vd + "";
    }
}
