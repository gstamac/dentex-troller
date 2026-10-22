package com.ta.utdid2.core.persistent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import com.ta.utdid2.android.utils.StringUtils;
import java.io.File;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class PersistentConfiguration {
    private static final String KEY_TIMESTAMP = "t";
    private static final String KEY_TIMESTAMP2 = "t2";
    private boolean mCanRead;
    private boolean mCanWrite;
    private String mConfigName;
    private Context mContext;
    private String mFolderName;
    private boolean mIsLessMode;
    private boolean mIsSafety;
    private MySharedPreferences mMySP;
    private SharedPreferences mSp;
    private TransactionXMLFile mTxf;
    private SharedPreferences.Editor mEditor = null;
    private MySharedPreferences.MyEditor mMyEditor = null;

    /* JADX WARN: Code duplicated, block: B:56:0x0133  */
    public PersistentConfiguration(Context context, String str, String str2, boolean z, boolean z2) {
        long j;
        long j2;
        this.mCanRead = false;
        this.mCanWrite = false;
        this.mSp = null;
        this.mMySP = null;
        this.mTxf = null;
        this.mIsSafety = z;
        this.mIsLessMode = z2;
        this.mConfigName = str2;
        this.mFolderName = str;
        this.mContext = context;
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(str2, 0);
            this.mSp = sharedPreferences;
            j = sharedPreferences.getLong(KEY_TIMESTAMP, 0L);
        } else {
            j = 0;
        }
        String externalStorageState = Environment.getExternalStorageState();
        if (StringUtils.isEmpty(externalStorageState)) {
            this.mCanWrite = false;
            this.mCanRead = false;
        } else if (externalStorageState.equals("mounted")) {
            this.mCanWrite = true;
            this.mCanRead = true;
        } else if (externalStorageState.equals("mounted_ro")) {
            this.mCanRead = true;
            this.mCanWrite = false;
        } else {
            this.mCanWrite = false;
            this.mCanRead = false;
        }
        if ((!this.mCanRead && !this.mCanWrite) || context == null || StringUtils.isEmpty(str)) {
            j2 = 0;
        } else {
            TransactionXMLFile transactionXMLFile = getTransactionXMLFile(str);
            this.mTxf = transactionXMLFile;
            if (transactionXMLFile != null) {
                try {
                    MySharedPreferences mySharedPreferences = transactionXMLFile.getMySharedPreferences(str2, 0);
                    this.mMySP = mySharedPreferences;
                    j2 = mySharedPreferences.getLong(KEY_TIMESTAMP, 0L);
                    try {
                        if (z2) {
                            long j3 = this.mSp.getLong(KEY_TIMESTAMP2, 0L);
                            try {
                                j2 = this.mMySP.getLong(KEY_TIMESTAMP2, 0L);
                                if (j3 < j2 && j3 > 0) {
                                    copySPToMySP(this.mSp, this.mMySP);
                                    this.mMySP = this.mTxf.getMySharedPreferences(str2, 0);
                                } else if (j3 > j2 && j2 > 0) {
                                    copyMySPToSP(this.mMySP, this.mSp);
                                    this.mSp = context.getSharedPreferences(str2, 0);
                                } else if (j3 == 0 && j2 > 0) {
                                    copyMySPToSP(this.mMySP, this.mSp);
                                    this.mSp = context.getSharedPreferences(str2, 0);
                                } else if ((j2 == 0 && j3 > 0) || j3 == j2) {
                                    copySPToMySP(this.mSp, this.mMySP);
                                    this.mMySP = this.mTxf.getMySharedPreferences(str2, 0);
                                }
                            } catch (Exception unused) {
                            }
                            j = j3;
                        } else if (j > j2) {
                            copySPToMySP(this.mSp, this.mMySP);
                            this.mMySP = this.mTxf.getMySharedPreferences(str2, 0);
                        } else if (j < j2) {
                            copyMySPToSP(this.mMySP, this.mSp);
                            this.mSp = context.getSharedPreferences(str2, 0);
                        } else if (j == j2) {
                            copySPToMySP(this.mSp, this.mMySP);
                            this.mMySP = this.mTxf.getMySharedPreferences(str2, 0);
                        }
                    } catch (Exception unused2) {
                    }
                } catch (Exception unused3) {
                    j2 = 0;
                }
            } else {
                j2 = 0;
            }
        }
        if (j != j2 || (j == 0 && j2 == 0)) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            boolean z3 = this.mIsLessMode;
            if (!z3 || (z3 && j == 0 && j2 == 0)) {
                SharedPreferences sharedPreferences2 = this.mSp;
                if (sharedPreferences2 != null) {
                    SharedPreferences.Editor editorEdit = sharedPreferences2.edit();
                    editorEdit.putLong(KEY_TIMESTAMP2, jCurrentTimeMillis);
                    editorEdit.commit();
                }
                try {
                    MySharedPreferences mySharedPreferences2 = this.mMySP;
                    if (mySharedPreferences2 != null) {
                        MySharedPreferences.MyEditor myEditorEdit = mySharedPreferences2.edit();
                        myEditorEdit.putLong(KEY_TIMESTAMP2, jCurrentTimeMillis);
                        myEditorEdit.commit();
                    }
                } catch (Exception unused4) {
                }
            }
        }
    }

    private TransactionXMLFile getTransactionXMLFile(String str) {
        File rootFolder = getRootFolder(str);
        if (rootFolder == null) {
            return null;
        }
        TransactionXMLFile transactionXMLFile = new TransactionXMLFile(rootFolder.getAbsolutePath());
        this.mTxf = transactionXMLFile;
        return transactionXMLFile;
    }

    private File getRootFolder(String str) {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory == null) {
            return null;
        }
        File file = new File(String.format("%s%s%s", externalStorageDirectory.getAbsolutePath(), File.separator, str));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private void copySPToMySP(SharedPreferences sharedPreferences, MySharedPreferences mySharedPreferences) {
        MySharedPreferences.MyEditor myEditorEdit;
        if (sharedPreferences == null || mySharedPreferences == null || (myEditorEdit = mySharedPreferences.edit()) == null) {
            return;
        }
        myEditorEdit.clear();
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                myEditorEdit.putString(key, (String) value);
            } else if (value instanceof Integer) {
                myEditorEdit.putInt(key, ((Integer) value).intValue());
            } else if (value instanceof Long) {
                myEditorEdit.putLong(key, ((Long) value).longValue());
            } else if (value instanceof Float) {
                myEditorEdit.putFloat(key, ((Float) value).floatValue());
            } else if (value instanceof Boolean) {
                myEditorEdit.putBoolean(key, ((Boolean) value).booleanValue());
            }
        }
        myEditorEdit.commit();
    }

    private void copyMySPToSP(MySharedPreferences mySharedPreferences, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editorEdit;
        if (mySharedPreferences == null || sharedPreferences == null || (editorEdit = sharedPreferences.edit()) == null) {
            return;
        }
        editorEdit.clear();
        for (Map.Entry<String, ?> entry : mySharedPreferences.getAll().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                editorEdit.putString(key, (String) value);
            } else if (value instanceof Integer) {
                editorEdit.putInt(key, ((Integer) value).intValue());
            } else if (value instanceof Long) {
                editorEdit.putLong(key, ((Long) value).longValue());
            } else if (value instanceof Float) {
                editorEdit.putFloat(key, ((Float) value).floatValue());
            } else if (value instanceof Boolean) {
                editorEdit.putBoolean(key, ((Boolean) value).booleanValue());
            }
        }
        editorEdit.commit();
    }

    private boolean checkSDCardXMLFile() {
        MySharedPreferences mySharedPreferences = this.mMySP;
        if (mySharedPreferences == null) {
            return false;
        }
        boolean zCheckFile = mySharedPreferences.checkFile();
        if (!zCheckFile) {
            commit();
        }
        return zCheckFile;
    }

    private void initEditor() {
        MySharedPreferences mySharedPreferences;
        SharedPreferences sharedPreferences;
        if (this.mEditor == null && (sharedPreferences = this.mSp) != null) {
            this.mEditor = sharedPreferences.edit();
        }
        if (this.mCanWrite && this.mMyEditor == null && (mySharedPreferences = this.mMySP) != null) {
            this.mMyEditor = mySharedPreferences.edit();
        }
        checkSDCardXMLFile();
    }

    public void putInt(String str, int i) {
        if (StringUtils.isEmpty(str) || str.equals(KEY_TIMESTAMP)) {
            return;
        }
        initEditor();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.putInt(str, i);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.putInt(str, i);
        }
    }

    public void putLong(String str, long j) {
        if (StringUtils.isEmpty(str) || str.equals(KEY_TIMESTAMP)) {
            return;
        }
        initEditor();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.putLong(str, j);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.putLong(str, j);
        }
    }

    public void putBoolean(String str, boolean z) {
        if (StringUtils.isEmpty(str) || str.equals(KEY_TIMESTAMP)) {
            return;
        }
        initEditor();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.putBoolean(str, z);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.putBoolean(str, z);
        }
    }

    public void putFloat(String str, float f) {
        if (StringUtils.isEmpty(str) || str.equals(KEY_TIMESTAMP)) {
            return;
        }
        initEditor();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.putFloat(str, f);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.putFloat(str, f);
        }
    }

    public void putString(String str, String str2) {
        if (StringUtils.isEmpty(str) || str.equals(KEY_TIMESTAMP)) {
            return;
        }
        initEditor();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.putString(str, str2);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.putString(str, str2);
        }
    }

    public void remove(String str) {
        if (StringUtils.isEmpty(str) || str.equals(KEY_TIMESTAMP)) {
            return;
        }
        initEditor();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.remove(str);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.remove(str);
        }
    }

    public void reload() {
        Context context;
        if (this.mSp != null && (context = this.mContext) != null) {
            this.mSp = context.getSharedPreferences(this.mConfigName, 0);
        }
        String externalStorageState = Environment.getExternalStorageState();
        if (StringUtils.isEmpty(externalStorageState)) {
            return;
        }
        if (externalStorageState.equals("mounted") || (externalStorageState.equals("mounted_ro") && this.mMySP != null)) {
            try {
                TransactionXMLFile transactionXMLFile = this.mTxf;
                if (transactionXMLFile != null) {
                    this.mMySP = transactionXMLFile.getMySharedPreferences(this.mConfigName, 0);
                }
            } catch (Exception unused) {
            }
        }
    }

    public void clear() {
        initEditor();
        long jCurrentTimeMillis = System.currentTimeMillis();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.clear();
            this.mEditor.putLong(KEY_TIMESTAMP, jCurrentTimeMillis);
        }
        MySharedPreferences.MyEditor myEditor = this.mMyEditor;
        if (myEditor != null) {
            myEditor.clear();
            this.mMyEditor.putLong(KEY_TIMESTAMP, jCurrentTimeMillis);
        }
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0020  */
    public boolean commit() {
        boolean z;
        Context context;
        long jCurrentTimeMillis = System.currentTimeMillis();
        SharedPreferences.Editor editor = this.mEditor;
        if (editor == null) {
            z = true;
        } else {
            if (!this.mIsLessMode && this.mSp != null) {
                editor.putLong(KEY_TIMESTAMP, jCurrentTimeMillis);
            }
            if (this.mEditor.commit()) {
                z = true;
            } else {
                z = false;
            }
        }
        if (this.mSp != null && (context = this.mContext) != null) {
            this.mSp = context.getSharedPreferences(this.mConfigName, 0);
        }
        String externalStorageState = Environment.getExternalStorageState();
        if (!StringUtils.isEmpty(externalStorageState)) {
            if (externalStorageState.equals("mounted")) {
                if (this.mMySP == null) {
                    TransactionXMLFile transactionXMLFile = getTransactionXMLFile(this.mFolderName);
                    if (transactionXMLFile != null) {
                        MySharedPreferences mySharedPreferences = transactionXMLFile.getMySharedPreferences(this.mConfigName, 0);
                        this.mMySP = mySharedPreferences;
                        if (!this.mIsLessMode) {
                            copySPToMySP(this.mSp, mySharedPreferences);
                        } else {
                            copyMySPToSP(mySharedPreferences, this.mSp);
                        }
                        this.mMyEditor = this.mMySP.edit();
                    }
                } else {
                    MySharedPreferences.MyEditor myEditor = this.mMyEditor;
                    if (myEditor != null && !myEditor.commit()) {
                        z = false;
                    }
                }
            }
            if (externalStorageState.equals("mounted") || (externalStorageState.equals("mounted_ro") && this.mMySP != null)) {
                try {
                    TransactionXMLFile transactionXMLFile2 = this.mTxf;
                    if (transactionXMLFile2 != null) {
                        this.mMySP = transactionXMLFile2.getMySharedPreferences(this.mConfigName, 0);
                    }
                } catch (Exception unused) {
                }
            }
        }
        return z;
    }

    public String getString(String str) {
        checkSDCardXMLFile();
        SharedPreferences sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
            String string = sharedPreferences.getString(str, "");
            if (!StringUtils.isEmpty(string)) {
                return string;
            }
        }
        MySharedPreferences mySharedPreferences = this.mMySP;
        return mySharedPreferences != null ? mySharedPreferences.getString(str, "") : "";
    }

    public int getInt(String str) {
        checkSDCardXMLFile();
        SharedPreferences sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(str, 0);
        }
        MySharedPreferences mySharedPreferences = this.mMySP;
        if (mySharedPreferences != null) {
            return mySharedPreferences.getInt(str, 0);
        }
        return 0;
    }

    public long getLong(String str) {
        checkSDCardXMLFile();
        SharedPreferences sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
            return sharedPreferences.getLong(str, 0L);
        }
        MySharedPreferences mySharedPreferences = this.mMySP;
        if (mySharedPreferences != null) {
            return mySharedPreferences.getLong(str, 0L);
        }
        return 0L;
    }

    public float getFloat(String str) {
        checkSDCardXMLFile();
        SharedPreferences sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
            return sharedPreferences.getFloat(str, 0.0f);
        }
        MySharedPreferences mySharedPreferences = this.mMySP;
        if (mySharedPreferences != null) {
            return mySharedPreferences.getFloat(str, 0.0f);
        }
        return 0.0f;
    }

    public boolean getBoolean(String str) {
        checkSDCardXMLFile();
        SharedPreferences sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(str, false);
        }
        MySharedPreferences mySharedPreferences = this.mMySP;
        if (mySharedPreferences != null) {
            return mySharedPreferences.getBoolean(str, false);
        }
        return false;
    }

    public Map<String, ?> getAll() {
        checkSDCardXMLFile();
        SharedPreferences sharedPreferences = this.mSp;
        if (sharedPreferences != null) {
            return sharedPreferences.getAll();
        }
        MySharedPreferences mySharedPreferences = this.mMySP;
        if (mySharedPreferences != null) {
            return mySharedPreferences.getAll();
        }
        return null;
    }
}
