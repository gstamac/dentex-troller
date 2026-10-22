package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_mission_write_partial_list extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_MISSION_WRITE_PARTIAL_LIST = 38;
    public static final int MAVLINK_MSG_LENGTH = 6;
    private static final long serialVersionUID = 38;
    public short end_index;
    public short start_index;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(6);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 38;
        mAVLinkPacket.payload.putShort(this.start_index);
        mAVLinkPacket.payload.putShort(this.end_index);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.start_index = mAVLinkPayload.getShort();
        this.end_index = mAVLinkPayload.getShort();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
    }

    public msg_mission_write_partial_list() {
        this.msgid = 38;
    }

    public msg_mission_write_partial_list(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 38;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_MISSION_WRITE_PARTIAL_LIST - sysid:" + this.sysid + " compid:" + this.compid + " start_index:" + ((int) this.start_index) + " end_index:" + ((int) this.end_index) + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + "";
    }
}
