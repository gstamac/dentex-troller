package com.zendesk.sdk.model;

import android.content.Context;
import android.os.Build;
import com.zendesk.sdk.R;
import com.zendesk.sdk.power.PowerConfig;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceInfo {
    private static final String DEVICE_INFO_API_VERSION = "device_api";
    private static final String DEVICE_INFO_BATTERY_OK = "device_battery_ok";
    private static final String DEVICE_INFO_DEVICE_NAME = "device_name";
    private static final String DEVICE_INFO_LOW_MEMORY = "device_low_memory";
    private static final String DEVICE_INFO_MANUFACTURER = "device_manufacturer";
    private static final String DEVICE_INFO_MODEL_TYPE = "device_model";
    private static final String DEVICE_INFO_OS_VERSION = "device_os";
    private static final String DEVICE_INFO_TOTAL_MEMORY = "device_total_memory";
    private static final String DEVICE_INFO_USED_MEMORY = "device_used_memory";
    private Context mContext;
    private String mModelName = Build.MODEL;
    private String mVersionName = Build.VERSION.RELEASE;
    private int mVersionCode = Build.VERSION.SDK_INT;
    private String mModelManufacturer = Build.MANUFACTURER;
    private String mModelDeviceName = Build.DEVICE;

    public DeviceInfo(Context context) {
        this.mContext = context;
    }

    public String getModelName() {
        return this.mModelName;
    }

    public String getVersionName() {
        return this.mVersionName;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public String getModelManufacturer() {
        return this.mModelManufacturer;
    }

    public String getModelDeviceName() {
        return this.mModelDeviceName;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(String.format(Locale.US, this.mContext.getString(R.string.rate_my_app_dialog_feedback_device_os_version), getVersionName())).append("\n");
        sb.append(String.format(Locale.US, this.mContext.getString(R.string.rate_my_app_dialog_feedback_device_api_version), Integer.valueOf(getVersionCode()))).append("\n");
        sb.append(String.format(Locale.US, this.mContext.getString(R.string.rate_my_app_dialog_feedback_device_model), getModelName())).append("\n");
        sb.append(String.format(Locale.US, this.mContext.getString(R.string.rate_my_app_dialog_feedback_device_name), getModelDeviceName())).append("\n");
        sb.append(new MemoryInformation(this.mContext).formatMemoryUsage());
        return sb.toString();
    }

    public Map<String, String> getDeviceInfoAsMap() {
        HashMap map = new HashMap();
        map.put(DEVICE_INFO_OS_VERSION, getVersionName());
        map.put(DEVICE_INFO_API_VERSION, String.valueOf(getVersionCode()));
        map.put(DEVICE_INFO_MODEL_TYPE, getModelName());
        map.put(DEVICE_INFO_DEVICE_NAME, getModelDeviceName());
        map.put(DEVICE_INFO_MANUFACTURER, getModelManufacturer());
        MemoryInformation memoryInformation = new MemoryInformation(this.mContext);
        map.put(DEVICE_INFO_TOTAL_MEMORY, String.valueOf(memoryInformation.getTotalMemory()));
        map.put(DEVICE_INFO_USED_MEMORY, String.valueOf(memoryInformation.getUsedMemory()));
        map.put(DEVICE_INFO_LOW_MEMORY, String.valueOf(memoryInformation.isLowMemory()));
        map.put(DEVICE_INFO_BATTERY_OK, String.valueOf(PowerConfig.getInstance(this.mContext).isBatteryOk()));
        return map;
    }
}
