package im.helmsman.lib.error;

/* JADX INFO: loaded from: classes2.dex */
public class HMError {
    public static final int ECODE_NO_GPS = -4;
    public static final int ECODE_SUCCESS = 0;
    public static final int ECODE_TIMEOUT = -1;
    public static final int ECODE_UNKNOW_TYPE = -2;
    public int code;

    public HMError(int i) {
        this.code = i;
    }
}
