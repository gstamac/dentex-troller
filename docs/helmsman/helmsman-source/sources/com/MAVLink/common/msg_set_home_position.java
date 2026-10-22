package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_set_home_position extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_SET_HOME_POSITION = 243;
    public static final int MAVLINK_MSG_LENGTH = 53;
    private static final long serialVersionUID = 243;
    public int altitude;
    public float approach_x;
    public float approach_y;
    public float approach_z;
    public int latitude;
    public int longitude;
    public float[] q;
    public short target_system;
    public float x;
    public float y;
    public float z;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(53);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 243;
        mAVLinkPacket.payload.putInt(this.latitude);
        mAVLinkPacket.payload.putInt(this.longitude);
        mAVLinkPacket.payload.putInt(this.altitude);
        mAVLinkPacket.payload.putFloat(this.x);
        mAVLinkPacket.payload.putFloat(this.y);
        mAVLinkPacket.payload.putFloat(this.z);
        for (int i = 0; i < this.q.length; i++) {
            mAVLinkPacket.payload.putFloat(this.q[i]);
        }
        mAVLinkPacket.payload.putFloat(this.approach_x);
        mAVLinkPacket.payload.putFloat(this.approach_y);
        mAVLinkPacket.payload.putFloat(this.approach_z);
        mAVLinkPacket.payload.putUnsignedByte(this.target_system);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.latitude = mAVLinkPayload.getInt();
        this.longitude = mAVLinkPayload.getInt();
        this.altitude = mAVLinkPayload.getInt();
        this.x = mAVLinkPayload.getFloat();
        this.y = mAVLinkPayload.getFloat();
        this.z = mAVLinkPayload.getFloat();
        int i = 0;
        while (true) {
            float[] fArr = this.q;
            if (i < fArr.length) {
                fArr[i] = mAVLinkPayload.getFloat();
                i++;
            } else {
                this.approach_x = mAVLinkPayload.getFloat();
                this.approach_y = mAVLinkPayload.getFloat();
                this.approach_z = mAVLinkPayload.getFloat();
                this.target_system = mAVLinkPayload.getUnsignedByte();
                return;
            }
        }
    }

    public msg_set_home_position() {
        this.q = new float[4];
        this.msgid = 243;
    }

    public msg_set_home_position(MAVLinkPacket mAVLinkPacket) {
        this.q = new float[4];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 243;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_SET_HOME_POSITION - sysid:" + this.sysid + " compid:" + this.compid + " latitude:" + this.latitude + " longitude:" + this.longitude + " altitude:" + this.altitude + " x:" + this.x + " y:" + this.y + " z:" + this.z + " q:" + this.q + " approach_x:" + this.approach_x + " approach_y:" + this.approach_y + " approach_z:" + this.approach_z + " target_system:" + ((int) this.target_system) + "";
    }
}
