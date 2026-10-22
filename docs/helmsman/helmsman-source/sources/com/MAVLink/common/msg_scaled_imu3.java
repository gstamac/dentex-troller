package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_scaled_imu3 extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_SCALED_IMU3 = 129;
    public static final int MAVLINK_MSG_LENGTH = 22;
    private static final long serialVersionUID = 129;
    public long time_boot_ms;
    public short xacc;
    public short xgyro;
    public short xmag;
    public short yacc;
    public short ygyro;
    public short ymag;
    public short zacc;
    public short zgyro;
    public short zmag;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(22);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 129;
        mAVLinkPacket.payload.putUnsignedInt(this.time_boot_ms);
        mAVLinkPacket.payload.putShort(this.xacc);
        mAVLinkPacket.payload.putShort(this.yacc);
        mAVLinkPacket.payload.putShort(this.zacc);
        mAVLinkPacket.payload.putShort(this.xgyro);
        mAVLinkPacket.payload.putShort(this.ygyro);
        mAVLinkPacket.payload.putShort(this.zgyro);
        mAVLinkPacket.payload.putShort(this.xmag);
        mAVLinkPacket.payload.putShort(this.ymag);
        mAVLinkPacket.payload.putShort(this.zmag);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_boot_ms = mAVLinkPayload.getUnsignedInt();
        this.xacc = mAVLinkPayload.getShort();
        this.yacc = mAVLinkPayload.getShort();
        this.zacc = mAVLinkPayload.getShort();
        this.xgyro = mAVLinkPayload.getShort();
        this.ygyro = mAVLinkPayload.getShort();
        this.zgyro = mAVLinkPayload.getShort();
        this.xmag = mAVLinkPayload.getShort();
        this.ymag = mAVLinkPayload.getShort();
        this.zmag = mAVLinkPayload.getShort();
    }

    public msg_scaled_imu3() {
        this.msgid = 129;
    }

    public msg_scaled_imu3(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 129;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_SCALED_IMU3 - sysid:" + this.sysid + " compid:" + this.compid + " time_boot_ms:" + this.time_boot_ms + " xacc:" + ((int) this.xacc) + " yacc:" + ((int) this.yacc) + " zacc:" + ((int) this.zacc) + " xgyro:" + ((int) this.xgyro) + " ygyro:" + ((int) this.ygyro) + " zgyro:" + ((int) this.zgyro) + " xmag:" + ((int) this.xmag) + " ymag:" + ((int) this.ymag) + " zmag:" + ((int) this.zmag) + "";
    }
}
