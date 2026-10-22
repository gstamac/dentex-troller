package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_timesync extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_TIMESYNC = 111;
    public static final int MAVLINK_MSG_LENGTH = 16;
    private static final long serialVersionUID = 111;
    public long tc1;
    public long ts1;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(16);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 111;
        mAVLinkPacket.payload.putLong(this.tc1);
        mAVLinkPacket.payload.putLong(this.ts1);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.tc1 = mAVLinkPayload.getLong();
        this.ts1 = mAVLinkPayload.getLong();
    }

    public msg_timesync() {
        this.msgid = 111;
    }

    public msg_timesync(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 111;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_TIMESYNC - sysid:" + this.sysid + " compid:" + this.compid + " tc1:" + this.tc1 + " ts1:" + this.ts1 + "";
    }
}
