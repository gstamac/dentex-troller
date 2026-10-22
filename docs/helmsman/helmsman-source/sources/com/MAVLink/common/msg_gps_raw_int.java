package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_gps_raw_int extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GPS_RAW_INT = 24;
    public static final int MAVLINK_MSG_LENGTH = 30;
    private static final long serialVersionUID = 24;
    public int alt;
    public int cog;
    public int eph;
    public int epv;
    public short fix_type;
    public int lat;
    public int lon;
    public short satellites_visible;
    public long time_usec;
    public int vel;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(30);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 24;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putInt(this.lat);
        mAVLinkPacket.payload.putInt(this.lon);
        mAVLinkPacket.payload.putInt(this.alt);
        mAVLinkPacket.payload.putUnsignedShort(this.eph);
        mAVLinkPacket.payload.putUnsignedShort(this.epv);
        mAVLinkPacket.payload.putUnsignedShort(this.vel);
        mAVLinkPacket.payload.putUnsignedShort(this.cog);
        mAVLinkPacket.payload.putUnsignedByte(this.fix_type);
        mAVLinkPacket.payload.putUnsignedByte(this.satellites_visible);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.lat = mAVLinkPayload.getInt();
        this.lon = mAVLinkPayload.getInt();
        this.alt = mAVLinkPayload.getInt();
        this.eph = mAVLinkPayload.getUnsignedShort();
        this.epv = mAVLinkPayload.getUnsignedShort();
        this.vel = mAVLinkPayload.getUnsignedShort();
        this.cog = mAVLinkPayload.getUnsignedShort();
        this.fix_type = mAVLinkPayload.getUnsignedByte();
        this.satellites_visible = mAVLinkPayload.getUnsignedByte();
    }

    public msg_gps_raw_int() {
        this.msgid = 24;
    }

    public msg_gps_raw_int(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 24;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_GPS_RAW_INT - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " lat:" + this.lat + " lon:" + this.lon + " alt:" + this.alt + " eph:" + this.eph + " epv:" + this.epv + " vel:" + this.vel + " cog:" + this.cog + " fix_type:" + ((int) this.fix_type) + " satellites_visible:" + ((int) this.satellites_visible) + "";
    }
}
