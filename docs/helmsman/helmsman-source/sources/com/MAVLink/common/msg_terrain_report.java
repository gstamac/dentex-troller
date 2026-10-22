package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_terrain_report extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_TERRAIN_REPORT = 136;
    public static final int MAVLINK_MSG_LENGTH = 22;
    private static final long serialVersionUID = 136;
    public float current_height;
    public int lat;
    public int loaded;
    public int lon;
    public int pending;
    public int spacing;
    public float terrain_height;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(22);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_TERRAIN_REPORT;
        mAVLinkPacket.payload.putInt(this.lat);
        mAVLinkPacket.payload.putInt(this.lon);
        mAVLinkPacket.payload.putFloat(this.terrain_height);
        mAVLinkPacket.payload.putFloat(this.current_height);
        mAVLinkPacket.payload.putUnsignedShort(this.spacing);
        mAVLinkPacket.payload.putUnsignedShort(this.pending);
        mAVLinkPacket.payload.putUnsignedShort(this.loaded);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.lat = mAVLinkPayload.getInt();
        this.lon = mAVLinkPayload.getInt();
        this.terrain_height = mAVLinkPayload.getFloat();
        this.current_height = mAVLinkPayload.getFloat();
        this.spacing = mAVLinkPayload.getUnsignedShort();
        this.pending = mAVLinkPayload.getUnsignedShort();
        this.loaded = mAVLinkPayload.getUnsignedShort();
    }

    public msg_terrain_report() {
        this.msgid = MAVLINK_MSG_ID_TERRAIN_REPORT;
    }

    public msg_terrain_report(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_TERRAIN_REPORT;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_TERRAIN_REPORT - sysid:" + this.sysid + " compid:" + this.compid + " lat:" + this.lat + " lon:" + this.lon + " terrain_height:" + this.terrain_height + " current_height:" + this.current_height + " spacing:" + this.spacing + " pending:" + this.pending + " loaded:" + this.loaded + "";
    }
}
