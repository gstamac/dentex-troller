package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_gps_aid_info extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GPS_AID_INFO = 161;
    public static final int MAVLINK_MSG_LENGTH = 7;
    private static final long serialVersionUID = 161;
    public long aid_data_size;
    public short target_component;
    public short target_system;
    public short type;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(7);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_GPS_AID_INFO;
        mAVLinkPacket.payload.putUnsignedInt(this.aid_data_size);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        mAVLinkPacket.payload.putUnsignedByte(this.type);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.aid_data_size = mAVLinkPayload.getUnsignedInt();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
        this.type = mAVLinkPayload.getUnsignedByte();
    }

    public msg_gps_aid_info() {
        this.msgid = MAVLINK_MSG_ID_GPS_AID_INFO;
    }

    public msg_gps_aid_info(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_GPS_AID_INFO;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_GPS_AID_INFO - sysid:" + this.sysid + " compid:" + this.compid + " aid_data_size:" + this.aid_data_size + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " type:" + ((int) this.type) + "";
    }
}
