package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_firmware_update extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_FIRMWARE_UPDATE = 174;
    public static final int MAVLINK_MSG_LENGTH = 14;
    private static final long serialVersionUID = 174;
    public long build;
    public long size;
    public short target_component;
    public short target_system;
    public long version;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(14);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_FIRMWARE_UPDATE;
        mAVLinkPacket.payload.putUnsignedInt(this.version);
        mAVLinkPacket.payload.putUnsignedInt(this.build);
        mAVLinkPacket.payload.putUnsignedInt(this.size);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.version = mAVLinkPayload.getUnsignedInt();
        this.build = mAVLinkPayload.getUnsignedInt();
        this.size = mAVLinkPayload.getUnsignedInt();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
    }

    public msg_firmware_update() {
        this.msgid = MAVLINK_MSG_ID_FIRMWARE_UPDATE;
    }

    public msg_firmware_update(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_FIRMWARE_UPDATE;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_FIRMWARE_UPDATE - sysid:" + this.sysid + " compid:" + this.compid + " version:" + this.version + " build:" + this.build + " size:" + this.size + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + "";
    }
}
