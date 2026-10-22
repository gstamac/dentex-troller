package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_radio_status extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_RADIO_STATUS = 109;
    public static final int MAVLINK_MSG_LENGTH = 9;
    private static final long serialVersionUID = 109;
    public int fixed;
    public short noise;
    public short remnoise;
    public short remrssi;
    public short rssi;
    public int rxerrors;
    public short txbuf;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(9);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 109;
        mAVLinkPacket.payload.putUnsignedShort(this.rxerrors);
        mAVLinkPacket.payload.putUnsignedShort(this.fixed);
        mAVLinkPacket.payload.putUnsignedByte(this.rssi);
        mAVLinkPacket.payload.putUnsignedByte(this.remrssi);
        mAVLinkPacket.payload.putUnsignedByte(this.txbuf);
        mAVLinkPacket.payload.putUnsignedByte(this.noise);
        mAVLinkPacket.payload.putUnsignedByte(this.remnoise);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.rxerrors = mAVLinkPayload.getUnsignedShort();
        this.fixed = mAVLinkPayload.getUnsignedShort();
        this.rssi = mAVLinkPayload.getUnsignedByte();
        this.remrssi = mAVLinkPayload.getUnsignedByte();
        this.txbuf = mAVLinkPayload.getUnsignedByte();
        this.noise = mAVLinkPayload.getUnsignedByte();
        this.remnoise = mAVLinkPayload.getUnsignedByte();
    }

    public msg_radio_status() {
        this.msgid = 109;
    }

    public msg_radio_status(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 109;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_RADIO_STATUS - sysid:" + this.sysid + " compid:" + this.compid + " rxerrors:" + this.rxerrors + " fixed:" + this.fixed + " rssi:" + ((int) this.rssi) + " remrssi:" + ((int) this.remrssi) + " txbuf:" + ((int) this.txbuf) + " noise:" + ((int) this.noise) + " remnoise:" + ((int) this.remnoise) + "";
    }
}
