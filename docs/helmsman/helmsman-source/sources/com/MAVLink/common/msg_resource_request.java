package com.MAVLink.common;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/* JADX INFO: loaded from: classes.dex */
public class msg_resource_request extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_RESOURCE_REQUEST = 142;
    public static final int MAVLINK_MSG_LENGTH = 243;
    private static final long serialVersionUID = 142;
    public short request_id;
    public short[] storage;
    public short transfer_type;
    public short[] uri;
    public short uri_type;

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public MAVLinkPacket pack() {
        MAVLinkPacket mAVLinkPacket = new MAVLinkPacket(243);
        mAVLinkPacket.sysid = 255;
        mAVLinkPacket.compid = 190;
        mAVLinkPacket.msgid = 142;
        mAVLinkPacket.payload.putUnsignedByte(this.request_id);
        mAVLinkPacket.payload.putUnsignedByte(this.uri_type);
        for (int i = 0; i < this.uri.length; i++) {
            mAVLinkPacket.payload.putUnsignedByte(this.uri[i]);
        }
        mAVLinkPacket.payload.putUnsignedByte(this.transfer_type);
        for (int i2 = 0; i2 < this.storage.length; i2++) {
            mAVLinkPacket.payload.putUnsignedByte(this.storage[i2]);
        }
        return mAVLinkPacket;
    }

    @Override // com.MAVLink.Messages.MAVLinkMessage
    public void unpack(MAVLinkPayload mAVLinkPayload) {
        mAVLinkPayload.resetIndex();
        this.request_id = mAVLinkPayload.getUnsignedByte();
        this.uri_type = mAVLinkPayload.getUnsignedByte();
        int i = 0;
        int i2 = 0;
        while (true) {
            short[] sArr = this.uri;
            if (i2 >= sArr.length) {
                break;
            }
            sArr[i2] = mAVLinkPayload.getUnsignedByte();
            i2++;
        }
        this.transfer_type = mAVLinkPayload.getUnsignedByte();
        while (true) {
            short[] sArr2 = this.storage;
            if (i >= sArr2.length) {
                return;
            }
            sArr2[i] = mAVLinkPayload.getUnsignedByte();
            i++;
        }
    }

    public msg_resource_request() {
        this.uri = new short[120];
        this.storage = new short[120];
        this.msgid = 142;
    }

    public msg_resource_request(MAVLinkPacket mAVLinkPacket) {
        this.uri = new short[120];
        this.storage = new short[120];
        this.sysid = mAVLinkPacket.sysid;
        this.compid = mAVLinkPacket.compid;
        this.msgid = 142;
        unpack(mAVLinkPacket.payload);
    }

    public String toString() {
        return "MAVLINK_MSG_ID_RESOURCE_REQUEST - sysid:" + this.sysid + " compid:" + this.compid + " request_id:" + ((int) this.request_id) + " uri_type:" + ((int) this.uri_type) + " uri:" + this.uri + " transfer_type:" + ((int) this.transfer_type) + " storage:" + this.storage + "";
    }
}
