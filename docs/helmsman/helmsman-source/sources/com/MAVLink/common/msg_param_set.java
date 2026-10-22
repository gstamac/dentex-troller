package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_param_set extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_PARAM_SET = 23;
    public static final int MAVLINK_MSG_LENGTH = 23;
    private static final long serialVersionUID = 23;
    public byte[] param_id;
    public short param_type;
    public float param_value;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(23);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 23;
        mAVLinkPacket.payload.putFloat(this.param_value);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
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
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
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

    public msg_param_set() {
        this.param_id = new byte[16];
        this.msgid = 23;
    }

    public msg_param_set(MAVLinkPacket mAVLinkPacket) {
        this.param_id = new byte[16];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 23;
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
        return "MAVLINK_MSG_ID_PARAM_SET - sysid:" + this.sysid + " compid:" + this.compid + " param_value:" + this.param_value + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " param_id:" + this.param_id + " param_type:" + ((int) this.param_type) + "";
    }
}
