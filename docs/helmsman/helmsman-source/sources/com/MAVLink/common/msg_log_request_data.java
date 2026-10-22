package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_log_request_data extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_LOG_REQUEST_DATA = 119;
    public static final int MAVLINK_MSG_LENGTH = 12;
    private static final long serialVersionUID = 119;
    public long count;
    public int id;
    public long ofs;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(12);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 119;
        mAVLinkPacket.payload.putUnsignedInt(this.ofs);
        mAVLinkPacket.payload.putUnsignedInt(this.count);
        mAVLinkPacket.payload.putUnsignedShort(this.id);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.ofs = mAVLinkPayload.getUnsignedInt();
        this.count = mAVLinkPayload.getUnsignedInt();
        this.id = mAVLinkPayload.getUnsignedShort();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
    }

    public msg_log_request_data() {
        this.msgid = 119;
    }

    public msg_log_request_data(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 119;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_LOG_REQUEST_DATA - sysid:" + this.sysid + " compid:" + this.compid + " ofs:" + this.ofs + " count:" + this.count + " id:" + this.id + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + "";
    }
}
