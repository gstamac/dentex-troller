package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_new_command_long_ack extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_NEW_COMMAND_LONG_ACK = 187;
    public static final int MAVLINK_MSG_LENGTH = 4;
    private static final long serialVersionUID = 187;
    public int command;
    public short result;
    public short seq;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(4);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_NEW_COMMAND_LONG_ACK;
        mAVLinkPacket.payload.putUnsignedShort(this.command);
        mAVLinkPacket.payload.putUnsignedByte(this.result);
        mAVLinkPacket.payload.putUnsignedByte(this.seq);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.command = mAVLinkPayload.getUnsignedShort();
        this.result = mAVLinkPayload.getUnsignedByte();
        this.seq = mAVLinkPayload.getUnsignedByte();
    }

    public msg_new_command_long_ack() {
        this.msgid = MAVLINK_MSG_ID_NEW_COMMAND_LONG_ACK;
    }

    public msg_new_command_long_ack(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_NEW_COMMAND_LONG_ACK;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_NEW_COMMAND_LONG_ACK - sysid:" + this.sysid + " compid:" + this.compid + " command:" + this.command + " result:" + ((int) this.result) + " seq:" + ((int) this.seq) + "";
    }
}
