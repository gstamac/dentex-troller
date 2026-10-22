package im.helmsman.lib.util;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Parser;
import kotlin.UByte;

/* JADX INFO: loaded from: classes2.dex */
public class MavlinkUtil {
    private static Parser parser;

    public static MAVLinkMessage dataToMAVLinkMessage(byte[] bArr) {
        if (parser == null) {
            parser = new Parser();
        }
        for (byte b : bArr) {
            MAVLinkPacket mAVLinkPacketMavlink_parse_char = parser.mavlink_parse_char(b & UByte.MAX_VALUE);
            if (mAVLinkPacketMavlink_parse_char != null) {
                return mAVLinkPacketMavlink_parse_char.unpack();
            }
        }
        return null;
    }
}
