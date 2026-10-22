package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_data_transmission_handshake extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE = 130;
    public static final int MAVLINK_MSG_LENGTH = 13;
    private static final long serialVersionUID = 130;
    public int height;
    public short jpg_quality;
    public int packets;
    public short payload;
    public long size;
    public short type;
    public int width;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(13);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE;
        mAVLinkPacket.payload.putUnsignedInt(this.size);
        mAVLinkPacket.payload.putUnsignedShort(this.width);
        mAVLinkPacket.payload.putUnsignedShort(this.height);
        mAVLinkPacket.payload.putUnsignedShort(this.packets);
        mAVLinkPacket.payload.putUnsignedByte(this.type);
        mAVLinkPacket.payload.putUnsignedByte(this.payload);
        mAVLinkPacket.payload.putUnsignedByte(this.jpg_quality);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.size = mAVLinkPayload.getUnsignedInt();
        this.width = mAVLinkPayload.getUnsignedShort();
        this.height = mAVLinkPayload.getUnsignedShort();
        this.packets = mAVLinkPayload.getUnsignedShort();
        this.type = mAVLinkPayload.getUnsignedByte();
        this.payload = mAVLinkPayload.getUnsignedByte();
        this.jpg_quality = mAVLinkPayload.getUnsignedByte();
    }

    public msg_data_transmission_handshake() {
        this.msgid = MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE;
    }

    public msg_data_transmission_handshake(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE - sysid:" + this.sysid + " compid:" + this.compid + " size:" + this.size + " width:" + this.width + " height:" + this.height + " packets:" + this.packets + " type:" + ((int) this.type) + " payload:" + ((int) this.payload) + " jpg_quality:" + ((int) this.jpg_quality) + "";
    }
}
