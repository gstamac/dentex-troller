package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_hil_rc_inputs_raw extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_HIL_RC_INPUTS_RAW = 92;
    public static final int MAVLINK_MSG_LENGTH = 33;
    private static final long serialVersionUID = 92;
    public int chan10_raw;
    public int chan11_raw;
    public int chan12_raw;
    public int chan1_raw;
    public int chan2_raw;
    public int chan3_raw;
    public int chan4_raw;
    public int chan5_raw;
    public int chan6_raw;
    public int chan7_raw;
    public int chan8_raw;
    public int chan9_raw;
    public short rssi;
    public long time_usec;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(33);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 92;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
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
        mAVLinkPacket.payload.putUnsignedByte(this.rssi);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
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
        this.rssi = mAVLinkPayload.getUnsignedByte();
    }

    public msg_hil_rc_inputs_raw() {
        this.msgid = 92;
    }

    public msg_hil_rc_inputs_raw(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 92;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_HIL_RC_INPUTS_RAW - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " chan1_raw:" + this.chan1_raw + " chan2_raw:" + this.chan2_raw + " chan3_raw:" + this.chan3_raw + " chan4_raw:" + this.chan4_raw + " chan5_raw:" + this.chan5_raw + " chan6_raw:" + this.chan6_raw + " chan7_raw:" + this.chan7_raw + " chan8_raw:" + this.chan8_raw + " chan9_raw:" + this.chan9_raw + " chan10_raw:" + this.chan10_raw + " chan11_raw:" + this.chan11_raw + " chan12_raw:" + this.chan12_raw + " rssi:" + ((int) this.rssi) + "";
    }
}
