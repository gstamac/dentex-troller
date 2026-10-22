package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_log_data extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_LOG_DATA = 120;
    public static final int MAVLINK_MSG_LENGTH = 97;
    private static final long serialVersionUID = 120;
    public short count;
    public short[] data;
    public int id;
    public long ofs;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(97);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 120;
        mAVLinkPacket.payload.putUnsignedInt(this.ofs);
        mAVLinkPacket.payload.putUnsignedShort(this.id);
        mAVLinkPacket.payload.putUnsignedByte(this.count);
        for (int i = 0; i < this.data.length; i++) {
            mAVLinkPacket.payload.putUnsignedByte(this.data[i]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.ofs = mAVLinkPayload.getUnsignedInt();
        this.id = mAVLinkPayload.getUnsignedShort();
        this.count = mAVLinkPayload.getUnsignedByte();
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

    public msg_log_data() {
        this.data = new short[90];
        this.msgid = 120;
    }

    public msg_log_data(MAVLinkPacket mAVLinkPacket) {
        this.data = new short[90];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 120;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_LOG_DATA - sysid:" + this.sysid + " compid:" + this.compid + " ofs:" + this.ofs + " id:" + this.id + " count:" + ((int) this.count) + " data:" + this.data + "";
    }
}
