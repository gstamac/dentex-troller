package com.zendesk.sdk.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import com.squareup.picasso.Transformation;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public enum ZendeskPicassoTransformationFactory {
    INSTANCE;

    public Transformation getResizeTransformationWidth(int i) {
        return new ResizeTransformWidth(i);
    }

    public Transformation getResizeTransformationHeight(int i) {
        return new ResizeTransformHeight(i);
    }

    public Transformation getRoundedTransformation(int i, int i2) {
        return new RoundedTransformation(i, i2);
    }

    private class ResizeTransformWidth implements Transformation {
        private final int maxWidth;

        public ResizeTransformWidth(int i) {
            this.maxWidth = i;
        }

        @Override // com.squareup.picasso.Transformation
        public Bitmap transform(Bitmap bitmap) {
            int height;
            int width = bitmap.getWidth();
            int width2 = this.maxWidth;
            if (width > width2) {
                height = (int) (((double) width2) * (((double) bitmap.getHeight()) / ((double) bitmap.getWidth())));
            } else {
                height = bitmap.getHeight();
                width2 = bitmap.getWidth();
            }
            Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmap, width2, height, false);
            if (bitmapCreateScaledBitmap != bitmap) {
                bitmap.recycle();
            }
            return bitmapCreateScaledBitmap;
        }

        @Override // com.squareup.picasso.Transformation
        public String key() {
            return "max-width-" + this.maxWidth;
        }
    }

    private class ResizeTransformHeight implements Transformation {
        private final int maxHeight;

        public ResizeTransformHeight(int i) {
            this.maxHeight = i;
        }

        @Override // com.squareup.picasso.Transformation
        public Bitmap transform(Bitmap bitmap) {
            int width;
            int height = bitmap.getHeight();
            int height2 = this.maxHeight;
            if (height > height2) {
                width = (int) (((double) height2) * (((double) bitmap.getWidth()) / ((double) bitmap.getHeight())));
            } else {
                height2 = bitmap.getHeight();
                width = bitmap.getWidth();
            }
            Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height2, false);
            if (bitmapCreateScaledBitmap != bitmap) {
                bitmap.recycle();
            }
            return bitmapCreateScaledBitmap;
        }

        @Override // com.squareup.picasso.Transformation
        public String key() {
            return "max-height-" + this.maxHeight;
        }
    }

    private class RoundedTransformation implements Transformation {
        private final int margin;
        private final int radius;

        public RoundedTransformation(int i, int i2) {
            this.radius = i;
            this.margin = i2;
        }

        @Override // com.squareup.picasso.Transformation
        public Bitmap transform(Bitmap bitmap) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            int i = this.margin;
            RectF rectF = new RectF(i, i, bitmap.getWidth() - this.margin, bitmap.getHeight() - this.margin);
            int i2 = this.radius;
            canvas.drawRoundRect(rectF, i2, i2, paint);
            if (bitmap != bitmapCreateBitmap) {
                bitmap.recycle();
            }
            return bitmapCreateBitmap;
        }

        @Override // com.squareup.picasso.Transformation
        public String key() {
            return String.format(Locale.US, "rounded-%s-%s", Integer.valueOf(this.radius), Integer.valueOf(this.margin));
        }
    }
}
