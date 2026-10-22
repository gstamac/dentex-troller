package com.zendesk.service;

import com.zendesk.util.StringUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Headers;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class RetrofitErrorResponse implements ErrorResponse {
    private static final String LOG_TAG = "RetrofitErrorResponse";
    private Response mResponse;
    private Throwable mThrowable;

    public static RetrofitErrorResponse throwable(Throwable th) {
        return new RetrofitErrorResponse(th);
    }

    private RetrofitErrorResponse(Throwable th) {
        this.mThrowable = th;
    }

    public static RetrofitErrorResponse response(Response response) {
        return new RetrofitErrorResponse(response);
    }

    private RetrofitErrorResponse(Response response) {
        this.mResponse = response;
    }

    @Override // com.zendesk.service.ErrorResponse
    public boolean isNetworkError() {
        Throwable th = this.mThrowable;
        return th != null && (th instanceof IOException);
    }

    @Override // com.zendesk.service.ErrorResponse
    public boolean isConversionError() {
        return isNetworkError();
    }

    @Override // com.zendesk.service.ErrorResponse
    public boolean isHTTPError() {
        Response response;
        return (this.mThrowable != null || (response = this.mResponse) == null || response.isSuccessful()) ? false : true;
    }

    @Override // com.zendesk.service.ErrorResponse
    public String getReason() {
        Throwable th = this.mThrowable;
        if (th != null) {
            return th.getMessage();
        }
        StringBuilder sb = new StringBuilder();
        Response response = this.mResponse;
        if (response != null) {
            if (StringUtils.hasLength(response.message())) {
                sb.append(this.mResponse.message());
            } else {
                sb.append(this.mResponse.code());
            }
        }
        return sb.toString();
    }

    @Override // com.zendesk.service.ErrorResponse
    public int getStatus() {
        Response response = this.mResponse;
        if (response != null) {
            return response.code();
        }
        return -1;
    }

    @Override // com.zendesk.service.ErrorResponse
    public String getUrl() {
        Response response = this.mResponse;
        return (response == null || response.raw().request() == null || this.mResponse.raw().request().url() == null) ? "" : this.mResponse.raw().request().url().getUrl();
    }

    @Override // com.zendesk.service.ErrorResponse
    public String getResponseBody() {
        Response response = this.mResponse;
        if (response != null && response.errorBody() != null) {
            try {
                return new String(this.mResponse.errorBody().bytes(), "UTF-8");
            } catch (UnsupportedEncodingException unused) {
                throw new AssertionError("UTF-8 must be supported");
            } catch (IOException unused2) {
            }
        }
        return "";
    }

    @Override // com.zendesk.service.ErrorResponse
    public String getResponseBodyType() {
        Response response = this.mResponse;
        return (response == null || response.errorBody() == null) ? "" : this.mResponse.errorBody().get$contentType().getMediaType();
    }

    @Override // com.zendesk.service.ErrorResponse
    public List<Header> getResponseHeaders() {
        if (this.mThrowable != null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        Response response = this.mResponse;
        if (response != null && response.headers() != null && this.mResponse.headers().size() > 0) {
            Headers headers = this.mResponse.headers();
            for (String str : headers.names()) {
                arrayList.add(new Header(str, headers.get(str)));
            }
        }
        return arrayList;
    }
}
