package javy.lib.jsave.converter;

import android.util.Log;
import com.google.gson.Gson;

/* JADX INFO: loaded from: classes2.dex */
public class JSaveGsonConverter implements JSaveConverter {
    private Gson gson = new Gson();

    @Override // javy.lib.jsave.converter.JSaveConverter
    public <T> String toString(T t) {
        return this.gson.toJson(t);
    }

    @Override // javy.lib.jsave.converter.JSaveConverter
    public <T> T fromString(String str, String str2) {
        try {
            return (T) this.gson.fromJson(str, (Class) Class.forName(str2));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e("convert error", str + "," + str2);
            return null;
        }
    }
}
