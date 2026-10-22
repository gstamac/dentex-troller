package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_servo_output_raw extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_SERVO_OUTPUT_RAW = 36;
    public static final int MAVLINK_MSG_LENGTH = 21;
    private static final long serialVersionUID = 36;
    public short port;
    public int servo1_raw;
    public int servo2_raw;
    public int servo3_raw;
    public int servo4_raw;
    public int servo5_raw;
    public int servo6_raw;
    public int servo7_raw;
    public int servo8_raw;
    public long time_usec;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(21);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 36;
        mAVLinkPacket.payload.putUnsignedInt(this.time_usec);
        mAVLinkPacket.payload.putUnsignedShort(this.servo1_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.servo2_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.servo3_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.servo4_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.servo5_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.servo6_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.servo7_raw);
        mAVLinkPacket.payload.putUnsignedShort(this.servo8_raw);
        mAVLinkPacket.payload.putUnsignedByte(this.port);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedInt();
        this.servo1_raw = mAVLinkPayload.getUnsignedShort();
        this.servo2_raw = mAVLinkPayload.getUnsignedShort();
        this.servo3_raw = mAVLinkPayload.getUnsignedShort();
        this.servo4_raw = mAVLinkPayload.getUnsignedShort();
        this.servo5_raw = mAVLinkPayload.getUnsignedShort();
        this.servo6_raw = mAVLinkPayload.getUnsignedShort();
        this.servo7_raw = mAVLinkPayload.getUnsignedShort();
        this.servo8_raw = mAVLinkPayload.getUnsignedShort();
        this.port = mAVLinkPayload.getUnsignedByte();
    }

    public msg_servo_output_raw() {
        this.msgid = 36;
    }

    public msg_servo_output_raw(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 36;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_SERVO_OUTPUT_RAW - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " servo1_raw:" + this.servo1_raw + " servo2_raw:" + this.servo2_raw + " servo3_raw:" + this.servo3_raw + " servo4_raw:" + this.servo4_raw + " servo5_raw:" + this.servo5_raw + " servo6_raw:" + this.servo6_raw + " servo7_raw:" + this.servo7_raw + " servo8_raw:" + this.servo8_raw + " port:" + ((int) this.port) + "";
    }
}
