package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_wind_cov extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_WIND_COV = 231;
    public static final int MAVLINK_MSG_LENGTH = 40;
    private static final long serialVersionUID = 231;
    public float horiz_accuracy;
    public long time_usec;
    public float var_horiz;
    public float var_vert;
    public float vert_accuracy;
    public float wind_alt;
    public float wind_x;
    public float wind_y;
    public float wind_z;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(40);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_WIND_COV;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putFloat(this.wind_x);
        mAVLinkPacket.payload.putFloat(this.wind_y);
        mAVLinkPacket.payload.putFloat(this.wind_z);
        mAVLinkPacket.payload.putFloat(this.var_horiz);
        mAVLinkPacket.payload.putFloat(this.var_vert);
        mAVLinkPacket.payload.putFloat(this.wind_alt);
        mAVLinkPacket.payload.putFloat(this.horiz_accuracy);
        mAVLinkPacket.payload.putFloat(this.vert_accuracy);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.wind_x = mAVLinkPayload.getFloat();
        this.wind_y = mAVLinkPayload.getFloat();
        this.wind_z = mAVLinkPayload.getFloat();
        this.var_horiz = mAVLinkPayload.getFloat();
        this.var_vert = mAVLinkPayload.getFloat();
        this.wind_alt = mAVLinkPayload.getFloat();
        this.horiz_accuracy = mAVLinkPayload.getFloat();
        this.vert_accuracy = mAVLinkPayload.getFloat();
    }

    public msg_wind_cov() {
        this.msgid = MAVLINK_MSG_ID_WIND_COV;
    }

    public msg_wind_cov(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_WIND_COV;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_WIND_COV - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " wind_x:" + this.wind_x + " wind_y:" + this.wind_y + " wind_z:" + this.wind_z + " var_horiz:" + this.var_horiz + " var_vert:" + this.var_vert + " wind_alt:" + this.wind_alt + " horiz_accuracy:" + this.horiz_accuracy + " vert_accuracy:" + this.vert_accuracy + "";
    }
}
