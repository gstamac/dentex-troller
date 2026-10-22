package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_hil_gps extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_HIL_GPS = 113;
    public static final int MAVLINK_MSG_LENGTH = 36;
    private static final long serialVersionUID = 113;
    public int alt;
    public int cog;
    public int eph;
    public int epv;
    public short fix_type;
    public int lat;
    public int lon;
    public short satellites_visible;
    public long time_usec;
    public short vd;
    public short ve;
    public int vel;
    public short vn;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(36);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 113;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putInt(this.lat);
        mAVLinkPacket.payload.putInt(this.lon);
        mAVLinkPacket.payload.putInt(this.alt);
        mAVLinkPacket.payload.putUnsignedShort(this.eph);
        mAVLinkPacket.payload.putUnsignedShort(this.epv);
        mAVLinkPacket.payload.putUnsignedShort(this.vel);
        mAVLinkPacket.payload.putShort(this.vn);
        mAVLinkPacket.payload.putShort(this.ve);
        mAVLinkPacket.payload.putShort(this.vd);
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
        this.vn = mAVLinkPayload.getShort();
        this.ve = mAVLinkPayload.getShort();
        this.vd = mAVLinkPayload.getShort();
        this.cog = mAVLinkPayload.getUnsignedShort();
        this.fix_type = mAVLinkPayload.getUnsignedByte();
        this.satellites_visible = mAVLinkPayload.getUnsignedByte();
    }

    public msg_hil_gps() {
        this.msgid = 113;
    }

    public msg_hil_gps(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 113;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_HIL_GPS - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " lat:" + this.lat + " lon:" + this.lon + " alt:" + this.alt + " eph:" + this.eph + " epv:" + this.epv + " vel:" + this.vel + " vn:" + ((int) this.vn) + " ve:" + ((int) this.ve) + " vd:" + ((int) this.vd) + " cog:" + this.cog + " fix_type:" + ((int) this.fix_type) + " satellites_visible:" + ((int) this.satellites_visible) + "";
    }
}
