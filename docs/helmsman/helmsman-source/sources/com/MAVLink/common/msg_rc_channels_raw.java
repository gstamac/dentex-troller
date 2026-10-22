package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_rc_channels_raw extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_RC_CHANNELS_RAW = 35;
    public static final int MAVLINK_MSG_LENGTH = 22;
    private static final long serialVersionUID = 35;
    public int chan1_raw;
    public int chan2_raw;
    public int chan3_raw;
    public int chan4_raw;
    public int chan5_raw;
    public int chan6_raw;
    public int chan7_raw;
    public int chan8_raw;
    public short port;
    public short rssi;
    public long time_boot_ms;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(22);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 35;
        mAVLinkPacket.payload.putUnsignedInt(this.time_boot_ms);
        mAVLinkPacket.payload.putUnsignedShort(this.chan1_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan2_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan3_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan4_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan5_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan6_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan7_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan8_raw);
        mAVLinkPacket.payload.putUnsignedByte(this.port);
        mAVLinkPacket.payload.putUnsignedByte(this.rssi);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_boot_ms = mAVLinkPayload.getUnsignedInt();
        this.chan1_raw = mAVLinkPayload.getUnsignedShort();
        this.chan2_raw = mAVLinkPayload.getUnsignedShort();
        this.chan3_raw = mAVLinkPayload.getUnsignedShort();
        this.chan4_raw = mAVLinkPayload.getUnsignedShort();
        this.chan5_raw = mAVLinkPayload.getUnsignedShort();
        this.chan6_raw = mAVLinkPayload.getUnsignedShort();
        this.chan7_raw = mAVLinkPayload.getUnsignedShort();
        this.chan8_raw = mAVLinkPayload.getUnsignedShort();
        this.port = mAVLinkPayload.getUnsignedByte();
        this.rssi = mAVLinkPayload.getUnsignedByte();
    }

    public msg_rc_channels_raw() {
        this.msgid = 35;
    }

    public msg_rc_channels_raw(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 35;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_RC_CHANNELS_RAW - sysid:" + this.sysid + " compid:" + this.compid + " time_boot_ms:" + this.time_boot_ms + " chan1_raw:" + this.chan1_raw + " chan2_raw:" + this.chan2_raw + " chan3_raw:" + this.chan3_raw + " chan4_raw:" + this.chan4_raw + " chan5_raw:" + this.chan5_raw + " chan6_raw:" + this.chan6_raw + " chan7_raw:" + this.chan7_raw + " chan8_raw:" + this.chan8_raw + " port:" + ((int) this.port) + " rssi:" + ((int) this.rssi) + "";
    }
}
