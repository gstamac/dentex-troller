package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_command_long extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_COMMAND_LONG = 76;
    public static final int MAVLINK_MSG_LENGTH = 33;
    private static final long serialVersionUID = 76;
    public int command;
    public short confirmation;
    public float param1;
    public float param2;
    public float param3;
    public float param4;
    public float param5;
    public float param6;
    public float param7;
    public short target_component;
    public short target_system;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(33);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 76;
        mAVLinkPacket.payload.putFloat(this.param1);
        mAVLinkPacket.payload.putFloat(this.param2);
        mAVLinkPacket.payload.putFloat(this.param3);
        mAVLinkPacket.payload.putFloat(this.param4);
        mAVLinkPacket.payload.putFloat(this.param5);
        mAVLinkPacket.payload.putFloat(this.param6);
        mAVLinkPacket.payload.putFloat(this.param7);
        mAVLinkPacket.payload.putUnsignedShort(this.command);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        mAVLinkPacket.payload.putUnsignedByte(this.confirmation);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.param1 = mAVLinkPayload.getFloat();
        this.param2 = mAVLinkPayload.getFloat();
        this.param3 = mAVLinkPayload.getFloat();
        this.param4 = mAVLinkPayload.getFloat();
        this.param5 = mAVLinkPayload.getFloat();
        this.param6 = mAVLinkPayload.getFloat();
        this.param7 = mAVLinkPayload.getFloat();
        this.command = mAVLinkPayload.getUnsignedShort();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
        this.confirmation = mAVLinkPayload.getUnsignedByte();
    }

    public msg_command_long() {
        this.msgid = 76;
    }

    public msg_command_long(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 76;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_COMMAND_LONG - sysid:" + this.sysid + " compid:" + this.compid + " param1:" + this.param1 + " param2:" + this.param2 + " param3:" + this.param3 + " param4:" + this.param4 + " param5:" + this.param5 + " param6:" + this.param6 + " param7:" + this.param7 + " command:" + this.command + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " confirmation:" + ((int) this.confirmation) + "";
    }
}
