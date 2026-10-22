package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_gps_module_info extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GPS_MODULE_INFO = 178;
    public static final int MAVLINK_MSG_LENGTH = 1;
    private static final long serialVersionUID = 178;
    public short type;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(1);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 178;
        mAVLinkPacket.payload.putUnsignedByte(this.type);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.type = mAVLinkPayload.getUnsignedByte();
    }

    public msg_gps_module_info() {
        this.msgid = 178;
    }

    public msg_gps_module_info(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 178;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_GPS_MODULE_INFO - sysid:" + this.sysid + " compid:" + this.compid + " type:" + ((int) this.type) + "";
    }
}
