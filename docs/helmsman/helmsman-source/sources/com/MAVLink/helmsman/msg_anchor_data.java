package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_anchor_data extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_ANCHOR_DATA = 169;
    public static final int MAVLINK_MSG_LENGTH = 11;
    private static final long serialVersionUID = 169;
    public long lat;
    public long lon;
    public int speed;
    public short state;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(11);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_ANCHOR_DATA;
        mAVLinkPacket.payload.putUnsignedInt(this.lat);
        mAVLinkPacket.payload.putUnsignedInt(this.lon);
        mAVLinkPacket.payload.putUnsignedShort(this.speed);
        mAVLinkPacket.payload.putUnsignedByte(this.state);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.lat = mAVLinkPayload.getUnsignedInt();
        this.lon = mAVLinkPayload.getUnsignedInt();
        this.speed = mAVLinkPayload.getUnsignedShort();
        this.state = mAVLinkPayload.getUnsignedByte();
    }

    public msg_anchor_data() {
        this.msgid = MAVLINK_MSG_ID_ANCHOR_DATA;
    }

    public msg_anchor_data(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_ANCHOR_DATA;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_ANCHOR_DATA - lat:" + this.lat + " lon:" + this.lon + " speed:" + this.speed + " state:" + ((int) this.state) + "";
    }
}
