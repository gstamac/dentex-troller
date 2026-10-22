package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_distance_sensor extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_DISTANCE_SENSOR = 132;
    public static final int MAVLINK_MSG_LENGTH = 14;
    private static final long serialVersionUID = 132;
    public short covariance;
    public int current_distance;
    public short id;
    public int max_distance;
    public int min_distance;
    public short orientation;
    public long time_boot_ms;
    public short type;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(14);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_DISTANCE_SENSOR;
        mAVLinkPacket.payload.putUnsignedInt(this.time_boot_ms);
        mAVLinkPacket.payload.putUnsignedShort(this.min_distance);
        mAVLinkPacket.payload.putUnsignedShort(this.max_distance);
        mAVLinkPacket.payload.putUnsignedShort(this.current_distance);
        mAVLinkPacket.payload.putUnsignedByte(this.type);
        mAVLinkPacket.payload.putUnsignedByte(this.id);
        mAVLinkPacket.payload.putUnsignedByte(this.orientation);
        mAVLinkPacket.payload.putUnsignedByte(this.covariance);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_boot_ms = mAVLinkPayload.getUnsignedInt();
        this.min_distance = mAVLinkPayload.getUnsignedShort();
        this.max_distance = mAVLinkPayload.getUnsignedShort();
        this.current_distance = mAVLinkPayload.getUnsignedShort();
        this.type = mAVLinkPayload.getUnsignedByte();
        this.id = mAVLinkPayload.getUnsignedByte();
        this.orientation = mAVLinkPayload.getUnsignedByte();
        this.covariance = mAVLinkPayload.getUnsignedByte();
    }

    public msg_distance_sensor() {
        this.msgid = MAVLINK_MSG_ID_DISTANCE_SENSOR;
    }

    public msg_distance_sensor(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_DISTANCE_SENSOR;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_DISTANCE_SENSOR - sysid:" + this.sysid + " compid:" + this.compid + " time_boot_ms:" + this.time_boot_ms + " min_distance:" + this.min_distance + " max_distance:" + this.max_distance + " current_distance:" + this.current_distance + " type:" + ((int) this.type) + " id:" + ((int) this.id) + " orientation:" + ((int) this.orientation) + " covariance:" + ((int) this.covariance) + "";
    }
}
