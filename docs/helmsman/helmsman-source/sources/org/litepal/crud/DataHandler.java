package org.litepal.crud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.facebook.appevents.AppEventsConstants;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.litepal.LitePalBase;
import org.litepal.annotation.Encrypt;
import org.litepal.crud.model.AssociationsInfo;
import org.litepal.exceptions.DataSupportException;
import org.litepal.exceptions.DatabaseGenerateException;
import org.litepal.tablemanager.model.GenericModel;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;
import org.litepal.util.cipher.CipherUtil;

/* JADX INFO: loaded from: classes2.dex */
abstract class DataHandler extends LitePalBase {
    public static final String TAG = "DataHandler";
    private List<AssociationsInfo> fkInCurrentModel;
    private List<AssociationsInfo> fkInOtherModel;
    SQLiteDatabase mDatabase;
    private DataSupport tempEmptyModel;

    protected boolean shouldGetOrSet(DataSupport dataSupport, Field field) {
        return (dataSupport == null || field == null) ? false : true;
    }

    DataHandler() {
    }

    protected <T> List<T> query(Class<T> cls, String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, String str5, List<AssociationsInfo> list) {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = null;
        try {
            try {
                List<Field> supportedFields = getSupportedFields(cls.getName());
                List<Field> supportedGenericFields = getSupportedGenericFields(cls.getName());
                String[] strArrConvertSelectClauseToValidNames = DBUtility.convertSelectClauseToValidNames(getCustomizedColumns(strArr, supportedGenericFields, list));
                cursorQuery = this.mDatabase.query(getTableName(cls), strArrConvertSelectClauseToValidNames, str, strArr2, str2, str3, str4, str5);
                if (cursorQuery.moveToFirst()) {
                    SparseArray<QueryInfoCache> sparseArray = new SparseArray<>();
                    HashMap map = new HashMap();
                    do {
                        Object objCreateInstanceFromClass = createInstanceFromClass(cls);
                        giveBaseObjIdValue((DataSupport) objCreateInstanceFromClass, cursorQuery.getLong(cursorQuery.getColumnIndexOrThrow("id")));
                        setValueToModel(objCreateInstanceFromClass, supportedFields, list, cursorQuery, sparseArray);
                        setGenericValueToModel((DataSupport) objCreateInstanceFromClass, supportedGenericFields, map);
                        if (list != null) {
                            setAssociatedModel((DataSupport) objCreateInstanceFromClass);
                        }
                        arrayList.add(objCreateInstanceFromClass);
                    } while (cursorQuery.moveToNext());
                    sparseArray.clear();
                    map.clear();
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                return arrayList;
            } catch (Exception e) {
                throw new DataSupportException(e.getMessage(), e);
            }
        } catch (Throwable th) {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            throw th;
        }
    }

    protected <T> T mathQuery(String str, String[] strArr, String[] strArr2, Class<T> cls) throws Throwable {
        BaseUtility.checkConditionsCorrect(strArr2);
        Cursor cursor = (T) null;
        try {
            try {
                Cursor cursorQuery = this.mDatabase.query(str, strArr, getWhereClause(strArr2), getWhereArgs(strArr2), null, null, null);
                try {
                    if (cursorQuery.moveToFirst()) {
                        cursor = (T) cursorQuery.getClass().getMethod(genGetColumnMethod((Class<?>) cls), Integer.TYPE).invoke(cursorQuery, 0);
                    }
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    return (T) cursor;
                } catch (Exception e) {
                    e = e;
                    cursor = (T) cursorQuery;
                    throw new DataSupportException(e.getMessage(), e);
                } catch (Throwable th) {
                    th = th;
                    cursor = cursorQuery;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    protected void giveBaseObjIdValue(DataSupport dataSupport, long j) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        if (j > 0) {
            DynamicExecutor.set(dataSupport, "baseObjId", Long.valueOf(j), DataSupport.class);
        }
    }

    protected void putFieldsValue(DataSupport dataSupport, List<Field> list, ContentValues contentValues) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        for (Field field : list) {
            if (!isIdColumn(field.getName())) {
                putFieldsValueDependsOnSaveOrUpdate(dataSupport, field, contentValues);
            }
        }
    }

    protected void putContentValuesForSave(DataSupport dataSupport, Field field, ContentValues contentValues) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Object field2 = DynamicExecutor.getField(dataSupport, field.getName(), dataSupport.getClass());
        if (field2 != null) {
            if ("java.util.Date".equals(field.getType().getName())) {
                field2 = Long.valueOf(((Date) field2).getTime());
            }
            Encrypt encrypt = (Encrypt) field.getAnnotation(Encrypt.class);
            if (encrypt != null && "java.lang.String".equals(field.getType().getName())) {
                field2 = encryptValue(encrypt.algorithm(), field2);
            }
            Object[] objArr = {BaseUtility.changeCase(DBUtility.convertToValidColumnName(field.getName())), field2};
            DynamicExecutor.send(contentValues, "put", objArr, contentValues.getClass(), getParameterTypes(field, field2, objArr));
        }
    }

    protected void putContentValuesForUpdate(DataSupport dataSupport, Field field, ContentValues contentValues) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Object fieldValue = getFieldValue(dataSupport, field);
        if ("java.util.Date".equals(field.getType().getName()) && fieldValue != null) {
            fieldValue = Long.valueOf(((Date) fieldValue).getTime());
        }
        Encrypt encrypt = (Encrypt) field.getAnnotation(Encrypt.class);
        if (encrypt != null && "java.lang.String".equals(field.getType().getName())) {
            fieldValue = encryptValue(encrypt.algorithm(), fieldValue);
        }
        Object[] objArr = {BaseUtility.changeCase(DBUtility.convertToValidColumnName(field.getName())), fieldValue};
        DynamicExecutor.send(contentValues, "put", objArr, contentValues.getClass(), getParameterTypes(field, fieldValue, objArr));
    }

    protected Object encryptValue(String str, Object obj) {
        if (str == null || obj == null) {
            return obj;
        }
        if ("AES".equalsIgnoreCase(str)) {
            return CipherUtil.aesEncrypt((String) obj);
        }
        return "MD5".equalsIgnoreCase(str) ? CipherUtil.md5Encrypt((String) obj) : obj;
    }

    protected Object getFieldValue(DataSupport dataSupport, Field field) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (shouldGetOrSet(dataSupport, field)) {
            return DynamicExecutor.getField(dataSupport, field.getName(), dataSupport.getClass());
        }
        return null;
    }

