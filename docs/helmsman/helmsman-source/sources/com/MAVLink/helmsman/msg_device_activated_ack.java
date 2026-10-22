package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_device_activated_ack extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_DEVICE_ACTIVATED_ACK = 181;
    public static final int MAVLINK_MSG_LENGTH = 2;
    private static final long serialVersionUID = 181;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(2);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 181;
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
    }

    public msg_device_activated_ack() {
        this.msgid = 181;
    }

    public msg_device_activated_ack(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 181;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_DEVICE_ACTIVATED_ACK - sysid:" + this.sysid + " compid:" + this.compid + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + "";
    }
}
