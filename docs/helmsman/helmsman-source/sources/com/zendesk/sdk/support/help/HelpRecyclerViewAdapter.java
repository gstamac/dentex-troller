package com.zendesk.sdk.support.help;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.model.helpcenter.SimpleArticle;
import com.zendesk.sdk.model.helpcenter.help.CategoryItem;
import com.zendesk.sdk.model.helpcenter.help.HelpItem;
import com.zendesk.sdk.model.helpcenter.help.SectionItem;
import com.zendesk.sdk.model.helpcenter.help.SeeAllArticlesItem;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.support.SupportMvp;
import com.zendesk.sdk.support.SupportUiConfig;
import com.zendesk.sdk.support.ViewArticleActivity;
import com.zendesk.sdk.util.UiUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
class HelpRecyclerViewAdapter extends RecyclerView.Adapter<HelpViewHolder> implements HelpMvp.View {
    private static final String LOG_TAG = "HelpRecyclerViewAdapter";
    private Context context;
    private int defaultCategoryTitleColour;
    private int highlightCategoryTitleColour;
    private HelpMvp.Presenter presenter;

    public HelpRecyclerViewAdapter(SupportUiConfig supportUiConfig) {
        this.presenter = new HelpAdapterPresenter(this, new HelpModel(ZendeskConfig.INSTANCE.provider().helpCenterProvider()), ZendeskConfig.INSTANCE.provider().networkInfoProvider(), supportUiConfig);
    }

