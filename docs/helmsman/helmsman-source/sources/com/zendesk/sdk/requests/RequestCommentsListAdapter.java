package com.zendesk.sdk.requests;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.zendesk.sdk.R;
import com.zendesk.sdk.model.request.Attachment;
import com.zendesk.sdk.model.request.CommentResponse;
import com.zendesk.sdk.network.impl.ZendeskPicassoProvider;
import com.zendesk.sdk.ui.ListRowView;
import com.zendesk.sdk.ui.ZendeskPicassoTransformationFactory;
import com.zendesk.util.CollectionUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
class RequestCommentsListAdapter extends ArrayAdapter<CommentWithUser> {
    private static final int VIEW_COUNT = 2;
    private static final int VIEW_TYPE_AGENT = 0;
    private static final int VIEW_TYPE_ENDUSER = 1;
    private AttachmentOnClickListener mAttachmentOnClickListener;
    private final Context mContext;

    interface AttachmentOnClickListener {
        void onAttachmentClicked(View view, Attachment attachment);
    }

    private interface IdHolder {
        int getAttachmentsContainerId();

        int getAvatarId();

        int getContainerId();

        int getDateId();

        int getNameId();

        int getResponseId();
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 2;
    }

    public RequestCommentsListAdapter(Context context, int i, List<CommentWithUser> list, AttachmentOnClickListener attachmentOnClickListener) {
        super(context, i, list);
        this.mContext = context;
        this.mAttachmentOnClickListener = attachmentOnClickListener;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int i) {
        return !getItem(i).getAuthor().isAgent() ? 1 : 0;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        CommentRow commentRow;
        CommentWithUser item = getItem(i);
        if (item.getAuthor().isAgent()) {
            commentRow = new CommentRow(this.mContext, new AgentRowIdHolder(), this.mAttachmentOnClickListener);
        } else {
            commentRow = new CommentRow(this.mContext, new EndUserRowIdHolder(), this.mAttachmentOnClickListener);
        }
        commentRow.bind(item);
        return commentRow.getView();
    }

    private static class AgentRowIdHolder implements IdHolder {
        private AgentRowIdHolder() {
        }

        @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.IdHolder
        public int getContainerId() {
            return R.layout.row_agent_comment;
        }

        @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.IdHolder
        public int getAvatarId() {
            return R.id.view_request_agent_avatar_imageview;
        }

        @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.IdHolder
        public int getNameId() {
            return R.id.view_request_agent_name_textview;
        }

        @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.IdHolder
        public int getResponseId() {
            return R.id.view_request_agent_response_textview;
        }

        @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.IdHolder
        public int getDateId() {
            return R.id.view_request_agent_comment_date;
        }

        @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.IdHolder
        public int getAttachmentsContainerId() {
            return R.id.view_request_agent_response_attachment_container;
        }
    }

    private static class EndUserRowIdHolder implements IdHolder {
        private EndUserRowIdHolder() {
        }

        @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.IdHolder
        public int getContainerId() {
            return R.layout.row_end_user_comment;
        }

        @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.IdHolder
        public int getAvatarId() {
            return R.id.view_request_end_user_avatar_imageview;
        }

        @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.IdHolder
        public int getNameId() {
            return R.id.view_request_end_user_name_textview;
        }

        @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.IdHolder
        public int getResponseId() {
            return R.id.view_request_end_user_response_textview;
        }

        @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.IdHolder
        public int getDateId() {
            return R.id.view_request_end_user_comment_date;
        }

        @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.IdHolder
        public int getAttachmentsContainerId() {
            return R.id.view_request_end_user_response_attachment_container;
        }
    }

    private static class CommentRow extends RelativeLayout implements ListRowView<CommentWithUser> {
        private static final String CREATED_AT_DATE_FORMAT = "dd MMMM yyy";
        private ViewGroup mAttachmentsContainer;
        private ImageView mAvatarImageView;
        private TextView mCommentDateTextView;
        private final Context mContext;
        private final IdHolder mIdHolder;
        private AttachmentOnClickListener mLoadImageCallback;
        private TextView mNameTextView;
        private TextView mResponseTextView;

