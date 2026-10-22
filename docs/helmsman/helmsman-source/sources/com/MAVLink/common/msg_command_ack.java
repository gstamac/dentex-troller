package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_command_ack extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_COMMAND_ACK = 77;
    public static final int MAVLINK_MSG_LENGTH = 3;
    private static final long serialVersionUID = 77;
    public int command;
    public short result;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(3);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 77;
        mAVLinkPacket.payload.putUnsignedShort(this.command);
        mAVLinkPacket.payload.putUnsignedByte(this.result);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.command = mAVLinkPayload.getUnsignedShort();
        this.result = mAVLinkPayload.getUnsignedByte();
    }

    public msg_command_ack() {
        this.msgid = 77;
    }

    public msg_command_ack(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 77;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_COMMAND_ACK - sysid:" + this.sysid + " compid:" + this.compid + " command:" + this.command + " result:" + ((int) this.result) + "";
    }
}
