package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_encapsulated_data extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_ENCAPSULATED_DATA = 131;
    public static final int MAVLINK_MSG_LENGTH = 255;
    private static final long serialVersionUID = 131;
    public short[] data;
    public int seqnr;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(255);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_ENCAPSULATED_DATA;
        mAVLinkPacket.payload.putUnsignedShort(this.seqnr);
        for (int i = 0; i < this.data.length; i++) {
            mAVLinkPacket.payload.putUnsignedByte(this.data[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.seqnr = mAVLinkPayload.getUnsignedShort();
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

    public msg_encapsulated_data() {
        this.data = new short[msg_statustext.MAVLINK_MSG_ID_STATUSTEXT];
        this.msgid = MAVLINK_MSG_ID_ENCAPSULATED_DATA;
    }

    public msg_encapsulated_data(MAVLinkPacket mAVLinkPacket) {
        this.data = new short[msg_statustext.MAVLINK_MSG_ID_STATUSTEXT];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_ENCAPSULATED_DATA;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_ENCAPSULATED_DATA - sysid:" + this.sysid + " compid:" + this.compid + " seqnr:" + this.seqnr + " data:" + this.data + "";
    }
}
