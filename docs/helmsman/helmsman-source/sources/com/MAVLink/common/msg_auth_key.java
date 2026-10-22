package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_auth_key extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_AUTH_KEY = 7;
    public static final int MAVLINK_MSG_LENGTH = 32;
    private static final long serialVersionUID = 7;
    public byte[] key;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(32);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 7;
        for (int i = 0; i < this.key.length; i++) {
            mAVLinkPacket.payload.putByte(this.key[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        int i = 0;
        while (true) {
            byte[] bArr = this.key;
            if (i >= bArr.length) {
                return;
            }
            bArr[i] = mAVLinkPayload.getByte();
            i++;
        }
    }

    public msg_auth_key() {
        this.key = new byte[32];
        this.msgid = 7;
    }

    public msg_auth_key(MAVLinkPacket mAVLinkPacket) {
        this.key = new byte[32];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 7;
        unpack(mAVLinkPacket.payload);
    }

    public void setKey(String str) {
        int iMin = Math.min(str.length(), 32);
        for (int i = 0; i < iMin; i++) {
            this.key[i] = (byte) str.charAt(i);
        }
        while (iMin < 32) {
            this.key[iMin] = 0;
            iMin++;
        }
    }

    public String getKey() {
        byte b;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 32 && (b = this.key[i]) != 0; i++) {
            stringBuffer.append((char) b);
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return "MAVLINK_MSG_ID_AUTH_KEY - sysid:" + this.sysid + " compid:" + this.compid + " key:" + this.key + "";
    }
}
