package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_terrain_request extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_TERRAIN_REQUEST = 133;
    public static final int MAVLINK_MSG_LENGTH = 18;
    private static final long serialVersionUID = 133;
    public int grid_spacing;
    public int lat;
    public int lon;
    public long mask;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(18);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_TERRAIN_REQUEST;
        mAVLinkPacket.payload.putUnsignedLong(this.mask);
        mAVLinkPacket.payload.putInt(this.lat);
        mAVLinkPacket.payload.putInt(this.lon);
        mAVLinkPacket.payload.putUnsignedShort(this.grid_spacing);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.mask = mAVLinkPayload.getUnsignedLong();
        this.lat = mAVLinkPayload.getInt();
        this.lon = mAVLinkPayload.getInt();
        this.grid_spacing = mAVLinkPayload.getUnsignedShort();
    }

    public msg_terrain_request() {
        this.msgid = MAVLINK_MSG_ID_TERRAIN_REQUEST;
    }

    public msg_terrain_request(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_TERRAIN_REQUEST;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_TERRAIN_REQUEST - sysid:" + this.sysid + " compid:" + this.compid + " mask:" + this.mask + " lat:" + this.lat + " lon:" + this.lon + " grid_spacing:" + this.grid_spacing + "";
    }
}
