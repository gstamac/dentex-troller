package im.helmsman.helmsmanandroid.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;

/* JADX INFO: loaded from: classes2.dex */
public class ShareUtils {
    public static void shareToWx(boolean z, String str, Bitmap bitmap, String str2, String str3, int i) {
        WXWebpageObject wXWebpageObject = new WXWebpageObject();
        wXWebpageObject.webpageUrl = str;
        WXMediaMessage wXMediaMessage = new WXMediaMessage(wXWebpageObject);
        wXMediaMessage.title = str2;
        wXMediaMessage.description = getShareDescription(str3, i);
        if (bitmap != null) {
            wXMediaMessage.setThumbImage(bitmap);
        } else {
            wXMediaMessage.thumbData = new byte[1];
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage";
        req.message = wXMediaMessage;
        req.scene = z ? 1 : 0;
        MyApplication.getWxApi().sendReq(req);
    }

    public static void shareToFaceBook(String str, String str2, String str3, String str4, int i) {
        if (TextUtils.isEmpty(str)) {
            Log.e("shareToFaceBook", "分享失败,航线连接为空");
            return;
        }
        ShareLinkContent shareLinkContentBuild = new ShareLinkContent.Builder().setContentUrl(Uri.parse(str)).setImageUrl(Uri.parse(str2)).setContentTitle(str3).setContentDescription(getShareDescription(str4, i)).build();
        ShareButton shareButton = new ShareButton(Base2Activity.getTopActivity());
        shareButton.setShareContent(shareLinkContentBuild);
        shareButton.performClick();
    }

    public static String getShareDescription(String str, int i) {
        Context context = MyApplication.getContext();
        return context.getString(R.string.waypoint_distance) + " " + str + " km,\n" + context.getString(R.string.waypoint_count) + i + " " + context.getString(R.string.ge);
    }
}
