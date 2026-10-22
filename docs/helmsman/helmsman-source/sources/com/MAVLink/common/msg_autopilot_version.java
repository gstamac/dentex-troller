package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_autopilot_version extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_AUTOPILOT_VERSION = 148;
    public static final int MAVLINK_MSG_LENGTH = 60;
    private static final long serialVersionUID = 148;
    public long board_version;
    public long capabilities;
    public short[] flight_custom_version;
    public long flight_sw_version;
    public short[] middleware_custom_version;
    public long middleware_sw_version;
    public short[] os_custom_version;
    public long os_sw_version;
    public int product_id;
    public long uid;
    public int vendor_id;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(60);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 148;
        mAVLinkPacket.payload.putUnsignedLong(this.capabilities);
        mAVLinkPacket.payload.putUnsignedLong(this.uid);
        mAVLinkPacket.payload.putUnsignedInt(this.flight_sw_version);
        mAVLinkPacket.payload.putUnsignedInt(this.middleware_sw_version);
        mAVLinkPacket.payload.putUnsignedInt(this.os_sw_version);
        mAVLinkPacket.payload.putUnsignedInt(this.board_version);
        mAVLinkPacket.payload.putUnsignedShort(this.vendor_id);
        mAVLinkPacket.payload.putUnsignedShort(this.product_id);
        for (int i = 0; i < this.flight_custom_version.length; i++) {
            mAVLinkPacket.payload.putUnsignedByte(this.flight_custom_version[i]);
        }
        for (int i2 = 0; i2 < this.middleware_custom_version.length; i2++) {
            mAVLinkPacket.payload.putUnsignedByte(this.middleware_custom_version[i2]);
        }
        for (int i3 = 0; i3 < this.os_custom_version.length; i3++) {
            mAVLinkPacket.payload.putUnsignedByte(this.os_custom_version[i3]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.capabilities = mAVLinkPayload.getUnsignedLong();
        this.uid = mAVLinkPayload.getUnsignedLong();
        this.flight_sw_version = mAVLinkPayload.getUnsignedInt();
        this.middleware_sw_version = mAVLinkPayload.getUnsignedInt();
        this.os_sw_version = mAVLinkPayload.getUnsignedInt();
        this.board_version = mAVLinkPayload.getUnsignedInt();
        this.vendor_id = mAVLinkPayload.getUnsignedShort();
        this.product_id = mAVLinkPayload.getUnsignedShort();
        int i = 0;
        int i2 = 0;
        while (true) {
            short[] sArr = this.flight_custom_version;
            if (i2 >= sArr.length) {
                break;
            }
            sArr[i2] = mAVLinkPayload.getUnsignedByte();
            i2++;
        }
        int i3 = 0;
        while (true) {
            short[] sArr2 = this.middleware_custom_version;
            if (i3 >= sArr2.length) {
                break;
            }
            sArr2[i3] = mAVLinkPayload.getUnsignedByte();
            i3++;
        }
        while (true) {
            short[] sArr3 = this.os_custom_version;
            if (i >= sArr3.length) {
                return;
            }
            sArr3[i] = mAVLinkPayload.getUnsignedByte();
            i++;
        }
    }

    public msg_autopilot_version() {
        this.flight_custom_version = new short[8];
        this.middleware_custom_version = new short[8];
        this.os_custom_version = new short[8];
        this.msgid = 148;
    }

    public msg_autopilot_version(MAVLinkPacket mAVLinkPacket) {
        this.flight_custom_version = new short[8];
        this.middleware_custom_version = new short[8];
        this.os_custom_version = new short[8];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 148;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_AUTOPILOT_VERSION - sysid:" + this.sysid + " compid:" + this.compid + " capabilities:" + this.capabilities + " uid:" + this.uid + " flight_sw_version:" + this.flight_sw_version + " middleware_sw_version:" + this.middleware_sw_version + " os_sw_version:" + this.os_sw_version + " board_version:" + this.board_version + " vendor_id:" + this.vendor_id + " product_id:" + this.product_id + " flight_custom_version:" + this.flight_custom_version + " middleware_custom_version:" + this.middleware_custom_version + " os_custom_version:" + this.os_custom_version + "";
    }
}
