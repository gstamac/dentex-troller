package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_rc_channels extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_RC_CHANNELS = 65;
    public static final int MAVLINK_MSG_LENGTH = 42;
    private static final long serialVersionUID = 65;
    public int chan10_raw;
    public int chan11_raw;
    public int chan12_raw;
    public int chan13_raw;
    public int chan14_raw;
    public int chan15_raw;
    public int chan16_raw;
    public int chan17_raw;
    public int chan18_raw;
    public int chan1_raw;
    public int chan2_raw;
    public int chan3_raw;
    public int chan4_raw;
    public int chan5_raw;
    public int chan6_raw;
    public int chan7_raw;
    public int chan8_raw;
    public int chan9_raw;
    public short chancount;
    public short rssi;
    public long time_boot_ms;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(42);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 65;
        mAVLinkPacket.payload.putUnsignedInt(this.time_boot_ms);
        mAVLinkPacket.payload.putUnsignedShort(this.chan1_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan2_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan3_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan4_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan5_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan6_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan7_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan8_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan9_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan10_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan11_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan12_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan13_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan14_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan15_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan16_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan17_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan18_raw);
        mAVLinkPacket.payload.putUnsignedByte(this.chancount);
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
        this.chan9_raw = mAVLinkPayload.getUnsignedShort();
        this.chan10_raw = mAVLinkPayload.getUnsignedShort();
        this.chan11_raw = mAVLinkPayload.getUnsignedShort();
        this.chan12_raw = mAVLinkPayload.getUnsignedShort();
        this.chan13_raw = mAVLinkPayload.getUnsignedShort();
        this.chan14_raw = mAVLinkPayload.getUnsignedShort();
        this.chan15_raw = mAVLinkPayload.getUnsignedShort();
        this.chan16_raw = mAVLinkPayload.getUnsignedShort();
        this.chan17_raw = mAVLinkPayload.getUnsignedShort();
        this.chan18_raw = mAVLinkPayload.getUnsignedShort();
        this.chancount = mAVLinkPayload.getUnsignedByte();
        this.rssi = mAVLinkPayload.getUnsignedByte();
    }

    public msg_rc_channels() {
        this.msgid = 65;
    }

    public msg_rc_channels(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 65;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_RC_CHANNELS - sysid:" + this.sysid + " compid:" + this.compid + " time_boot_ms:" + this.time_boot_ms + " chan1_raw:" + this.chan1_raw + " chan2_raw:" + this.chan2_raw + " chan3_raw:" + this.chan3_raw + " chan4_raw:" + this.chan4_raw + " chan5_raw:" + this.chan5_raw + " chan6_raw:" + this.chan6_raw + " chan7_raw:" + this.chan7_raw + " chan8_raw:" + this.chan8_raw + " chan9_raw:" + this.chan9_raw + " chan10_raw:" + this.chan10_raw + " chan11_raw:" + this.chan11_raw + " chan12_raw:" + this.chan12_raw + " chan13_raw:" + this.chan13_raw + " chan14_raw:" + this.chan14_raw + " chan15_raw:" + this.chan15_raw + " chan16_raw:" + this.chan16_raw + " chan17_raw:" + this.chan17_raw + " chan18_raw:" + this.chan18_raw + " chancount:" + ((int) this.chancount) + " rssi:" + ((int) this.rssi) + "";
    }
}
