package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_nav_controller_output extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_NAV_CONTROLLER_OUTPUT = 62;
    public static final int MAVLINK_MSG_LENGTH = 26;
    private static final long serialVersionUID = 62;
    public float alt_error;
    public float aspd_error;
    public short nav_bearing;
    public float nav_pitch;
    public float nav_roll;
    public short target_bearing;
    public int wp_dist;
    public float xtrack_error;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(26);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 62;
        mAVLinkPacket.payload.putFloat(this.nav_roll);
        mAVLinkPacket.payload.putFloat(this.nav_pitch);
        mAVLinkPacket.payload.putFloat(this.alt_error);
        mAVLinkPacket.payload.putFloat(this.aspd_error);
        mAVLinkPacket.payload.putFloat(this.xtrack_error);
        mAVLinkPacket.payload.putShort(this.nav_bearing);
        mAVLinkPacket.payload.putShort(this.target_bearing);
        mAVLinkPacket.payload.putUnsignedShort(this.wp_dist);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.nav_roll = mAVLinkPayload.getFloat();
        this.nav_pitch = mAVLinkPayload.getFloat();
        this.alt_error = mAVLinkPayload.getFloat();
        this.aspd_error = mAVLinkPayload.getFloat();
        this.xtrack_error = mAVLinkPayload.getFloat();
        this.nav_bearing = mAVLinkPayload.getShort();
        this.target_bearing = mAVLinkPayload.getShort();
        this.wp_dist = mAVLinkPayload.getUnsignedShort();
    }

    public msg_nav_controller_output() {
        this.msgid = 62;
    }

    public msg_nav_controller_output(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 62;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_NAV_CONTROLLER_OUTPUT - sysid:" + this.sysid + " compid:" + this.compid + " nav_roll:" + this.nav_roll + " nav_pitch:" + this.nav_pitch + " alt_error:" + this.alt_error + " aspd_error:" + this.aspd_error + " xtrack_error:" + this.xtrack_error + " nav_bearing:" + ((int) this.nav_bearing) + " target_bearing:" + ((int) this.target_bearing) + " wp_dist:" + this.wp_dist + "";
    }
}
