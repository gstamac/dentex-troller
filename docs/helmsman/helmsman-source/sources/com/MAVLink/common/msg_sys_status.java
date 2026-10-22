package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_sys_status extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_SYS_STATUS = 1;
    public static final int MAVLINK_MSG_LENGTH = 31;
    private static final long serialVersionUID = 1;
    public byte battery_remaining;
    public short current_battery;
    public int drop_rate_comm;
    public int errors_comm;
    public int errors_count1;
    public int errors_count2;
    public int errors_count3;
    public int errors_count4;
    public int load;
    public long onboard_control_sensors_enabled;
    public long onboard_control_sensors_health;
    public long onboard_control_sensors_present;
    public int voltage_battery;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(31);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 1;
        mAVLinkPacket.payload.putUnsignedInt(this.onboard_control_sensors_present);
        mAVLinkPacket.payload.putUnsignedInt(this.onboard_control_sensors_enabled);
        mAVLinkPacket.payload.putUnsignedInt(this.onboard_control_sensors_health);
        mAVLinkPacket.payload.putUnsignedShort(this.load);
        mAVLinkPacket.payload.putUnsignedShort(this.voltage_battery);
        mAVLinkPacket.payload.putShort(this.current_battery);
        mAVLinkPacket.payload.putUnsignedShort(this.drop_rate_comm);
        mAVLinkPacket.payload.putUnsignedShort(this.errors_comm);
        mAVLinkPacket.payload.putUnsignedShort(this.errors_count1);
        mAVLinkPacket.payload.putUnsignedShort(this.errors_count2);
        mAVLinkPacket.payload.putUnsignedShort(this.errors_count3);
        mAVLinkPacket.payload.putUnsignedShort(this.errors_count4);
        mAVLinkPacket.payload.putByte(this.battery_remaining);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.onboard_control_sensors_present = mAVLinkPayload.getUnsignedInt();
        this.onboard_control_sensors_enabled = mAVLinkPayload.getUnsignedInt();
        this.onboard_control_sensors_health = mAVLinkPayload.getUnsignedInt();
        this.load = mAVLinkPayload.getUnsignedShort();
        this.voltage_battery = mAVLinkPayload.getUnsignedShort();
        this.current_battery = mAVLinkPayload.getShort();
        this.drop_rate_comm = mAVLinkPayload.getUnsignedShort();
        this.errors_comm = mAVLinkPayload.getUnsignedShort();
        this.errors_count1 = mAVLinkPayload.getUnsignedShort();
        this.errors_count2 = mAVLinkPayload.getUnsignedShort();
        this.errors_count3 = mAVLinkPayload.getUnsignedShort();
        this.errors_count4 = mAVLinkPayload.getUnsignedShort();
        this.battery_remaining = mAVLinkPayload.getByte();
    }

    public msg_sys_status() {
        this.msgid = 1;
    }

    public msg_sys_status(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 1;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_SYS_STATUS - sysid:" + this.sysid + " compid:" + this.compid + " onboard_control_sensors_present:" + this.onboard_control_sensors_present + " onboard_control_sensors_enabled:" + this.onboard_control_sensors_enabled + " onboard_control_sensors_health:" + this.onboard_control_sensors_health + " load:" + this.load + " voltage_battery:" + this.voltage_battery + " current_battery:" + ((int) this.current_battery) + " drop_rate_comm:" + this.drop_rate_comm + " errors_comm:" + this.errors_comm + " errors_count1:" + this.errors_count1 + " errors_count2:" + this.errors_count2 + " errors_count3:" + this.errors_count3 + " errors_count4:" + this.errors_count4 + " battery_remaining:" + ((int) this.battery_remaining) + "";
    }
}
