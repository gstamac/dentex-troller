package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_navigation_data extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_NAVIGATION_DATA = 171;
    public static final int MAVLINK_MSG_LENGTH = 5;
    private static final long serialVersionUID = 171;
    public int next_wp_index;
    public int speed;
    public short state;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(5);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_NAVIGATION_DATA;
        mAVLinkPacket.payload.putUnsignedShort(this.next_wp_index);
        mAVLinkPacket.payload.putUnsignedShort(this.speed);
        mAVLinkPacket.payload.putUnsignedByte(this.state);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.next_wp_index = mAVLinkPayload.getUnsignedShort();
        this.speed = mAVLinkPayload.getUnsignedShort();
        this.state = mAVLinkPayload.getUnsignedByte();
    }

    public msg_navigation_data() {
        this.msgid = MAVLINK_MSG_ID_NAVIGATION_DATA;
    }

    public msg_navigation_data(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_NAVIGATION_DATA;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_NAVIGATION_DATA - next_wp_index:" + this.next_wp_index + " speed:" + this.speed + " state:" + ((int) this.state) + "";
    }
}
