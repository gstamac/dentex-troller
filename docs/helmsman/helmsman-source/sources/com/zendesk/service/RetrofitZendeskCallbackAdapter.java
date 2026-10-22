package com.zendesk.service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class RetrofitZendeskCallbackAdapter<E, F> implements Callback<E> {
    protected static final RequestExtractor DEFAULT_EXTRACTOR = new DefaultExtractor();
    private final ZendeskCallback<F> mCallback;
    private final RequestExtractor<E, F> mExtractor;

    public interface RequestExtractor<E, F> {
        F extract(E e);
    }

    public RetrofitZendeskCallbackAdapter(ZendeskCallback<F> zendeskCallback, RequestExtractor<E, F> requestExtractor) {
        this.mCallback = zendeskCallback;
        this.mExtractor = requestExtractor;
    }

    public RetrofitZendeskCallbackAdapter(ZendeskCallback<F> zendeskCallback) {
        this(zendeskCallback, DEFAULT_EXTRACTOR);
    }

    @Override // retrofit2.Callback
    public void onResponse(Call<E> call, Response<E> response) {
        if (this.mCallback != null) {
            if (response.isSuccessful()) {
                this.mCallback.onSuccess(this.mExtractor.extract(response.body()));
            } else {
                this.mCallback.onError(RetrofitErrorResponse.response(response));
            }
        }
    }

    @Override // retrofit2.Callback
    public void onFailure(Call<E> call, Throwable th) {
        ZendeskCallback<F> zendeskCallback = this.mCallback;
        if (zendeskCallback != null) {
            zendeskCallback.onError(RetrofitErrorResponse.throwable(th));
        }
    }

    static final class DefaultExtractor<E> implements RequestExtractor<E, E> {
        @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
        public E extract(E e) {
            return e;
        }

        DefaultExtractor() {
        }
    }
}
