package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_cruise_data extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_CRUISE_DATA = 172;
    public static final int MAVLINK_MSG_LENGTH = 7;
    private static final long serialVersionUID = 172;
    public int current_hdg;
    public short state;
    public int target_hdg;
    public int target_speed;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(7);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_CRUISE_DATA;
        mAVLinkPacket.payload.putUnsignedShort(this.target_hdg);
        mAVLinkPacket.payload.putUnsignedShort(this.target_speed);
        mAVLinkPacket.payload.putUnsignedShort(this.current_hdg);
        mAVLinkPacket.payload.putUnsignedByte(this.state);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.target_hdg = mAVLinkPayload.getUnsignedShort();
        this.target_speed = mAVLinkPayload.getUnsignedShort();
        this.current_hdg = mAVLinkPayload.getUnsignedShort();
        this.state = mAVLinkPayload.getUnsignedByte();
    }

    public msg_cruise_data() {
        this.msgid = MAVLINK_MSG_ID_CRUISE_DATA;
    }

    public msg_cruise_data(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_CRUISE_DATA;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_CRUISE_DATA - target_hdg:" + this.target_hdg + " target_speed:" + this.target_speed + " current_hdg:" + this.current_hdg + " state:" + ((int) this.state) + "";
    }
}
