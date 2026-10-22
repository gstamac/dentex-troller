package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_terrain_data extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_TERRAIN_DATA = 134;
    public static final int MAVLINK_MSG_LENGTH = 43;
    private static final long serialVersionUID = 134;
    public short[] data;
    public int grid_spacing;
    public short gridbit;
    public int lat;
    public int lon;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(43);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 134;
        mAVLinkPacket.payload.putInt(this.lat);
        mAVLinkPacket.payload.putInt(this.lon);
        mAVLinkPacket.payload.putUnsignedShort(this.grid_spacing);
        for (int i = 0; i < this.data.length; i++) {
            mAVLinkPacket.payload.putShort(this.data[i]);
        }
        mAVLinkPacket.payload.putUnsignedByte(this.gridbit);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.lat = mAVLinkPayload.getInt();
        this.lon = mAVLinkPayload.getInt();
        this.grid_spacing = mAVLinkPayload.getUnsignedShort();
        int i = 0;
        while (true) {
            short[] sArr = this.data;
            if (i < sArr.length) {
                sArr[i] = mAVLinkPayload.getShort();
                i++;
            } else {
                this.gridbit = mAVLinkPayload.getUnsignedByte();
                return;
            }
        }
    }

    public msg_terrain_data() {
        this.data = new short[16];
        this.msgid = 134;
    }

    public msg_terrain_data(MAVLinkPacket mAVLinkPacket) {
        this.data = new short[16];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 134;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_TERRAIN_DATA - sysid:" + this.sysid + " compid:" + this.compid + " lat:" + this.lat + " lon:" + this.lon + " grid_spacing:" + this.grid_spacing + " data:" + this.data + " gridbit:" + ((int) this.gridbit) + "";
    }
}
