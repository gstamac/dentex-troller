package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_att_pos_mocap extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_ATT_POS_MOCAP = 138;
    public static final int MAVLINK_MSG_LENGTH = 36;
    private static final long serialVersionUID = 138;
    public float[] q;
    public long time_usec;
    public float x;
    public float y;
    public float z;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(36);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = MAVLINK_MSG_ID_ATT_POS_MOCAP;
        mAVLinkPacket.payload.putUnsignedLong(this.time_usec);
        for (int i = 0; i < this.q.length; i++) {
            mAVLinkPacket.payload.putFloat(this.q[i]);
        }
        mAVLinkPacket.payload.putFloat(this.x);
        mAVLinkPacket.payload.putFloat(this.y);
        mAVLinkPacket.payload.putFloat(this.z);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_usec = mAVLinkPayload.getUnsignedLong();
        int i = 0;
        while (true) {
            float[] fArr = this.q;
            if (i < fArr.length) {
                fArr[i] = mAVLinkPayload.getFloat();
                i++;
            } else {
                this.x = mAVLinkPayload.getFloat();
                this.y = mAVLinkPayload.getFloat();
                this.z = mAVLinkPayload.getFloat();
                return;
            }
        }
    }

    public msg_att_pos_mocap() {
        this.q = new float[4];
        this.msgid = MAVLINK_MSG_ID_ATT_POS_MOCAP;
    }

    public msg_att_pos_mocap(MAVLinkPacket mAVLinkPacket) {
        this.q = new float[4];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_ATT_POS_MOCAP;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_ATT_POS_MOCAP - sysid:" + this.sysid + " compid:" + this.compid + " time_usec:" + this.time_usec + " q:" + this.q + " x:" + this.x + " y:" + this.y + " z:" + this.z + "";
    }
}
