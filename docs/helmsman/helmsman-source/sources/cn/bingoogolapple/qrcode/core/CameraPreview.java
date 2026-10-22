package cn.bingoogolapple.qrcode.core;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";
    Camera.AutoFocusCallback autoFocusCB;
    private Runnable doAutoFocus;
    private boolean mAutoFocus;
    private Camera mCamera;
    private CameraConfigurationManager mCameraConfigurationManager;
    private boolean mPreviewing;
    private boolean mSurfaceCreated;

    public CameraPreview(Context context) {
        super(context);
        this.mPreviewing = true;
        this.mAutoFocus = true;
        this.mSurfaceCreated = false;
        this.doAutoFocus = new Runnable() { // from class: cn.bingoogolapple.qrcode.core.CameraPreview.2
            @Override // java.lang.Runnable
            public void run() {
                if (CameraPreview.this.mCamera != null && CameraPreview.this.mPreviewing && CameraPreview.this.mAutoFocus && CameraPreview.this.mSurfaceCreated) {
                    try {
                        CameraPreview.this.mCamera.autoFocus(CameraPreview.this.autoFocusCB);
                    } catch (Exception unused) {
                    }
                }
            }
        };
        this.autoFocusCB = new Camera.AutoFocusCallback() { // from class: cn.bingoogolapple.qrcode.core.CameraPreview.3
            @Override // android.hardware.Camera.AutoFocusCallback
            public void onAutoFocus(boolean z, Camera camera) {
                CameraPreview cameraPreview = CameraPreview.this;
                cameraPreview.postDelayed(cameraPreview.doAutoFocus, 1000L);
            }
        };
    }

    public void setCamera(Camera camera) {
        this.mCamera = camera;
        if (camera != null) {
            CameraConfigurationManager cameraConfigurationManager = new CameraConfigurationManager(getContext());
            this.mCameraConfigurationManager = cameraConfigurationManager;
            cameraConfigurationManager.initFromCameraParameters(this.mCamera);
            getHolder().addCallback(this);
            if (this.mPreviewing) {
                requestLayout();
            } else {
                showCameraPreview();
            }
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mSurfaceCreated = true;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        stopCameraPreview();
        post(new Runnable() { // from class: cn.bingoogolapple.qrcode.core.CameraPreview.1
            @Override // java.lang.Runnable
            public void run() {
                CameraPreview.this.showCameraPreview();
            }
        });
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mSurfaceCreated = false;
        stopCameraPreview();
    }

    public void showCameraPreview() {
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                this.mPreviewing = true;
                camera.setPreviewDisplay(getHolder());
                this.mCameraConfigurationManager.setDesiredCameraParameters(this.mCamera);
                this.mCamera.startPreview();
                if (this.mAutoFocus) {
                    this.mCamera.autoFocus(this.autoFocusCB);
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString(), e);
            }
        }
    }

    public void stopCameraPreview() {
        if (this.mCamera != null) {
            try {
                removeCallbacks(this.doAutoFocus);
                this.mPreviewing = false;
                this.mCamera.cancelAutoFocus();
                this.mCamera.setOneShotPreviewCallback(null);
                this.mCamera.stopPreview();
            } catch (Exception e) {
                Log.e(TAG, e.toString(), e);
            }
        }
    }

    public void openFlashlight() {
        if (flashLightAvailable()) {
            this.mCameraConfigurationManager.openFlashlight(this.mCamera);
        }
    }

    public void closeFlashlight() {
        if (flashLightAvailable()) {
            this.mCameraConfigurationManager.closeFlashlight(this.mCamera);
        }
    }

    @Override // android.view.SurfaceView, android.view.View
    public void onMeasure(int i, int i2) {
        int defaultSize = getDefaultSize(getSuggestedMinimumWidth(), i);
        int defaultSize2 = getDefaultSize(getSuggestedMinimumHeight(), i2);
        CameraConfigurationManager cameraConfigurationManager = this.mCameraConfigurationManager;
        if (cameraConfigurationManager != null && cameraConfigurationManager.getCameraResolution() != null) {
            Point cameraResolution = this.mCameraConfigurationManager.getCameraResolution();
            float f = defaultSize;
            float f2 = defaultSize2;
            float f3 = (f * 1.0f) / f2;
            float f4 = cameraResolution.y;
            float f5 = cameraResolution.x;
            float f6 = (f4 * 1.0f) / f5;
            if (f3 < f6) {
                defaultSize = (int) ((f2 / ((f5 * 1.0f) / f4)) + 0.5f);
            } else {
                defaultSize2 = (int) ((f / f6) + 0.5f);
            }
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(defaultSize, 1073741824), View.MeasureSpec.makeMeasureSpec(defaultSize2, 1073741824));
    }

    private boolean flashLightAvailable() {
        return this.mCamera != null && this.mPreviewing && this.mSurfaceCreated && getContext().getPackageManager().hasSystemFeature("android.hardware.camera.flash");
    }
}
