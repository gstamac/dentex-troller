package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_gps_aid_data extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GPS_AID_DATA = 163;
    public static final int MAVLINK_MSG_LENGTH = 134;
    private static final long serialVersionUID = 163;
    public long address;
    public short[] data;
    public short length;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(134);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_GPS_AID_DATA;
        mAVLinkPacket.payload.putUnsignedInt(this.address);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        mAVLinkPacket.payload.putUnsignedByte(this.length);
        for (int i = 0; i < this.data.length; i++) {
            mAVLinkPacket.payload.putUnsignedByte(this.data[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.address = mAVLinkPayload.getUnsignedInt();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
        this.length = mAVLinkPayload.getUnsignedByte();
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

    public msg_gps_aid_data() {
        this.data = new short[127];
        this.msgid = MAVLINK_MSG_ID_GPS_AID_DATA;
    }

    public msg_gps_aid_data(MAVLinkPacket mAVLinkPacket) {
        this.data = new short[127];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_GPS_AID_DATA;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_GPS_AID_DATA - sysid:" + this.sysid + " compid:" + this.compid + " address:" + this.address + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " length:" + ((int) this.length) + " data:" + this.data + "";
    }
}