    void setContentUpdateListener(SupportMvp.Presenter presenter) {
        HelpMvp.Presenter presenter2 = this.presenter;
        if (presenter2 != null) {
            presenter2.setContentPresenter(presenter);
        }
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.View
    public void showItems(List<HelpItem> list) {
        notifyDataSetChanged();
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.View
    public void addItem(int i, HelpItem helpItem) {
        notifyItemInserted(i);
    }

    @Override // com.zendesk.sdk.support.help.HelpMvp.View
    public void removeItem(int i) {
        notifyItemRemoved(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.context = recyclerView.getContext();
        this.highlightCategoryTitleColour = UiUtils.themeAttributeToColor(R.attr.colorPrimary, this.context, R.color.fallback_text_color);
        this.defaultCategoryTitleColour = UiUtils.themeAttributeToColor(android.R.attr.textColorPrimary, this.context, R.color.fallback_text_color);
        this.presenter.onAttached();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.presenter.onDetached();
        this.context = null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public HelpViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch (i) {
            case 1:
                return new CategoryViewHolder(inflateView(viewGroup, R.layout.row_category));
            case 2:
                return new SectionViewHolder(inflateView(viewGroup, R.layout.row_section));
            case 3:
                return new ArticleViewHolder(inflateView(viewGroup, R.layout.row_article));
            case 4:
                return new SeeAllViewHolder(inflateView(viewGroup, R.layout.row_action));
            case 5:
                return new LoadingViewHolder(inflateView(viewGroup, R.layout.row_loading_progress));
            case 6:
            default:
                Logger.w(LOG_TAG, "Unknown item type, returning null for holder", new Object[0]);
                return null;
            case 7:
                return new NoResultsViewHolder(inflateView(viewGroup, R.layout.row_no_articles_found));
            case 8:
                return new ExtraPaddingViewHolder(inflateView(viewGroup, R.layout.row_padding));
        }
    }

    private View inflateView(ViewGroup viewGroup, int i) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.presenter.getItemCount();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(HelpViewHolder helpViewHolder, int i) {
        if (helpViewHolder == null) {
            Logger.w(LOG_TAG, "Holder was null, possible unexpected item type", new Object[0]);
        } else {
            helpViewHolder.bindTo(this.presenter.getItemForBinding(i), i);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return this.presenter.getItemViewType(i);
    }

    class CategoryViewHolder extends HelpViewHolder {
        private static final int ROTATION_END_LEVEL = 10000;
        private static final String ROTATION_PROPERTY_NAME = "level";
        private static final int ROTATION_START_LEVEL = 0;
        private boolean expanded;
        private Drawable expanderDrawable;

        CategoryViewHolder(View view) {
            super(view);
            TextView textView = (TextView) view;
            this.textView = textView;
            Drawable drawableMutate = DrawableCompat.wrap(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_expand_more)).mutate();
            this.expanderDrawable = drawableMutate;
            DrawableCompat.setTint(drawableMutate, UiUtils.themeAttributeToColor(android.R.attr.textColorSecondary, HelpRecyclerViewAdapter.this.context, R.color.fallback_text_color));
            DrawableCompat.setTintMode(this.expanderDrawable, PorterDuff.Mode.SRC_IN);
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds((Drawable) null, (Drawable) null, this.expanderDrawable, (Drawable) null);
        }

        @Override // com.zendesk.sdk.support.help.HelpRecyclerViewAdapter.HelpViewHolder
        public void bindTo(HelpItem helpItem, final int i) {
            if (helpItem == null) {
                Logger.e(HelpRecyclerViewAdapter.LOG_TAG, "Category item was null, cannot bind", new Object[0]);
                return;
            }
            this.textView.setText(helpItem.getName());
            final CategoryItem categoryItem = (CategoryItem) helpItem;
            boolean zIsExpanded = categoryItem.isExpanded();
            this.expanded = zIsExpanded;
            this.expanderDrawable.setLevel(zIsExpanded ? 10000 : 0);
            setHighlightColor(categoryItem.isExpanded());
            this.textView.setOnClickListener(new View.OnClickListener() { // from class: com.zendesk.sdk.support.help.HelpRecyclerViewAdapter.CategoryViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    CategoryViewHolder categoryViewHolder = CategoryViewHolder.this;
                    categoryViewHolder.expanded = HelpRecyclerViewAdapter.this.presenter.onCategoryClick(categoryItem, i);
                    Drawable drawable = CategoryViewHolder.this.expanderDrawable;
                    int[] iArr = new int[2];
                    iArr[0] = CategoryViewHolder.this.expanded ? 0 : 10000;
                    iArr[1] = CategoryViewHolder.this.expanded ? 10000 : 0;
                    ObjectAnimator.ofInt(drawable, CategoryViewHolder.ROTATION_PROPERTY_NAME, iArr).start();
                    CategoryViewHolder categoryViewHolder2 = CategoryViewHolder.this;
                    categoryViewHolder2.setHighlightColor(categoryViewHolder2.expanded);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setHighlightColor(boolean z) {
            if (z) {
                this.textView.setTextColor(HelpRecyclerViewAdapter.this.highlightCategoryTitleColour);
                this.expanderDrawable.setColorFilter(HelpRecyclerViewAdapter.this.highlightCategoryTitleColour, PorterDuff.Mode.SRC_IN);
            } else {
                this.textView.setTextColor(HelpRecyclerViewAdapter.this.defaultCategoryTitleColour);
                this.expanderDrawable.setColorFilter(HelpRecyclerViewAdapter.this.defaultCategoryTitleColour, PorterDuff.Mode.SRC_IN);
            }
        }

        public boolean isExpanded() {
            return this.expanded;
        }
    }

    class SectionViewHolder extends HelpViewHolder {
        SectionViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.section_title);
        }

        @Override // com.zendesk.sdk.support.help.HelpRecyclerViewAdapter.HelpViewHolder
        public void bindTo(HelpItem helpItem, int i) {
            if (helpItem == null) {
                Logger.e(HelpRecyclerViewAdapter.LOG_TAG, "Section item was null, cannot bind", new Object[0]);
            } else {
                this.textView.setText(helpItem.getName());
            }
        }
    }

    class ArticleViewHolder extends HelpViewHolder {
        ArticleViewHolder(View view) {
            super(view);
            this.textView = (TextView) view;
        }

        @Override // com.zendesk.sdk.support.help.HelpRecyclerViewAdapter.HelpViewHolder
        public void bindTo(final HelpItem helpItem, int i) {
            if (helpItem == null) {
                Logger.e(HelpRecyclerViewAdapter.LOG_TAG, "Article item was null, cannot bind", new Object[0]);
            } else {
                this.textView.setText(helpItem.getName());
                this.textView.setOnClickListener(new View.OnClickListener() { // from class: com.zendesk.sdk.support.help.HelpRecyclerViewAdapter.ArticleViewHolder.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        ViewArticleActivity.startActivity(HelpRecyclerViewAdapter.this.context, new SimpleArticle(helpItem.getId(), helpItem.getName()));
                    }
                });
            }
        }
    }

    private class SeeAllViewHolder extends HelpViewHolder {
        private ProgressBar progressBar;

        SeeAllViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.help_section_action_button);
            this.progressBar = (ProgressBar) view.findViewById(R.id.help_section_loading_progress);
        }

        @Override // com.zendesk.sdk.support.help.HelpRecyclerViewAdapter.HelpViewHolder
        public void bindTo(final HelpItem helpItem, int i) {
            String string;
            if (!(helpItem instanceof SeeAllArticlesItem)) {
                Logger.e(HelpRecyclerViewAdapter.LOG_TAG, "SeeAll item was null, cannot bind", new Object[0]);
                return;
            }
            final SeeAllArticlesItem seeAllArticlesItem = (SeeAllArticlesItem) helpItem;
            if (seeAllArticlesItem.isLoading()) {
                this.textView.setVisibility(8);
                this.progressBar.setVisibility(0);
            } else {
                this.textView.setVisibility(0);
                this.progressBar.setVisibility(8);
            }
            SectionItem section = seeAllArticlesItem.getSection();
            if (section == null) {
                string = HelpRecyclerViewAdapter.this.context.getString(R.string.help_see_all_articles_label);
            } else {
                string = HelpRecyclerViewAdapter.this.context.getString(R.string.help_see_all_n_articles_label, Integer.valueOf(section.getTotalArticlesCount()));
            }
            this.textView.setText(string);
            this.textView.setOnClickListener(new View.OnClickListener() { // from class: com.zendesk.sdk.support.help.HelpRecyclerViewAdapter.SeeAllViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    SeeAllViewHolder.this.textView.setVisibility(8);
                    SeeAllViewHolder.this.progressBar.setVisibility(0);
                    HelpRecyclerViewAdapter.this.presenter.onSeeAllClick((SeeAllArticlesItem) helpItem);
                    seeAllArticlesItem.setLoading(true);
                }
            });
        }
    }

    private class LoadingViewHolder extends HelpViewHolder {
        @Override // com.zendesk.sdk.support.help.HelpRecyclerViewAdapter.HelpViewHolder
        public void bindTo(HelpItem helpItem, int i) {
        }

        LoadingViewHolder(View view) {
            super(view);
        }
    }

    private class NoResultsViewHolder extends HelpViewHolder {
        @Override // com.zendesk.sdk.support.help.HelpRecyclerViewAdapter.HelpViewHolder
        public void bindTo(HelpItem helpItem, int i) {
        }

        NoResultsViewHolder(View view) {
            super(view);
        }
    }

    private class ExtraPaddingViewHolder extends HelpViewHolder {
        @Override // com.zendesk.sdk.support.help.HelpRecyclerViewAdapter.HelpViewHolder
        public void bindTo(HelpItem helpItem, int i) {
        }

        ExtraPaddingViewHolder(View view) {
            super(view);
        }
    }

    static abstract class HelpViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public abstract void bindTo(HelpItem helpItem, int i);

        HelpViewHolder(View view) {
            super(view);
        }
    }
}
