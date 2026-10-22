package com.MAVLink.helmsman;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_protocol_att_ekf_atti extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_ATTI = 166;
    public static final int MAVLINK_MSG_LENGTH = 14;
    private static final long serialVersionUID = 166;
    public int misc_status;
    public int pitch;
    public int rate_x;
    public int rate_y;
    public int rate_z;
    public int roll;
    public int yaw;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(14);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_ATTI;
        mAVLinkPacket.payload.putUnsignedShort(this.roll);
        mAVLinkPacket.payload.putUnsignedShort(this.pitch);
        mAVLinkPacket.payload.putUnsignedShort(this.yaw);
        mAVLinkPacket.payload.putUnsignedShort(this.rate_x);
        mAVLinkPacket.payload.putUnsignedShort(this.rate_y);
        mAVLinkPacket.payload.putUnsignedShort(this.rate_z);
        mAVLinkPacket.payload.putUnsignedShort(this.misc_status);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.roll = mAVLinkPayload.getUnsignedShort();
        this.pitch = mAVLinkPayload.getUnsignedShort();
        this.yaw = mAVLinkPayload.getUnsignedShort();
        this.rate_x = mAVLinkPayload.getUnsignedShort();
        this.rate_y = mAVLinkPayload.getUnsignedShort();
        this.rate_z = mAVLinkPayload.getUnsignedShort();
        this.misc_status = mAVLinkPayload.getUnsignedShort();
    }

    public msg_protocol_att_ekf_atti() {
        this.msgid = MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_ATTI;
    }

    public msg_protocol_att_ekf_atti(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_ATTI;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_ATTI - sysid:" + this.sysid + " compid:" + this.compid + " roll:" + this.roll + " pitch:" + this.pitch + " yaw:" + this.yaw + " rate_x:" + this.rate_x + " rate_y:" + this.rate_y + " rate_z:" + this.rate_z + " misc_status:" + this.misc_status + "";
    }
}
