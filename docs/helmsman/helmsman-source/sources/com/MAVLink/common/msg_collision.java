package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_collision extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_COLLISION = 247;
    public static final int MAVLINK_MSG_LENGTH = 19;
    private static final long serialVersionUID = 247;
    public short action;
    public float altitude_minimum_delta;
    public float horizontal_minimum_delta;
    public long id;
    public short src;
    public short threat_level;
    public float time_to_minimum_delta;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(19);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_COLLISION;
        mAVLinkPacket.payload.putUnsignedInt(this.id);
        mAVLinkPacket.payload.putFloat(this.time_to_minimum_delta);
        mAVLinkPacket.payload.putFloat(this.altitude_minimum_delta);
        mAVLinkPacket.payload.putFloat(this.horizontal_minimum_delta);
        mAVLinkPacket.payload.putUnsignedByte(this.src);
        mAVLinkPacket.payload.putUnsignedByte(this.action);
        mAVLinkPacket.payload.putUnsignedByte(this.threat_level);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.id = mAVLinkPayload.getUnsignedInt();
        this.time_to_minimum_delta = mAVLinkPayload.getFloat();
        this.altitude_minimum_delta = mAVLinkPayload.getFloat();
        this.horizontal_minimum_delta = mAVLinkPayload.getFloat();
        this.src = mAVLinkPayload.getUnsignedByte();
        this.action = mAVLinkPayload.getUnsignedByte();
        this.threat_level = mAVLinkPayload.getUnsignedByte();
    }

    public msg_collision() {
        this.msgid = MAVLINK_MSG_ID_COLLISION;
    }

    public msg_collision(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_COLLISION;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_COLLISION - sysid:" + this.sysid + " compid:" + this.compid + " id:" + this.id + " time_to_minimum_delta:" + this.time_to_minimum_delta + " altitude_minimum_delta:" + this.altitude_minimum_delta + " horizontal_minimum_delta:" + this.horizontal_minimum_delta + " src:" + ((int) this.src) + " action:" + ((int) this.action) + " threat_level:" + ((int) this.threat_level) + "";
    }
}
