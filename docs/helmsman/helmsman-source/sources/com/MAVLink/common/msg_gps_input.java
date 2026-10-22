package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_gps_input extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GPS_INPUT = 232;
    public static final int MAVLINK_MSG_LENGTH = 63;
    private static final long serialVersionUID = 232;
    public float alt;
    public short fix_type;
    public short gps_id;
    public float hdop;
    public float horiz_accuracy;
    public int ignore_flags;
    public int lat;
    public int lon;
    public short satellites_visible;
    public float speed_accuracy;
    public long time_usec;
    public int time_week;
    public long time_week_ms;
    public float vd;
    public float vdop;
    public float ve;
    public float vert_accuracy;
    public float vn;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(63);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_GPS_INPUT;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putUnsignedInt(this.time_week_ms);
        mAVLinkPacket.payload.putInt(this.lat);
        mAVLinkPacket.payload.putInt(this.lon);
        mAVLinkPacket.payload.putFloat(this.alt);
        mAVLinkPacket.payload.putFloat(this.hdop);
        mAVLinkPacket.payload.putFloat(this.vdop);
        mAVLinkPacket.payload.putFloat(this.vn);
        mAVLinkPacket.payload.putFloat(this.ve);
        mAVLinkPacket.payload.putFloat(this.vd);
        mAVLinkPacket.payload.putFloat(this.speed_accuracy);
        mAVLinkPacket.payload.putFloat(this.horiz_accuracy);
        mAVLinkPacket.payload.putFloat(this.vert_accuracy);
        mAVLinkPacket.payload.putUnsignedShort(this.ignore_flags);
        mAVLinkPacket.payload.putUnsignedShort(this.time_week);
        mAVLinkPacket.payload.putUnsignedByte(this.gps_id);
        mAVLinkPacket.payload.putUnsignedByte(this.fix_type);
        mAVLinkPacket.payload.putUnsignedByte(this.satellites_visible);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.time_week_ms = mAVLinkPayload.getUnsignedInt();
        this.lat = mAVLinkPayload.getInt();
        this.lon = mAVLinkPayload.getInt();
        this.alt = mAVLinkPayload.getFloat();
        this.hdop = mAVLinkPayload.getFloat();
        this.vdop = mAVLinkPayload.getFloat();
        this.vn = mAVLinkPayload.getFloat();
        this.ve = mAVLinkPayload.getFloat();
        this.vd = mAVLinkPayload.getFloat();
        this.speed_accuracy = mAVLinkPayload.getFloat();
        this.horiz_accuracy = mAVLinkPayload.getFloat();
        this.vert_accuracy = mAVLinkPayload.getFloat();
        this.ignore_flags = mAVLinkPayload.getUnsignedShort();
        this.time_week = mAVLinkPayload.getUnsignedShort();
        this.gps_id = mAVLinkPayload.getUnsignedByte();
        this.fix_type = mAVLinkPayload.getUnsignedByte();
        this.satellites_visible = mAVLinkPayload.getUnsignedByte();
    }

    public msg_gps_input() {
        this.msgid = MAVLINK_MSG_ID_GPS_INPUT;
    }

    public msg_gps_input(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_GPS_INPUT;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_GPS_INPUT - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " time_week_ms:" + this.time_week_ms + " lat:" + this.lat + " lon:" + this.lon + " alt:" + this.alt + " hdop:" + this.hdop + " vdop:" + this.vdop + " vn:" + this.vn + " ve:" + this.ve + " vd:" + this.vd + " speed_accuracy:" + this.speed_accuracy + " horiz_accuracy:" + this.horiz_accuracy + " vert_accuracy:" + this.vert_accuracy + " ignore_flags:" + this.ignore_flags + " time_week:" + this.time_week + " gps_id:" + ((int) this.gps_id) + " fix_type:" + ((int) this.fix_type) + " satellites_visible:" + ((int) this.satellites_visible) + "";
    }
}
