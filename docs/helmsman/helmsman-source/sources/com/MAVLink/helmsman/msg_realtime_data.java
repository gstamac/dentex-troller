package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_realtime_data extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_REALTIME_DATA = 184;
    public static final int MAVLINK_MSG_LENGTH = 22;
    private static final long serialVersionUID = 184;
    public int param1;
    public int param2;
    public float param3;
    public float param4;
    public long param5;
    public long param6;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(22);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 184;
        mAVLinkPacket.payload.putFloat(this.param3);
        mAVLinkPacket.payload.putFloat(this.param4);
        mAVLinkPacket.payload.putUnsignedInt(this.param5);
        mAVLinkPacket.payload.putUnsignedInt(this.param6);
        mAVLinkPacket.payload.putUnsignedShort(this.param1);
        mAVLinkPacket.payload.putUnsignedShort(this.param2);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.param3 = mAVLinkPayload.getFloat();
        this.param4 = mAVLinkPayload.getFloat();
        this.param5 = mAVLinkPayload.getUnsignedInt();
        this.param6 = mAVLinkPayload.getUnsignedInt();
        this.param1 = mAVLinkPayload.getUnsignedShort();
        this.param2 = mAVLinkPayload.getUnsignedShort();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
    }

    public msg_realtime_data() {
        this.msgid = 184;
    }

    public msg_realtime_data(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 184;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_REALTIME_DATA - sysid:" + this.sysid + " compid:" + this.compid + " param3:" + this.param3 + " param4:" + this.param4 + " param5:" + this.param5 + " param6:" + this.param6 + " param1:" + this.param1 + " param2:" + this.param2 + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + "";
    }
}
