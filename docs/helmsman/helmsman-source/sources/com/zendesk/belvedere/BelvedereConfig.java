package com.zendesk.belvedere;

import android.content.Context;
import java.util.Arrays;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes2.dex */
public class BelvedereConfig {
    private boolean allowMultiple;
    private BelvedereLogger belvedereLogger;
    private int cameraRequestCodeEnd;
    private int cameraRequestCodeStart;
    private String contentType;
    private String directoryName;
    private int galleryRequestCode;
    private TreeSet<BelvedereSource> sources;

    BelvedereConfig(Builder builder) {
        this.directoryName = builder.mDirectory;
        this.galleryRequestCode = builder.mGalleryRequestCode;
        this.cameraRequestCodeStart = builder.mCameraRequestCodeStart;
        this.cameraRequestCodeEnd = builder.mCameraRequestCodeEnd;
        this.allowMultiple = builder.mAllowMultiple;
        this.contentType = builder.mContentType;
        this.belvedereLogger = builder.mBelvedereLogger;
        this.sources = builder.mSources;
    }

    String getDirectoryName() {
        return this.directoryName;
    }

    int getGalleryRequestCode() {
        return this.galleryRequestCode;
    }

    int getCameraRequestCodeStart() {
        return this.cameraRequestCodeStart;
    }

    int getCameraRequestCodeEnd() {
        return this.cameraRequestCodeEnd;
    }

    boolean allowMultiple() {
        return this.allowMultiple;
    }

    String getContentType() {
        return this.contentType;
    }

    BelvedereLogger getBelvedereLogger() {
        return this.belvedereLogger;
    }

    TreeSet<BelvedereSource> getBelvedereSources() {
        return this.sources;
    }

    public static class Builder {
        private Context mContext;
        private String mDirectory = "belvedere-data";
        private int mGalleryRequestCode = 1602;
        private int mCameraRequestCodeStart = 1603;
        private int mCameraRequestCodeEnd = 1653;
        private boolean mAllowMultiple = true;
        private String mContentType = "*/*";
        private BelvedereLogger mBelvedereLogger = new DefaultLogger();
        private boolean mDebugEnabled = false;
        private TreeSet<BelvedereSource> mSources = new TreeSet<>(Arrays.asList(BelvedereSource.Camera, BelvedereSource.Gallery));

        Builder(Context context) {
            this.mContext = context;
        }

        public Builder withGalleryRequestCode(int i) {
            this.mGalleryRequestCode = i;
            return this;
        }

        public Builder withCameraRequestCode(int i, int i2) {
            if (i2 - i < 5) {
                throw new IllegalArgumentException("The following formula must be apply for the given arguments: (endRequestCode - startRequestCode) >= 5");
            }
            this.mCameraRequestCodeStart = i;
            this.mCameraRequestCodeEnd = i2;
            return this;
        }

        public Builder withAllowMultiple(boolean z) {
            this.mAllowMultiple = z;
            return this;
        }

        public Builder withContentType(String str) {
            this.mContentType = str;
            return this;
        }

        public Builder withCustomLogger(BelvedereLogger belvedereLogger) {
            if (belvedereLogger != null) {
                this.mBelvedereLogger = belvedereLogger;
                return this;
            }
            throw new IllegalArgumentException("Invalid logger provided");
        }

        public Builder withSource(BelvedereSource... belvedereSourceArr) {
            if (belvedereSourceArr == null || belvedereSourceArr.length == 0) {
                throw new IllegalArgumentException("Please provide at least one source");
            }
            this.mSources = new TreeSet<>(Arrays.asList(belvedereSourceArr));
            return this;
        }

        public Builder withDebug(boolean z) {
            this.mDebugEnabled = z;
            return this;
        }

        public Belvedere build() {
            this.mBelvedereLogger.setLoggable(this.mDebugEnabled);
            return new Belvedere(this.mContext, new BelvedereConfig(this));
        }
    }
}
