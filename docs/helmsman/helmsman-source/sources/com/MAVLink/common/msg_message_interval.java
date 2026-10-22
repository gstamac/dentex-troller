package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_message_interval extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_MESSAGE_INTERVAL = 244;
    public static final int MAVLINK_MSG_LENGTH = 6;
    private static final long serialVersionUID = 244;
    public int interval_us;
    public int message_id;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(6);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_MESSAGE_INTERVAL;
        mAVLinkPacket.payload.putInt(this.interval_us);
        mAVLinkPacket.payload.putUnsignedShort(this.message_id);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.interval_us = mAVLinkPayload.getInt();
        this.message_id = mAVLinkPayload.getUnsignedShort();
    }

    public msg_message_interval() {
        this.msgid = MAVLINK_MSG_ID_MESSAGE_INTERVAL;
    }

    public msg_message_interval(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_MESSAGE_INTERVAL;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_MESSAGE_INTERVAL - sysid:" + this.sysid + " compid:" + this.compid + " interval_us:" + this.interval_us + " message_id:" + this.message_id + "";
    }
}
