package org.litepal.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.litepal.exceptions.DatabaseGenerateException;
import org.litepal.tablemanager.model.ColumnModel;
import org.litepal.tablemanager.model.TableModel;

/* JADX INFO: loaded from: classes2.dex */
public class DBUtility {
    private static final String KEYWORDS_COLUMN_SUFFIX = "_lpcolumn";
    private static final String REG_COLLECTION = "\\s+(not\\s+)?(in|exists)\\s*\\(";
    private static final String REG_FUZZY = "\\s+(not\\s+)?(like|between)\\s+";
    private static final String REG_OPERATOR = "\\s*(=|!=|<>|<|>)";
    private static final String SQLITE_KEYWORDS = ",abort,add,after,all,alter,and,as,asc,autoincrement,before,begin,between,by,cascade,check,collate,column,commit,conflict,constraint,create,cross,database,deferrable,deferred,delete,desc,distinct,drop,each,end,escape,except,exclusive,exists,foreign,from,glob,group,having,in,index,inner,insert,intersect,into,is,isnull,join,like,limit,match,natural,not,notnull,null,of,offset,on,or,order,outer,plan,pragma,primary,query,raise,references,regexp,reindex,release,rename,replace,restrict,right,rollback,row,savepoint,select,set,table,temp,temporary,then,to,transaction,trigger,union,unique,update,using,vacuum,values,view,virtual,when,where,";
    private static final String TAG = "DBUtility";

    private DBUtility() {
    }

    public static String getTableNameByClassName(String str) {
        if (TextUtils.isEmpty(str) || '.' == str.charAt(str.length() - 1)) {
            return null;
        }
        return str.substring(str.lastIndexOf(".") + 1);
    }

