package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_named_value_float extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_NAMED_VALUE_FLOAT = 251;
    public static final int MAVLINK_MSG_LENGTH = 18;
    private static final long serialVersionUID = 251;
    public byte[] name;
    public long time_boot_ms;
    public float value;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(18);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 251;
        mAVLinkPacket.payload.putUnsignedInt(this.time_boot_ms);
        mAVLinkPacket.payload.putFloat(this.value);
        for (int i = 0; i < this.name.length; i++) {
            mAVLinkPacket.payload.putByte(this.name[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_boot_ms = mAVLinkPayload.getUnsignedInt();
        this.value = mAVLinkPayload.getFloat();
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

    public msg_named_value_float() {
        this.name = new byte[10];
        this.msgid = 251;
    }

    public msg_named_value_float(MAVLinkPacket mAVLinkPacket) {
        this.name = new byte[10];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 251;
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
        return "MAVLINK_MSG_ID_NAMED_VALUE_FLOAT - sysid:" + this.sysid + " compid:" + this.compid + " time_boot_ms:" + this.time_boot_ms + " value:" + this.value + " name:" + this.name + "";
    }
}
