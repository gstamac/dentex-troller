package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_firmware_update_ack extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_FIRMWARE_UPDATE_ACK = 154;
    public static final int MAVLINK_MSG_LENGTH = 3;
    private static final long serialVersionUID = 154;
    public short result;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(3);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 154;
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        mAVLinkPacket.payload.putUnsignedByte(this.result);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
        this.result = mAVLinkPayload.getUnsignedByte();
    }

    public msg_firmware_update_ack() {
        this.msgid = 154;
    }

    public msg_firmware_update_ack(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 154;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_FIRMWARE_UPDATE_ACK - sysid:" + this.sysid + " compid:" + this.compid + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " result:" + ((int) this.result) + "";
    }
}
