package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_mode_enable extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_MODE_ENABLE = 182;
    public static final int MAVLINK_MSG_LENGTH = 4;
    private static final long serialVersionUID = 182;
    public int control_mode_enable_flag;
    public int function_enable_flag;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(4);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 182;
        mAVLinkPacket.payload.putUnsignedShort(this.control_mode_enable_flag);
        mAVLinkPacket.payload.putUnsignedShort(this.function_enable_flag);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.control_mode_enable_flag = mAVLinkPayload.getUnsignedShort();
        this.function_enable_flag = mAVLinkPayload.getUnsignedShort();
    }

    public msg_mode_enable() {
        this.msgid = 182;
    }

    public msg_mode_enable(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 182;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_MODE_ENABLE - sysid:" + this.sysid + " compid:" + this.compid + " control_mode_enable_flag:" + this.control_mode_enable_flag + " function_enable_flag:" + this.function_enable_flag + "";
    }
}
