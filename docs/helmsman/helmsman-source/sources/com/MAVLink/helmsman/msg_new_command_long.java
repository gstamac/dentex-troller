package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_new_command_long extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_NEW_COMMAND_LONG = 186;
    public static final int MAVLINK_MSG_LENGTH = 30;
    private static final long serialVersionUID = 186;
    public int command;
    public short confirmation;
    public float param1;
    public float param2;
    public float param3;
    public float param4;
    public long param5;
    public long param6;
    public short seq;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(30);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 186;
        mAVLinkPacket.payload.putFloat(this.param1);
        mAVLinkPacket.payload.putFloat(this.param2);
        mAVLinkPacket.payload.putFloat(this.param3);
        mAVLinkPacket.payload.putFloat(this.param4);
        mAVLinkPacket.payload.putUnsignedInt(this.param5);
        mAVLinkPacket.payload.putUnsignedInt(this.param6);
        mAVLinkPacket.payload.putUnsignedShort(this.command);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        mAVLinkPacket.payload.putUnsignedByte(this.confirmation);
        mAVLinkPacket.payload.putUnsignedByte(this.seq);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.param1 = mAVLinkPayload.getFloat();
        this.param2 = mAVLinkPayload.getFloat();
        this.param3 = mAVLinkPayload.getFloat();
        this.param4 = mAVLinkPayload.getFloat();
        this.param5 = mAVLinkPayload.getUnsignedInt();
        this.param6 = mAVLinkPayload.getUnsignedInt();
        this.command = mAVLinkPayload.getUnsignedShort();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
        this.confirmation = mAVLinkPayload.getUnsignedByte();
        this.seq = mAVLinkPayload.getUnsignedByte();
    }

    public msg_new_command_long() {
        this.msgid = 186;
    }

    public msg_new_command_long(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 186;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_NEW_COMMAND_LONG - sysid:" + this.sysid + " compid:" + this.compid + " param1:" + this.param1 + " param2:" + this.param2 + " param3:" + this.param3 + " param4:" + this.param4 + " param5:" + this.param5 + " param6:" + this.param6 + " command:" + this.command + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " confirmation:" + ((int) this.confirmation) + " seq:" + ((int) this.seq) + "";
    }
}
