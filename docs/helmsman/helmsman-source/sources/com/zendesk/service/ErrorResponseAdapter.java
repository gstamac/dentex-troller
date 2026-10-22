package com.zendesk.service;

import com.zendesk.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ErrorResponseAdapter implements ErrorResponse {
    private final String errorMessage;

    @Override // com.zendesk.service.ErrorResponse
    public String getResponseBodyType() {
        return "text/plain; charset=UTF8";
    }

    @Override // com.zendesk.service.ErrorResponse
    public int getStatus() {
        return -1;
    }

    @Override // com.zendesk.service.ErrorResponse
    public String getUrl() {
        return "";
    }

    @Override // com.zendesk.service.ErrorResponse
    public boolean isConversionError() {
        return false;
    }

    @Override // com.zendesk.service.ErrorResponse
    public boolean isHTTPError() {
        return false;
    }

    @Override // com.zendesk.service.ErrorResponse
    public boolean isNetworkError() {
        return false;
    }

    public ErrorResponseAdapter() {
        this("");
    }

    public ErrorResponseAdapter(String str) {
        this.errorMessage = str;
    }

    @Override // com.zendesk.service.ErrorResponse
    public String getReason() {
        return this.errorMessage;
    }

    @Override // com.zendesk.service.ErrorResponse
    public String getResponseBody() {
        return this.errorMessage;
    }

    @Override // com.zendesk.service.ErrorResponse
    public List<Header> getResponseHeaders() {
        return CollectionUtils.unmodifiableList(new ArrayList());
    }
}
