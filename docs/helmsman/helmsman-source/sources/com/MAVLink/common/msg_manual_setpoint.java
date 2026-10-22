package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_manual_setpoint extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_MANUAL_SETPOINT = 81;
    public static final int MAVLINK_MSG_LENGTH = 22;
    private static final long serialVersionUID = 81;
    public short manual_override_switch;
    public short mode_switch;
    public float pitch;
    public float roll;
    public float thrust;
    public long time_boot_ms;
    public float yaw;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(22);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 81;
        mAVLinkPacket.payload.putUnsignedInt(this.time_boot_ms);
        mAVLinkPacket.payload.putFloat(this.roll);
        mAVLinkPacket.payload.putFloat(this.pitch);
        mAVLinkPacket.payload.putFloat(this.yaw);
        mAVLinkPacket.payload.putFloat(this.thrust);
        mAVLinkPacket.payload.putUnsignedByte(this.mode_switch);
        mAVLinkPacket.payload.putUnsignedByte(this.manual_override_switch);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_boot_ms = mAVLinkPayload.getUnsignedInt();
        this.roll = mAVLinkPayload.getFloat();
        this.pitch = mAVLinkPayload.getFloat();
        this.yaw = mAVLinkPayload.getFloat();
        this.thrust = mAVLinkPayload.getFloat();
        this.mode_switch = mAVLinkPayload.getUnsignedByte();
        this.manual_override_switch = mAVLinkPayload.getUnsignedByte();
    }

    public msg_manual_setpoint() {
        this.msgid = 81;
    }

    public msg_manual_setpoint(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 81;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_MANUAL_SETPOINT - sysid:" + this.sysid + " compid:" + this.compid + " time_boot_ms:" + this.time_boot_ms + " roll:" + this.roll + " pitch:" + this.pitch + " yaw:" + this.yaw + " thrust:" + this.thrust + " mode_switch:" + ((int) this.mode_switch) + " manual_override_switch:" + ((int) this.manual_override_switch) + "";
    }
}
