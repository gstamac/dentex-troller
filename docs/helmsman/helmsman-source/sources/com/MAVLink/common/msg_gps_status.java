package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_gps_status extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GPS_STATUS = 25;
    public static final int MAVLINK_MSG_LENGTH = 101;
    private static final long serialVersionUID = 25;
    public short[] satellite_azimuth;
    public short[] satellite_elevation;
    public short[] satellite_prn;
    public short[] satellite_snr;
    public short[] satellite_used;
    public short satellites_visible;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(101);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 25;
        mAVLinkPacket.payload.putUnsignedByte(this.satellites_visible);
        for (int i = 0; i < this.satellite_prn.length; i++) {
            mAVLinkPacket.payload.putUnsignedByte(this.satellite_prn[i]);
        }
        for (int i2 = 0; i2 < this.satellite_used.length; i2++) {
            mAVLinkPacket.payload.putUnsignedByte(this.satellite_used[i2]);
        }
        for (int i3 = 0; i3 < this.satellite_elevation.length; i3++) {
            mAVLinkPacket.payload.putUnsignedByte(this.satellite_elevation[i3]);
        }
        for (int i4 = 0; i4 < this.satellite_azimuth.length; i4++) {
            mAVLinkPacket.payload.putUnsignedByte(this.satellite_azimuth[i4]);
        }
        for (int i5 = 0; i5 < this.satellite_snr.length; i5++) {
            mAVLinkPacket.payload.putUnsignedByte(this.satellite_snr[i5]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.satellites_visible = mAVLinkPayload.getUnsignedByte();
        int i = 0;
        int i2 = 0;
        while (true) {
            short[] sArr = this.satellite_prn;
            if (i2 >= sArr.length) {
                break;
            }
            sArr[i2] = mAVLinkPayload.getUnsignedByte();
            i2++;
        }
        int i3 = 0;
        while (true) {
            short[] sArr2 = this.satellite_used;
            if (i3 >= sArr2.length) {
                break;
            }
            sArr2[i3] = mAVLinkPayload.getUnsignedByte();
            i3++;
        }
        int i4 = 0;
        while (true) {
            short[] sArr3 = this.satellite_elevation;
            if (i4 >= sArr3.length) {
                break;
            }
            sArr3[i4] = mAVLinkPayload.getUnsignedByte();
            i4++;
        }
        int i5 = 0;
        while (true) {
            short[] sArr4 = this.satellite_azimuth;
            if (i5 >= sArr4.length) {
                break;
            }
            sArr4[i5] = mAVLinkPayload.getUnsignedByte();
            i5++;
        }
        while (true) {
            short[] sArr5 = this.satellite_snr;
            if (i >= sArr5.length) {
                return;
            }
            sArr5[i] = mAVLinkPayload.getUnsignedByte();
            i++;
        }
    }

    public msg_gps_status() {
        this.satellite_prn = new short[20];
        this.satellite_used = new short[20];
        this.satellite_elevation = new short[20];
        this.satellite_azimuth = new short[20];
        this.satellite_snr = new short[20];
        this.msgid = 25;
    }

    public msg_gps_status(MAVLinkPacket mAVLinkPacket) {
        this.satellite_prn = new short[20];
        this.satellite_used = new short[20];
        this.satellite_elevation = new short[20];
        this.satellite_azimuth = new short[20];
        this.satellite_snr = new short[20];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 25;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_GPS_STATUS - sysid:" + this.sysid + " compid:" + this.compid + " satellites_visible:" + ((int) this.satellites_visible) + " satellite_prn:" + this.satellite_prn + " satellite_used:" + this.satellite_used + " satellite_elevation:" + this.satellite_elevation + " satellite_azimuth:" + this.satellite_azimuth + " satellite_snr:" + this.satellite_snr + "";
    }
}
