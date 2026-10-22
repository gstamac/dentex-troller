package im.helmsman.helmsmanandroid.utils;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Parser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.UByte;

/* JADX INFO: loaded from: classes2.dex */
public class MavlinkUtil {
    private static List<OncreateMavlinkMessageListener> listeners = new ArrayList();
    private static Parser parser;

    public interface OncreateMavlinkMessageListener {
        void generateMavlink(MAVLinkMessage mAVLinkMessage);
    }

    public static void dataToMAVLinkMessage(byte[] bArr) {
        if (parser == null) {
            parser = new Parser();
        }
        for (byte b : bArr) {
            MAVLinkPacket mAVLinkPacketMavlink_parse_char = parser.mavlink_parse_char(b & UByte.MAX_VALUE);
            if (mAVLinkPacketMavlink_parse_char != null) {
                callback(mAVLinkPacketMavlink_parse_char);
            }
        }
    }

    private static void callback(MAVLinkPacket mAVLinkPacket) {
        MAVLinkMessage mAVLinkMessageUnpack = mAVLinkPacket.unpack();
        Iterator<OncreateMavlinkMessageListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().generateMavlink(mAVLinkMessageUnpack);
        }
    }

    public static void addOnCreateMavlinkMessageListener(OncreateMavlinkMessageListener oncreateMavlinkMessageListener) {
        if (listeners.contains(oncreateMavlinkMessageListener)) {
            return;
        }
        listeners.add(oncreateMavlinkMessageListener);
    }

    public static void removeOnCreateMavlinkMessageListener(OncreateMavlinkMessageListener oncreateMavlinkMessageListener) {
        listeners.remove(oncreateMavlinkMessageListener);
    }
}
