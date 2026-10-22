package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_change_operator_control extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_CHANGE_OPERATOR_CONTROL = 5;
    public static final int MAVLINK_MSG_LENGTH = 28;
    private static final long serialVersionUID = 5;
    public short control_request;
    public byte[] passkey;
    public short target_system;
    public short version;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(28);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 5;
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.control_request);
        mAVLinkPacket.payload.putUnsignedByte(this.version);
        for (int i = 0; i < this.passkey.length; i++) {
            mAVLinkPacket.payload.putByte(this.passkey[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.control_request = mAVLinkPayload.getUnsignedByte();
        this.version = mAVLinkPayload.getUnsignedByte();
        int i = 0;
        while (true) {
            byte[] bArr = this.passkey;
            if (i >= bArr.length) {
                return;
            }
            bArr[i] = mAVLinkPayload.getByte();
            i++;
        }
    }

    public msg_change_operator_control() {
        this.passkey = new byte[25];
        this.msgid = 5;
    }

    public msg_change_operator_control(MAVLinkPacket mAVLinkPacket) {
        this.passkey = new byte[25];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 5;
        unpack(mAVLinkPacket.payload);
    }

    public void setPasskey(String str) {
        int iMin = Math.min(str.length(), 25);
        for (int i = 0; i < iMin; i++) {
            this.passkey[i] = (byte) str.charAt(i);
        }
        while (iMin < 25) {
            this.passkey[iMin] = 0;
            iMin++;
        }
    }

    public String getPasskey() {
        byte b;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 25 && (b = this.passkey[i]) != 0; i++) {
            stringBuffer.append((char) b);
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return "MAVLINK_MSG_ID_CHANGE_OPERATOR_CONTROL - sysid:" + this.sysid + " compid:" + this.compid + " target_system:" + ((int) this.target_system) + " control_request:" + ((int) this.control_request) + " version:" + ((int) this.version) + " passkey:" + this.passkey + "";
    }
}
