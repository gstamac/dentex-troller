package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_rc_channels_scaled extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_RC_CHANNELS_SCALED = 34;
    public static final int MAVLINK_MSG_LENGTH = 22;
    private static final long serialVersionUID = 34;
    public short chan1_scaled;
    public short chan2_scaled;
    public short chan3_scaled;
    public short chan4_scaled;
    public short chan5_scaled;
    public short chan6_scaled;
    public short chan7_scaled;
    public short chan8_scaled;
    public short port;
    public short rssi;
    public long time_boot_ms;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(22);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 34;
        mAVLinkPacket.payload.putUnsignedInt(this.time_boot_ms);
        mAVLinkPacket.payload.putShort(this.chan1_scaled);
        mAVLinkPacket.payload.putShort(this.chan2_scaled);
        mAVLinkPacket.payload.putShort(this.chan3_scaled);
        mAVLinkPacket.payload.putShort(this.chan4_scaled);
        mAVLinkPacket.payload.putShort(this.chan5_scaled);
        mAVLinkPacket.payload.putShort(this.chan6_scaled);
        mAVLinkPacket.payload.putShort(this.chan7_scaled);
        mAVLinkPacket.payload.putShort(this.chan8_scaled);
        mAVLinkPacket.payload.putUnsignedByte(this.port);
        mAVLinkPacket.payload.putUnsignedByte(this.rssi);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_boot_ms = mAVLinkPayload.getUnsignedInt();
        this.chan1_scaled = mAVLinkPayload.getShort();
        this.chan2_scaled = mAVLinkPayload.getShort();
        this.chan3_scaled = mAVLinkPayload.getShort();
        this.chan4_scaled = mAVLinkPayload.getShort();
        this.chan5_scaled = mAVLinkPayload.getShort();
        this.chan6_scaled = mAVLinkPayload.getShort();
        this.chan7_scaled = mAVLinkPayload.getShort();
        this.chan8_scaled = mAVLinkPayload.getShort();
        this.port = mAVLinkPayload.getUnsignedByte();
        this.rssi = mAVLinkPayload.getUnsignedByte();
    }

    public msg_rc_channels_scaled() {
        this.msgid = 34;
    }

    public msg_rc_channels_scaled(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 34;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_RC_CHANNELS_SCALED - sysid:" + this.sysid + " compid:" + this.compid + " time_boot_ms:" + this.time_boot_ms + " chan1_scaled:" + ((int) this.chan1_scaled) + " chan2_scaled:" + ((int) this.chan2_scaled) + " chan3_scaled:" + ((int) this.chan3_scaled) + " chan4_scaled:" + ((int) this.chan4_scaled) + " chan5_scaled:" + ((int) this.chan5_scaled) + " chan6_scaled:" + ((int) this.chan6_scaled) + " chan7_scaled:" + ((int) this.chan7_scaled) + " chan8_scaled:" + ((int) this.chan8_scaled) + " port:" + ((int) this.port) + " rssi:" + ((int) this.rssi) + "";
    }
}
