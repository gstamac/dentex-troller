package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_gps_global_origin extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GPS_GLOBAL_ORIGIN = 49;
    public static final int MAVLINK_MSG_LENGTH = 12;
    private static final long serialVersionUID = 49;
    public int altitude;
    public int latitude;
    public int longitude;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(12);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 49;
        mAVLinkPacket.payload.putInt(this.latitude);
        mAVLinkPacket.payload.putInt(this.longitude);
        mAVLinkPacket.payload.putInt(this.altitude);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.latitude = mAVLinkPayload.getInt();
        this.longitude = mAVLinkPayload.getInt();
        this.altitude = mAVLinkPayload.getInt();
    }

    public msg_gps_global_origin() {
        this.msgid = 49;
    }

    public msg_gps_global_origin(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 49;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_GPS_GLOBAL_ORIGIN - sysid:" + this.sysid + " compid:" + this.compid + " latitude:" + this.latitude + " longitude:" + this.longitude + " altitude:" + this.altitude + "";
    }
}
