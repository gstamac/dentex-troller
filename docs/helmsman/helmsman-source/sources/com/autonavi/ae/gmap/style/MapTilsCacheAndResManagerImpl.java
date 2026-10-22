package com.autonavi.ae.gmap.style;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.autonavi.ae.gmap.utils.GLFileUtil;
import com.autonavi.ae.gmap.utils.GLMapUtil;
import com.autonavi.amap.mapcore.FileUtil;
import com.qiniu.android.common.Constants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class MapTilsCacheAndResManagerImpl extends MapTilsCacheAndResManager {
    private static final int CREATE_DIR_COUNT = 5;
    private static final long Style_Update_Internal_Time = 43200000;
    private static volatile MapTilsCacheAndResManagerImpl instance;
    private String mCacheDir;
    private String mCachePath;
    private Context mContext;
    private String mMapExtResPath;

    public MapTilsCacheAndResManagerImpl(Context context, String str) {
        this.mCacheDir = str;
        this.mContext = context;
    }

    @Override // com.autonavi.ae.gmap.style.MapTilsCacheAndResManager
    public void setMapCachePath(String str) {
        this.mCacheDir = str;
    }

    @Override // com.autonavi.ae.gmap.style.MapTilsCacheAndResManager
    public String getMapCachePath() {
        return this.mCacheDir;
    }

    @Override // com.autonavi.ae.gmap.style.MapTilsCacheAndResManager
    public void checkDir() {
        File file = new File(this.mCacheDir, MapTilsCacheAndResManager.MAP_RES_EXT_PATH_NAME);
        int i = 0;
        while (!file.exists()) {
            int i2 = i + 1;
            if (i >= 5) {
                break;
            }
            file.mkdir();
            i = i2;
        }
        this.mMapExtResPath = file.toString() + "/";
    }

    public synchronized void reset() {
        instance = null;
    }

    @Override // com.autonavi.ae.gmap.style.MapTilsCacheAndResManager
    public byte[] getStyleData(String str, MapTilsCacheAndResManager.RetStyleIconsFile retStyleIconsFile) {
        try {
            return getStyleIconsData(str, 1, retStyleIconsFile);
        } catch (IOException unused) {
            return null;
        }
    }

    @Override // com.autonavi.ae.gmap.style.MapTilsCacheAndResManager
    public byte[] getIconsData(String str, MapTilsCacheAndResManager.RetStyleIconsFile retStyleIconsFile) {
        try {
            return getStyleIconsData(str, 2, retStyleIconsFile);
        } catch (IOException unused) {
            return null;
        }
    }

    @Override // com.autonavi.ae.gmap.style.MapTilsCacheAndResManager
    public byte[] getOtherResData(String str) {
        try {
            InputStream inputStreamOpen = this.mContext.getAssets().open("map_assets/" + str);
            int iAvailable = inputStreamOpen.available();
            if (iAvailable == 0) {
                return null;
            }
            byte[] bArr = new byte[iAvailable];
            for (int i = 0; i < iAvailable; i += inputStreamOpen.read(bArr, i, iAvailable - i)) {
            }
            inputStreamOpen.close();
            return bArr;
        } catch (IOException | OutOfMemoryError unused) {
            return null;
        }
    }

    @Override // com.autonavi.ae.gmap.style.MapTilsCacheAndResManager
    public byte[] getOtherResDataFromDisk(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return FileUtil.readFileContents(str);
    }

    @Override // com.autonavi.ae.gmap.style.MapTilsCacheAndResManager
    public void saveFile(String str, int i, int i2, byte[] bArr) throws Throwable {
        if (bArr == null || bArr.length <= 0) {
            return;
        }
        File[] fileArrListFiles = new File(this.mMapExtResPath).listFiles();
        if (fileArrListFiles != null) {
            String str2 = str + "_";
            for (File file : fileArrListFiles) {
                if (file.getName().contains(str2)) {
                    file.delete();
                    break;
                }
            }
        }
        addUdateRecorder(str + "_" + i);
        GLFileUtil.writeDatasToFile(this.mMapExtResPath + (str + "_" + i + "_" + i2 + ".data"), bArr);
    }

    private String getFilePreName(String str) {
        if (str == null) {
            return null;
        }
        String[] strArrSplit = str.split("_");
        if (strArrSplit.length < 3) {
            return null;
        }
        return strArrSplit[0] + "_" + strArrSplit[1] + "_" + strArrSplit[2];
    }

    private void setRetFile(MapTilsCacheAndResManager.RetStyleIconsFile retStyleIconsFile, String str) {
        if (retStyleIconsFile == null) {
            return;
        }
        retStyleIconsFile.fullName = str;
        String[] strArrSplit = str.split("_|\\.");
        if (strArrSplit.length < 2) {
            return;
        }
        retStyleIconsFile.name = strArrSplit[0] + "_" + strArrSplit[1];
        try {
            retStyleIconsFile.clientVersion = Integer.parseInt(strArrSplit[2]);
            if (strArrSplit.length > 3) {
                retStyleIconsFile.serverVersion = Integer.parseInt(strArrSplit[3]);
            } else {
                retStyleIconsFile.serverVersion = 1;
            }
        } catch (NumberFormatException unused) {
            retStyleIconsFile.clientVersion = 1;
            retStyleIconsFile.serverVersion = 1;
        }
    }

    private byte[] getStyleIconsData(String str, int i, MapTilsCacheAndResManager.RetStyleIconsFile retStyleIconsFile) throws Throwable {
        String filePreName = getFilePreName(str);
        if (filePreName == null) {
            return null;
        }
        MapTilsCacheAndResManager.RetStyleIconsFile retStyleIconsFile2 = new MapTilsCacheAndResManager.RetStyleIconsFile();
        setRetFile(retStyleIconsFile2, str);
        File[] fileArrListFiles = new File(this.mMapExtResPath).listFiles();
        if (fileArrListFiles != null) {
            try {
                for (File file : fileArrListFiles) {
                    if (file.getName().contains(filePreName)) {
                        setRetFile(retStyleIconsFile, file.getName());
                        if (retStyleIconsFile2.serverVersion < retStyleIconsFile.serverVersion) {
                            byte[] fileContents = GLFileUtil.readFileContents(file.getAbsolutePath());
                            if (fileContents != null && fileContents.length > 0) {
                                return fileContents;
                            }
                        } else {
                            GLFileUtil.deleteFile(file);
                        }
                    }
                }
            } catch (OutOfMemoryError unused) {
            }
        }
        setRetFile(retStyleIconsFile, str);
        return GLMapUtil.decodeAssetResData(this.mContext, "map_assets/" + str);
    }

    static void copyAssertToTmp(Context context, String str, File file) {
        byte[] bArrDecodeAssetResData;
        if (file.exists()) {
            return;
        }
        try {
            if (!file.createNewFile() || (bArrDecodeAssetResData = GLMapUtil.decodeAssetResData(context, str)) == null) {
                return;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bArrDecodeAssetResData);
            fileOutputStream.close();
        } catch (IOException unused) {
        }
    }

    @Override // com.autonavi.ae.gmap.style.MapTilsCacheAndResManager
    public void addUdateRecorder(String str) {
        SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("styles_icons_update_recorder", 0).edit();
        editorEdit.putLong(str, System.currentTimeMillis());
        editorEdit.commit();
    }

    @Override // com.autonavi.ae.gmap.style.MapTilsCacheAndResManager
    public synchronized boolean canUpate(String str) {
        long j;
        j = this.mContext.getSharedPreferences("styles_icons_update_recorder", 0).getLong(str, -1L);
        return j <= 0 || System.currentTimeMillis() - j >= Style_Update_Internal_Time;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v2 */
    /* JADX WARN: Type inference failed for: r14v3, types: [byte[]] */
    @Override // com.autonavi.ae.gmap.style.MapTilsCacheAndResManager
    public byte[] getPolyIconFilePath(String str, MapTilsCacheAndResManager.RetStyleIconsFile retStyleIconsFile) {
        try {
            String filePreName = getFilePreName(str);
            if (TextUtils.isEmpty(filePreName)) {
                return null;
            }
            MapTilsCacheAndResManager.RetStyleIconsFile retStyleIconsFile2 = new MapTilsCacheAndResManager.RetStyleIconsFile();
            setRetFile(retStyleIconsFile2, str);
            File[] fileArrListFiles = new File(this.mMapExtResPath).listFiles();
            if (fileArrListFiles != null) {
                try {
                    for (File file : fileArrListFiles) {
                        if (file.getName().contains(filePreName)) {
                            setRetFile(retStyleIconsFile, file.getName());
                            if (retStyleIconsFile2.serverVersion >= retStyleIconsFile.serverVersion) {
                                GLFileUtil.deleteFile(file);
                            } else if (file.length() > 0) {
                                str = (file.getAbsolutePath() + "\u0000").getBytes(Constants.UTF_8);
                                return str;
                            }
                        }
                    }
                } catch (Throwable unused) {
                }
            }
            copyAssetsFile(str);
            setRetFile(retStyleIconsFile, str);
            return (this.mMapExtResPath + str + "\u0000").getBytes(Constants.UTF_8);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private void copyAssetsFile(String str) {
        try {
            if (new File(this.mMapExtResPath + str).exists()) {
                return;
            }
            InputStream inputStreamOpen = this.mContext.getAssets().open("map_assets/" + str);
            if (inputStreamOpen.available() == 0) {
                return;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(this.mMapExtResPath + str);
            byte[] bArr = new byte[1024];
            for (int i = inputStreamOpen.read(bArr); i > 0; i = inputStreamOpen.read(bArr)) {
                fileOutputStream.write(bArr, 0, i);
            }
            fileOutputStream.flush();
            inputStreamOpen.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
