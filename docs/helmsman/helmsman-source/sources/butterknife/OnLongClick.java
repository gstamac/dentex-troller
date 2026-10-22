package butterknife;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import com.facebook.internal.ServerProtocol;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ListenerClass(method = {@ListenerMethod(defaultReturn = ServerProtocol.DIALOG_RETURN_SCOPES_TRUE, name = "onLongClick", parameters = {"android.view.View"}, returnType = TypedValues.Custom.S_BOOLEAN)}, setter = "setOnLongClickListener", targetType = "android.view.View", type = "android.view.View.OnLongClickListener")
public @interface OnLongClick {
    int[] value() default {-1};
}
