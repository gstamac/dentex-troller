package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_record_route_data extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_RECORD_ROUTE_DATA = 170;
    public static final int MAVLINK_MSG_LENGTH = 3;
    private static final long serialVersionUID = 170;
    public int recorded_count;
    public short state;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(3);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_RECORD_ROUTE_DATA;
        mAVLinkPacket.payload.putUnsignedShort(this.recorded_count);
        mAVLinkPacket.payload.putUnsignedByte(this.state);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.recorded_count = mAVLinkPayload.getUnsignedShort();
        this.state = mAVLinkPayload.getUnsignedByte();
    }

    public msg_record_route_data() {
        this.msgid = MAVLINK_MSG_ID_RECORD_ROUTE_DATA;
    }

    public msg_record_route_data(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_RECORD_ROUTE_DATA;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_RECORD_ROUTE_DATA - recorded_count:" + this.recorded_count + " state:" + ((int) this.state) + "";
    }
}
