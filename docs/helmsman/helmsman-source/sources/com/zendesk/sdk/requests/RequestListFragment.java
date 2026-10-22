package com.zendesk.sdk.requests;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.model.request.Request;
import com.zendesk.sdk.network.NetworkAware;
import com.zendesk.sdk.network.RequestLoadingListener;
import com.zendesk.sdk.network.Retryable;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.ui.LoadingState;
import com.zendesk.sdk.ui.NetworkAwareActionbarActivity;
import com.zendesk.sdk.util.NetworkUtils;
import com.zendesk.sdk.util.UiUtils;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.SafeZendeskCallback;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.CollectionUtils;
import com.zendesk.util.StringUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes2.dex */
public class RequestListFragment extends ListFragment implements NetworkAware {
    private static final String FILTER_BY_STATUSES = "new,open,pending,hold,solved";
    private static final String LOG_TAG = "RequestListFragment";
    private View mEmptyView;
    private AtomicBoolean mIsLoading = new AtomicBoolean(false);
    private RequestLoadingListener mListener;
    private View mProgressView;
    private SafeZendeskCallback<List<Request>> mRequestsCallback;
    private Retryable mRetryable;

    @Override // androidx.fragment.app.ListFragment, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_request_list, viewGroup, false);
        this.mProgressView = viewInflate.findViewById(R.id.request_list_fragment_progress);
        this.mEmptyView = viewInflate.findViewById(android.R.id.empty);
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        setupCallback();
        refreshRequests();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        teardownCallback();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Retryable) {
            this.mRetryable = (Retryable) context;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mRetryable = null;
    }

    public void refreshRequests() {
        if (!this.mIsLoading.compareAndSet(false, true)) {
            Logger.d(LOG_TAG, "Already loading requests, skipping refreshRequests()", new Object[0]);
            return;
        }
        if (!ZendeskConfig.INSTANCE.isAuthenticationAvailable()) {
            Logger.e(LOG_TAG, "Cannot load requests because no identity has been set up. See ZendeskConfig.INSTANCE.setIdentity(Identity identity)", new Object[0]);
            RequestLoadingListener requestLoadingListener = this.mListener;
            if (requestLoadingListener != null) {
                requestLoadingListener.onLoadError(new ErrorResponseAdapter("Cannot load requests because no identity has been set up. See ZendeskConfig.INSTANCE.setIdentity(Identity identity)"));
                return;
            }
            return;
        }
        if (!NetworkUtils.isConnected(getContext())) {
            Logger.d(LOG_TAG, "No network connect", new Object[0]);
            setLoadingState(LoadingState.ERRORED);
            this.mIsLoading.set(false);
            FragmentActivity activity = getActivity();
            if (activity == null || !(activity instanceof NetworkAwareActionbarActivity)) {
                return;
            }
            ((NetworkAwareActionbarActivity) activity).onNetworkUnavailable();
            return;
        }
        Logger.d(LOG_TAG, "Requesting requests from the network", new Object[0]);
        setLoadingState(LoadingState.LOADING);
        RequestLoadingListener requestLoadingListener2 = this.mListener;
        if (requestLoadingListener2 != null) {
            requestLoadingListener2.onLoadStarted();
        }
        try {
            ZendeskConfig.INSTANCE.provider().requestProvider().getRequests(FILTER_BY_STATUSES, this.mRequestsCallback);
        } catch (Exception unused) {
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    public void setRequestLoadingListener(RequestLoadingListener requestLoadingListener) {
        this.mListener = requestLoadingListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLoadingState(LoadingState loadingState) {
        if (loadingState == null) {
            Logger.w(LOG_TAG, "LoadingState was null, nothing to do", new Object[0]);
            return;
        }
        if (!isAdded()) {
            Logger.w(LOG_TAG, "Activity is not attached, nothing to do", new Object[0]);
            return;
        }
        int i = AnonymousClass2.$SwitchMap$com$zendesk$sdk$ui$LoadingState[loadingState.ordinal()];
        if (i == 1) {
            UiUtils.setVisibility(getListView(), 8);
            UiUtils.setVisibility(this.mProgressView, 0);
            UiUtils.setVisibility(this.mEmptyView, 8);
            Retryable retryable = this.mRetryable;
            if (retryable != null) {
                retryable.onRetryUnavailable();
                return;
            }
            return;
        }
        if (i == 2) {
            UiUtils.setVisibility(getListView(), 0);
            UiUtils.setVisibility(this.mProgressView, 8);
            UiUtils.setVisibility(this.mEmptyView, 8);
            Retryable retryable2 = this.mRetryable;
            if (retryable2 != null) {
                retryable2.onRetryUnavailable();
                return;
            }
            return;
        }
        if (i != 3) {
            return;
        }
        UiUtils.setVisibility(getListView(), 0);
        UiUtils.setVisibility(this.mProgressView, 8);
        UiUtils.setVisibility(this.mEmptyView, 8);
        Retryable retryable3 = this.mRetryable;
        if (retryable3 != null) {
            retryable3.onRetryAvailable(getString(R.string.request_list_fragment_error_message), new View.OnClickListener() { // from class: com.zendesk.sdk.requests.RequestListFragment.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    RequestListFragment.this.refreshRequests();
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.zendesk.sdk.requests.RequestListFragment$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$zendesk$sdk$ui$LoadingState;

        static {
            int[] iArr = new int[LoadingState.values().length];
            $SwitchMap$com$zendesk$sdk$ui$LoadingState = iArr;
            try {
                iArr[LoadingState.LOADING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$ui$LoadingState[LoadingState.DISPLAYING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$ui$LoadingState[LoadingState.ERRORED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    private void setupCallback() {
        this.mRequestsCallback = SafeZendeskCallback.from(new RequestsCallback());
    }

    private void teardownCallback() {
        this.mRequestsCallback.cancel();
        this.mRequestsCallback = null;
        this.mIsLoading.set(false);
    }

    @Override // com.zendesk.sdk.network.NetworkAware
    public void onNetworkAvailable() {
        Logger.d(LOG_TAG, "RequestListFragment received onNetworkAvailable", new Object[0]);
        refreshRequests();
    }

    @Override // com.zendesk.sdk.network.NetworkAware
    public void onNetworkUnavailable() {
        Logger.d(LOG_TAG, "RequestListFragment received onNetworkUnavailable", new Object[0]);
    }

    class RequestsCallback extends ZendeskCallback<List<Request>> {
        RequestsCallback() {
        }

        @Override // com.zendesk.service.ZendeskCallback
        public void onSuccess(List<Request> list) {
            RequestListFragment.this.mIsLoading.set(false);
            RequestListFragment.this.setLoadingState(LoadingState.DISPLAYING);
            if (RequestListFragment.this.mListener != null) {
                RequestListFragment.this.mListener.onLoadFinished(CollectionUtils.isEmpty(list) ? 0 : list.size());
            }
            final ListView listView = RequestListFragment.this.getListView();
            if (listView == null || RequestListFragment.this.getActivity() == null) {
                return;
            }
            RequestListFragment.this.setListAdapter(new RequestsAdapter(RequestListFragment.this.getActivity(), list));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.zendesk.sdk.requests.RequestListFragment.RequestsCallback.1
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    if (!NetworkUtils.isConnected(RequestListFragment.this.getActivity())) {
                        Logger.d(RequestListFragment.LOG_TAG, "Ignoring conversation selection because there is no network connection", new Object[0]);
                        return;
                    }
                    Request request = (Request) listView.getAdapter().getItem(i);
                    Intent intent = new Intent(RequestListFragment.this.getActivity(), (Class<?>) ViewRequestActivity.class);
                    intent.putExtra("requestId", request.getId());
                    if (StringUtils.hasLength(request.getSubject())) {
                        intent.putExtra("subject", request.getSubject());
                    }
                    RequestListFragment.this.startActivity(intent);
                }
            });
        }

        @Override // com.zendesk.service.ZendeskCallback
        public void onError(ErrorResponse errorResponse) {
            RequestListFragment.this.mIsLoading.set(false);
            Logger.w(RequestListFragment.LOG_TAG, "Failed to fetch requests: " + errorResponse.getReason() + " status " + errorResponse.getStatus(), new Object[0]);
            RequestListFragment.this.setLoadingState(LoadingState.ERRORED);
            if (RequestListFragment.this.mListener != null) {
                RequestListFragment.this.mListener.onLoadError(errorResponse);
            }
        }
    }
}
