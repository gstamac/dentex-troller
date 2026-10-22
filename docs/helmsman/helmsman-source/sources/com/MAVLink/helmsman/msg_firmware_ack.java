package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_firmware_ack extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_FIRMWARE_ACK = 154;
    public static final int MAVLINK_MSG_LENGTH = 1;
    private static final long serialVersionUID = 154;
    public short status;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(1);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 154;
        mAVLinkPacket.payload.putUnsignedByte(this.status);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.status = mAVLinkPayload.getUnsignedByte();
    }

    public msg_firmware_ack() {
        this.msgid = 154;
    }

    public msg_firmware_ack(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 154;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_FIRMWARE_ACK - status:" + ((int) this.status) + "";
    }
}
