package cn.jiguang.ae;

import com.facebook.appevents.AppEventsConstants;
import java.io.File;
import java.io.FileFilter;

/* JADX INFO: loaded from: classes.dex */
final class e implements FileFilter {
    e() {
    }

    @Override // java.io.FileFilter
    public final boolean accept(File file) {
        return (file.getName().length() == 24 || file.getName().equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) ? false : true;
    }
}
