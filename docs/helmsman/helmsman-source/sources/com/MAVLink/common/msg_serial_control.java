package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_serial_control extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_SERIAL_CONTROL = 126;
    public static final int MAVLINK_MSG_LENGTH = 79;
    private static final long serialVersionUID = 126;
    public long baudrate;
    public short count;
    public short[] data;
    public short device;
    public short flags;
    public int timeout;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(79);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 126;
        mAVLinkPacket.payload.putUnsignedInt(this.baudrate);
        mAVLinkPacket.payload.putUnsignedShort(this.timeout);
        mAVLinkPacket.payload.putUnsignedByte(this.device);
        mAVLinkPacket.payload.putUnsignedByte(this.flags);
        mAVLinkPacket.payload.putUnsignedByte(this.count);
        for (int i = 0; i < this.data.length; i++) {
            mAVLinkPacket.payload.putUnsignedByte(this.data[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.baudrate = mAVLinkPayload.getUnsignedInt();
        this.timeout = mAVLinkPayload.getUnsignedShort();
        this.device = mAVLinkPayload.getUnsignedByte();
        this.flags = mAVLinkPayload.getUnsignedByte();
        this.count = mAVLinkPayload.getUnsignedByte();
        int i = 0;
        while (true) {
            short[] sArr = this.data;
            if (i >= sArr.length) {
                return;
            }
            sArr[i] = mAVLinkPayload.getUnsignedByte();
            i++;
        }
    }

    public msg_serial_control() {
        this.data = new short[70];
        this.msgid = 126;
    }

    public msg_serial_control(MAVLinkPacket mAVLinkPacket) {
        this.data = new short[70];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 126;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_SERIAL_CONTROL - sysid:" + this.sysid + " compid:" + this.compid + " baudrate:" + this.baudrate + " timeout:" + this.timeout + " device:" + ((int) this.device) + " flags:" + ((int) this.flags) + " count:" + ((int) this.count) + " data:" + this.data + "";
    }
}
