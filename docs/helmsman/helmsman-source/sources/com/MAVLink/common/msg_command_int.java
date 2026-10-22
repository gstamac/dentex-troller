package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_command_int extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_COMMAND_INT = 75;
    public static final int MAVLINK_MSG_LENGTH = 35;
    private static final long serialVersionUID = 75;
    public short autocontinue;
    public int command;
    public short current;
    public short frame;
    public float param1;
    public float param2;
    public float param3;
    public float param4;
    public short target_component;
    public short target_system;
    public int x;
    public int y;
    public float z;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(35);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 75;
        mAVLinkPacket.payload.putFloat(this.param1);
        mAVLinkPacket.payload.putFloat(this.param2);
        mAVLinkPacket.payload.putFloat(this.param3);
        mAVLinkPacket.payload.putFloat(this.param4);
        mAVLinkPacket.payload.putInt(this.x);
        mAVLinkPacket.payload.putInt(this.y);
        mAVLinkPacket.payload.putFloat(this.z);
        mAVLinkPacket.payload.putUnsignedShort(this.command);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        mAVLinkPacket.payload.putUnsignedByte(this.target_component);
        mAVLinkPacket.payload.putUnsignedByte(this.frame);
        mAVLinkPacket.payload.putUnsignedByte(this.current);
        mAVLinkPacket.payload.putUnsignedByte(this.autocontinue);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.param1 = mAVLinkPayload.getFloat();
        this.param2 = mAVLinkPayload.getFloat();
        this.param3 = mAVLinkPayload.getFloat();
        this.param4 = mAVLinkPayload.getFloat();
        this.x = mAVLinkPayload.getInt();
        this.y = mAVLinkPayload.getInt();
        this.z = mAVLinkPayload.getFloat();
        this.command = mAVLinkPayload.getUnsignedShort();
        this.target_system = mAVLinkPayload.getUnsignedByte();
        this.target_component = mAVLinkPayload.getUnsignedByte();
        this.frame = mAVLinkPayload.getUnsignedByte();
        this.current = mAVLinkPayload.getUnsignedByte();
        this.autocontinue = mAVLinkPayload.getUnsignedByte();
    }

    public msg_command_int() {
        this.msgid = 75;
    }

    public msg_command_int(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 75;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_COMMAND_INT - sysid:" + this.sysid + " compid:" + this.compid + " param1:" + this.param1 + " param2:" + this.param2 + " param3:" + this.param3 + " param4:" + this.param4 + " x:" + this.x + " y:" + this.y + " z:" + this.z + " command:" + this.command + " target_system:" + ((int) this.target_system) + " target_component:" + ((int) this.target_component) + " frame:" + ((int) this.frame) + " current:" + ((int) this.current) + " autocontinue:" + ((int) this.autocontinue) + "";
    }
}
