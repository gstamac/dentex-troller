package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_power_status extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_POWER_STATUS = 125;
    public static final int MAVLINK_MSG_LENGTH = 6;
    private static final long serialVersionUID = 125;
    public int Vcc;
    public int Vservo;
    public int flags;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(6);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_POWER_STATUS;
        mAVLinkPacket.payload.putUnsignedShort(this.Vcc);
        mAVLinkPacket.payload.putUnsignedShort(this.Vservo);
        mAVLinkPacket.payload.putUnsignedShort(this.flags);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.Vcc = mAVLinkPayload.getUnsignedShort();
        this.Vservo = mAVLinkPayload.getUnsignedShort();
        this.flags = mAVLinkPayload.getUnsignedShort();
    }

    public msg_power_status() {
        this.msgid = MAVLINK_MSG_ID_POWER_STATUS;
    }

    public msg_power_status(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_POWER_STATUS;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_POWER_STATUS - sysid:" + this.sysid + " compid:" + this.compid + " Vcc:" + this.Vcc + " Vservo:" + this.Vservo + " flags:" + this.flags + "";
    }
}