    public static List<String> getTableNameListByClassNameList(List<String> list) {
        ArrayList arrayList = new ArrayList();
        if (list != null && !list.isEmpty()) {
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(getTableNameByClassName(it.next()));
            }
        }
        return arrayList;
    }

    public static String getTableNameByForeignColumn(String str) {
        if (TextUtils.isEmpty(str) || !str.toLowerCase(Locale.US).endsWith("_id")) {
            return null;
        }
        return str.substring(0, str.length() - 3);
    }

    public static String getIntermediateTableName(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        if (str.toLowerCase(Locale.US).compareTo(str2.toLowerCase(Locale.US)) <= 0) {
            return str + "_" + str2;
        }
        return str2 + "_" + str;
    }

    public static String getGenericTableName(String str, String str2) {
        return BaseUtility.changeCase(getTableNameByClassName(str) + "_" + str2);
    }

    public static String getGenericValueIdColumnName(String str) {
        return BaseUtility.changeCase(getTableNameByClassName(str) + "_id");
    }

    public static boolean isIntermediateTable(String str, SQLiteDatabase sQLiteDatabase) {
        if (TextUtils.isEmpty(str) || !str.matches("[0-9a-zA-Z]+_[0-9a-zA-Z]+")) {
            return false;
        }
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = sQLiteDatabase.query(Const.TableSchema.TABLE_NAME, null, null, null, null, null, null);
                if (cursorQuery.moveToFirst()) {
                    do {
                        if (str.equalsIgnoreCase(cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("name")))) {
                            if (cursorQuery.getInt(cursorQuery.getColumnIndexOrThrow("type")) != 1) {
                                break;
                            }
                            if (cursorQuery != null) {
                                cursorQuery.close();
                            }
                            return true;
                        }
                    } while (cursorQuery.moveToNext());
                }
                if (cursorQuery == null) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (cursorQuery == null) {
                    return false;
                }
            }
            return false;
        } finally {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        }
    }

    public static boolean isGenericTable(String str, SQLiteDatabase sQLiteDatabase) {
        if (TextUtils.isEmpty(str) || !str.matches("[0-9a-zA-Z]+_[0-9a-zA-Z]+")) {
            return false;
        }
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = sQLiteDatabase.query(Const.TableSchema.TABLE_NAME, null, null, null, null, null, null);
                if (cursorQuery.moveToFirst()) {
                    do {
                        if (str.equalsIgnoreCase(cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("name")))) {
                            if (cursorQuery.getInt(cursorQuery.getColumnIndexOrThrow("type")) != 2) {
                                break;
                            }
                            if (cursorQuery == null) {
                                return true;
                            }
                            cursorQuery.close();
                            return true;
                        }
                    } while (cursorQuery.moveToNext());
                }
                if (cursorQuery == null) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (cursorQuery == null) {
                    return false;
                }
            }
            return false;
        } finally {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        }
    }

    public static boolean isTableExists(String str, SQLiteDatabase sQLiteDatabase) {
        try {
            return BaseUtility.containsIgnoreCases(findAllTableNames(sQLiteDatabase), str);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isColumnExists(String str, String str2, SQLiteDatabase sQLiteDatabase) {
        boolean z = false;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        Cursor cursorRawQuery = null;
        try {
            try {
                cursorRawQuery = sQLiteDatabase.rawQuery("pragma table_info(" + str2 + ")", null);
                if (cursorRawQuery.moveToFirst()) {
                    do {
                        if (str.equalsIgnoreCase(cursorRawQuery.getString(cursorRawQuery.getColumnIndexOrThrow("name")))) {
                            z = true;
                            break;
                        }
                    } while (cursorRawQuery.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return z;
        } finally {
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
        }
    }

    public static List<String> findAllTableNames(SQLiteDatabase sQLiteDatabase) {
        ArrayList arrayList = new ArrayList();
        Cursor cursorRawQuery = null;
        try {
            try {
                cursorRawQuery = sQLiteDatabase.rawQuery("select * from sqlite_master where type = ?", new String[]{"table"});
                if (cursorRawQuery.moveToFirst()) {
                    do {
                        String string = cursorRawQuery.getString(cursorRawQuery.getColumnIndexOrThrow("tbl_name"));
                        if (!arrayList.contains(string)) {
                            arrayList.add(string);
                        }
                    } while (cursorRawQuery.moveToNext());
                }
                if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
                return arrayList;
            } catch (Exception e) {
                e.printStackTrace();
                throw new DatabaseGenerateException(e.getMessage());
            }
        } catch (Throwable th) {
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
            throw th;
        }
    }

    public static TableModel findPragmaTableInfo(String str, SQLiteDatabase sQLiteDatabase) throws Throwable {
        if (isTableExists(str, sQLiteDatabase)) {
            List<String> listFindUniqueColumns = findUniqueColumns(str, sQLiteDatabase);
            TableModel tableModel = new TableModel();
            tableModel.setTableName(str);
            Cursor cursorRawQuery = null;
            try {
                try {
                    cursorRawQuery = sQLiteDatabase.rawQuery("pragma table_info(" + str + ")", null);
                    if (cursorRawQuery.moveToFirst()) {
                        do {
                            ColumnModel columnModel = new ColumnModel();
                            String string = cursorRawQuery.getString(cursorRawQuery.getColumnIndexOrThrow("name"));
                            String string2 = cursorRawQuery.getString(cursorRawQuery.getColumnIndexOrThrow("type"));
                            boolean z = true;
                            if (cursorRawQuery.getInt(cursorRawQuery.getColumnIndexOrThrow("notnull")) == 1) {
                                z = false;
                            }
                            boolean zContains = listFindUniqueColumns.contains(string);
                            String string3 = cursorRawQuery.getString(cursorRawQuery.getColumnIndexOrThrow("dflt_value"));
                            columnModel.setColumnName(string);
                            columnModel.setColumnType(string2);
                            columnModel.setNullable(z);
                            columnModel.setUnique(zContains);
                            columnModel.setDefaultValue(string3 != null ? string3.replace("'", "") : "");
                            tableModel.addColumnModel(columnModel);
                        } while (cursorRawQuery.moveToNext());
                    }
                    if (cursorRawQuery != null) {
                        cursorRawQuery.close();
                    }
                    return tableModel;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DatabaseGenerateException(e.getMessage());
                }
            } catch (Throwable th) {
                if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
                throw th;
            }
        }
        throw new DatabaseGenerateException(DatabaseGenerateException.TABLE_DOES_NOT_EXIST_WHEN_EXECUTING + str);
    }

    /* JADX WARN: Code duplicated, block: B:38:0x009d  */
    /* JADX WARN: Code duplicated, block: B:40:0x00a2  */
    public static List<String> findUniqueColumns(String str, SQLiteDatabase sQLiteDatabase) throws Throwable {
        Cursor cursorRawQuery;
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            Cursor cursorRawQuery2 = sQLiteDatabase.rawQuery("pragma index_list(" + str + ")", null);
            try {
                if (cursorRawQuery2.moveToFirst()) {
                    cursorRawQuery = null;
                    do {
                        try {
                            if (cursorRawQuery2.getInt(cursorRawQuery2.getColumnIndexOrThrow("unique")) == 1) {
                                cursorRawQuery = sQLiteDatabase.rawQuery("pragma index_info(" + cursorRawQuery2.getString(cursorRawQuery2.getColumnIndexOrThrow("name")) + ")", null);
                                if (cursorRawQuery.moveToFirst()) {
                                    arrayList.add(cursorRawQuery.getString(cursorRawQuery.getColumnIndexOrThrow("name")));
                                }
                            }
                        } catch (Exception e) {
                            e = e;
                            cursor = cursorRawQuery2;
                            try {
                                e.printStackTrace();
                                throw new DatabaseGenerateException(e.getMessage());
                            } catch (Throwable th) {
                                th = th;
                                if (cursor != null) {
                                    cursor.close();
                                }
                                if (cursorRawQuery != null) {
                                    cursorRawQuery.close();
                                }
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            cursor = cursorRawQuery2;
                            if (cursor != null) {
                                cursor.close();
                            }
                            if (cursorRawQuery != null) {
                                cursorRawQuery.close();
                            }
                            throw th;
                        }
                    } while (cursorRawQuery2.moveToNext());
                    cursor = cursorRawQuery;
                }
                if (cursorRawQuery2 != null) {
                    cursorRawQuery2.close();
                }
                if (cursor != null) {
                    cursor.close();
                }
                return arrayList;
            } catch (Exception e2) {
                e = e2;
                cursorRawQuery = null;
            } catch (Throwable th3) {
                th = th3;
                cursorRawQuery = null;
            }
        } catch (Exception e3) {
            e = e3;
            cursorRawQuery = null;
        } catch (Throwable th4) {
            th = th4;
            cursorRawQuery = null;
        }
    }

    public static boolean isFieldNameConflictWithSQLiteKeywords(String str) {
        return !TextUtils.isEmpty(str) && SQLITE_KEYWORDS.contains(new StringBuilder(",").append(str.toLowerCase(Locale.US)).append(",").toString());
    }

    public static String convertToValidColumnName(String str) {
        return isFieldNameConflictWithSQLiteKeywords(str) ? str + KEYWORDS_COLUMN_SUFFIX : str;
    }

    public static String convertWhereClauseToColumnName(String str) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            Matcher matcher = Pattern.compile("(\\w+\\s*(=|!=|<>|<|>)|\\w+\\s+(not\\s+)?(like|between)\\s+|\\w+\\s+(not\\s+)?(in|exists)\\s*\\()").matcher(str);
            while (matcher.find()) {
                String strGroup = matcher.group();
                String strReplaceAll = strGroup.replaceAll("(\\s*(=|!=|<>|<|>)|\\s+(not\\s+)?(like|between)\\s+|\\s+(not\\s+)?(in|exists)\\s*\\()", "");
                matcher.appendReplacement(stringBuffer, convertToValidColumnName(strReplaceAll) + strGroup.replace(strReplaceAll, ""));
            }
            matcher.appendTail(stringBuffer);
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String[] convertSelectClauseToValidNames(String[] strArr) {
        if (strArr == null || strArr.length <= 0) {
            return null;
        }
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr2[i] = convertToValidColumnName(strArr[i]);
        }
        return strArr2;
    }

    public static String convertOrderByClauseToValidName(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String lowerCase = str.trim().toLowerCase(Locale.US);
        if (lowerCase.contains(",")) {
            String[] strArrSplit = lowerCase.split(",");
            StringBuilder sb = new StringBuilder();
            int length = strArrSplit.length;
            int i = 0;
            boolean z = false;
            while (i < length) {
                String str2 = strArrSplit[i];
                if (z) {
                    sb.append(",");
                }
                sb.append(convertOrderByItem(str2));
                i++;
                z = true;
            }
            return sb.toString();
        }
        return convertOrderByItem(lowerCase);
    }

    private static String convertOrderByItem(String str) {
        String str2 = "";
        if (str.endsWith("asc")) {
            str = str.replace("asc", "").trim();
            str2 = " asc";
        } else if (str.endsWith("desc")) {
            str = str.replace("desc", "").trim();
            str2 = " desc";
        }
        return convertToValidColumnName(str) + str2;
    }
}
