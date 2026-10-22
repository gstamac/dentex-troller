package javy.lib.jsave.bean;

/* JADX INFO: loaded from: classes2.dex */
public class FileBean {
    private byte[] datas;
    private String fileName;

    public FileBean(String str, byte[] bArr) {
        this.fileName = str;
        this.datas = bArr;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public byte[] getDatas() {
        return this.datas;
    }

    public void setDatas(byte[] bArr) {
        this.datas = bArr;
    }
}
