package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_control_command extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_CONTROL_COMMAND = 168;
    public static final int MAVLINK_MSG_LENGTH = 1;
    private static final long serialVersionUID = 168;
    public short command;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(1);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_CONTROL_COMMAND;
        mAVLinkPacket.payload.putUnsignedByte(this.command);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.command = mAVLinkPayload.getUnsignedByte();
    }

    public msg_control_command() {
        this.msgid = MAVLINK_MSG_ID_CONTROL_COMMAND;
    }

    public msg_control_command(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_CONTROL_COMMAND;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_CONTROL_COMMAND - command:" + ((int) this.command) + "";
    }
}
