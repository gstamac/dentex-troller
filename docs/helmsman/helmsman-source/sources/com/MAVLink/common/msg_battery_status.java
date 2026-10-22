package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_battery_status extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_BATTERY_STATUS = 147;
    public static final int MAVLINK_MSG_LENGTH = 36;
    private static final long serialVersionUID = 147;
    public short battery_function;
    public byte battery_remaining;
    public short current_battery;
    public int current_consumed;
    public int energy_consumed;
    public short id;
    public short temperature;
    public short type;
    public int[] voltages;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(36);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 147;
        mAVLinkPacket.payload.putInt(this.current_consumed);
        mAVLinkPacket.payload.putInt(this.energy_consumed);
        mAVLinkPacket.payload.putShort(this.temperature);
        for (int i = 0; i < this.voltages.length; i++) {
            mAVLinkPacket.payload.putUnsignedShort(this.voltages[i]);
        }
        mAVLinkPacket.payload.putShort(this.current_battery);
        mAVLinkPacket.payload.putUnsignedByte(this.id);
        mAVLinkPacket.payload.putUnsignedByte(this.battery_function);
        mAVLinkPacket.payload.putUnsignedByte(this.type);
        mAVLinkPacket.payload.putByte(this.battery_remaining);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.current_consumed = mAVLinkPayload.getInt();
        this.energy_consumed = mAVLinkPayload.getInt();
        this.temperature = mAVLinkPayload.getShort();
        int i = 0;
        while (true) {
            int[] iArr = this.voltages;
            if (i < iArr.length) {
                iArr[i] = mAVLinkPayload.getUnsignedShort();
                i++;
            } else {
                this.current_battery = mAVLinkPayload.getShort();
                this.id = mAVLinkPayload.getUnsignedByte();
                this.battery_function = mAVLinkPayload.getUnsignedByte();
                this.type = mAVLinkPayload.getUnsignedByte();
                this.battery_remaining = mAVLinkPayload.getByte();
                return;
            }
        }
    }

    public msg_battery_status() {
        this.voltages = new int[10];
        this.msgid = 147;
    }

    public msg_battery_status(MAVLinkPacket mAVLinkPacket) {
        this.voltages = new int[10];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 147;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_BATTERY_STATUS - sysid:" + this.sysid + " compid:" + this.compid + " current_consumed:" + this.current_consumed + " energy_consumed:" + this.energy_consumed + " temperature:" + ((int) this.temperature) + " voltages:" + this.voltages + " current_battery:" + ((int) this.current_battery) + " id:" + ((int) this.id) + " battery_function:" + ((int) this.battery_function) + " type:" + ((int) this.type) + " battery_remaining:" + ((int) this.battery_remaining) + "";
    }
}
