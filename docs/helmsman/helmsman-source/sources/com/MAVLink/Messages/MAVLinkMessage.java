package com.MAVLink.Messages;

import com.MAVLink.MAVLinkPacket;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public abstract class MAVLinkMessage implements Serializable {
    private static final long serialVersionUID = -7754622750478538539L;
    public int compid;
    public int msgid;
    public int sysid;

    public abstract MAVLinkPacket pack();

    public abstract void unpack(MAVLinkPayload mAVLinkPayload);
}
