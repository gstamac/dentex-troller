package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
class ImageViewUtils {
    private static final String TAG = "ImageViewUtils";
    private static Method sAnimateTransformMethod;
    private static boolean sAnimateTransformMethodFetched;

    static void reserveEndAnimateTransform(ImageView imageView, Animator animator) {
    }

    static void startAnimateTransform(ImageView imageView) {
    }

    static void animateTransform(ImageView imageView, Matrix matrix) {
        fetchAnimateTransformMethod();
        Method method = sAnimateTransformMethod;
        if (method != null) {
            try {
                method.invoke(imageView, matrix);
            } catch (IllegalAccessException unused) {
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    private static void fetchAnimateTransformMethod() {
        if (sAnimateTransformMethodFetched) {
            return;
        }
        try {
            Method declaredMethod = ImageView.class.getDeclaredMethod("animateTransform", Matrix.class);
            sAnimateTransformMethod = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Failed to retrieve animateTransform method", e);
        }
        sAnimateTransformMethodFetched = true;
    }

    /* JADX INFO: renamed from: androidx.transition.ImageViewUtils$1, reason: invalid class name */
    static class AnonymousClass1 extends AnimatorListenerAdapter {
        final /* synthetic */ ImageView val$view;

        AnonymousClass1(ImageView imageView) {
            this.val$view = imageView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ImageView.ScaleType scaleType = (ImageView.ScaleType) this.val$view.getTag(R.id.save_scale_type);
            this.val$view.setScaleType(scaleType);
            this.val$view.setTag(R.id.save_scale_type, null);
            if (scaleType == ImageView.ScaleType.MATRIX) {
                ImageView imageView = this.val$view;
                imageView.setImageMatrix((Matrix) imageView.getTag(R.id.save_image_matrix));
                this.val$view.setTag(R.id.save_image_matrix, null);
            }
            animator.removeListener(this);
        }
    }

    private ImageViewUtils() {
    }
}
