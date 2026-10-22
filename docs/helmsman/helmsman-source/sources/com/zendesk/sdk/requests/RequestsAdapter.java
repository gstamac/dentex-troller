package com.zendesk.sdk.requests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.model.request.Request;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.ui.ListRowView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
class RequestsAdapter extends ArrayAdapter<Request> {
    public RequestsAdapter(Context context, List<Request> list) {
        super(context, R.layout.row_request, list);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        RequestRow requestRow;
        if (view instanceof RequestRow) {
            requestRow = (RequestRow) view;
        } else {
            requestRow = new RequestRow(getContext());
        }
        requestRow.bind(getItem(i));
        return requestRow.getView();
    }

    private static class RequestRow extends RelativeLayout implements ListRowView<Request> {
        private static final String CREATED_AT_DATE_FORMAT = "dd MMMM yyy";
        private static final String LOG_TAG = "RequestRow";
        private final Context context;
        private TextView date;
        private TextView description;
        private ImageView unreadIndicator;

        @Override // com.zendesk.sdk.ui.ListRowView
        public View getView() {
            return this;
        }

        public RequestRow(Context context) {
            super(context);
            this.context = context;
            initialise();
        }

        private void initialise() {
            View viewInflate = LayoutInflater.from(this.context).inflate(R.layout.row_request, this);
            if (viewInflate != null) {
                this.description = (TextView) viewInflate.findViewById(R.id.row_request_description);
                this.date = (TextView) viewInflate.findViewById(R.id.row_request_date);
                this.unreadIndicator = (ImageView) viewInflate.findViewById(R.id.row_request_unread_indicator);
            }
        }

        @Override // com.zendesk.sdk.ui.ListRowView
        public void bind(Request request) {
            if (request == null) {
                Logger.w(LOG_TAG, "request is null, nothing to bind.", new Object[0]);
                return;
            }
            TextView textView = this.description;
            if (textView != null) {
                textView.setText(request.getDescription());
            }
            if (this.date != null && request.getCreatedAt() != null) {
                this.date.setText(new SimpleDateFormat(CREATED_AT_DATE_FORMAT, Locale.getDefault()).format(request.getUpdatedAt()));
            }
            if (this.unreadIndicator != null) {
                Integer commentCount = request.getId() == null ? null : ZendeskConfig.INSTANCE.storage().requestStorage().getCommentCount(request.getId());
                if (commentCount != null && request.getCommentCount() != null) {
                    if (commentCount.intValue() < request.getCommentCount().intValue()) {
                        setUnreadIndicator(true);
                        return;
                    } else {
                        setUnreadIndicator(false);
                        return;
                    }
                }
                setUnreadIndicator(true);
            }
        }

        public void setUnreadIndicator(boolean z) {
            ImageView imageView = this.unreadIndicator;
            if (imageView != null) {
                if (z) {
                    imageView.setVisibility(0);
                } else {
                    imageView.setVisibility(4);
                }
            }
        }
    }
}
