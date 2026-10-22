package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_optical_flow extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_OPTICAL_FLOW = 100;
    public static final int MAVLINK_MSG_LENGTH = 26;
    private static final long serialVersionUID = 100;
    public float flow_comp_m_x;
    public float flow_comp_m_y;
    public short flow_x;
    public short flow_y;
    public float ground_distance;
    public short quality;
    public short sensor_id;
    public long time_usec;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(26);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 100;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putFloat(this.flow_comp_m_x);
        mAVLinkPacket.payload.putFloat(this.flow_comp_m_y);
        mAVLinkPacket.payload.putFloat(this.ground_distance);
        mAVLinkPacket.payload.putShort(this.flow_x);
        mAVLinkPacket.payload.putShort(this.flow_y);
        mAVLinkPacket.payload.putUnsignedByte(this.sensor_id);
        mAVLinkPacket.payload.putUnsignedByte(this.quality);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.flow_comp_m_x = mAVLinkPayload.getFloat();
        this.flow_comp_m_y = mAVLinkPayload.getFloat();
        this.ground_distance = mAVLinkPayload.getFloat();
        this.flow_x = mAVLinkPayload.getShort();
        this.flow_y = mAVLinkPayload.getShort();
        this.sensor_id = mAVLinkPayload.getUnsignedByte();
        this.quality = mAVLinkPayload.getUnsignedByte();
    }

    public msg_optical_flow() {
        this.msgid = 100;
    }

    public msg_optical_flow(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 100;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_OPTICAL_FLOW - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " flow_comp_m_x:" + this.flow_comp_m_x + " flow_comp_m_y:" + this.flow_comp_m_y + " ground_distance:" + this.ground_distance + " flow_x:" + ((int) this.flow_x) + " flow_y:" + ((int) this.flow_y) + " sensor_id:" + ((int) this.sensor_id) + " quality:" + ((int) this.quality) + "";
    }
}
