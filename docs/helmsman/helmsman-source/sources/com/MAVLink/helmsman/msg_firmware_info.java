package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_firmware_info extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_FIRMWARE_INFO = 152;
    public static final int MAVLINK_MSG_LENGTH = 8;
    private static final long serialVersionUID = 152;
    public long build;
    public long version;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(8);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 152;
        mAVLinkPacket.payload.putUnsignedInt(this.version);
        mAVLinkPacket.payload.putUnsignedInt(this.build);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.version = mAVLinkPayload.getUnsignedInt();
        this.build = mAVLinkPayload.getUnsignedInt();
    }

    public msg_firmware_info() {
        this.msgid = 152;
    }

    public msg_firmware_info(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 152;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_FIRMWARE_INFO - sysid:" + this.sysid + " compid:" + this.compid + " version:" + this.version + " build:" + this.build + "";
    }
}
