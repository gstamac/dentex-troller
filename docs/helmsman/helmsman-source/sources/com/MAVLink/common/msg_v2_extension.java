package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_v2_extension extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_V2_EXTENSION = 248;
    public static final int MAVLINK_MSG_LENGTH = 254;
    private static final long serialVersionUID = 248;
    public int message_type;
    public short[] payload;
    public short target_component;
    public short target_network;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(254);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_V2_EXTENSION;
        mAVLinkPacket.payload.putUnsignedShort(this.message_type);
        mAVLinkPacket.payload.putUnsignedByte(this.target_network);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        for (int i = 0; i < this.payload.length; i++) {
            mAVLinkPacket.payload.putUnsignedByte(this.payload[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.message_type = mAVLinkPayload.getUnsignedShort();
        this.target_network = mAVLinkPayload.getUnsignedByte();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
        int i = 0;
        while (true) {
            short[] sArr = this.payload;
            if (i >= sArr.length) {
                return;
            }
            sArr[i] = mAVLinkPayload.getUnsignedByte();
            i++;
        }
    }

    public msg_v2_extension() {
        this.payload = new short[msg_memory_vect.MAVLINK_MSG_ID_MEMORY_VECT];
        this.msgid = MAVLINK_MSG_ID_V2_EXTENSION;
    }

    public msg_v2_extension(MAVLinkPacket mAVLinkPacket) {
        this.payload = new short[msg_memory_vect.MAVLINK_MSG_ID_MEMORY_VECT];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_V2_EXTENSION;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_V2_EXTENSION - sysid:" + this.sysid + " compid:" + this.compid + " message_type:" + this.message_type + " target_network:" + ((int) this.target_network) + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " payload:" + this.payload + "";
    }
}
