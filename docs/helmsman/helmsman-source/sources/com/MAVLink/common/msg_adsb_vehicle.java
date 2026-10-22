package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_adsb_vehicle extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_ADSB_VEHICLE = 246;
    public static final int MAVLINK_MSG_LENGTH = 38;
    private static final long serialVersionUID = 246;
    public long ICAO_address;
    public int altitude;
    public short altitude_type;
    public byte[] callsign;
    public short emitter_type;
    public int flags;
    public int heading;
    public int hor_velocity;
    public int lat;
    public int lon;
    public int squawk;
    public short tslc;
    public short ver_velocity;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(38);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 246;
        mAVLinkPacket.payload.putUnsignedInt(this.ICAO_address);
        mAVLinkPacket.payload.putInt(this.lat);
        mAVLinkPacket.payload.putInt(this.lon);
        mAVLinkPacket.payload.putInt(this.altitude);
        mAVLinkPacket.payload.putUnsignedShort(this.heading);
        mAVLinkPacket.payload.putUnsignedShort(this.hor_velocity);
        mAVLinkPacket.payload.putShort(this.ver_velocity);
        mAVLinkPacket.payload.putUnsignedShort(this.flags);
        mAVLinkPacket.payload.putUnsignedShort(this.squawk);
        mAVLinkPacket.payload.putUnsignedByte(this.altitude_type);
        for (int i = 0; i < this.callsign.length; i++) {
            mAVLinkPacket.payload.putByte(this.callsign[i]);
        }
        mAVLinkPacket.payload.putUnsignedByte(this.emitter_type);
        mAVLinkPacket.payload.putUnsignedByte(this.tslc);
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.ICAO_address = mAVLinkPayload.getUnsignedInt();
        this.lat = mAVLinkPayload.getInt();
        this.lon = mAVLinkPayload.getInt();
        this.altitude = mAVLinkPayload.getInt();
        this.heading = mAVLinkPayload.getUnsignedShort();
        this.hor_velocity = mAVLinkPayload.getUnsignedShort();
        this.ver_velocity = mAVLinkPayload.getShort();
        this.flags = mAVLinkPayload.getUnsignedShort();
        this.squawk = mAVLinkPayload.getUnsignedShort();
        this.altitude_type = mAVLinkPayload.getUnsignedByte();
        int i = 0;
        while (true) {
            byte[] bArr = this.callsign;
            if (i < bArr.length) {
                bArr[i] = mAVLinkPayload.getByte();
                i++;
            } else {
                this.emitter_type = mAVLinkPayload.getUnsignedByte();
                this.tslc = mAVLinkPayload.getUnsignedByte();
                return;
            }
        }
    }

    public msg_adsb_vehicle() {
        this.callsign = new byte[9];
        this.msgid = 246;
    }

    public msg_adsb_vehicle(MAVLinkPacket mAVLinkPacket) {
        this.callsign = new byte[9];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 246;
        unpack(mAVLinkPacket.payload);
    }

    public void setCallsign(String str) {
        int iMin = Math.min(str.length(), 9);
        for (int i = 0; i < iMin; i++) {
            this.callsign[i] = (byte) str.charAt(i);
        }
        while (iMin < 9) {
            this.callsign[iMin] = 0;
            iMin++;
        }
    }

    public String getCallsign() {
        byte b;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 9 && (b = this.callsign[i]) != 0; i++) {
            stringBuffer.append((char) b);
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return "MAVLINK_MSG_ID_ADSB_VEHICLE - sysid:" + this.sysid + " compid:" + this.compid + " ICAO_address:" + this.ICAO_address + " lat:" + this.lat + " lon:" + this.lon + " altitude:" + this.altitude + " heading:" + this.heading + " hor_velocity:" + this.hor_velocity + " ver_velocity:" + ((int) this.ver_velocity) + " flags:" + this.flags + " squawk:" + this.squawk + " altitude_type:" + ((int) this.altitude_type) + " callsign:" + this.callsign + " emitter_type:" + ((int) this.emitter_type) + " tslc:" + ((int) this.tslc) + "";
    }
}
