package cn.jpush.android.helper;

import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/* JADX INFO: loaded from: classes.dex */
public class e {
    private static int a = -1;
    private static int b = -1;
    private static float c = -1.0f;
    private static float d = -1.0f;

    private interface a {
        void a(View view);
    }

    public static int a() {
        return a;
    }

    public static void a(Context context) {
        try {
            if (a == -1 || b == -1) {
                c(context);
                if (a == -1 || b == -1) {
                    b(context);
                }
            }
        } catch (Exception unused) {
        }
    }

    private static void a(View view, a aVar) {
        if (view == null || aVar == null) {
            return;
        }
        aVar.a(view);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                a(viewGroup.getChildAt(i), aVar);
            }
        }
    }

    public static int b() {
        return b;
    }

    private static void b(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("title");
        builder.setContentText("content");
        builder.build();
        try {
            ViewGroup viewGroup = (ViewGroup) builder.createContentView().apply(context, new FrameLayout(context));
            Logger.d("NotificationStyleHelper", "begain fetch colors by internal , root view is :" + viewGroup);
            a(viewGroup, new a() { // from class: cn.jpush.android.helper.e.1
                @Override // cn.jpush.android.helper.e.a
                public void a(View view) {
                    if (view instanceof TextView) {
                        TextView textView = (TextView) view;
                        if ("title".equals(textView.getText().toString()) && e.a == -1) {
                            int unused = e.a = textView.getCurrentTextColor();
                            float unused2 = e.c = textView.getTextSize();
                        }
                        if ("content".equals(textView.getText().toString()) && e.b == -1) {
                            int unused3 = e.b = textView.getCurrentTextColor();
                            float unused4 = e.d = textView.getTextSize();
                        }
                    }
                }
            });
        } catch (Throwable th) {
            Logger.e("NotificationStyleHelper", "getNotificationColorInternal error:" + th.getMessage());
        }
    }

    public static float c() {
        return c;
    }

    private static void c(Context context) {
        try {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("title");
            builder.setContentText("content");
            builder.setSubText("subtext");
            builder.build();
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(builder.createContentView().getLayoutId(), (ViewGroup) null);
            Logger.d("NotificationStyleHelper", "begain fetch colors by compat, root view is :" + viewGroup);
            a(viewGroup, new a() { // from class: cn.jpush.android.helper.e.2
                @Override // cn.jpush.android.helper.e.a
                public void a(View view) {
                    if (view instanceof TextView) {
                        TextView textView = (TextView) view;
                        if ("title".equals(textView.getText().toString()) && e.a == -1) {
                            int unused = e.a = textView.getCurrentTextColor();
                            float unused2 = e.c = textView.getTextSize();
                            Logger.d("NotificationStyleHelper", "title color is :" + e.a);
                        }
                        if ("content".equals(textView.getText().toString()) && e.b == -1) {
                            int unused3 = e.b = textView.getCurrentTextColor();
                            float unused4 = e.d = textView.getTextSize();
                            Logger.d("NotificationStyleHelper", "content color is :" + e.b);
                        }
                    }
                }
            });
        } catch (Throwable th) {
            Logger.e("NotificationStyleHelper", "getNotificationColorCompat error:" + th.getMessage());
        }
    }

    public static float d() {
        return d;
    }
}
