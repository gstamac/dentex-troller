package org.litepal.crud;

import com.facebook.appevents.AppEventsConstants;
import java.util.List;
import org.litepal.LitePal;
import org.litepal.crud.async.AverageExecutor;
import org.litepal.crud.async.CountExecutor;
import org.litepal.crud.async.FindExecutor;
import org.litepal.crud.async.FindMultiExecutor;
import org.litepal.tablemanager.Connector;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;

/* JADX INFO: loaded from: classes2.dex */
public class ClusterQuery {
    String[] mColumns;
    String[] mConditions;
    String mLimit;
    String mOffset;
    String mOrderBy;

    ClusterQuery() {
    }

    public ClusterQuery select(String... strArr) {
        this.mColumns = strArr;
        return this;
    }

    public ClusterQuery where(String... strArr) {
        this.mConditions = strArr;
        return this;
    }

    public ClusterQuery order(String str) {
        this.mOrderBy = str;
        return this;
    }

    public ClusterQuery limit(int i) {
        this.mLimit = String.valueOf(i);
        return this;
    }

    public ClusterQuery offset(int i) {
        this.mOffset = String.valueOf(i);
        return this;
    }

    public <T> List<T> find(Class<T> cls) {
        return find(cls, false);
    }

    public <T> FindMultiExecutor findAsync(Class<T> cls) {
        return findAsync(cls, false);
    }

    public synchronized <T> List<T> find(Class<T> cls, boolean z) {
        QueryHandler queryHandler;
        String str;
        queryHandler = new QueryHandler(Connector.getDatabase());
        if (this.mOffset == null) {
            str = this.mLimit;
        } else {
            if (this.mLimit == null) {
                this.mLimit = AppEventsConstants.EVENT_PARAM_VALUE_NO;
            }
            str = this.mOffset + "," + this.mLimit;
        }
        return queryHandler.onFind(cls, this.mColumns, this.mConditions, this.mOrderBy, str, z);
    }

