package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_mission_current extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_MISSION_CURRENT = 42;
    public static final int MAVLINK_MSG_LENGTH = 2;
    private static final long serialVersionUID = 42;
    public int seq;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(2);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 42;
        mAVLinkPacket.payload.putUnsignedShort(this.seq);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.seq = mAVLinkPayload.getUnsignedShort();
    }

    public msg_mission_current() {
        this.msgid = 42;
    }

    public msg_mission_current(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 42;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_MISSION_CURRENT - sysid:" + this.sysid + " compid:" + this.compid + " seq:" + this.seq + "";
    }
}
