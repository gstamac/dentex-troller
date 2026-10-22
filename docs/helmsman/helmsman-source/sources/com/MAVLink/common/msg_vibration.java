package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_vibration extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_VIBRATION = 241;
    public static final int MAVLINK_MSG_LENGTH = 32;
    private static final long serialVersionUID = 241;
    public long clipping_0;
    public long clipping_1;
    public long clipping_2;
    public long time_usec;
    public float vibration_x;
    public float vibration_y;
    public float vibration_z;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(32);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 241;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        mAVLinkPacket.payload.putFloat(this.vibration_x);
        mAVLinkPacket.payload.putFloat(this.vibration_y);
        mAVLinkPacket.payload.putFloat(this.vibration_z);
        mAVLinkPacket.payload.putUnsignedInt(this.clipping_0);
        mAVLinkPacket.payload.putUnsignedInt(this.clipping_1);
        mAVLinkPacket.payload.putUnsignedInt(this.clipping_2);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        this.vibration_x = mAVLinkPayload.getFloat();
        this.vibration_y = mAVLinkPayload.getFloat();
        this.vibration_z = mAVLinkPayload.getFloat();
        this.clipping_0 = mAVLinkPayload.getUnsignedInt();
        this.clipping_1 = mAVLinkPayload.getUnsignedInt();
        this.clipping_2 = mAVLinkPayload.getUnsignedInt();
    }

    public msg_vibration() {
        this.msgid = 241;
    }

    public msg_vibration(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 241;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_VIBRATION - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " vibration_x:" + this.vibration_x + " vibration_y:" + this.vibration_y + " vibration_z:" + this.vibration_z + " clipping_0:" + this.clipping_0 + " clipping_1:" + this.clipping_1 + " clipping_2:" + this.clipping_2 + "";
    }
}
