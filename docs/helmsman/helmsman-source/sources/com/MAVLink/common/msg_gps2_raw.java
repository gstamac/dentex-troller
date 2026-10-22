package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_gps2_raw extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GPS2_RAW = 124;
    public static final int MAVLINK_MSG_LENGTH = 35;
    private static final long serialVersionUID = 124;
    public int alt;
    public int cog;
    public long dgps_age;
    public short dgps_numch;
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
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(35);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 124;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putInt(this.lat);
        mAVLinkPacket.payload.putInt(this.lon);
        mAVLinkPacket.payload.putInt(this.alt);
        mAVLinkPacket.payload.putUnsignedInt(this.dgps_age);
        mAVLinkPacket.payload.putUnsignedShort(this.eph);
        mAVLinkPacket.payload.putUnsignedShort(this.epv);
        mAVLinkPacket.payload.putUnsignedShort(this.vel);
        mAVLinkPacket.payload.putUnsignedShort(this.cog);
        mAVLinkPacket.payload.putUnsignedByte(this.fix_type);
        mAVLinkPacket.payload.putUnsignedByte(this.satellites_visible);
        mAVLinkPacket.payload.putUnsignedByte(this.dgps_numch);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.lat = mAVLinkPayload.getInt();
        this.lon = mAVLinkPayload.getInt();
        this.alt = mAVLinkPayload.getInt();
        this.dgps_age = mAVLinkPayload.getUnsignedInt();
        this.eph = mAVLinkPayload.getUnsignedShort();
        this.epv = mAVLinkPayload.getUnsignedShort();
        this.vel = mAVLinkPayload.getUnsignedShort();
        this.cog = mAVLinkPayload.getUnsignedShort();
        this.fix_type = mAVLinkPayload.getUnsignedByte();
        this.satellites_visible = mAVLinkPayload.getUnsignedByte();
        this.dgps_numch = mAVLinkPayload.getUnsignedByte();
    }

    public msg_gps2_raw() {
        this.msgid = 124;
    }

    public msg_gps2_raw(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 124;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_GPS2_RAW - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " lat:" + this.lat + " lon:" + this.lon + " alt:" + this.alt + " dgps_age:" + this.dgps_age + " eph:" + this.eph + " epv:" + this.epv + " vel:" + this.vel + " cog:" + this.cog + " fix_type:" + ((int) this.fix_type) + " satellites_visible:" + ((int) this.satellites_visible) + " dgps_numch:" + ((int) this.dgps_numch) + "";
    }
}
