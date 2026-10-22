package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_debug_vect extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_DEBUG_VECT = 250;
    public static final int MAVLINK_MSG_LENGTH = 30;
    private static final long serialVersionUID = 250;
    public byte[] name;
    public long time_usec;
    public float x;
    public float y;
    public float z;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(30);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 250;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putFloat(this.x);
        mAVLinkPacket.payload.putFloat(this.y);
        mAVLinkPacket.payload.putFloat(this.z);
        for (int i = 0; i < this.name.length; i++) {
            mAVLinkPacket.payload.putByte(this.name[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.x = mAVLinkPayload.getFloat();
        this.y = mAVLinkPayload.getFloat();
        this.z = mAVLinkPayload.getFloat();
        int i = 0;
        while (true) {
            byte[] bArr = this.name;
            if (i >= bArr.length) {
                return;
            }
            bArr[i] = mAVLinkPayload.getByte();
            i++;
        }
    }

    public msg_debug_vect() {
        this.name = new byte[10];
        this.msgid = 250;
    }

    public msg_debug_vect(MAVLinkPacket mAVLinkPacket) {
        this.name = new byte[10];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 250;
        unpack(mAVLinkPacket.payload);
    }

    public void setName(String str) {
        int iMin = Math.min(str.length(), 10);
        for (int i = 0; i < iMin; i++) {
            this.name[i] = (byte) str.charAt(i);
        }
        while (iMin < 10) {
            this.name[iMin] = 0;
            iMin++;
        }
    }

    public String getName() {
        byte b;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 10 && (b = this.name[i]) != 0; i++) {
            stringBuffer.append((char) b);
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return "MAVLINK_MSG_ID_DEBUG_VECT - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " x:" + this.x + " y:" + this.y + " z:" + this.z + " name:" + this.name + "";
    }
}