        @Override // com.zendesk.sdk.ui.ListRowView
        public View getView() {
            return this;
        }

        public CommentRow(Context context, IdHolder idHolder, AttachmentOnClickListener attachmentOnClickListener) {
            super(context);
            this.mContext = context;
            this.mIdHolder = idHolder;
            this.mLoadImageCallback = attachmentOnClickListener;
            initialise();
        }

        private void initialise() {
            View viewInflate = LayoutInflater.from(this.mContext).inflate(this.mIdHolder.getContainerId(), this);
            this.mAvatarImageView = (ImageView) viewInflate.findViewById(this.mIdHolder.getAvatarId());
            this.mNameTextView = (TextView) viewInflate.findViewById(this.mIdHolder.getNameId());
            this.mResponseTextView = (TextView) viewInflate.findViewById(this.mIdHolder.getResponseId());
            this.mCommentDateTextView = (TextView) viewInflate.findViewById(this.mIdHolder.getDateId());
            this.mAttachmentsContainer = (ViewGroup) viewInflate.findViewById(this.mIdHolder.getAttachmentsContainerId());
        }

        @Override // com.zendesk.sdk.ui.ListRowView
        public void bind(final CommentWithUser commentWithUser) {
            this.mNameTextView.setText(commentWithUser.getAuthor().getName());
            this.mResponseTextView.setText(commentWithUser.getComment().getBody());
            this.mCommentDateTextView.setText(new SimpleDateFormat(CREATED_AT_DATE_FORMAT, Locale.getDefault()).format(commentWithUser.getComment().getCreatedAt()));
            Map<Long, Integer> map = CommentRowAttachmentHelper.heightMap.get(commentWithUser.getComment().getId());
            if (map != null) {
                Iterator<Long> it = map.keySet().iterator();
                int iIntValue = 0;
                while (it.hasNext()) {
                    iIntValue += map.get(it.next()).intValue();
                }
                this.mAttachmentsContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, iIntValue));
            }
            int dimension = (int) (getResources().getDimension(R.dimen.view_request_comment_avatar_size) / getResources().getDisplayMetrics().density);
            if (commentWithUser.getAuthor().getPhoto() != null) {
                ZendeskPicassoProvider.getInstance(this.mContext).load(commentWithUser.getAuthor().getPhoto().getContentUrl()).error(R.drawable.zd_user_default_avatar).placeholder(R.drawable.zd_user_default_avatar).transform(ZendeskPicassoTransformationFactory.INSTANCE.getRoundedTransformation(dimension * 2, 0)).into(this.mAvatarImageView);
            } else {
                ZendeskPicassoProvider.getInstance(this.mContext).load(R.drawable.zd_user_default_avatar).transform(ZendeskPicassoTransformationFactory.INSTANCE.getRoundedTransformation(dimension * 2, 0)).into(this.mAvatarImageView);
            }
            new Handler().post(new Runnable() { // from class: com.zendesk.sdk.requests.RequestCommentsListAdapter.CommentRow.1
                @Override // java.lang.Runnable
                public void run() {
                    new CommentRowAttachmentHelper().attachPhotoToCommentRow(CommentRow.this.mAttachmentsContainer, commentWithUser.getComment(), CommentRow.this.mContext, CommentRow.this.mLoadImageCallback);
                }
            });
        }
    }

    private static class CommentRowAttachmentHelper {
        private static final String IMAGE_MIME_TYPE_START = "image/";
        public static Map<Long, Map<Long, Integer>> heightMap = new HashMap();

        private CommentRowAttachmentHelper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r10v1, types: [android.view.View, android.widget.FrameLayout] */
        /* JADX WARN: Type inference failed for: r15v0 */
        /* JADX WARN: Type inference failed for: r15v1, types: [boolean, int] */
        /* JADX WARN: Type inference failed for: r15v3 */
        /* JADX WARN: Type inference failed for: r23v0, types: [android.view.ViewGroup] */
        /* JADX WARN: Type inference failed for: r4v5, types: [android.view.LayoutInflater] */
        /* JADX WARN: Type inference failed for: r4v8, types: [android.view.ViewGroup$LayoutParams, android.widget.LinearLayout$LayoutParams] */
        /* JADX WARN: Type inference failed for: r7v1, types: [android.widget.ProgressBar] */
        /* JADX WARN: Type inference failed for: r9v0 */
        /* JADX WARN: Type inference failed for: r9v1, types: [int] */
        /* JADX WARN: Type inference failed for: r9v2 */
        public void attachPhotoToCommentRow(ViewGroup viewGroup, CommentResponse commentResponse, Context context, final AttachmentOnClickListener attachmentOnClickListener) {
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.attachments_horizontal_margin);
            int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R.dimen.attachments_vertical_margin);
            int measuredWidth = viewGroup.getMeasuredWidth();
            if (measuredWidth == 0) {
                measuredWidth = context.getResources().getDisplayMetrics().widthPixels;
            }
            int i = measuredWidth - (dimensionPixelSize * 2);
            List<Attachment> attachments = commentResponse.getAttachments();
            ArrayList<Attachment> arrayList = new ArrayList();
            if (CollectionUtils.isNotEmpty(attachments)) {
                for (Attachment attachment : attachments) {
                    if (attachment != null && attachment.getContentType() != null && attachment.getContentType().startsWith(IMAGE_MIME_TYPE_START)) {
                        arrayList.add(attachment);
                    }
                }
            }
            final HashMap map = new HashMap();
            heightMap.put(commentResponse.getId(), map);
            ?? r15 = 0;
            int i2 = 0;
            for (final Attachment attachment2 : arrayList) {
                int i3 = i2 + 1;
                final ?? r9 = i2 == arrayList.size() + (-1) ? dimensionPixelSize2 : r15;
                ?? r10 = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.view_attachment_inline_item, viewGroup, r15);
                ?? r4 = (LinearLayout.LayoutParams) r10.getLayoutParams();
                r4.setMargins(dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize, r9);
                r10.setLayoutParams(r4);
                final ImageView imageView = (ImageView) r10.findViewById(R.id.attachment_inline_image);
                final ?? r7 = (ProgressBar) r10.findViewById(R.id.attachment_inline_progressbar);
                r7.setVisibility(r15);
                final int i4 = dimensionPixelSize2;
                ZendeskPicassoProvider.getInstance(context).load(attachment2.getContentUrl()).transform(ZendeskPicassoTransformationFactory.INSTANCE.getResizeTransformationWidth(i)).noFade().into(imageView, new Callback() { // from class: com.zendesk.sdk.requests.RequestCommentsListAdapter.CommentRowAttachmentHelper.1
                    @Override // com.squareup.picasso.Callback
                    public void onSuccess() {
                        map.put(attachment2.getId(), Integer.valueOf(i4 + r9 + imageView.getDrawable().getIntrinsicHeight()));
                        r7.setVisibility(8);
                    }

                    @Override // com.squareup.picasso.Callback
                    public void onError() {
                        r7.setVisibility(8);
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() { // from class: com.zendesk.sdk.requests.RequestCommentsListAdapter.CommentRowAttachmentHelper.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        attachmentOnClickListener.onAttachmentClicked(view, attachment2);
                    }
                });
                viewGroup.addView(r10);
                i2 = i3;
                dimensionPixelSize = dimensionPixelSize;
                dimensionPixelSize2 = dimensionPixelSize2;
                i = i;
                r15 = 0;
            }
        }
    }
}
