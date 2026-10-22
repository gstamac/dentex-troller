package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_manual_control extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_MANUAL_CONTROL = 69;
    public static final int MAVLINK_MSG_LENGTH = 11;
    private static final long serialVersionUID = 69;
    public int buttons;
    public short r;
    public short target;
    public short x;
    public short y;
    public short z;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(11);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 69;
        mAVLinkPacket.payload.putShort(this.x);
        mAVLinkPacket.payload.putShort(this.y);
        mAVLinkPacket.payload.putShort(this.z);
        mAVLinkPacket.payload.putShort(this.r);
        mAVLinkPacket.payload.putUnsignedShort(this.buttons);
        mAVLinkPacket.payload.putUnsignedByte(this.target);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.x = mAVLinkPayload.getShort();
        this.y = mAVLinkPayload.getShort();
        this.z = mAVLinkPayload.getShort();
        this.r = mAVLinkPayload.getShort();
        this.buttons = mAVLinkPayload.getUnsignedShort();
        this.target = mAVLinkPayload.getUnsignedByte();
    }

    public msg_manual_control() {
        this.msgid = 69;
    }

    public msg_manual_control(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 69;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_MANUAL_CONTROL - sysid:" + this.sysid + " compid:" + this.compid + " x:" + ((int) this.x) + " y:" + ((int) this.y) + " z:" + ((int) this.z) + " r:" + ((int) this.r) + " buttons:" + this.buttons + " target:" + ((int) this.target) + "";
    }
}
