package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_gps_rtk extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GPS_RTK = 127;
    public static final int MAVLINK_MSG_LENGTH = 35;
    private static final long serialVersionUID = 127;
    public long accuracy;
    public int baseline_a_mm;
    public int baseline_b_mm;
    public int baseline_c_mm;
    public short baseline_coords_type;
    public int iar_num_hypotheses;
    public short nsats;
    public short rtk_health;
    public short rtk_rate;
    public short rtk_receiver_id;
    public long time_last_baseline_ms;
    public long tow;
    public int wn;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(35);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 127;
        mAVLinkPacket.payload.putUnsignedInt(this.time_last_baseline_ms);
        mAVLinkPacket.payload.putUnsignedInt(this.tow);
        mAVLinkPacket.payload.putInt(this.baseline_a_mm);
        mAVLinkPacket.payload.putInt(this.baseline_b_mm);
        mAVLinkPacket.payload.putInt(this.baseline_c_mm);
        mAVLinkPacket.payload.putUnsignedInt(this.accuracy);
        mAVLinkPacket.payload.putInt(this.iar_num_hypotheses);
        mAVLinkPacket.payload.putUnsignedShort(this.wn);
        mAVLinkPacket.payload.putUnsignedByte(this.rtk_receiver_id);
        mAVLinkPacket.payload.putUnsignedByte(this.rtk_health);
        mAVLinkPacket.payload.putUnsignedByte(this.rtk_rate);
        mAVLinkPacket.payload.putUnsignedByte(this.nsats);
        mAVLinkPacket.payload.putUnsignedByte(this.baseline_coords_type);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.time_last_baseline_ms = mAVLinkPayload.getUnsignedInt();
        this.tow = mAVLinkPayload.getUnsignedInt();
        this.baseline_a_mm = mAVLinkPayload.getInt();
        this.baseline_b_mm = mAVLinkPayload.getInt();
        this.baseline_c_mm = mAVLinkPayload.getInt();
        this.accuracy = mAVLinkPayload.getUnsignedInt();
        this.iar_num_hypotheses = mAVLinkPayload.getInt();
        this.wn = mAVLinkPayload.getUnsignedShort();
        this.rtk_receiver_id = mAVLinkPayload.getUnsignedByte();
        this.rtk_health = mAVLinkPayload.getUnsignedByte();
        this.rtk_rate = mAVLinkPayload.getUnsignedByte();
        this.nsats = mAVLinkPayload.getUnsignedByte();
        this.baseline_coords_type = mAVLinkPayload.getUnsignedByte();
    }

    public msg_gps_rtk() {
        this.msgid = 127;
    }

    public msg_gps_rtk(MAVLinkPacket mAVLinkPacket) {
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 127;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_GPS_RTK - sysid:" + this.sysid + " compid:" + this.compid + " time_last_baseline_ms:" + this.time_last_baseline_ms + " tow:" + this.tow + " baseline_a_mm:" + this.baseline_a_mm + " baseline_b_mm:" + this.baseline_b_mm + " baseline_c_mm:" + this.baseline_c_mm + " accuracy:" + this.accuracy + " iar_num_hypotheses:" + this.iar_num_hypotheses + " wn:" + this.wn + " rtk_receiver_id:" + ((int) this.rtk_receiver_id) + " rtk_health:" + ((int) this.rtk_health) + " rtk_rate:" + ((int) this.rtk_rate) + " nsats:" + ((int) this.nsats) + " baseline_coords_type:" + ((int) this.baseline_coords_type) + "";
    }
}
