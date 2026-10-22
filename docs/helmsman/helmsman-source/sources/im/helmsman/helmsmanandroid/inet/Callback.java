package im.helmsman.helmsmanandroid.inet;

import android.content.DialogInterface;
import android.util.Log;
import com.google.gson.Gson;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.utils.LanguageUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public abstract class Callback<T> implements retrofit2.Callback<T> {
    public abstract void onFailure(ErrorCode errorCode);

    public abstract void onResponse(Response<T> response);

    @Override // retrofit2.Callback
    public void onResponse(Call<T> call, Response<T> response) throws Throwable {
        if (response.code() < 200 || response.code() > 299) {
            Log.e("callback:code", response.code() + "");
            Log.e("callback:isSuccess", "" + response.isSuccessful());
            try {
                String strString = response.errorBody().string();
                Log.e("callback:response", strString);
                onFailure(call, new Throwable(strString));
                return;
            } catch (IOException e) {
                e.printStackTrace();
                onFailure(call, e);
                return;
            }
        }
        onResponse(response);
    }

    @Override // retrofit2.Callback
    public void onFailure(Call<T> call, Throwable th) throws Throwable {
        ErrorCode errorCode;
        String message = th.getMessage();
        if (message != null) {
            Log.e("jsonError", message);
        }
        ErrorCode errorCode2 = null;
        try {
            try {
                errorCode = (ErrorCode) new Gson().fromJson(message, (Class) ErrorCode.class);
                try {
                    if (errorCode == null) {
                        errorCode = new ErrorCode("unknown error");
                    } else if (LanguageUtils.isZh()) {
                        errorCode.setTempMsg(errorCode.getMsg_chn());
                        errorCode.setTempTitle(errorCode.getTitle_chn());
                    } else {
                        errorCode.setTempMsg(errorCode.getMsg());
                        errorCode.setTempTitle(errorCode.getTitle());
                    }
                } catch (Exception unused) {
                    errorCode2 = errorCode;
                    errorCode = new ErrorCode("unknown error");
                } catch (Throwable th2) {
                    th = th2;
                    errorCode2 = errorCode;
                    initHintWindow(errorCode2);
                    ViewUtils.cancelProgressDialog();
                    throw th;
                }
            } catch (Exception unused2) {
            }
            initHintWindow(errorCode);
            ViewUtils.cancelProgressDialog();
            onFailure(errorCode);
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private void initHintWindow(ErrorCode errorCode) {
        int type = errorCode.getType();
        if (type == 0) {
            onFailure(errorCode);
            return;
        }
        if (type == 1) {
            ViewUtils.ShowToast(errorCode.getTempMsg());
        } else if (type == 2) {
            ViewUtils.showAlertDialog(errorCode.getTempTitle(), errorCode.getTempMsg(), (CharSequence) null, (CharSequence) null, (DialogInterface.OnClickListener) null);
        } else {
            if (type != 3) {
                return;
            }
            Log.e("ErrorCode", errorCode.toString());
        }
    }
}
