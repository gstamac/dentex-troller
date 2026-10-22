package im.helmsman.lib;

import android.util.Log;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Parser;
import com.MAVLink.common.msg_heartbeat;
import im.helmsman.lib.component.TeleControlComponent;
import im.helmsman.lib.factory.ProductFactory;
import kotlin.UByte;

/* JADX INFO: loaded from: classes2.dex */
public class DataIOManager {
    private MAVLinkPacket mavLinkPacket;
    Parser p = new Parser();
    private ABaseProduct product;
    private OnSendDatasListener sendDatasListener;

    public interface OnSendDatasListener {
        void sendDatas(byte[] bArr);
    }

    protected DataIOManager() {
    }

    public void setProduct(ABaseProduct aBaseProduct) {
        this.product = aBaseProduct;
    }

    public synchronized void decode(byte[] bArr) {
        if (bArr != null) {
            if (bArr.length != 0) {
                if (this.p == null) {
                    this.p = new Parser();
                }
                for (byte b : bArr) {
                    MAVLinkPacket mAVLinkPacketMavlink_parse_char = this.p.mavlink_parse_char(b & UByte.MAX_VALUE);
                    if (mAVLinkPacketMavlink_parse_char != null) {
                        decodeMAVLinkMessage(mAVLinkPacketMavlink_parse_char.unpack());
                    }
                }
            }
        }
    }

    private void decodeMAVLinkMessage(MAVLinkMessage mAVLinkMessage) {
        if (mAVLinkMessage.compid == 99) {
            TeleControlComponent teleControlComponentInstance = TeleControlComponent.getTeleControlComponentInstance(mAVLinkMessage.compid);
            Log.e("createTeleContril", teleControlComponentInstance + "");
            teleControlComponentInstance.decode(mAVLinkMessage);
        }
        if (this.product == null || (mAVLinkMessage.msgid == 0 && this.product.getType() != ((msg_heartbeat) mAVLinkMessage).type)) {
            msg_heartbeat msg_heartbeatVar = (msg_heartbeat) mAVLinkMessage;
            this.product = ProductFactory.createProduct(msg_heartbeatVar.type, (int) msg_heartbeatVar.custom_mode);
        } else {
            this.product.onReceiverData(mAVLinkMessage);
        }
    }

    public void sendData(MAVLinkMessage mAVLinkMessage) {
        sendData(mAVLinkMessage.pack().encodePacket());
    }

    public void sendData(byte[] bArr) {
        OnSendDatasListener onSendDatasListener = this.sendDatasListener;
        if (onSendDatasListener != null) {
            onSendDatasListener.sendDatas(bArr);
        } else {
            Log.w("HelmsmanSDK", "你必须设置数据发送监听器，以监听数据发送");
        }
    }

    public void sendData(MAVLinkPacket mAVLinkPacket) {
        sendData(mAVLinkPacket.encodePacket());
    }

    public void setSendDatasListener(OnSendDatasListener onSendDatasListener) {
        this.sendDatasListener = onSendDatasListener;
    }
}
