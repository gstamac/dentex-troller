package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_gps_rtcm_data extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GPS_RTCM_DATA = 233;
    public static final int MAVLINK_MSG_LENGTH = 182;
    private static final long serialVersionUID = 233;
    public short[] data;
    public short flags;
    public short len;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(182);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_GPS_RTCM_DATA;
        mAVLinkPacket.payload.putUnsignedByte(this.flags);
        mAVLinkPacket.payload.putUnsignedByte(this.len);
        for (int i = 0; i < this.data.length; i++) {
            mAVLinkPacket.payload.putUnsignedByte(this.data[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.flags = mAVLinkPayload.getUnsignedByte();
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

    public msg_gps_rtcm_data() {
        this.data = new short[180];
        this.msgid = MAVLINK_MSG_ID_GPS_RTCM_DATA;
    }

    public msg_gps_rtcm_data(MAVLinkPacket mAVLinkPacket) {
        this.data = new short[180];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_GPS_RTCM_DATA;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_GPS_RTCM_DATA - sysid:" + this.sysid + " compid:" + this.compid + " flags:" + ((int) this.flags) + " len:" + ((int) this.len) + " data:" + this.data + "";
    }
}
