package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_file_transfer_protocol extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL = 110;
    public static final int MAVLINK_MSG_LENGTH = 254;
    private static final long serialVersionUID = 110;
    public short[] payload;
    public short target_component;
    public short target_network;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(254);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 110;
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

    public msg_file_transfer_protocol() {
        this.payload = new short[251];
        this.msgid = 110;
    }

    public msg_file_transfer_protocol(MAVLinkPacket mAVLinkPacket) {
        this.payload = new short[251];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 110;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL - sysid:" + this.sysid + " compid:" + this.compid + " target_network:" + ((int) this.target_network) + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " payload:" + this.payload + "";
    }
}
