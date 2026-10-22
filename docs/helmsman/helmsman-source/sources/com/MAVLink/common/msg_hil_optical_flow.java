package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_hil_optical_flow extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_HIL_OPTICAL_FLOW = 114;
    public static final int MAVLINK_MSG_LENGTH = 44;
    private static final long serialVersionUID = 114;
    public float distance;
    public float integrated_x;
    public float integrated_xgyro;
    public float integrated_y;
    public float integrated_ygyro;
    public float integrated_zgyro;
    public long integration_time_us;
    public short quality;
    public short sensor_id;
    public short temperature;
    public long time_delta_distance_us;
    public long time_usec;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(44);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 114;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putUnsignedInt(this.integration_time_us);
        mAVLinkPacket.payload.putFloat(this.integrated_x);
        mAVLinkPacket.payload.putFloat(this.integrated_y);
        mAVLinkPacket.payload.putFloat(this.integrated_xgyro);
        mAVLinkPacket.payload.putFloat(this.integrated_ygyro);
        mAVLinkPacket.payload.putFloat(this.integrated_zgyro);
        mAVLinkPacket.payload.putUnsignedInt(this.time_delta_distance_us);
        mAVLinkPacket.payload.putFloat(this.distance);
        mAVLinkPacket.payload.putShort(this.temperature);
        mAVLinkPacket.payload.putUnsignedByte(this.sensor_id);
        mAVLinkPacket.payload.putUnsignedByte(this.quality);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.integration_time_us = mAVLinkPayload.getUnsignedInt();
        this.integrated_x = mAVLinkPayload.getFloat();
        this.integrated_y = mAVLinkPayload.getFloat();
        this.integrated_xgyro = mAVLinkPayload.getFloat();
        this.integrated_ygyro = mAVLinkPayload.getFloat();
        this.integrated_zgyro = mAVLinkPayload.getFloat();
        this.time_delta_distance_us = mAVLinkPayload.getUnsignedInt();
        this.distance = mAVLinkPayload.getFloat();
        this.temperature = mAVLinkPayload.getShort();
        this.sensor_id = mAVLinkPayload.getUnsignedByte();
        this.quality = mAVLinkPayload.getUnsignedByte();
    }

    public msg_hil_optical_flow() {
        this.msgid = 114;
    }

    public msg_hil_optical_flow(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 114;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_HIL_OPTICAL_FLOW - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " integration_time_us:" + this.integration_time_us + " integrated_x:" + this.integrated_x + " integrated_y:" + this.integrated_y + " integrated_xgyro:" + this.integrated_xgyro + " integrated_ygyro:" + this.integrated_ygyro + " integrated_zgyro:" + this.integrated_zgyro + " time_delta_distance_us:" + this.time_delta_distance_us + " distance:" + this.distance + " temperature:" + ((int) this.temperature) + " sensor_id:" + ((int) this.sensor_id) + " quality:" + ((int) this.quality) + "";
    }
}
