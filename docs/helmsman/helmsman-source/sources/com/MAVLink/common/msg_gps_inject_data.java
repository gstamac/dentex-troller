package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_gps_inject_data extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GPS_INJECT_DATA = 123;
    public static final int MAVLINK_MSG_LENGTH = 113;
    private static final long serialVersionUID = 123;
    public short[] data;
    public short len;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(113);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 123;
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        mAVLinkPacket.payload.putUnsignedByte(this.len);
        for (int i = 0; i < this.data.length; i++) {
            mAVLinkPacket.payload.putUnsignedByte(this.data[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
        this.len = mAVLinkPayload.getUnsignedByte();
        int i = 0;
        while (true) {
            short[] sArr = this.data;
            if (i >= sArr.length) {
                return;
            }
            sArr[i] = mAVLinkPayload.getUnsignedByte();
            i++;
        }
    }

    public msg_gps_inject_data() {
        this.data = new short[110];
        this.msgid = 123;
    }

    public msg_gps_inject_data(MAVLinkPacket mAVLinkPacket) {
        this.data = new short[110];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 123;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_GPS_INJECT_DATA - sysid:" + this.sysid + " compid:" + this.compid + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " len:" + ((int) this.len) + " data:" + this.data + "";
    }
}
