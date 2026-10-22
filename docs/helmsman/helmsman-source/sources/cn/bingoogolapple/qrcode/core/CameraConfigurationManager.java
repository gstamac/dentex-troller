package cn.bingoogolapple.qrcode.core;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Display;
import android.view.WindowManager;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import kotlinx.coroutines.DebugKt;

/* JADX INFO: loaded from: classes.dex */
final class CameraConfigurationManager {
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    private static final int TEN_DESIRED_ZOOM = 27;
    private Point cameraResolution;
    private final Context mContext;
    private Point mScreenResolution;

    public CameraConfigurationManager(Context context) {
        this.mContext = context;
    }

    public void initFromCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Display defaultDisplay = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
        this.mScreenResolution = new Point(defaultDisplay.getWidth(), defaultDisplay.getHeight());
        Point point = new Point();
        point.x = this.mScreenResolution.x;
        point.y = this.mScreenResolution.y;
        if (this.mScreenResolution.x < this.mScreenResolution.y) {
            point.x = this.mScreenResolution.y;
            point.y = this.mScreenResolution.x;
        }
        this.cameraResolution = getCameraResolution(parameters, point);
    }

    public Point getCameraResolution() {
        return this.cameraResolution;
    }

    public void setDesiredCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(this.cameraResolution.x, this.cameraResolution.y);
        setZoom(parameters);
        camera.setDisplayOrientation(getDisplayOrientation());
        camera.setParameters(parameters);
    }

    public void openFlashlight(Camera camera) {
        doSetTorch(camera, true);
    }

    public void closeFlashlight(Camera camera) {
        doSetTorch(camera, false);
    }

    private void doSetTorch(Camera camera, boolean z) {
        String strFindSettableValue;
        Camera.Parameters parameters = camera.getParameters();
        if (z) {
            strFindSettableValue = findSettableValue(parameters.getSupportedFlashModes(), "torch", DebugKt.DEBUG_PROPERTY_VALUE_ON);
        } else {
            strFindSettableValue = findSettableValue(parameters.getSupportedFlashModes(), DebugKt.DEBUG_PROPERTY_VALUE_OFF);
        }
        if (strFindSettableValue != null) {
            parameters.setFlashMode(strFindSettableValue);
        }
        camera.setParameters(parameters);
    }

    private static String findSettableValue(Collection<String> collection, String... strArr) {
        if (collection != null) {
            for (String str : strArr) {
                if (collection.contains(str)) {
                    return str;
                }
            }
        }
        return null;
    }

    public int getDisplayOrientation() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int i = 0;
        Camera.getCameraInfo(0, cameraInfo);
        int rotation = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getRotation();
        if (rotation != 0) {
            if (rotation == 1) {
                i = 90;
            } else if (rotation == 2) {
                i = 180;
            } else if (rotation == 3) {
                i = 270;
            }
        }
        if (cameraInfo.facing == 1) {
            return (360 - ((cameraInfo.orientation + i) % 360)) % 360;
        }
        return ((cameraInfo.orientation - i) + 360) % 360;
    }

    private static Point getCameraResolution(Camera.Parameters parameters, Point point) {
        Point pointFindBestPreviewSizeValue = findBestPreviewSizeValue(parameters.getSupportedPreviewSizes(), point);
        return pointFindBestPreviewSizeValue == null ? new Point((point.x >> 3) << 3, (point.y >> 3) << 3) : pointFindBestPreviewSizeValue;
    }

    private static Point findBestPreviewSizeValue(List<Camera.Size> list, Point point) {
        int i = 0;
        int i2 = Integer.MAX_VALUE;
        int i3 = 0;
        for (Camera.Size size : list) {
            int i4 = size.width;
            int i5 = size.height;
            int iAbs = Math.abs(i4 - point.x) + Math.abs(i5 - point.y);
            if (iAbs == 0) {
                i3 = i5;
                i = i4;
                break;
            }
            if (iAbs < i2) {
                i3 = i5;
                i = i4;
                i2 = iAbs;
            }
        }
        if (i <= 0 || i3 <= 0) {
            return null;
        }
        return new Point(i, i3);
    }

    private static int findBestMotZoomValue(CharSequence charSequence, int i) {
        int i2 = 0;
        for (String str : COMMA_PATTERN.split(charSequence)) {
            try {
                double d = Double.parseDouble(str.trim());
                int i3 = (int) (10.0d * d);
                if (Math.abs(((double) i) - d) < Math.abs(i - i2)) {
                    i2 = i3;
                }
            } catch (NumberFormatException unused) {
                return i;
            }
        }
        return i2;
    }

    private void setZoom(Camera.Parameters parameters) {
        String str = parameters.get("zoom-supported");
        if (str == null || Boolean.parseBoolean(str)) {
            String str2 = parameters.get("max-zoom");
            int iFindBestMotZoomValue = 27;
            if (str2 != null) {
                try {
                    int i = (int) (Double.parseDouble(str2) * 10.0d);
                    if (27 > i) {
                        iFindBestMotZoomValue = i;
                    }
                } catch (NumberFormatException unused) {
                }
            }
            String str3 = parameters.get("taking-picture-zoom-max");
            if (str3 != null) {
                try {
                    int i2 = Integer.parseInt(str3);
                    if (iFindBestMotZoomValue > i2) {
                        iFindBestMotZoomValue = i2;
                    }
                } catch (NumberFormatException unused2) {
                }
            }
            String str4 = parameters.get("mot-zoom-values");
            if (str4 != null) {
                iFindBestMotZoomValue = findBestMotZoomValue(str4, iFindBestMotZoomValue);
            }
            String str5 = parameters.get("mot-zoom-step");
            if (str5 != null) {
                try {
                    int i3 = (int) (Double.parseDouble(str5.trim()) * 10.0d);
                    if (i3 > 1) {
                        iFindBestMotZoomValue -= iFindBestMotZoomValue % i3;
                    }
                } catch (NumberFormatException unused3) {
                }
            }
            if (str2 != null || str4 != null) {
                parameters.set("zoom", String.valueOf(((double) iFindBestMotZoomValue) / 10.0d));
            }
            if (str3 != null) {
                parameters.set("taking-picture-zoom", iFindBestMotZoomValue);
            }
        }
    }
}
