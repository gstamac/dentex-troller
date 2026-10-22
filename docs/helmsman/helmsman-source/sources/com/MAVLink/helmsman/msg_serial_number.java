package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_serial_number extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_SERIAL_NUMBER = 176;
    public static final int MAVLINK_MSG_LENGTH = 13;
    private static final long serialVersionUID = 176;
    public short is_activated;
    public byte[] serial_number;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(13);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 176;
        mAVLinkPacket.payload.putUnsignedByte(this.is_activated);
        for (int i = 0; i < this.serial_number.length; i++) {
            mAVLinkPacket.payload.putByte(this.serial_number[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.is_activated = mAVLinkPayload.getUnsignedByte();
        int i = 0;
        while (true) {
            byte[] bArr = this.serial_number;
            if (i >= bArr.length) {
                return;
            }
            bArr[i] = mAVLinkPayload.getByte();
            i++;
        }
    }

    public msg_serial_number() {
        this.serial_number = new byte[12];
        this.msgid = 176;
    }

    public msg_serial_number(MAVLinkPacket mAVLinkPacket) {
        this.serial_number = new byte[12];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 176;
        unpack(mAVLinkPacket.payload);
    }

    public void setSerial_Number(String str) {
        int iMin = Math.min(str.length(), 12);
        for (int i = 0; i < iMin; i++) {
            this.serial_number[i] = (byte) str.charAt(i);
        }
        while (iMin < 12) {
            this.serial_number[iMin] = 0;
            iMin++;
        }
    }

    public String getSerial_Number() {
        byte b;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 12 && (b = this.serial_number[i]) != 0; i++) {
            stringBuffer.append((char) b);
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return "MAVLINK_MSG_ID_SERIAL_NUMBER - sysid:" + this.sysid + " compid:" + this.compid + " is_activated:" + ((int) this.is_activated) + " serial_number:" + this.serial_number + "";
    }
}