    public <T> FindMultiExecutor findAsync(final Class<T> cls, final boolean z) {
        final FindMultiExecutor findMultiExecutor = new FindMultiExecutor();
        findMultiExecutor.submit(new Runnable() { // from class: org.litepal.crud.ClusterQuery.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final List listFind = ClusterQuery.this.find(cls, z);
                    if (findMultiExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.ClusterQuery.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                findMultiExecutor.getListener().onFinish(listFind);
                            }
                        });
                    }
                }
            }
        });
        return findMultiExecutor;
    }

    public <T> T findFirst(Class<T> cls) {
        return (T) findFirst(cls, false);
    }

    public <T> FindExecutor findFirstAsync(Class<T> cls) {
        return findFirstAsync(cls, false);
    }

    public <T> T findFirst(Class<T> cls, boolean z) {
        List<T> listFind = find(cls, z);
        if (listFind.size() > 0) {
            return listFind.get(0);
        }
        return null;
    }

    public <T> FindExecutor findFirstAsync(final Class<T> cls, final boolean z) {
        final FindExecutor findExecutor = new FindExecutor();
        findExecutor.submit(new Runnable() { // from class: org.litepal.crud.ClusterQuery.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final Object objFindFirst = ClusterQuery.this.findFirst(cls, z);
                    if (findExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.ClusterQuery.2.1
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

    public <T> T findLast(Class<T> cls) {
        return (T) findLast(cls, false);
    }

    public <T> FindExecutor findLastAsync(Class<T> cls) {
        return findLastAsync(cls, false);
    }

    public <T> T findLast(Class<T> cls, boolean z) {
        List<T> listFind = find(cls, z);
        int size = listFind.size();
        if (size > 0) {
            return listFind.get(size - 1);
        }
        return null;
    }

    public <T> FindExecutor findLastAsync(final Class<T> cls, final boolean z) {
        final FindExecutor findExecutor = new FindExecutor();
        findExecutor.submit(new Runnable() { // from class: org.litepal.crud.ClusterQuery.3
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final Object objFindLast = ClusterQuery.this.findLast(cls, z);
                    if (findExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.ClusterQuery.3.1
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

    public synchronized int count(Class<?> cls) {
        return count(BaseUtility.changeCase(cls.getSimpleName()));
    }

    public CountExecutor countAsync(Class<?> cls) {
        return countAsync(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())));
    }

    public synchronized int count(String str) {
        return new QueryHandler(Connector.getDatabase()).onCount(str, this.mConditions);
    }

    public CountExecutor countAsync(final String str) {
        final CountExecutor countExecutor = new CountExecutor();
        countExecutor.submit(new Runnable() { // from class: org.litepal.crud.ClusterQuery.4
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final int iCount = ClusterQuery.this.count(str);
                    if (countExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.ClusterQuery.4.1
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

    public synchronized double average(Class<?> cls, String str) {
        return average(BaseUtility.changeCase(cls.getSimpleName()), str);
    }

    public AverageExecutor averageAsync(Class<?> cls, String str) {
        return averageAsync(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str);
    }

    public synchronized double average(String str, String str2) {
        return new QueryHandler(Connector.getDatabase()).onAverage(str, str2, this.mConditions);
    }

    public AverageExecutor averageAsync(final String str, final String str2) {
        final AverageExecutor averageExecutor = new AverageExecutor();
        averageExecutor.submit(new Runnable() { // from class: org.litepal.crud.ClusterQuery.5
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final double dAverage = ClusterQuery.this.average(str, str2);
                    if (averageExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.ClusterQuery.5.1
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

    public synchronized <T> T max(Class<?> cls, String str, Class<T> cls2) {
        return (T) max(BaseUtility.changeCase(cls.getSimpleName()), str, cls2);
    }

    public <T> FindExecutor maxAsync(Class<?> cls, String str, Class<T> cls2) {
        return maxAsync(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str, cls2);
    }

    public synchronized <T> T max(String str, String str2, Class<T> cls) {
        return (T) new QueryHandler(Connector.getDatabase()).onMax(str, str2, this.mConditions, cls);
    }

    public <T> FindExecutor maxAsync(final String str, final String str2, final Class<T> cls) {
        final FindExecutor findExecutor = new FindExecutor();
        findExecutor.submit(new Runnable() { // from class: org.litepal.crud.ClusterQuery.6
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final Object objMax = ClusterQuery.this.max(str, str2, (Class<Object>) cls);
                    if (findExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.ClusterQuery.6.1
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

    public synchronized <T> T min(Class<?> cls, String str, Class<T> cls2) {
        return (T) min(BaseUtility.changeCase(cls.getSimpleName()), str, cls2);
    }

    public <T> FindExecutor minAsync(Class<?> cls, String str, Class<T> cls2) {
        return minAsync(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str, cls2);
    }

    public synchronized <T> T min(String str, String str2, Class<T> cls) {
        return (T) new QueryHandler(Connector.getDatabase()).onMin(str, str2, this.mConditions, cls);
    }

    public <T> FindExecutor minAsync(final String str, final String str2, final Class<T> cls) {
        final FindExecutor findExecutor = new FindExecutor();
        findExecutor.submit(new Runnable() { // from class: org.litepal.crud.ClusterQuery.7
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final Object objMin = ClusterQuery.this.min(str, str2, (Class<Object>) cls);
                    if (findExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.ClusterQuery.7.1
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

    public synchronized <T> T sum(Class<?> cls, String str, Class<T> cls2) {
        return (T) sum(BaseUtility.changeCase(cls.getSimpleName()), str, cls2);
    }

    public <T> FindExecutor sumAsync(Class<?> cls, String str, Class<T> cls2) {
        return sumAsync(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str, cls2);
    }

    public synchronized <T> T sum(String str, String str2, Class<T> cls) {
        return (T) new QueryHandler(Connector.getDatabase()).onSum(str, str2, this.mConditions, cls);
    }

    public <T> FindExecutor sumAsync(final String str, final String str2, final Class<T> cls) {
        final FindExecutor findExecutor = new FindExecutor();
        findExecutor.submit(new Runnable() { // from class: org.litepal.crud.ClusterQuery.8
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataSupport.class) {
                    final Object objSum = ClusterQuery.this.sum(str, str2, (Class<Object>) cls);
                    if (findExecutor.getListener() != null) {
                        LitePal.getHandler().post(new Runnable() { // from class: org.litepal.crud.ClusterQuery.8.1
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
}
