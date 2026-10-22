package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_log_entry extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_LOG_ENTRY = 118;
    public static final int MAVLINK_MSG_LENGTH = 14;
    private static final long serialVersionUID = 118;
    public int id;
    public int last_log_num;
    public int num_logs;
    public long size;
    public long time_utc;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(14);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 118;
        mAVLinkPacket.payload.putUnsignedInt(this.time_utc);
        mAVLinkPacket.payload.putUnsignedInt(this.size);
        mAVLinkPacket.payload.putUnsignedShort(this.id);
        mAVLinkPacket.payload.putUnsignedShort(this.num_logs);
        mAVLinkPacket.payload.putUnsignedShort(this.last_log_num);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_utc = mAVLinkPayload.getUnsignedInt();
        this.size = mAVLinkPayload.getUnsignedInt();
        this.id = mAVLinkPayload.getUnsignedShort();
        this.num_logs = mAVLinkPayload.getUnsignedShort();
        this.last_log_num = mAVLinkPayload.getUnsignedShort();
    }

    public msg_log_entry() {
        this.msgid = 118;
    }

    public msg_log_entry(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 118;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_LOG_ENTRY - sysid:" + this.sysid + " compid:" + this.compid + " time_utc:" + this.time_utc + " size:" + this.size + " id:" + this.id + " num_logs:" + this.num_logs + " last_log_num:" + this.last_log_num + "";
    }
}
