package javy.lib.jsave.storage;

/* JADX INFO: loaded from: classes2.dex */
public interface JSaveStorage<T> {
    boolean delete(String str);

    int deleteAll();

    T read(String str);

    boolean save(T t);
}
