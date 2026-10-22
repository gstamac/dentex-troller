package org.litepal.crud;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.litepal.annotation.Encrypt;
import org.litepal.exceptions.DataSupportException;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;

/* JADX INFO: loaded from: classes2.dex */
class UpdateHandler extends DataHandler {
    UpdateHandler(SQLiteDatabase sQLiteDatabase) {
        this.mDatabase = sQLiteDatabase;
    }

    int onUpdate(DataSupport dataSupport, long j) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        List<Field> supportedFields = getSupportedFields(dataSupport.getClassName());
        updateGenericTables(dataSupport, getSupportedGenericFields(dataSupport.getClassName()), j);
        ContentValues contentValues = new ContentValues();
        putFieldsValue(dataSupport, supportedFields, contentValues);
        putFieldsToDefaultValue(dataSupport, contentValues, j);
        if (contentValues.size() > 0) {
            return this.mDatabase.update(dataSupport.getTableName(), contentValues, "id = " + j, null);
        }
        return 0;
    }

    int onUpdate(Class<?> cls, long j, ContentValues contentValues) {
        if (contentValues.size() <= 0) {
            return 0;
        }
        convertContentValues(contentValues);
        return this.mDatabase.update(getTableName(cls), contentValues, "id = " + j, null);
    }

    /* JADX WARN: Code duplicated, block: B:15:0x005e  */
    int onUpdateAll(DataSupport dataSupport, String... strArr) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        long[] jArr;
        BaseUtility.checkConditionsCorrect(strArr);
        if (strArr != null && strArr.length > 0) {
            strArr[0] = DBUtility.convertWhereClauseToColumnName(strArr[0]);
        }
        List<Field> supportedFields = getSupportedFields(dataSupport.getClassName());
        List<Field> supportedGenericFields = getSupportedGenericFields(dataSupport.getClassName());
        if (supportedGenericFields.isEmpty()) {
            jArr = null;
        } else {
            List listFind = DataSupport.select("id").where(strArr).find(dataSupport.getClass());
            if (listFind.size() > 0) {
                int size = listFind.size();
                jArr = new long[size];
                for (int i = 0; i < size; i++) {
                    jArr[i] = ((DataSupport) listFind.get(i)).getBaseObjId();
                }
                updateGenericTables(dataSupport, supportedGenericFields, jArr);
            } else {
                jArr = null;
            }
        }
        ContentValues contentValues = new ContentValues();
        putFieldsValue(dataSupport, supportedFields, contentValues);
        putFieldsToDefaultValue(dataSupport, contentValues, jArr);
        return doUpdateAllAction(dataSupport.getTableName(), contentValues, strArr);
    }

    int onUpdateAll(String str, ContentValues contentValues, String... strArr) {
        BaseUtility.checkConditionsCorrect(strArr);
        if (strArr != null && strArr.length > 0) {
            strArr[0] = DBUtility.convertWhereClauseToColumnName(strArr[0]);
        }
        convertContentValues(contentValues);
        return doUpdateAllAction(str, contentValues, strArr);
    }

    private int doUpdateAllAction(String str, ContentValues contentValues, String... strArr) {
        BaseUtility.checkConditionsCorrect(strArr);
        if (contentValues.size() > 0) {
            return this.mDatabase.update(str, contentValues, getWhereClause(strArr), getWhereArgs(strArr));
        }
        return 0;
    }

    private void putFieldsToDefaultValue(DataSupport dataSupport, ContentValues contentValues, long... jArr) {
        String str = null;
        try {
            try {
                DataSupport emptyModel = getEmptyModel(dataSupport);
                Class<?> cls = emptyModel.getClass();
                String str2 = null;
                for (String str3 : dataSupport.getFieldsToSetToDefault()) {
                    try {
                        if (!isIdColumn(str3)) {
                            try {
                                Field declaredField = cls.getDeclaredField(str3);
                                if (isCollection(declaredField.getType())) {
                                    if (jArr != null && jArr.length > 0 && BaseUtility.isGenericTypeSupported(getGenericTypeName(declaredField))) {
                                        String genericTableName = DBUtility.getGenericTableName(dataSupport.getClassName(), declaredField.getName());
                                        String genericValueIdColumnName = DBUtility.getGenericValueIdColumnName(dataSupport.getClassName());
                                        StringBuilder sb = new StringBuilder();
                                        int length = jArr.length;
                                        int i = 0;
                                        boolean z = false;
                                        while (i < length) {
                                            long j = jArr[i];
                                            if (z) {
                                                sb.append(" or ");
                                            }
                                            sb.append(genericValueIdColumnName).append(" = ").append(j);
                                            i++;
                                            z = true;
                                        }
                                        this.mDatabase.delete(genericTableName, sb.toString(), null);
                                    }
                                } else {
                                    putContentValuesForUpdate(emptyModel, declaredField, contentValues);
                                }
                                str2 = str3;
                            } catch (NoSuchFieldException e) {
                                e = e;
                                str = str3;
                                throw new DataSupportException(DataSupportException.noSuchFieldExceptioin(dataSupport.getClassName(), str), e);
                            }
                        }
                    } catch (NoSuchFieldException e2) {
                        e = e2;
                        str = str2;
                    }
                }
            } catch (Exception e3) {
                throw new DataSupportException(e3.getMessage(), e3);
            }
        } catch (NoSuchFieldException e4) {
            e = e4;
        }
    }

    private int doUpdateAssociations(DataSupport dataSupport, long j, ContentValues contentValues) {
        analyzeAssociations(dataSupport);
        updateSelfTableForeignKey(dataSupport, contentValues);
        return updateAssociatedTableForeignKey(dataSupport, j) + 0;
    }

    private void analyzeAssociations(DataSupport dataSupport) {
        try {
            analyzeAssociatedModels(dataSupport, getAssociationInfo(dataSupport.getClassName()));
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage(), e);
        }
    }

    private void updateSelfTableForeignKey(DataSupport dataSupport, ContentValues contentValues) {
        Map<String, Long> associatedModelsMapWithoutFK = dataSupport.getAssociatedModelsMapWithoutFK();
        for (String str : associatedModelsMapWithoutFK.keySet()) {
            contentValues.put(getForeignKeyColumnName(str), associatedModelsMapWithoutFK.get(str));
        }
    }

    private int updateAssociatedTableForeignKey(DataSupport dataSupport, long j) {
        Map<String, Set<Long>> associatedModelsMapWithFK = dataSupport.getAssociatedModelsMapWithFK();
        ContentValues contentValues = new ContentValues();
        for (String str : associatedModelsMapWithFK.keySet()) {
            contentValues.clear();
            contentValues.put(getForeignKeyColumnName(dataSupport.getTableName()), Long.valueOf(j));
            Set<Long> set = associatedModelsMapWithFK.get(str);
            if (set != null && !set.isEmpty()) {
                return this.mDatabase.update(str, contentValues, getWhereOfIdsWithOr(set), null);
            }
        }
        return 0;
    }

    private void updateGenericTables(DataSupport dataSupport, List<Field> list, long... jArr) throws IllegalAccessException, InvocationTargetException {
        long[] jArr2 = jArr;
        if (jArr2 == null || jArr2.length <= 0) {
            return;
        }
        Iterator<Field> it = list.iterator();
        while (it.hasNext()) {
            Field next = it.next();
            Encrypt encrypt = (Encrypt) next.getAnnotation(Encrypt.class);
            String strAlgorithm = (encrypt == null || !"java.lang.String".equals(getGenericTypeName(next))) ? null : encrypt.algorithm();
            next.setAccessible(true);
            Collection collection = (Collection) next.get(dataSupport);
            if (collection != null && !collection.isEmpty()) {
                String genericTableName = DBUtility.getGenericTableName(dataSupport.getClassName(), next.getName());
                String genericValueIdColumnName = DBUtility.getGenericValueIdColumnName(dataSupport.getClassName());
                int length = jArr2.length;
                int i = 0;
                while (i < length) {
                    long j = jArr2[i];
                    this.mDatabase.delete(genericTableName, genericValueIdColumnName + " = ?", new String[]{String.valueOf(j)});
                    for (Object obj : collection) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(genericValueIdColumnName, Long.valueOf(j));
                        DynamicExecutor.send(contentValues, "put", new Object[]{DBUtility.convertToValidColumnName(BaseUtility.changeCase(next.getName())), encryptValue(strAlgorithm, obj)}, contentValues.getClass(), new Class[]{String.class, getGenericTypeClass(next)});
                        this.mDatabase.insert(genericTableName, null, contentValues);
                        it = it;
                        next = next;
                    }
                    i++;
                    jArr2 = jArr;
                    it = it;
                }
            }
            jArr2 = jArr;
            it = it;
        }
    }

    private void convertContentValues(ContentValues contentValues) {
        HashMap map = new HashMap();
        for (String str : contentValues.keySet()) {
            if (DBUtility.isFieldNameConflictWithSQLiteKeywords(str)) {
                map.put(str, contentValues.get(str));
            }
        }
        for (String str2 : map.keySet()) {
            String strConvertToValidColumnName = DBUtility.convertToValidColumnName(str2);
            Object obj = contentValues.get(str2);
            contentValues.remove(str2);
            if (obj == null) {
                contentValues.putNull(strConvertToValidColumnName);
            } else {
                String name = obj.getClass().getName();
                if ("java.lang.Byte".equals(name)) {
                    contentValues.put(strConvertToValidColumnName, (Byte) obj);
                } else if ("[B".equals(name)) {
                    contentValues.put(strConvertToValidColumnName, (byte[]) obj);
                } else if ("java.lang.Boolean".equals(name)) {
                    contentValues.put(strConvertToValidColumnName, (Boolean) obj);
                } else if ("java.lang.String".equals(name)) {
                    contentValues.put(strConvertToValidColumnName, (String) obj);
                } else if ("java.lang.Float".equals(name)) {
                    contentValues.put(strConvertToValidColumnName, (Float) obj);
                } else if ("java.lang.Long".equals(name)) {
                    contentValues.put(strConvertToValidColumnName, (Long) obj);
                } else if ("java.lang.Integer".equals(name)) {
                    contentValues.put(strConvertToValidColumnName, (Integer) obj);
                } else if ("java.lang.Short".equals(name)) {
                    contentValues.put(strConvertToValidColumnName, (Short) obj);
                } else if ("java.lang.Double".equals(name)) {
                    contentValues.put(strConvertToValidColumnName, (Double) obj);
                }
            }
        }
    }
}
