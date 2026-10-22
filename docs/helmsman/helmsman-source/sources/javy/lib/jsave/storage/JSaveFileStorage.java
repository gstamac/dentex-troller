package javy.lib.jsave.storage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javy.lib.jsave.JSave;
import javy.lib.jsave.bean.FileBean;

/* JADX INFO: loaded from: classes2.dex */
public class JSaveFileStorage implements JSaveStorage<FileBean> {
    private File pathFile;

    public JSaveFileStorage() {
        try {
            File file = new File(JSave.mContext.getCacheDir().getAbsolutePath() + "/customDirctory/");
            this.pathFile = file;
            if (!file.exists() && !this.pathFile.mkdirs()) {
                throw new RuntimeException("You must ensure that application has access to the SD card");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // javy.lib.jsave.storage.JSaveStorage
    public boolean save(FileBean fileBean) throws Throwable {
        File file = new File(this.pathFile, fileBean.getFileName());
        byte[] datas = fileBean.getDatas();
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                try {
                    fileOutputStream2.write(datas);
                    try {
                        fileOutputStream2.close();
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                } catch (FileNotFoundException e2) {
                    e = e2;
                    fileOutputStream = fileOutputStream2;
                    e.printStackTrace();
                    try {
                        fileOutputStream.close();
                        return false;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        return false;
                    }
                } catch (IOException e4) {
                    e = e4;
                    fileOutputStream = fileOutputStream2;
                    e.printStackTrace();
                    try {
                        fileOutputStream.close();
                        return false;
                    } catch (IOException e5) {
                        e5.printStackTrace();
                        return false;
                    }
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    try {
                        fileOutputStream.close();
                        throw th;
                    } catch (IOException e6) {
                        e6.printStackTrace();
                        return false;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (FileNotFoundException e7) {
            e = e7;
        } catch (IOException e8) {
            e = e8;
        }
    }

    @Override // javy.lib.jsave.storage.JSaveStorage
    public FileBean read(String str) {
        File file = new File(this.pathFile, str);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[512];
            while (true) {
                int i = fileInputStream.read(bArr);
                if (i > 0) {
                    byteArrayOutputStream.write(bArr, 0, i);
                } else {
                    return new FileBean(str, byteArrayOutputStream.toByteArray());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    @Override // javy.lib.jsave.storage.JSaveStorage
    public int deleteAll() {
        File[] fileArrListFiles = this.pathFile.listFiles();
        for (File file : fileArrListFiles) {
            file.delete();
        }
        return fileArrListFiles.length;
    }

    @Override // javy.lib.jsave.storage.JSaveStorage
    public boolean delete(String str) {
        return new File(this.pathFile, str).delete();
    }
}
