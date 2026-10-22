package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_memory_vect extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_MEMORY_VECT = 249;
    public static final int MAVLINK_MSG_LENGTH = 36;
    private static final long serialVersionUID = 249;
    public int address;
    public short type;
    public byte[] value;
    public short ver;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(36);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_MEMORY_VECT;
        mAVLinkPacket.payload.putUnsignedShort(this.address);
        mAVLinkPacket.payload.putUnsignedByte(this.ver);
        mAVLinkPacket.payload.putUnsignedByte(this.type);
        for (int i = 0; i < this.value.length; i++) {
            mAVLinkPacket.payload.putByte(this.value[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.address = mAVLinkPayload.getUnsignedShort();
        this.ver = mAVLinkPayload.getUnsignedByte();
        this.type = mAVLinkPayload.getUnsignedByte();
        int i = 0;
        while (true) {
            byte[] bArr = this.value;
            if (i >= bArr.length) {
                return;
            }
            bArr[i] = mAVLinkPayload.getByte();
            i++;
        }
    }

    public msg_memory_vect() {
        this.value = new byte[32];
        this.msgid = MAVLINK_MSG_ID_MEMORY_VECT;
    }

    public msg_memory_vect(MAVLinkPacket mAVLinkPacket) {
        this.value = new byte[32];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_MEMORY_VECT;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_MEMORY_VECT - sysid:" + this.sysid + " compid:" + this.compid + " address:" + this.address + " ver:" + ((int) this.ver) + " type:" + ((int) this.type) + " value:" + this.value + "";
    }
}
