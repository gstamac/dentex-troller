package javy.lib.jsave.converter;

/* JADX INFO: loaded from: classes2.dex */
public interface JSaveConverter {
    <T> T fromString(String str, String str2);

    <T> String toString(T t);
}
