package org.litepal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.io.File;
import org.litepal.parser.LitePalAttr;
import org.litepal.parser.LitePalParser;
import org.litepal.tablemanager.Connector;
import org.litepal.util.BaseUtility;
import org.litepal.util.Const;
import org.litepal.util.SharedUtil;
import org.litepal.util.cipher.CipherUtil;

/* JADX INFO: loaded from: classes2.dex */
public class LitePal {
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void initialize(Context context) {
        LitePalApplication.sContext = context;
    }

    public static SQLiteDatabase getDatabase() {
        return Connector.getDatabase();
    }

    public static Handler getHandler() {
        return handler;
    }

    public static void use(LitePalDB litePalDB) {
        LitePalAttr litePalAttr = LitePalAttr.getInstance();
        litePalAttr.setDbName(litePalDB.getDbName());
        litePalAttr.setVersion(litePalDB.getVersion());
        litePalAttr.setStorage(litePalDB.getStorage());
        litePalAttr.setClassNames(litePalDB.getClassNames());
        if (!isDefaultDatabase(litePalDB.getDbName())) {
            litePalAttr.setExtraKeyName(litePalDB.getDbName());
            litePalAttr.setCases(Const.Config.CASES_LOWER);
        }
        Connector.clearLitePalOpenHelperInstance();
    }

    public static void useDefault() {
        LitePalAttr.clearInstance();
        Connector.clearLitePalOpenHelperInstance();
    }

    public static boolean deleteDatabase(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (!str.endsWith(Const.Config.DB_NAME_SUFFIX)) {
            str = str + Const.Config.DB_NAME_SUFFIX;
        }
        File databasePath = LitePalApplication.getContext().getDatabasePath(str);
        if (databasePath.exists()) {
            boolean zDelete = databasePath.delete();
            if (zDelete) {
                removeVersionInSharedPreferences(str);
                Connector.clearLitePalOpenHelperInstance();
            }
            return zDelete;
        }
        boolean zDelete2 = new File((LitePalApplication.getContext().getExternalFilesDir("") + "/databases/") + str).delete();
        if (zDelete2) {
            removeVersionInSharedPreferences(str);
            Connector.clearLitePalOpenHelperInstance();
        }
        return zDelete2;
    }

    public static void aesKey(String str) {
        CipherUtil.aesKey = str;
    }

    private static void removeVersionInSharedPreferences(String str) {
        if (isDefaultDatabase(str)) {
            SharedUtil.removeVersion(null);
        } else {
            SharedUtil.removeVersion(str);
        }
    }

    private static boolean isDefaultDatabase(String str) {
        if (!BaseUtility.isLitePalXMLExists()) {
            return false;
        }
        if (!str.endsWith(Const.Config.DB_NAME_SUFFIX)) {
            str = str + Const.Config.DB_NAME_SUFFIX;
        }
        String dbName = LitePalParser.parseLitePalConfiguration().getDbName();
        if (!dbName.endsWith(Const.Config.DB_NAME_SUFFIX)) {
            dbName = dbName + Const.Config.DB_NAME_SUFFIX;
        }
        return str.equalsIgnoreCase(dbName);
    }
}
