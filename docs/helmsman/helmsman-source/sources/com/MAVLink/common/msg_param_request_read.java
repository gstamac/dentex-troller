package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_param_request_read extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_PARAM_REQUEST_READ = 20;
    public static final int MAVLINK_MSG_LENGTH = 20;
    private static final long serialVersionUID = 20;
    public byte[] param_id;
    public short param_index;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(20);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 20;
        mAVLinkPacket.payload.putShort(this.param_index);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        for (int i = 0; i < this.param_id.length; i++) {
            mAVLinkPacket.payload.putByte(this.param_id[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.param_index = mAVLinkPayload.getShort();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
        int i = 0;
        while (true) {
            byte[] bArr = this.param_id;
            if (i >= bArr.length) {
                return;
            }
            bArr[i] = mAVLinkPayload.getByte();
            i++;
        }
    }

    public msg_param_request_read() {
        this.param_id = new byte[16];
        this.msgid = 20;
    }

    public msg_param_request_read(MAVLinkPacket mAVLinkPacket) {
        this.param_id = new byte[16];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 20;
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
        return "MAVLINK_MSG_ID_PARAM_REQUEST_READ - sysid:" + this.sysid + " compid:" + this.compid + " param_index:" + ((int) this.param_index) + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " param_id:" + this.param_id + "";
    }
}
