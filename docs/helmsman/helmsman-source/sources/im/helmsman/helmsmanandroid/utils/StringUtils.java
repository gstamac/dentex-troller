package im.helmsman.helmsmanandroid.utils;

import android.content.Intent;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.GetUserInfoByUserNameResultModel;
import im.helmsman.helmsmanandroid.ui.activity.FriendsMessageInfoActivity;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class StringUtils {
    private static final Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private static final Pattern phone = Pattern.compile("^((13[0-9])|170|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    public static boolean isPhone(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return phone.matcher(str).matches();
    }

    public static boolean isEmail(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return emailer.matcher(str).matches();
    }

    public static boolean isHaveSpecialCharacters(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        for (char c : str.toCharArray()) {
            System.out.println((int) c);
            if ((c < '0' || c > '9') && ((c < 'A' || c > 'Z') && ((c < 'a' || c > 'z') && c != '_'))) {
                return false;
            }
        }
        return true;
    }

    public static String addRandomInOldUrl(String str) {
        int iIndexOf = str.indexOf("?");
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.insert(iIndexOf + 1, String.valueOf(System.currentTimeMillis()));
        return stringBuffer.toString();
    }

    public static boolean checkUserName(String str) {
        return Pattern.compile("[A-Za-z0-9][A-Za-z0-9_]{4,}").matcher(str).matches();
    }

    public static SpannableString userNameToBlueColor(String str) {
        Matcher matcher = Pattern.compile("@[A-Za-z0-9][A-Za-z0-9_]{4,}\\s").matcher(str);
        SpannableString spannableString = new SpannableString(str);
        while (matcher.find()) {
            Log.e("matcher", matcher.start() + " " + matcher.end());
            spannableString.setSpan(new ForegroundColorSpan(-16776961), matcher.start(), matcher.end() - 1, 33);
            spannableString.setSpan(new ClickTextListener(str.substring(matcher.start() + 1, matcher.end())), matcher.start(), matcher.end() - 1, 33);
        }
        return spannableString;
    }

    static class ClickTextListener extends ClickableSpan {
        private String username;

        public ClickTextListener(String str) {
            this.username = str.trim();
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(final View view) {
            RequestInetUtils.instance().getUserInfoByUsername(this.username, new Callback<GetUserInfoByUserNameResultModel>() { // from class: im.helmsman.helmsmanandroid.utils.StringUtils.ClickTextListener.1
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<GetUserInfoByUserNameResultModel> response) {
                    Intent intent = new Intent(Base2Activity.getTopActivity(), (Class<?>) FriendsMessageInfoActivity.class);
                    intent.putExtra("username", ClickTextListener.this.username);
                    Base2Activity.getTopActivity().startActivity(intent);
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    if (errorCode.getError_code() == 1005) {
                        Snackbar.make(view, MyApplication.getInstance().getString(R.string.username_not_exist), 0).show();
                    }
                }
            });
        }

        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            textPaint.setColor(MyApplication.getInstance().getResources().getColor(R.color.at_text_blue));
        }
    }

    public static String getCountryNameByCode(String str) {
        return changeRegionName(str, new Locale("", String.valueOf(str)).getDisplayCountry());
    }

    public static String changeRegionName(String str, String str2) {
        if (str.equalsIgnoreCase("HK")) {
            return MyApplication.getInstance().getString(R.string.Hong_Kong);
        }
        if (str.equalsIgnoreCase("MO")) {
            return MyApplication.getInstance().getString(R.string.Macau);
        }
        return str.equalsIgnoreCase("TW") ? MyApplication.getInstance().getString(R.string.Taiwan) : str2;
    }
}
