package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_device_activation_request extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_DEVICE_ACTIVATION_REQUEST = 179;
    public static final int MAVLINK_MSG_LENGTH = 15;
    private static final long serialVersionUID = 179;
    public byte[] serial_number;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(15);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 179;
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        for (int i = 0; i < this.serial_number.length; i++) {
            mAVLinkPacket.payload.putByte(this.serial_number[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
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

    public msg_device_activation_request() {
        this.serial_number = new byte[13];
        this.msgid = 179;
    }

    public msg_device_activation_request(MAVLinkPacket mAVLinkPacket) {
        this.serial_number = new byte[13];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 179;
        unpack(mAVLinkPacket.payload);
    }

    public void setSerial_Number(String str) {
        int iMin = Math.min(str.length(), 13);
        for (int i = 0; i < iMin; i++) {
            this.serial_number[i] = (byte) str.charAt(i);
        }
        while (iMin < 13) {
            this.serial_number[iMin] = 0;
            iMin++;
        }
    }

    public String getSerial_Number() {
        byte b;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 13 && (b = this.serial_number[i]) != 0; i++) {
            stringBuffer.append((char) b);
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return "MAVLINK_MSG_ID_DEVICE_ACTIVATION_REQUEST - sysid:" + this.sysid + " compid:" + this.compid + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " serial_number:" + this.serial_number + "";
    }
}
