package org.litepal.crud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.litepal.LitePal;
import org.litepal.crud.async.AverageExecutor;
import org.litepal.crud.async.CountExecutor;
import org.litepal.crud.async.FindExecutor;
import org.litepal.crud.async.FindMultiExecutor;
import org.litepal.crud.async.SaveExecutor;
import org.litepal.crud.async.UpdateOrDeleteExecutor;
import org.litepal.exceptions.DataSupportException;
import org.litepal.tablemanager.Connector;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;

/* JADX INFO: loaded from: classes2.dex */
public class DataSupport {
    protected static final String AES = "AES";
    protected static final String MD5 = "MD5";
    private Map<String, Set<Long>> associatedModelsMapForJoinTable;
    private Map<String, Set<Long>> associatedModelsMapWithFK;
    private Map<String, Long> associatedModelsMapWithoutFK;
    private long baseObjId;
    private List<String> fieldsToSetToDefault;
    private List<String> listToClearAssociatedFK;
    private List<String> listToClearSelfFK;

    public static synchronized ClusterQuery select(String... strArr) {
        ClusterQuery clusterQuery;
        clusterQuery = new ClusterQuery();
        clusterQuery.mColumns = strArr;
        return clusterQuery;
    }

    public static synchronized ClusterQuery where(String... strArr) {
        ClusterQuery clusterQuery;
        clusterQuery = new ClusterQuery();
        clusterQuery.mConditions = strArr;
        return clusterQuery;
    }

    public static synchronized ClusterQuery order(String str) {
        ClusterQuery clusterQuery;
        clusterQuery = new ClusterQuery();
        clusterQuery.mOrderBy = str;
        return clusterQuery;
    }

    public static synchronized ClusterQuery limit(int i) {
        ClusterQuery clusterQuery;
        clusterQuery = new ClusterQuery();
        clusterQuery.mLimit = String.valueOf(i);
        return clusterQuery;
    }

    public static synchronized ClusterQuery offset(int i) {
        ClusterQuery clusterQuery;
        clusterQuery = new ClusterQuery();
        clusterQuery.mOffset = String.valueOf(i);
        return clusterQuery;
    }

