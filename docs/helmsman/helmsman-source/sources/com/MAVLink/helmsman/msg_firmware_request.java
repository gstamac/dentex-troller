package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_firmware_request extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_FIRMWARE_REQUEST = 151;
    public static final int MAVLINK_MSG_LENGTH = 17;
    private static final long serialVersionUID = 151;
    public long address;
    public long current_build;
    public long current_version;
    public short info;
    public long size;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(17);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 151;
        mAVLinkPacket.payload.putUnsignedInt(this.address);
        mAVLinkPacket.payload.putUnsignedInt(this.size);
        mAVLinkPacket.payload.putUnsignedInt(this.current_version);
        mAVLinkPacket.payload.putUnsignedInt(this.current_build);
        mAVLinkPacket.payload.putUnsignedByte(this.info);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.address = mAVLinkPayload.getUnsignedInt();
        this.size = mAVLinkPayload.getUnsignedInt();
        this.current_version = mAVLinkPayload.getUnsignedInt();
        this.current_build = mAVLinkPayload.getUnsignedInt();
        this.info = mAVLinkPayload.getUnsignedByte();
    }

    public msg_firmware_request() {
        this.msgid = 151;
    }

    public msg_firmware_request(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 151;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_FIRMWARE_REQUEST - address:" + this.address + " size:" + this.size + " current_version:" + this.current_version + " current_build:" + this.current_build + " info:" + ((int) this.info) + "";
    }
}
