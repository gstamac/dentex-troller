package cn.jpush.android.webview.bridge;

import android.text.TextUtils;
import android.webkit.WebView;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import cn.jpush.android.helper.Logger;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class c {
    private HashMap<String, Method> a;
    private String b;
    private String c;

    public c(String str, Class cls) {
        String strA;
        try {
            if (TextUtils.isEmpty(str)) {
                throw new Exception("injected name can not be null");
            }
            this.b = str;
            this.a = new HashMap<>();
            Method[] declaredMethods = cls.getDeclaredMethods();
            StringBuilder sb = new StringBuilder("javascript:(function(b){console.log(\"");
            sb.append(this.b);
            sb.append(" initialization begin\");var a={queue:[],callback:function(){var d=Array.prototype.slice.call(arguments,0);var c=d.shift();var e=d.shift();this.queue[c].apply(this,d);if(!e){delete this.queue[c]}}};");
            for (Method method : declaredMethods) {
                if (method.getModifiers() == 9 && (strA = a(method)) != null) {
                    this.a.put(strA, method);
                    sb.append(String.format(Locale.ENGLISH, "a.%s=", method.getName()));
                }
            }
            sb.append("function(){var f=Array.prototype.slice.call(arguments,0);if(f.length<1){throw\"");
            sb.append(this.b);
            sb.append(" call error, message:miss method name\"}var e=[];for(var h=1;h<f.length;h++){var c=f[h];var j=typeof c;e[e.length]=j;if(j==\"function\"){var d=a.queue.length;a.queue[d]=c;f[h]=d}}var g=JSON.parse(prompt(JSON.stringify({method:f.shift(),types:e,args:f})));if(g.code!=200){throw\"");
            sb.append(this.b);
            sb.append(" call error, code:\"+g.code+\", message:\"+g.result}return g.result};Object.getOwnPropertyNames(a).forEach(function(d){var c=a[d];if(typeof c===\"function\"&&d!==\"callback\"){a[d]=function(){return c.apply(a,[d].concat(Array.prototype.slice.call(arguments,0)))}}});b.");
            sb.append(this.b);
            sb.append("=a;console.log(\"");
            sb.append(this.b);
            sb.append(" initialization end\")})(window);");
            this.c = sb.toString();
            Logger.d("JsCallJava", "----------" + sb.toString());
        } catch (Exception e) {
            Logger.e("JsCallJava", "init js error:" + e.getMessage());
        }
    }

    private String a(String str, int i, Object obj) {
        String strValueOf;
        if (obj == null) {
            strValueOf = "null";
        } else if (obj instanceof String) {
            strValueOf = "\"" + ((Object) ((String) obj).replace("\"", "\\\"")) + "\"";
        } else {
            strValueOf = ((obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Boolean) || (obj instanceof Float) || (obj instanceof Double) || (obj instanceof JSONObject)) ? String.valueOf(obj) : "";
        }
        String str2 = String.format(Locale.ENGLISH, "{\"code\": %d, \"result\": %s}", Integer.valueOf(i), strValueOf);
        Logger.d("JsCallJava", this.b + " call json: " + str + " result:" + str2);
        return str2;
    }

    private String a(Method method) {
        StringBuilder sbAppend;
        String str;
        String name = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        int length = parameterTypes.length;
        if (length < 1 || parameterTypes[0] != WebView.class) {
            Logger.w("JsCallJava", "method(" + name + ") must use webview to be first parameter, will be pass");
            return null;
        }
        for (int i = 1; i < length; i++) {
            Class<?> cls = parameterTypes[i];
            if (cls == String.class) {
                sbAppend = new StringBuilder().append(name);
                str = "_S";
            } else if (cls == Integer.TYPE || cls == Long.TYPE || cls == Float.TYPE || cls == Double.TYPE) {
                sbAppend = new StringBuilder().append(name);
                str = "_N";
            } else if (cls == Boolean.TYPE) {
                sbAppend = new StringBuilder().append(name);
                str = "_B";
            } else if (cls == JSONObject.class) {
                sbAppend = new StringBuilder().append(name);
                str = "_O";
            } else {
                sbAppend = new StringBuilder().append(name);
                str = "_P";
            }
            name = sbAppend.append(str).toString();
        }
        return name;
    }

    public String a() {
        return this.c;
    }

    public String a(WebView webView, String str) {
        StringBuilder sb;
        String message;
        String string;
        if (TextUtils.isEmpty(str)) {
            string = "call data empty";
        } else {
            try {
                JSONObject jSONObject = new JSONObject(str);
                String string2 = jSONObject.getString("method");
                JSONArray jSONArray = jSONObject.getJSONArray("types");
                JSONArray jSONArray2 = jSONObject.getJSONArray("args");
                int length = jSONArray.length();
                Object[] objArr = new Object[length + 1];
                int i = 0;
                objArr[0] = webView;
                int i2 = 0;
                while (true) {
                    Object jSONObject2 = null;
                    if (i >= length) {
                        break;
                    }
                    String strOptString = jSONArray.optString(i);
                    if (TypedValues.Custom.S_STRING.equals(strOptString)) {
                        string2 = string2 + "_S";
                        int i3 = i + 1;
                        if (!jSONArray2.isNull(i)) {
                            jSONObject2 = jSONArray2.getString(i);
                        }
                        objArr[i3] = jSONObject2;
                    } else if ("number".equals(strOptString)) {
                        string2 = string2 + "_N";
                        i2 = (i2 * 10) + i + 1;
                    } else if (TypedValues.Custom.S_BOOLEAN.equals(strOptString)) {
                        string2 = string2 + "_B";
                        objArr[i + 1] = Boolean.valueOf(jSONArray2.getBoolean(i));
                    } else if ("object".equals(strOptString)) {
                        string2 = string2 + "_O";
                        int i4 = i + 1;
                        if (!jSONArray2.isNull(i)) {
                            jSONObject2 = jSONArray2.getJSONObject(i);
                        }
                        objArr[i4] = jSONObject2;
                    } else {
                        string2 = string2 + "_P";
                    }
                    i++;
                }
                Method method = this.a.get(string2);
                if (method == null) {
                    return a(str, 500, "not found method(" + string2 + ") with valid parameters");
                }
                if (i2 > 0) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    while (i2 > 0) {
                        int i5 = i2 - ((i2 / 10) * 10);
                        Class<?> cls = parameterTypes[i5];
                        if (cls == Integer.TYPE) {
                            objArr[i5] = Integer.valueOf(jSONArray2.getInt(i5 - 1));
                        } else if (cls == Long.TYPE) {
                            objArr[i5] = Long.valueOf(Long.parseLong(jSONArray2.getString(i5 - 1)));
                        } else {
                            objArr[i5] = Double.valueOf(jSONArray2.getDouble(i5 - 1));
                        }
                        i2 /= 10;
                    }
                }
                return a(str, 200, method.invoke(null, objArr));
            } catch (Exception e) {
                if (e.getCause() != null) {
                    sb = new StringBuilder("method execute error:");
                    message = e.getCause().getMessage();
                } else {
                    sb = new StringBuilder("method execute error:");
                    message = e.getMessage();
                }
                string = sb.append(message).toString();
            }
        }
        return a(str, 500, string);
    }
}
