package im.helmsman.helmsmanandroid.utils;

import android.os.Environment;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Parser;
import com.MAVLink.common.msg_command_long;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes2.dex */
public class LogUtil implements MavlinkUtil.OncreateMavlinkMessageListener {
    private static LogUtil logUtil;
    private AccessMessageCreateListener accessMessageCreateListener;
    private File file;
    private FileOutputStream fos;
    private OutputStreamWriter osw;
    private Parser parser;
    private boolean motorIsStart = false;
    private float modelState = 0.0f;
    private float batteryState = 0.0f;
    private boolean isSaveType = false;
    private boolean isConnected = false;
    private boolean isSaveSSid = false;
    private boolean isConnectedProtect = false;
    private boolean isSaveVersion = false;
    private int speedLeve = 0;
    private boolean isInitLogFileStream = false;
    private final String path = Environment.getExternalStorageDirectory().getPath() + "/helmsman";

    public void closeLogFileStream() {
    }

    public void initLogFileStream() {
    }

    public void saveAccectCommand(MAVLinkMessage mAVLinkMessage) {
    }

    public void saveLog(String str) {
    }

    public void saveSendCommand(byte[] bArr) {
    }

    private LogUtil() {
    }

    public static LogUtil instance() {
        if (logUtil == null) {
            LogUtil logUtil2 = new LogUtil();
            logUtil = logUtil2;
            logUtil2.initLogFileStream();
        }
        return logUtil;
    }

    public void saveIsConnectedStatelog(boolean z) {
        if (z != this.isConnected) {
            this.isConnected = z;
            if (z) {
                saveLog("BC/Connection/Activate");
            } else {
                saveLog(" BC/Connection/Deactivate");
            }
        }
    }

    public void inTofront() {
        saveLog("MB/Status/Foreground");
    }

    public void intoBackground() {
        saveLog("MB/Status/Background");
    }

    public void magnetometerCheck() {
        saveLog("MB/Command/Send/BC/Compass/Calibration");
    }

    @Override // im.helmsman.helmsmanandroid.utils.MavlinkUtil.OncreateMavlinkMessageListener
    public void generateMavlink(MAVLinkMessage mAVLinkMessage) {
        if (mAVLinkMessage == null) {
        }
        int i = mAVLinkMessage.msgid;
        if (i == 76) {
            int i2 = ((msg_command_long) mAVLinkMessage).command;
            if (i2 == 3) {
                saveLog("MB/Command/Send/TM/Speed/Fullspeed");
                return;
            }
            if (i2 != 181) {
                switch (i2) {
                    case 8:
                        saveLog("MB/Command/Send/TM/Speed/Up");
                        break;
                    case 9:
                        saveLog("MB/Command/Send/TM/Speed/Down");
                        break;
                    case 10:
                        saveLog("MB/Command/Send/TM/Start");
                        break;
                    case 11:
                        saveLog("MB/Command/Send/TM/Stop");
                        break;
                    case 12:
                        saveLog(" MB/Command/Send/BC/Mode/Anchor");
                        break;
                    case 13:
                        saveLog("MB/Command/Send/BC/Mode/Manual");
                        break;
                    case 14:
                        saveLog("MB/Command/Send/BC/Mode/Cruise");
                        break;
                    case 15:
                        saveLog("MB/Command/Send/BC/Mode/Navigation");
                        break;
                }
            }
            saveLog("MB/Command/Send/BC/Mode/record");
            return;
        }
        switch (i) {
            case 43:
                saveLog("MB/Command/Send/BC/Route/Download");
                break;
            case 44:
                saveLog("MB/Command/Send/BC/Route/Upload");
                break;
            case 45:
                saveLog("MB/Command/Send/BC/Route/Cache/Clear");
                break;
        }
    }

    class upLoadTimeTask extends TimerTask {
        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
        }

        upLoadTimeTask() {
        }
    }

    class AccessMessageCreateListener implements MavlinkUtil.OncreateMavlinkMessageListener {
        @Override // im.helmsman.helmsmanandroid.utils.MavlinkUtil.OncreateMavlinkMessageListener
        public void generateMavlink(MAVLinkMessage mAVLinkMessage) {
        }

        AccessMessageCreateListener() {
        }
    }
}
