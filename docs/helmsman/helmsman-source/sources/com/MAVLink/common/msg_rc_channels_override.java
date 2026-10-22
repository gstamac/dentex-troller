package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_rc_channels_override extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_RC_CHANNELS_OVERRIDE = 70;
    public static final int MAVLINK_MSG_LENGTH = 18;
    private static final long serialVersionUID = 70;
    public int chan1_raw;
    public int chan2_raw;
    public int chan3_raw;
    public int chan4_raw;
    public int chan5_raw;
    public int chan6_raw;
    public int chan7_raw;
    public int chan8_raw;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(18);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 70;
        mAVLinkPacket.payload.putUnsignedShort(this.chan1_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan2_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan3_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan4_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan5_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan6_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan7_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.chan8_raw);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.chan1_raw = mAVLinkPayload.getUnsignedShort();
        this.chan2_raw = mAVLinkPayload.getUnsignedShort();
        this.chan3_raw = mAVLinkPayload.getUnsignedShort();
        this.chan4_raw = mAVLinkPayload.getUnsignedShort();
        this.chan5_raw = mAVLinkPayload.getUnsignedShort();
        this.chan6_raw = mAVLinkPayload.getUnsignedShort();
        this.chan7_raw = mAVLinkPayload.getUnsignedShort();
        this.chan8_raw = mAVLinkPayload.getUnsignedShort();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
    }

    public msg_rc_channels_override() {
        this.msgid = 70;
    }

    public msg_rc_channels_override(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 70;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_RC_CHANNELS_OVERRIDE - sysid:" + this.sysid + " compid:" + this.compid + " chan1_raw:" + this.chan1_raw + " chan2_raw:" + this.chan2_raw + " chan3_raw:" + this.chan3_raw + " chan4_raw:" + this.chan4_raw + " chan5_raw:" + this.chan5_raw + " chan6_raw:" + this.chan6_raw + " chan7_raw:" + this.chan7_raw + " chan8_raw:" + this.chan8_raw + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + "";
    }
}
