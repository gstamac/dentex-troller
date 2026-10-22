package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_set_position_target_global_int extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_SET_POSITION_TARGET_GLOBAL_INT = 86;
    public static final int MAVLINK_MSG_LENGTH = 53;
    private static final long serialVersionUID = 86;
    public float afx;
    public float afy;
    public float afz;
    public float alt;
    public short coordinate_frame;
    public int lat_int;
    public int lon_int;
    public short target_component;
    public short target_system;
    public long time_boot_ms;
    public int type_mask;
    public float vx;
    public float vy;
    public float vz;
    public float yaw;
    public float yaw_rate;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(53);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 86;
        mAVLinkPacket.payload.putUnsignedInt(this.time_boot_ms);
        mAVLinkPacket.payload.putInt(this.lat_int);
        mAVLinkPacket.payload.putInt(this.lon_int);
        mAVLinkPacket.payload.putFloat(this.alt);
        mAVLinkPacket.payload.putFloat(this.vx);
        mAVLinkPacket.payload.putFloat(this.vy);
        mAVLinkPacket.payload.putFloat(this.vz);
        mAVLinkPacket.payload.putFloat(this.afx);
        mAVLinkPacket.payload.putFloat(this.afy);
        mAVLinkPacket.payload.putFloat(this.afz);
        mAVLinkPacket.payload.putFloat(this.yaw);
        mAVLinkPacket.payload.putFloat(this.yaw_rate);
        mAVLinkPacket.payload.putUnsignedShort(this.type_mask);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        mAVLinkPacket.payload.putUnsignedByte(this.coordinate_frame);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_boot_ms = mAVLinkPayload.getUnsignedInt();
        this.lat_int = mAVLinkPayload.getInt();
        this.lon_int = mAVLinkPayload.getInt();
        this.alt = mAVLinkPayload.getFloat();
        this.vx = mAVLinkPayload.getFloat();
        this.vy = mAVLinkPayload.getFloat();
        this.vz = mAVLinkPayload.getFloat();
        this.afx = mAVLinkPayload.getFloat();
        this.afy = mAVLinkPayload.getFloat();
        this.afz = mAVLinkPayload.getFloat();
        this.yaw = mAVLinkPayload.getFloat();
        this.yaw_rate = mAVLinkPayload.getFloat();
        this.type_mask = mAVLinkPayload.getUnsignedShort();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
        this.coordinate_frame = mAVLinkPayload.getUnsignedByte();
    }

    public msg_set_position_target_global_int() {
        this.msgid = 86;
    }

    public msg_set_position_target_global_int(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 86;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_SET_POSITION_TARGET_GLOBAL_INT - sysid:" + this.sysid + " compid:" + this.compid + " time_boot_ms:" + this.time_boot_ms + " lat_int:" + this.lat_int + " lon_int:" + this.lon_int + " alt:" + this.alt + " vx:" + this.vx + " vy:" + this.vy + " vz:" + this.vz + " afx:" + this.afx + " afy:" + this.afy + " afz:" + this.afz + " yaw:" + this.yaw + " yaw_rate:" + this.yaw_rate + " type_mask:" + this.type_mask + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " coordinate_frame:" + ((int) this.coordinate_frame) + "";
    }
}
