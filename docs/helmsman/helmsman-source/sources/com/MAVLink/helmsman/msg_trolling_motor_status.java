package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_trolling_motor_status extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_TROLLING_MOTOR_STATUS = 185;
    public static final int MAVLINK_MSG_LENGTH = 20;
    private static final long serialVersionUID = 185;
    public int component_health;
    public int component_present;
    public short current_battery;
    public int error;
    public short is_motor_on;
    public int mode_flag1;
    public int mode_flag2;
    public short motor_output;
    public short motor_speed;
    public short steering_motor_output;
    public short voltage_battery;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(20);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 185;
        mAVLinkPacket.payload.putShort(this.motor_output);
        mAVLinkPacket.payload.putShort(this.steering_motor_output);
        mAVLinkPacket.payload.putShort(this.voltage_battery);
        mAVLinkPacket.payload.putShort(this.current_battery);
        mAVLinkPacket.payload.putUnsignedShort(this.component_present);
        mAVLinkPacket.payload.putUnsignedShort(this.component_health);
        mAVLinkPacket.payload.putUnsignedShort(this.mode_flag1);
        mAVLinkPacket.payload.putUnsignedShort(this.mode_flag2);
        mAVLinkPacket.payload.putUnsignedShort(this.error);
        mAVLinkPacket.payload.putUnsignedByte(this.is_motor_on);
        mAVLinkPacket.payload.putUnsignedByte(this.motor_speed);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.motor_output = mAVLinkPayload.getShort();
        this.steering_motor_output = mAVLinkPayload.getShort();
        this.voltage_battery = mAVLinkPayload.getShort();
        this.current_battery = mAVLinkPayload.getShort();
        this.component_present = mAVLinkPayload.getUnsignedShort();
        this.component_health = mAVLinkPayload.getUnsignedShort();
        this.mode_flag1 = mAVLinkPayload.getUnsignedShort();
        this.mode_flag2 = mAVLinkPayload.getUnsignedShort();
        this.error = mAVLinkPayload.getUnsignedShort();
        this.is_motor_on = mAVLinkPayload.getUnsignedByte();
        this.motor_speed = mAVLinkPayload.getUnsignedByte();
    }

    public msg_trolling_motor_status() {
        this.msgid = 185;
    }

    public msg_trolling_motor_status(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 185;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_TROLLING_MOTOR_STATUS - sysid:" + this.sysid + " compid:" + this.compid + " motor_output:" + ((int) this.motor_output) + " steering_motor_output:" + ((int) this.steering_motor_output) + " voltage_battery:" + ((int) this.voltage_battery) + " current_battery:" + ((int) this.current_battery) + " component_present:" + this.component_present + " component_health:" + this.component_health + " mode_flag1:" + this.mode_flag1 + " mode_flag2:" + this.mode_flag2 + " error:" + this.error + " is_motor_on:" + ((int) this.is_motor_on) + " motor_speed:" + ((int) this.motor_speed) + "";
    }
}
