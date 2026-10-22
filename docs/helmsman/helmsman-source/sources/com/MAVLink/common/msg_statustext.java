package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_statustext extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_STATUSTEXT = 253;
    public static final int MAVLINK_MSG_LENGTH = 51;
    private static final long serialVersionUID = 253;
    public short severity;
    public byte[] text;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(51);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_STATUSTEXT;
        mAVLinkPacket.payload.putUnsignedByte(this.severity);
        for (int i = 0; i < this.text.length; i++) {
            mAVLinkPacket.payload.putByte(this.text[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.severity = mAVLinkPayload.getUnsignedByte();
        int i = 0;
        while (true) {
            byte[] bArr = this.text;
            if (i >= bArr.length) {
                return;
            }
            bArr[i] = mAVLinkPayload.getByte();
            i++;
        }
    }

    public msg_statustext() {
        this.text = new byte[50];
        this.msgid = MAVLINK_MSG_ID_STATUSTEXT;
    }

    public msg_statustext(MAVLinkPacket mAVLinkPacket) {
        this.text = new byte[50];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_STATUSTEXT;
        unpack(mAVLinkPacket.payload);
    }

    public void setText(String str) {
        int iMin = Math.min(str.length(), 50);
        for (int i = 0; i < iMin; i++) {
            this.text[i] = (byte) str.charAt(i);
        }
        while (iMin < 50) {
            this.text[iMin] = 0;
            iMin++;
        }
    }

    public String getText() {
        byte b;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 50 && (b = this.text[i]) != 0; i++) {
            stringBuffer.append((char) b);
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return "MAVLINK_MSG_ID_STATUSTEXT - sysid:" + this.sysid + " compid:" + this.compid + " severity:" + ((int) this.severity) + " text:" + this.text + "";
    }
}