    protected void setFieldValue(DataSupport dataSupport, Field field, Object obj) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (shouldGetOrSet(dataSupport, field)) {
            DynamicExecutor.setField(dataSupport, field.getName(), obj, dataSupport.getClass());
        }
    }

    protected void analyzeAssociatedModels(DataSupport dataSupport, Collection<AssociationsInfo> collection) {
        try {
            for (AssociationsInfo associationsInfo : collection) {
                if (associationsInfo.getAssociationType() == 2) {
                    new Many2OneAnalyzer().analyze(dataSupport, associationsInfo);
                } else if (associationsInfo.getAssociationType() == 1) {
                    new One2OneAnalyzer().analyze(dataSupport, associationsInfo);
                } else if (associationsInfo.getAssociationType() == 3) {
                    new Many2ManyAnalyzer().analyze(dataSupport, associationsInfo);
                }
            }
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage(), e);
        }
    }

    protected DataSupport getAssociatedModel(DataSupport dataSupport, AssociationsInfo associationsInfo) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        return (DataSupport) getFieldValue(dataSupport, associationsInfo.getAssociateOtherModelFromSelf());
    }

    protected Collection<DataSupport> getAssociatedModels(DataSupport dataSupport, AssociationsInfo associationsInfo) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        return (Collection) getFieldValue(dataSupport, associationsInfo.getAssociateOtherModelFromSelf());
    }

    protected DataSupport getEmptyModel(DataSupport dataSupport) {
        DataSupport dataSupport2 = this.tempEmptyModel;
        if (dataSupport2 != null) {
            return dataSupport2;
        }
        String className = null;
        try {
            className = dataSupport.getClassName();
            DataSupport dataSupport3 = (DataSupport) Class.forName(className).newInstance();
            this.tempEmptyModel = dataSupport3;
            return dataSupport3;
        } catch (ClassNotFoundException unused) {
            throw new DatabaseGenerateException(DatabaseGenerateException.CLASS_NOT_FOUND + className);
        } catch (InstantiationException e) {
            throw new DataSupportException(className + DataSupportException.INSTANTIATION_EXCEPTION, e);
        } catch (Exception e2) {
            throw new DataSupportException(e2.getMessage(), e2);
        }
    }

    protected String getWhereClause(String... strArr) {
        if (isAffectAllLines(strArr) || strArr == null || strArr.length <= 0) {
            return null;
        }
        return strArr[0];
    }

    protected String[] getWhereArgs(String... strArr) {
        if (isAffectAllLines(strArr) || strArr == null || strArr.length <= 1) {
            return null;
        }
        String[] strArr2 = new String[strArr.length - 1];
        System.arraycopy(strArr, 1, strArr2, 0, strArr.length - 1);
        return strArr2;
    }

    protected boolean isAffectAllLines(Object... objArr) {
        return objArr != null && objArr.length == 0;
    }

    protected String getWhereOfIdsWithOr(Collection<Long> collection) {
        StringBuilder sb = new StringBuilder();
        Iterator<Long> it = collection.iterator();
        boolean z = false;
        while (it.hasNext()) {
            long jLongValue = it.next().longValue();
            if (z) {
                sb.append(" or ");
            }
            sb.append("id = ");
            sb.append(jLongValue);
            z = true;
        }
        return BaseUtility.changeCase(sb.toString());
    }

    protected String getWhereOfIdsWithOr(long... jArr) {
        StringBuilder sb = new StringBuilder();
        int length = jArr.length;
        int i = 0;
        boolean z = false;
        while (i < length) {
            long j = jArr[i];
            if (z) {
                sb.append(" or ");
            }
            sb.append("id = ");
            sb.append(j);
            i++;
            z = true;
        }
        return BaseUtility.changeCase(sb.toString());
    }

    protected String getIntermediateTableName(DataSupport dataSupport, String str) {
        return BaseUtility.changeCase(DBUtility.getIntermediateTableName(dataSupport.getTableName(), str));
    }

    protected String getTableName(Class<?> cls) {
        return BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName()));
    }

    protected Object createInstanceFromClass(Class<?> cls) {
        try {
            Constructor<?> constructorFindBestSuitConstructor = findBestSuitConstructor(cls);
            return constructorFindBestSuitConstructor.newInstance(getConstructorParams(cls, constructorFindBestSuitConstructor));
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage(), e);
        }
    }

    protected Constructor<?> findBestSuitConstructor(Class<?> cls) {
        Constructor<?>[] declaredConstructors = cls.getDeclaredConstructors();
        SparseArray sparseArray = new SparseArray();
        int i = Integer.MAX_VALUE;
        for (Constructor<?> constructor : declaredConstructors) {
            int length = constructor.getParameterTypes().length;
            for (Class<?> cls2 : constructor.getParameterTypes()) {
                if (cls2 == cls || cls2.getName().equals("com.android.tools.fd.runtime.InstantReloadException")) {
                    length += 10000;
                }
            }
            if (sparseArray.get(length) == null) {
                sparseArray.put(length, constructor);
            }
            if (length < i) {
                i = length;
            }
        }
        Constructor<?> constructor2 = (Constructor) sparseArray.get(i);
        if (constructor2 != null) {
            constructor2.setAccessible(true);
        }
        return constructor2;
    }

    protected Object[] getConstructorParams(Class<?> cls, Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] objArr = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            objArr[i] = getInitParamValue(cls, parameterTypes[i]);
        }
        return objArr;
    }

    protected void setValueToModel(Object obj, List<Field> list, List<AssociationsInfo> list2, Cursor cursor, SparseArray<QueryInfoCache> sparseArray) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        int size = sparseArray.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                int iKeyAt = sparseArray.keyAt(i);
                QueryInfoCache queryInfoCache = sparseArray.get(iKeyAt);
                setToModelByReflection(obj, queryInfoCache.field, iKeyAt, queryInfoCache.getMethodName, cursor);
            }
        } else {
            for (Field field : list) {
                String strGenGetColumnMethod = genGetColumnMethod(field);
                int columnIndex = cursor.getColumnIndex(BaseUtility.changeCase(isIdColumn(field.getName()) ? "id" : DBUtility.convertToValidColumnName(field.getName())));
                if (columnIndex != -1) {
                    setToModelByReflection(obj, field, columnIndex, strGenGetColumnMethod, cursor);
                    QueryInfoCache queryInfoCache2 = new QueryInfoCache();
                    queryInfoCache2.getMethodName = strGenGetColumnMethod;
                    queryInfoCache2.field = field;
                    sparseArray.put(columnIndex, queryInfoCache2);
                }
            }
        }
        if (list2 != null) {
            for (AssociationsInfo associationsInfo : list2) {
                int columnIndex2 = cursor.getColumnIndex(getForeignKeyColumnName(DBUtility.getTableNameByClassName(associationsInfo.getAssociatedClassName())));
                if (columnIndex2 != -1) {
                    try {
                        DataSupport dataSupport = (DataSupport) DataSupport.find(Class.forName(associationsInfo.getAssociatedClassName()), cursor.getLong(columnIndex2));
                        if (dataSupport != null) {
                            setFieldValue((DataSupport) obj, associationsInfo.getAssociateOtherModelFromSelf(), dataSupport);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void setGenericValueToModel(DataSupport dataSupport, List<Field> list, Map<Field, GenericModel> map) throws Throwable {
        String getMethodName;
        String str;
        String str2;
        String genericValueIdColumnName;
        for (Field field : list) {
            GenericModel genericModel = map.get(field);
            if (genericModel == null) {
                String genericTableName = DBUtility.getGenericTableName(dataSupport.getClassName(), field.getName());
                String strConvertToValidColumnName = DBUtility.convertToValidColumnName(field.getName());
                genericValueIdColumnName = DBUtility.getGenericValueIdColumnName(dataSupport.getClassName());
                String strGenGetColumnMethod = genGetColumnMethod(field);
                GenericModel genericModel2 = new GenericModel();
                genericModel2.setTableName(genericTableName);
                genericModel2.setValueColumnName(strConvertToValidColumnName);
                genericModel2.setValueIdColumnName(genericValueIdColumnName);
                genericModel2.setGetMethodName(strGenGetColumnMethod);
                map.put(field, genericModel2);
                str = genericTableName;
                str2 = strConvertToValidColumnName;
                getMethodName = strGenGetColumnMethod;
            } else {
                String tableName = genericModel.getTableName();
                String valueColumnName = genericModel.getValueColumnName();
                String valueIdColumnName = genericModel.getValueIdColumnName();
                getMethodName = genericModel.getGetMethodName();
                str = tableName;
                str2 = valueColumnName;
                genericValueIdColumnName = valueIdColumnName;
            }
            Cursor cursor = null;
            try {
                Cursor cursorQuery = this.mDatabase.query(str, null, genericValueIdColumnName + " = ?", new String[]{String.valueOf(dataSupport.getBaseObjId())}, null, null, null);
                try {
                    if (cursorQuery.moveToFirst()) {
                        do {
                            int columnIndex = cursorQuery.getColumnIndex(BaseUtility.changeCase(str2));
                            if (columnIndex != -1) {
                                setToModelByReflection(dataSupport, field, columnIndex, getMethodName, cursorQuery);
                            }
                        } while (cursorQuery.moveToNext());
                    }
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = cursorQuery;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    protected List<AssociationsInfo> getForeignKeyAssociations(String str, boolean z) {
        if (!z) {
            return null;
        }
        analyzeAssociations(str);
        return this.fkInCurrentModel;
    }

    protected Class<?>[] getParameterTypes(Field field, Object obj, Object[] objArr) {
        Class<?>[] clsArr;
        if (isCharType(field)) {
            objArr[1] = String.valueOf(obj);
            return new Class[]{String.class, String.class};
        }
        if (field.getType().isPrimitive()) {
            clsArr = new Class[]{String.class, getObjectType(field.getType())};
        } else {
            if ("java.util.Date".equals(field.getType().getName())) {
                return new Class[]{String.class, Long.class};
            }
            clsArr = new Class[]{String.class, field.getType()};
        }
        return clsArr;
    }

    private Class<?> getObjectType(Class<?> cls) {
        if (cls == null || !cls.isPrimitive()) {
            return null;
        }
        String name = cls.getName();
        if ("int".equals(name)) {
            return Integer.class;
        }
        if ("short".equals(name)) {
            return Short.class;
        }
        if ("long".equals(name)) {
            return Long.class;
        }
        if (TypedValues.Custom.S_FLOAT.equals(name)) {
            return Float.class;
        }
        if ("double".equals(name)) {
            return Double.class;
        }
        if (TypedValues.Custom.S_BOOLEAN.equals(name)) {
            return Boolean.class;
        }
        if ("char".equals(name)) {
            return Character.class;
        }
        return null;
    }

    private Object getInitParamValue(Class<?> cls, Class<?> cls2) {
        String name = cls2.getName();
        if (TypedValues.Custom.S_BOOLEAN.equals(name) || "java.lang.Boolean".equals(name)) {
            return false;
        }
        if (TypedValues.Custom.S_FLOAT.equals(name) || "java.lang.Float".equals(name)) {
            return Float.valueOf(0.0f);
        }
        if ("double".equals(name) || "java.lang.Double".equals(name)) {
            return Double.valueOf(0.0d);
        }
        if ("int".equals(name) || "java.lang.Integer".equals(name)) {
            return 0;
        }
        if ("long".equals(name) || "java.lang.Long".equals(name)) {
            return 0L;
        }
        if ("short".equals(name) || "java.lang.Short".equals(name)) {
            return 0;
        }
        if ("char".equals(name) || "java.lang.Character".equals(name)) {
            return ' ';
        }
        if ("[B".equals(name) || "[Ljava.lang.Byte;".equals(name)) {
            return new byte[0];
        }
        if ("java.lang.String".equals(name)) {
            return "";
        }
        if (cls == cls2) {
            return null;
        }
        return createInstanceFromClass(cls2);
    }

    private boolean isCharType(Field field) {
        String name = field.getType().getName();
        return name.equals("char") || name.endsWith("Character");
    }

    private boolean isPrimitiveBooleanType(Field field) {
        return TypedValues.Custom.S_BOOLEAN.equals(field.getType().getName());
    }

    private void putFieldsValueDependsOnSaveOrUpdate(DataSupport dataSupport, Field field, ContentValues contentValues) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (isUpdating()) {
            if (isFieldWithDefaultValue(dataSupport, field)) {
                return;
            }
            putContentValuesForUpdate(dataSupport, field, contentValues);
        } else if (isSaving()) {
            putContentValuesForSave(dataSupport, field, contentValues);
        }
    }

    private boolean isUpdating() {
        return UpdateHandler.class.getName().equals(getClass().getName());
    }

    private boolean isSaving() {
        return SaveHandler.class.getName().equals(getClass().getName());
    }

    private boolean isFieldWithDefaultValue(DataSupport dataSupport, Field field) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        DataSupport emptyModel = getEmptyModel(dataSupport);
        Object fieldValue = getFieldValue(dataSupport, field);
        Object fieldValue2 = getFieldValue(emptyModel, field);
        if (fieldValue == null || fieldValue2 == null) {
            return fieldValue == fieldValue2;
        }
        return fieldValue.toString().equals(fieldValue2.toString());
    }

    private String makeGetterMethodName(Field field) {
        String str;
        String name = field.getName();
        if (isPrimitiveBooleanType(field)) {
            if (name.matches("^is[A-Z]{1}.*$")) {
                name = name.substring(2);
            }
            str = "is";
        } else {
            str = "get";
        }
        if (name.matches("^[a-z]{1}[A-Z]{1}.*")) {
            return str + name;
        }
        return str + BaseUtility.capitalize(name);
    }

    private String makeSetterMethodName(Field field) {
        if (isPrimitiveBooleanType(field) && field.getName().matches("^is[A-Z]{1}.*$")) {
            return "set" + field.getName().substring(2);
        }
        if (field.getName().matches("^[a-z]{1}[A-Z]{1}.*")) {
            return "set" + field.getName();
        }
        return "set" + BaseUtility.capitalize(field.getName());
    }

    private String genGetColumnMethod(Field field) {
        Class<?> type;
        if (isCollection(field.getType())) {
            type = getGenericTypeClass(field);
        } else {
            type = field.getType();
        }
        return genGetColumnMethod(type);
    }

    private String genGetColumnMethod(Class<?> cls) {
        String simpleName;
        if (cls.isPrimitive()) {
            simpleName = BaseUtility.capitalize(cls.getName());
        } else {
            simpleName = cls.getSimpleName();
        }
        String str = "get" + simpleName;
        if (!"getBoolean".equals(str)) {
            if ("getChar".equals(str) || "getCharacter".equals(str)) {
                return "getString";
            }
            if ("getDate".equals(str)) {
                return "getLong";
            }
            if (!"getInteger".equals(str)) {
                return "getbyte[]".equalsIgnoreCase(str) ? "getBlob" : str;
            }
        }
        return "getInt";
    }

    private String[] getCustomizedColumns(String[] strArr, List<Field> list, List<AssociationsInfo> list2) {
        if (strArr == null || strArr.length <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(strArr));
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        Iterator<Field> it = list.iterator();
        while (it.hasNext()) {
            arrayList2.add(it.next().getName());
        }
        boolean z = false;
        for (int i = 0; i < arrayList.size(); i++) {
            String str = (String) arrayList.get(i);
            if (BaseUtility.containsIgnoreCases(arrayList2, str)) {
                arrayList3.add(Integer.valueOf(i));
            } else if (isIdColumn(str)) {
                if ("_id".equalsIgnoreCase(str)) {
                    arrayList.set(i, BaseUtility.changeCase("id"));
                }
                z = true;
            }
        }
        for (int size = arrayList3.size() - 1; size >= 0; size--) {
            arrayList4.add((String) arrayList.remove(((Integer) arrayList3.get(size)).intValue()));
        }
        for (Field field : list) {
            if (BaseUtility.containsIgnoreCases(arrayList4, field.getName())) {
                arrayList5.add(field);
            }
        }
        list.clear();
        list.addAll(arrayList5);
        if (list2 != null && list2.size() > 0) {
            for (int i2 = 0; i2 < list2.size(); i2++) {
                arrayList.add(getForeignKeyColumnName(DBUtility.getTableNameByClassName(list2.get(i2).getAssociatedClassName())));
            }
        }
        if (!z) {
            arrayList.add(BaseUtility.changeCase("id"));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private void analyzeAssociations(String str) {
        Collection<AssociationsInfo> associationInfo = getAssociationInfo(str);
        List<AssociationsInfo> list = this.fkInCurrentModel;
        if (list == null) {
            this.fkInCurrentModel = new ArrayList();
        } else {
            list.clear();
        }
        List<AssociationsInfo> list2 = this.fkInOtherModel;
        if (list2 == null) {
            this.fkInOtherModel = new ArrayList();
        } else {
            list2.clear();
        }
        for (AssociationsInfo associationsInfo : associationInfo) {
            if (associationsInfo.getAssociationType() == 2 || associationsInfo.getAssociationType() == 1) {
                if (associationsInfo.getClassHoldsForeignKey().equals(str)) {
                    this.fkInCurrentModel.add(associationsInfo);
                } else {
                    this.fkInOtherModel.add(associationsInfo);
                }
            } else if (associationsInfo.getAssociationType() == 3) {
                this.fkInOtherModel.add(associationsInfo);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:45:0x0188  */
    /* JADX WARN: Code duplicated, block: B:57:0x01a5  */
    private void setAssociatedModel(DataSupport dataSupport) throws Throwable {
        Cursor cursor;
        Cursor cursorQuery;
        Iterator<AssociationsInfo> it;
        HashMap map;
        List<AssociationsInfo> list = this.fkInOtherModel;
        if (list == null) {
            return;
        }
        Iterator<AssociationsInfo> it2 = list.iterator();
        while (it2.hasNext()) {
            AssociationsInfo next = it2.next();
            String associatedClassName = next.getAssociatedClassName();
            boolean z = next.getAssociationType() == 3;
            try {
                List<Field> supportedFields = getSupportedFields(associatedClassName);
                List<Field> supportedGenericFields = getSupportedGenericFields(associatedClassName);
                if (z) {
                    String tableName = dataSupport.getTableName();
                    String tableNameByClassName = DBUtility.getTableNameByClassName(associatedClassName);
                    String intermediateTableName = DBUtility.getIntermediateTableName(tableName, tableNameByClassName);
                    StringBuilder sb = new StringBuilder();
                    sb.append("select * from ").append(tableNameByClassName).append(" a inner join ").append(intermediateTableName).append(" b on a.id = b.").append(tableNameByClassName + "_id").append(" where b.").append(tableName).append("_id = ?");
                    cursorQuery = DataSupport.findBySQL(BaseUtility.changeCase(sb.toString()), String.valueOf(dataSupport.getBaseObjId()));
                } else {
                    cursorQuery = this.mDatabase.query(BaseUtility.changeCase(DBUtility.getTableNameByClassName(associatedClassName)), null, getForeignKeyColumnName(DBUtility.getTableNameByClassName(next.getSelfClassName())) + "=?", new String[]{String.valueOf(dataSupport.getBaseObjId())}, null, null, null, null);
                }
                Cursor cursor2 = cursorQuery;
                if (cursor2 != null) {
                    try {
                        if (cursor2.moveToFirst()) {
                            SparseArray<QueryInfoCache> sparseArray = new SparseArray<>();
                            HashMap map2 = new HashMap();
                            while (true) {
                                DataSupport dataSupport2 = (DataSupport) createInstanceFromClass(Class.forName(associatedClassName));
                                giveBaseObjIdValue(dataSupport2, cursor2.getLong(cursor2.getColumnIndexOrThrow("id")));
                                it = it2;
                                map = map2;
                                setValueToModel(dataSupport2, supportedFields, null, cursor2, sparseArray);
                                setGenericValueToModel(dataSupport2, supportedGenericFields, map);
                                if (next.getAssociationType() == 2 || z) {
                                    Field associateOtherModelFromSelf = next.getAssociateOtherModelFromSelf();
                                    Collection hashSet = (Collection) getFieldValue(dataSupport, associateOtherModelFromSelf);
                                    if (hashSet == null) {
                                        if (isList(associateOtherModelFromSelf.getType())) {
                                            hashSet = new ArrayList();
                                        } else {
                                            hashSet = new HashSet();
                                        }
                                        DynamicExecutor.setField(dataSupport, associateOtherModelFromSelf.getName(), hashSet, dataSupport.getClass());
                                    }
                                    hashSet.add(dataSupport2);
                                } else if (next.getAssociationType() == 1) {
                                    setFieldValue(dataSupport, next.getAssociateOtherModelFromSelf(), dataSupport2);
                                }
                                if (!cursor2.moveToNext()) {
                                    break;
                                }
                                map2 = map;
                                it2 = it;
                            }
                            sparseArray.clear();
                            map.clear();
                        } else {
                            it = it2;
                        }
                    } catch (Exception e) {
                        e = e;
                        cursor = cursor2;
                        try {
                            throw new DataSupportException(e.getMessage(), e);
                        } catch (Throwable th) {
                            th = th;
                            if (cursor != null) {
                                cursor.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        cursor = cursor2;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                } else {
                    it = it2;
                }
                if (cursor2 != null) {
                    cursor2.close();
                }
                it2 = it;
            } catch (Exception e2) {
                e = e2;
                cursor = null;
            } catch (Throwable th3) {
                th = th3;
                cursor = null;
            }
        }
    }

    private void setToModelByReflection(Object obj, Field field, int i, String str, Cursor cursor) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Object objInvoke = cursor.getClass().getMethod(str, Integer.TYPE).invoke(cursor, Integer.valueOf(i));
        if (field.getType() == Boolean.TYPE || field.getType() == Boolean.class) {
            if (AppEventsConstants.EVENT_PARAM_VALUE_NO.equals(String.valueOf(objInvoke))) {
                objInvoke = false;
            } else if ("1".equals(String.valueOf(objInvoke))) {
                objInvoke = true;
            }
        } else if (field.getType() == Character.TYPE || field.getType() == Character.class) {
            objInvoke = Character.valueOf(((String) objInvoke).charAt(0));
        } else if (field.getType() == Date.class) {
            long jLongValue = ((Long) objInvoke).longValue();
            objInvoke = jLongValue <= 0 ? null : new Date(jLongValue);
        }
        if (isCollection(field.getType())) {
            Collection hashSet = (Collection) DynamicExecutor.getField(obj, field.getName(), obj.getClass());
            if (hashSet == null) {
                if (isList(field.getType())) {
                    hashSet = new ArrayList();
                } else {
                    hashSet = new HashSet();
                }
                DynamicExecutor.setField(obj, field.getName(), hashSet, obj.getClass());
            }
            Encrypt encrypt = (Encrypt) field.getAnnotation(Encrypt.class);
            if (encrypt != null && "java.lang.String".equals(getGenericTypeName(field))) {
                objInvoke = decryptValue(encrypt.algorithm(), objInvoke);
            }
            hashSet.add(objInvoke);
            return;
        }
        Encrypt encrypt2 = (Encrypt) field.getAnnotation(Encrypt.class);
        if (encrypt2 != null && "java.lang.String".equals(field.getType().getName())) {
            objInvoke = decryptValue(encrypt2.algorithm(), objInvoke);
        }
        DynamicExecutor.setField(obj, field.getName(), objInvoke, obj.getClass());
    }

    protected Object decryptValue(String str, Object obj) {
        return (str == null || obj == null || !"AES".equalsIgnoreCase(str)) ? obj : CipherUtil.aesDecrypt((String) obj);
    }

    class QueryInfoCache {
        Field field;
        String getMethodName;

        QueryInfoCache() {
        }
    }
}
