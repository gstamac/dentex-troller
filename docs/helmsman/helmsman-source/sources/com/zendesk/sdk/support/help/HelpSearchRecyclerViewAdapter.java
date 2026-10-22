package com.zendesk.sdk.support.help;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.model.helpcenter.SearchArticle;
import com.zendesk.sdk.support.ViewArticleActivity;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
class HelpSearchRecyclerViewAdapter extends RecyclerView.Adapter {
    private static final String LOG_TAG = "HelpSearchRecyclerViewAdapter";
    private static final int TYPE_ARTICLE = 531;
    private static final int TYPE_NO_RESULTS = 441;
    private static final int TYPE_PADDING = 423;
    private boolean addEndPadding;
    private String query;
    private boolean resultsCleared = false;
    private List<SearchArticle> searchArticles;

    HelpSearchRecyclerViewAdapter(List<SearchArticle> list, String str, boolean z) {
        this.searchArticles = list;
        this.query = str;
        this.addEndPadding = z;
    }

    void update(List<SearchArticle> list, String str) {
        this.resultsCleared = false;
        this.searchArticles = list;
        this.query = str;
        notifyDataSetChanged();
    }

    void clearResults() {
        this.resultsCleared = true;
        this.searchArticles = Collections.emptyList();
        this.query = "";
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 423) {
            return new PaddingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_padding, viewGroup, false));
        }
        if (i == TYPE_NO_RESULTS) {
            return new NoResultsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_no_articles_found, viewGroup, false));
        }
        if (i != TYPE_ARTICLE) {
            return null;
        }
        return new HelpSearchViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_search_article, viewGroup, false), viewGroup.getContext());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (TYPE_ARTICLE == getItemViewType(i)) {
            ((HelpSearchViewHolder) viewHolder).bindTo(this.searchArticles.get(i));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.resultsCleared) {
            return 0;
        }
        return Math.max(this.searchArticles.size() + getPaddingExtraItem(), 1);
    }

    private int getPaddingExtraItem() {
        return this.addEndPadding ? 1 : 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (i == 0 && this.searchArticles.size() == 0) {
            return TYPE_NO_RESULTS;
        }
        if (i <= 0 || i != this.searchArticles.size()) {
            return TYPE_ARTICLE;
        }
        return 423;
    }

    private class HelpSearchViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView subtitleTextView;
        private TextView titleTextView;

        HelpSearchViewHolder(View view, Context context) {
            super(view);
            this.titleTextView = (TextView) view.findViewById(R.id.title);
            this.subtitleTextView = (TextView) view.findViewById(R.id.subtitle);
            this.context = context;
        }

        void bindTo(final SearchArticle searchArticle) {
            if (searchArticle == null || searchArticle.getArticle() == null) {
                Logger.e(HelpSearchRecyclerViewAdapter.LOG_TAG, "The article was null, cannot bind the view.", new Object[0]);
                return;
            }
            String title = searchArticle.getArticle().getTitle() != null ? searchArticle.getArticle().getTitle() : "";
            int iIndexOf = HelpSearchRecyclerViewAdapter.this.query == null ? -1 : title.toLowerCase(Locale.getDefault()).indexOf(HelpSearchRecyclerViewAdapter.this.query.toLowerCase());
            if (iIndexOf != -1) {
                SpannableString spannableString = new SpannableString(title);
                spannableString.setSpan(new StyleSpan(1), iIndexOf, HelpSearchRecyclerViewAdapter.this.query.length() + iIndexOf, 18);
                this.titleTextView.setText(spannableString);
            } else {
                this.titleTextView.setText(title);
            }
            this.subtitleTextView.setText(this.context.getString(R.string.guide_search_subtitle_format, searchArticle.getCategory().getName(), searchArticle.getSection().getName()));
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.zendesk.sdk.support.help.HelpSearchRecyclerViewAdapter.HelpSearchViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ViewArticleActivity.startActivity(HelpSearchViewHolder.this.itemView.getContext(), searchArticle.getArticle());
                }
            });
        }
    }

    private class NoResultsViewHolder extends RecyclerView.ViewHolder {
        NoResultsViewHolder(View view) {
            super(view);
        }
    }

    private class PaddingViewHolder extends RecyclerView.ViewHolder {
        PaddingViewHolder(View view) {
            super(view);
        }
    }
}