    public static synchronized int count(Class<?> cls) {
        return count(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())));
    }

    public static CountExecutor countAsync(Class<?> cls) {
        return countAsync(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())));
    }

    public static synchronized int count(String str) {
        return new ClusterQuery().count(str);
    }

    public static CountExecutor countAsync(final String str) {
        final CountExecutor countExecutor = new CountExecutor();
        countExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final int iCount = DataSupport.count(str);
                    if (countExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                countExecutor.getListener().onFinish(iCount);
                            }
                        });
                    }
                }
            }
        });
        return countExecutor;
    }

    public static synchronized double average(Class<?> cls, String str) {
        return average(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str);
    }

    public static AverageExecutor averageAsync(Class<?> cls, String str) {
        return averageAsync(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str);
    }

    public static synchronized double average(String str, String str2) {
        return new ClusterQuery().average(str, str2);
    }

    public static AverageExecutor averageAsync(final String str, final String str2) {
        final AverageExecutor averageExecutor = new AverageExecutor();
        averageExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final double dAverage = DataSupport.average(str, str2);
                    if (averageExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                averageExecutor.getListener().onFinish(dAverage);
                            }
                        });
                    }
                }
            }
        });
        return averageExecutor;
    }

    public static synchronized <T> T max(Class<?> cls, String str, Class<T> cls2) {
        return (T) max(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str, cls2);
    }

    public static <T> FindExecutor maxAsync(Class<?> cls, String str, Class<T> cls2) {
        return maxAsync(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str, cls2);
    }

    public static synchronized <T> T max(String str, String str2, Class<T> cls) {
        return (T) new ClusterQuery().max(str, str2, cls);
    }

    public static <T> FindExecutor maxAsync(final String str, final String str2, final Class<T> cls) {
        final FindExecutor findExecutor = new FindExecutor();
        findExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.3
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final Object objMax = DataSupport.max(str, str2, (Class<Object>) cls);
                    if (findExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.3.1
                            @Override // java.lang.Runnable
                            public void run() {
                                findExecutor.getListener().onFinish(objMax);
                            }
                        });
                    }
                }
            }
        });
        return findExecutor;
    }

    public static synchronized <T> T min(Class<?> cls, String str, Class<T> cls2) {
        return (T) min(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str, cls2);
    }

    public static <T> FindExecutor minAsync(Class<?> cls, String str, Class<T> cls2) {
        return minAsync(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str, cls2);
    }

    public static synchronized <T> T min(String str, String str2, Class<T> cls) {
        return (T) new ClusterQuery().min(str, str2, cls);
    }

    public static <T> FindExecutor minAsync(final String str, final String str2, final Class<T> cls) {
        final FindExecutor findExecutor = new FindExecutor();
        findExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.4
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final Object objMin = DataSupport.min(str, str2, (Class<Object>) cls);
                    if (findExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                findExecutor.getListener().onFinish(objMin);
                            }
                        });
                    }
                }
            }
        });
        return findExecutor;
    }

    public static synchronized <T> T sum(Class<?> cls, String str, Class<T> cls2) {
        return (T) sum(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str, cls2);
    }

    public static <T> FindExecutor sumAsync(Class<?> cls, String str, Class<T> cls2) {
        return sumAsync(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str, cls2);
    }

    public static synchronized <T> T sum(String str, String str2, Class<T> cls) {
        return (T) new ClusterQuery().sum(str, str2, cls);
    }

    public static <T> FindExecutor sumAsync(final String str, final String str2, final Class<T> cls) {
        final FindExecutor findExecutor = new FindExecutor();
        findExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.5
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final Object objSum = DataSupport.sum(str, str2, (Class<Object>) cls);
                    if (findExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.5.1
                            @Override // java.lang.Runnable
                            public void run() {
                                findExecutor.getListener().onFinish(objSum);
                            }
                        });
                    }
                }
            }
        });
        return findExecutor;
    }

    public static synchronized <T> T find(Class<T> cls, long j) {
        return (T) find(cls, j, false);
    }

    public static <T> FindExecutor findAsync(Class<T> cls, long j) {
        return findAsync(cls, j, false);
    }

    public static synchronized <T> T find(Class<T> cls, long j, boolean z) {
        return (T) new QueryHandler(Connector.getDatabase()).onFind(cls, j, z);
    }

    public static <T> FindExecutor findAsync(final Class<T> cls, final long j, final boolean z) {
        final FindExecutor findExecutor = new FindExecutor();
        findExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.6
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final Object objFind = DataSupport.find(cls, j, z);
                    if (findExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.6.1
                            @Override // java.lang.Runnable
                            public void run() {
                                findExecutor.getListener().onFinish(objFind);
                            }
                        });
                    }
                }
            }
        });
        return findExecutor;
    }

    public static synchronized <T> T findFirst(Class<T> cls) {
        return (T) findFirst(cls, false);
    }

    public static <T> FindExecutor findFirstAsync(Class<T> cls) {
        return findFirstAsync(cls, false);
    }

    public static synchronized <T> T findFirst(Class<T> cls, boolean z) {
        return (T) new QueryHandler(Connector.getDatabase()).onFindFirst(cls, z);
    }

    public static <T> FindExecutor findFirstAsync(final Class<T> cls, final boolean z) {
        final FindExecutor findExecutor = new FindExecutor();
        findExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.7
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final Object objFindFirst = DataSupport.findFirst(cls, z);
                    if (findExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.7.1
                            @Override // java.lang.Runnable
                            public void run() {
                                findExecutor.getListener().onFinish(objFindFirst);
                            }
                        });
                    }
                }
            }
        });
        return findExecutor;
    }

    public static synchronized <T> T findLast(Class<T> cls) {
        return (T) findLast(cls, false);
    }

    public static <T> FindExecutor findLastAsync(Class<T> cls) {
        return findLastAsync(cls, false);
    }

    public static synchronized <T> T findLast(Class<T> cls, boolean z) {
        return (T) new QueryHandler(Connector.getDatabase()).onFindLast(cls, z);
    }

    public static <T> FindExecutor findLastAsync(final Class<T> cls, final boolean z) {
        final FindExecutor findExecutor = new FindExecutor();
        findExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.8
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final Object objFindLast = DataSupport.findLast(cls, z);
                    if (findExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.8.1
                            @Override // java.lang.Runnable
                            public void run() {
                                findExecutor.getListener().onFinish(objFindLast);
                            }
                        });
                    }
                }
            }
        });
        return findExecutor;
    }

    public static synchronized <T> List<T> findAll(Class<T> cls, long... jArr) {
        return findAll(cls, false, jArr);
    }

    public static <T> FindMultiExecutor findAllAsync(Class<T> cls, long... jArr) {
        return findAllAsync(cls, false, jArr);
    }

    public static synchronized <T> List<T> findAll(Class<T> cls, boolean z, long... jArr) {
        return new QueryHandler(Connector.getDatabase()).onFindAll(cls, z, jArr);
    }

    public static <T> FindMultiExecutor findAllAsync(final Class<T> cls, final boolean z, final long... jArr) {
        final FindMultiExecutor findMultiExecutor = new FindMultiExecutor();
        findMultiExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.9
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final List listFindAll = DataSupport.findAll(cls, z, jArr);
                    if (findMultiExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.9.1
                            @Override // java.lang.Runnable
                            public void run() {
                                findMultiExecutor.getListener().onFinish(listFindAll);
                            }
                        });
                    }
                }
            }
        });
        return findMultiExecutor;
    }

    public static synchronized Cursor findBySQL(String... strArr) {
        BaseUtility.checkConditionsCorrect(strArr);
        String[] strArr2 = null;
        if (strArr == null) {
            return null;
        }
        if (strArr.length <= 0) {
            return null;
        }
        if (strArr.length != 1) {
            strArr2 = new String[strArr.length - 1];
            System.arraycopy(strArr, 1, strArr2, 0, strArr.length - 1);
        }
        return Connector.getDatabase().rawQuery(strArr[0], strArr2);
    }

    public static synchronized int delete(Class<?> cls, long j) {
        int iOnDelete;
        SQLiteDatabase database = Connector.getDatabase();
        database.beginTransaction();
        try {
            iOnDelete = new DeleteHandler(database).onDelete(cls, j);
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (Throwable th) {
            database.endTransaction();
            throw th;
        }
        return iOnDelete;
    }

    public static UpdateOrDeleteExecutor deleteAsync(final Class<?> cls, final long j) {
        final UpdateOrDeleteExecutor updateOrDeleteExecutor = new UpdateOrDeleteExecutor();
        updateOrDeleteExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.10
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final int iDelete = DataSupport.delete(cls, j);
                    if (updateOrDeleteExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.10.1
                            @Override // java.lang.Runnable
                            public void run() {
                                updateOrDeleteExecutor.getListener().onFinish(iDelete);
                            }
                        });
                    }
                }
            }
        });
        return updateOrDeleteExecutor;
    }

    public static synchronized int deleteAll(Class<?> cls, String... strArr) {
        return new DeleteHandler(Connector.getDatabase()).onDeleteAll(cls, strArr);
    }

    public static UpdateOrDeleteExecutor deleteAllAsync(final Class<?> cls, final String... strArr) {
        final UpdateOrDeleteExecutor updateOrDeleteExecutor = new UpdateOrDeleteExecutor();
        updateOrDeleteExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.11
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final int iDeleteAll = DataSupport.deleteAll((Class<?>) cls, strArr);
                    if (updateOrDeleteExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.11.1
                            @Override // java.lang.Runnable
                            public void run() {
                                updateOrDeleteExecutor.getListener().onFinish(iDeleteAll);
                            }
                        });
                    }
                }
            }
        });
        return updateOrDeleteExecutor;
    }

    public static synchronized int deleteAll(String str, String... strArr) {
        return new DeleteHandler(Connector.getDatabase()).onDeleteAll(str, strArr);
    }

    public static UpdateOrDeleteExecutor deleteAllAsync(final String str, final String... strArr) {
        final UpdateOrDeleteExecutor updateOrDeleteExecutor = new UpdateOrDeleteExecutor();
        updateOrDeleteExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.12
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final int iDeleteAll = DataSupport.deleteAll(str, strArr);
                    if (updateOrDeleteExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.12.1
                            @Override // java.lang.Runnable
                            public void run() {
                                updateOrDeleteExecutor.getListener().onFinish(iDeleteAll);
                            }
                        });
                    }
                }
            }
        });
        return updateOrDeleteExecutor;
    }

    public static synchronized int update(Class<?> cls, ContentValues contentValues, long j) {
        return new UpdateHandler(Connector.getDatabase()).onUpdate(cls, j, contentValues);
    }

    public static UpdateOrDeleteExecutor updateAsync(final Class<?> cls, final ContentValues contentValues, final long j) {
        final UpdateOrDeleteExecutor updateOrDeleteExecutor = new UpdateOrDeleteExecutor();
        updateOrDeleteExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.13
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final int iUpdate = DataSupport.update(cls, contentValues, j);
                    if (updateOrDeleteExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.13.1
                            @Override // java.lang.Runnable
                            public void run() {
                                updateOrDeleteExecutor.getListener().onFinish(iUpdate);
                            }
                        });
                    }
                }
            }
        });
        return updateOrDeleteExecutor;
    }

    public static synchronized int updateAll(Class<?> cls, ContentValues contentValues, String... strArr) {
        return updateAll(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), contentValues, strArr);
    }

    public static UpdateOrDeleteExecutor updateAllAsync(Class<?> cls, ContentValues contentValues, String... strArr) {
        return updateAllAsync(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), contentValues, strArr);
    }

    public static synchronized int updateAll(String str, ContentValues contentValues, String... strArr) {
        return new UpdateHandler(Connector.getDatabase()).onUpdateAll(str, contentValues, strArr);
    }

    public static UpdateOrDeleteExecutor updateAllAsync(final String str, final ContentValues contentValues, final String... strArr) {
        final UpdateOrDeleteExecutor updateOrDeleteExecutor = new UpdateOrDeleteExecutor();
        updateOrDeleteExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.14
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final int iUpdateAll = DataSupport.updateAll(str, contentValues, strArr);
                    if (updateOrDeleteExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.14.1
                            @Override // java.lang.Runnable
                            public void run() {
                                updateOrDeleteExecutor.getListener().onFinish(iUpdateAll);
                            }
                        });
                    }
                }
            }
        });
        return updateOrDeleteExecutor;
    }

    public static synchronized <T extends DataSupport> void saveAll(Collection<T> collection) {
        SQLiteDatabase database = Connector.getDatabase();
        database.beginTransaction();
        try {
            try {
                new SaveHandler(database).onSaveAll(collection);
                database.setTransactionSuccessful();
                database.endTransaction();
            } catch (Exception e) {
                throw new DataSupportException(e.getMessage(), e);
            }
        } catch (Throwable th) {
            database.endTransaction();
            throw th;
        }
    }

    public static <T extends DataSupport> SaveExecutor saveAllAsync(final Collection<T> collection) {
        final SaveExecutor saveExecutor = new SaveExecutor();
        saveExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.15
            @Override // java.lang.Runnable
            public void run() {
                final boolean z;
                synchronized (DataSupport.class) {
                    try {
                        DataSupport.saveAll(collection);
                        z = true;
                    } catch (Exception unused) {
                        z = false;
                    }
                    if (saveExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.15.1
                            @Override // java.lang.Runnable
                            public void run() {
                                saveExecutor.getListener().onFinish(z);
                            }
                        });
                    }
                }
            }
        });
        return saveExecutor;
    }

    public static <T extends DataSupport> void markAsDeleted(Collection<T> collection) {
        Iterator<T> it = collection.iterator();
        while (it.hasNext()) {
            it.next().clearSavedState();
        }
    }

    public static <T> boolean isExist(Class<T> cls, String... strArr) {
        return strArr != null && where(strArr).count((Class<?>) cls) > 0;
    }

    public synchronized int delete() {
        int iOnDelete;
        SQLiteDatabase database = Connector.getDatabase();
        database.beginTransaction();
        try {
            iOnDelete = new DeleteHandler(database).onDelete(this);
            this.baseObjId = 0L;
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (Throwable th) {
            database.endTransaction();
            throw th;
        }
        return iOnDelete;
    }

    public UpdateOrDeleteExecutor deleteAsync() {
        final UpdateOrDeleteExecutor updateOrDeleteExecutor = new UpdateOrDeleteExecutor();
        updateOrDeleteExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.16
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final int iDelete = DataSupport.this.delete();
                    if (updateOrDeleteExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.16.1
                            @Override // java.lang.Runnable
                            public void run() {
                                updateOrDeleteExecutor.getListener().onFinish(iDelete);
                            }
                        });
                    }
                }
            }
        });
        return updateOrDeleteExecutor;
    }

    public synchronized int update(long j) {
        int iOnUpdate;
        try {
            iOnUpdate = new UpdateHandler(Connector.getDatabase()).onUpdate(this, j);
            getFieldsToSetToDefault().clear();
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage(), e);
        }
        return iOnUpdate;
    }

    public UpdateOrDeleteExecutor updateAsync(final long j) {
        final UpdateOrDeleteExecutor updateOrDeleteExecutor = new UpdateOrDeleteExecutor();
        updateOrDeleteExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.17
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final int iUpdate = DataSupport.this.update(j);
                    if (updateOrDeleteExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.17.1
                            @Override // java.lang.Runnable
                            public void run() {
                                updateOrDeleteExecutor.getListener().onFinish(iUpdate);
                            }
                        });
                    }
                }
            }
        });
        return updateOrDeleteExecutor;
    }

    public synchronized int updateAll(String... strArr) {
        int iOnUpdateAll;
        try {
            iOnUpdateAll = new UpdateHandler(Connector.getDatabase()).onUpdateAll(this, strArr);
            getFieldsToSetToDefault().clear();
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage(), e);
        }
        return iOnUpdateAll;
    }

    public UpdateOrDeleteExecutor updateAllAsync(final String... strArr) {
        final UpdateOrDeleteExecutor updateOrDeleteExecutor = new UpdateOrDeleteExecutor();
        updateOrDeleteExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.18
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final int iUpdateAll = DataSupport.this.updateAll(strArr);
                    if (updateOrDeleteExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.18.1
                            @Override // java.lang.Runnable
                            public void run() {
                                updateOrDeleteExecutor.getListener().onFinish(iUpdateAll);
                            }
                        });
                    }
                }
            }
        });
        return updateOrDeleteExecutor;
    }

    public synchronized boolean save() {
        try {
            saveThrows();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public SaveExecutor saveAsync() {
        final SaveExecutor saveExecutor = new SaveExecutor();
        saveExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.19
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final boolean zSave = DataSupport.this.save();
                    if (saveExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.19.1
                            @Override // java.lang.Runnable
                            public void run() {
                                saveExecutor.getListener().onFinish(zSave);
                            }
                        });
                    }
                }
            }
        });
        return saveExecutor;
    }

    public synchronized void saveThrows() {
        SQLiteDatabase database = Connector.getDatabase();
        database.beginTransaction();
        try {
            try {
                new SaveHandler(database).onSave(this);
                clearAssociatedData();
                database.setTransactionSuccessful();
                database.endTransaction();
            } catch (Exception e) {
                throw new DataSupportException(e.getMessage(), e);
            }
        } catch (Throwable th) {
            database.endTransaction();
            throw th;
        }
    }

    @Deprecated
    public synchronized boolean saveIfNotExist(String... strArr) {
        if (isExist(getClass(), strArr)) {
            return false;
        }
        return save();
    }

    public synchronized boolean saveOrUpdate(String... strArr) {
        if (strArr == null) {
            return save();
        }
        List listFind = where(strArr).find(getClass());
        if (listFind.isEmpty()) {
            return save();
        }
        SQLiteDatabase database = Connector.getDatabase();
        database.beginTransaction();
        try {
            try {
                Iterator it = listFind.iterator();
                while (it.hasNext()) {
                    this.baseObjId = ((DataSupport) it.next()).getBaseObjId();
                    new SaveHandler(database).onSave(this);
                    clearAssociatedData();
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                database.endTransaction();
                return false;
            }
        } catch (Throwable th) {
            database.endTransaction();
            throw th;
        }
    }

    public SaveExecutor saveOrUpdateAsync(final String... strArr) {
        final SaveExecutor saveExecutor = new SaveExecutor();
        saveExecutor.submit(new Runnable() { // from class: org.litepal.crud.DataSupport.20
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final boolean zSaveOrUpdate = DataSupport.this.saveOrUpdate(strArr);
                    if (saveExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.DataSupport.20.1
                            @Override // java.lang.Runnable
                            public void run() {
                                saveExecutor.getListener().onFinish(zSaveOrUpdate);
                            }
                        });
                    }
                }
            }
        });
        return saveExecutor;
    }

    @Deprecated
    public synchronized boolean saveFast() {
        SQLiteDatabase database = Connector.getDatabase();
        database.beginTransaction();
        try {
            try {
                new SaveHandler(database).onSaveFast(this);
                database.setTransactionSuccessful();
                database.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                database.endTransaction();
                return false;
            }
        } catch (Throwable th) {
            database.endTransaction();
            throw th;
        }
        return true;
    }

    public boolean isSaved() {
        return this.baseObjId > 0;
    }

    public void clearSavedState() {
        this.baseObjId = 0L;
    }

    public void setToDefault(String str) {
        getFieldsToSetToDefault().add(str);
    }

    public void assignBaseObjId(int i) {
        this.baseObjId = i;
    }

    protected DataSupport() {
    }

    protected long getBaseObjId() {
        return this.baseObjId;
    }

    protected String getClassName() {
        return getClass().getName();
    }

    protected String getTableName() {
        return BaseUtility.changeCase(DBUtility.getTableNameByClassName(getClassName()));
    }

    List<String> getFieldsToSetToDefault() {
        if (this.fieldsToSetToDefault == null) {
            this.fieldsToSetToDefault = new ArrayList();
        }
        return this.fieldsToSetToDefault;
    }

    void addAssociatedModelWithFK(String str, long j) {
        Set<Long> set = getAssociatedModelsMapWithFK().get(str);
        if (set == null) {
            HashSet hashSet = new HashSet();
            hashSet.add(Long.valueOf(j));
            this.associatedModelsMapWithFK.put(str, hashSet);
            return;
        }
        set.add(Long.valueOf(j));
    }

    Map<String, Set<Long>> getAssociatedModelsMapWithFK() {
        if (this.associatedModelsMapWithFK == null) {
            this.associatedModelsMapWithFK = new HashMap();
        }
        return this.associatedModelsMapWithFK;
    }

    void addAssociatedModelForJoinTable(String str, long j) {
        Set<Long> set = getAssociatedModelsMapForJoinTable().get(str);
        if (set == null) {
            HashSet hashSet = new HashSet();
            hashSet.add(Long.valueOf(j));
            this.associatedModelsMapForJoinTable.put(str, hashSet);
            return;
        }
        set.add(Long.valueOf(j));
    }

    void addEmptyModelForJoinTable(String str) {
        if (getAssociatedModelsMapForJoinTable().get(str) == null) {
            this.associatedModelsMapForJoinTable.put(str, new HashSet());
        }
    }

    Map<String, Set<Long>> getAssociatedModelsMapForJoinTable() {
        if (this.associatedModelsMapForJoinTable == null) {
            this.associatedModelsMapForJoinTable = new HashMap();
        }
        return this.associatedModelsMapForJoinTable;
    }

    void addAssociatedModelWithoutFK(String str, long j) {
        getAssociatedModelsMapWithoutFK().put(str, Long.valueOf(j));
    }

    Map<String, Long> getAssociatedModelsMapWithoutFK() {
        if (this.associatedModelsMapWithoutFK == null) {
            this.associatedModelsMapWithoutFK = new HashMap();
        }
        return this.associatedModelsMapWithoutFK;
    }

    void addFKNameToClearSelf(String str) {
        List<String> listToClearSelfFK = getListToClearSelfFK();
        if (listToClearSelfFK.contains(str)) {
            return;
        }
        listToClearSelfFK.add(str);
    }

    List<String> getListToClearSelfFK() {
        if (this.listToClearSelfFK == null) {
            this.listToClearSelfFK = new ArrayList();
        }
        return this.listToClearSelfFK;
    }

    void addAssociatedTableNameToClearFK(String str) {
        List<String> listToClearAssociatedFK = getListToClearAssociatedFK();
        if (listToClearAssociatedFK.contains(str)) {
            return;
        }
        listToClearAssociatedFK.add(str);
    }

    List<String> getListToClearAssociatedFK() {
        if (this.listToClearAssociatedFK == null) {
            this.listToClearAssociatedFK = new ArrayList();
        }
        return this.listToClearAssociatedFK;
    }

    void clearAssociatedData() {
        clearIdOfModelWithFK();
        clearIdOfModelWithoutFK();
        clearIdOfModelForJoinTable();
        clearFKNameList();
    }

    private void clearIdOfModelWithFK() {
        Iterator<String> it = getAssociatedModelsMapWithFK().keySet().iterator();
        while (it.hasNext()) {
            this.associatedModelsMapWithFK.get(it.next()).clear();
        }
        this.associatedModelsMapWithFK.clear();
    }

    private void clearIdOfModelWithoutFK() {
        getAssociatedModelsMapWithoutFK().clear();
    }

    private void clearIdOfModelForJoinTable() {
        Iterator<String> it = getAssociatedModelsMapForJoinTable().keySet().iterator();
        while (it.hasNext()) {
            this.associatedModelsMapForJoinTable.get(it.next()).clear();
        }
        this.associatedModelsMapForJoinTable.clear();
    }

    private void clearFKNameList() {
        getListToClearSelfFK().clear();
        getListToClearAssociatedFK().clear();
    }
}
