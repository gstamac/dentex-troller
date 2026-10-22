package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_param_value extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_PARAM_VALUE = 22;
    public static final int MAVLINK_MSG_LENGTH = 25;
    private static final long serialVersionUID = 22;
    public int param_count;
    public byte[] param_id;
    public int param_index;
    public short param_type;
    public float param_value;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(25);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 22;
        mAVLinkPacket.payload.putFloat(this.param_value);
        mAVLinkPacket.payload.putUnsignedShort(this.param_count);
        mAVLinkPacket.payload.putUnsignedShort(this.param_index);
        for (int i = 0; i < this.param_id.length; i++) {
            mAVLinkPacket.payload.putByte(this.param_id[i]);
        }
        mAVLinkPacket.payload.putUnsignedByte(this.param_type);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.param_value = mAVLinkPayload.getFloat();
        this.param_count = mAVLinkPayload.getUnsignedShort();
        this.param_index = mAVLinkPayload.getUnsignedShort();
        int i = 0;
        while (true) {
            byte[] bArr = this.param_id;
            if (i < bArr.length) {
                bArr[i] = mAVLinkPayload.getByte();
                i++;
            } else {
                this.param_type = mAVLinkPayload.getUnsignedByte();
                return;
            }
        }
    }

    public msg_param_value() {
        this.param_id = new byte[16];
        this.msgid = 22;
    }

    public msg_param_value(MAVLinkPacket mAVLinkPacket) {
        this.param_id = new byte[16];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 22;
        unpack(mAVLinkPacket.payload);
    }

    public void setParam_Id(String str) {
        int iMin = Math.min(str.length(), 16);
        for (int i = 0; i < iMin; i++) {
            this.param_id[i] = (byte) str.charAt(i);
        }
        while (iMin < 16) {
            this.param_id[iMin] = 0;
            iMin++;
        }
    }

    public String getParam_Id() {
        byte b;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 16 && (b = this.param_id[i]) != 0; i++) {
            stringBuffer.append((char) b);
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return "MAVLINK_MSG_ID_PARAM_VALUE - sysid:" + this.sysid + " compid:" + this.compid + " param_value:" + this.param_value + " param_count:" + this.param_count + " param_index:" + this.param_index + " param_id:" + this.param_id + " param_type:" + ((int) this.param_type) + "";
    }
}
