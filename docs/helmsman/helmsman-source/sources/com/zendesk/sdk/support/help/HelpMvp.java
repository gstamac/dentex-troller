package com.zendesk.sdk.support.help;

import com.zendesk.sdk.model.helpcenter.help.CategoryItem;
import com.zendesk.sdk.model.helpcenter.help.HelpItem;
import com.zendesk.sdk.model.helpcenter.help.SectionItem;
import com.zendesk.sdk.model.helpcenter.help.SeeAllArticlesItem;
import com.zendesk.sdk.support.SupportMvp;
import com.zendesk.service.ZendeskCallback;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
interface HelpMvp {

    public interface Model {
        void getArticles(List<Long> list, List<Long> list2, String[] strArr, ZendeskCallback<List<HelpItem>> zendeskCallback);

        void getArticlesForSection(SectionItem sectionItem, String[] strArr, ZendeskCallback<List<HelpItem>> zendeskCallback);
    }

    public interface Presenter {
        HelpItem getItem(int i);

        int getItemCount();

        HelpItem getItemForBinding(int i);

        int getItemViewType(int i);

        void onAttached();

        boolean onCategoryClick(CategoryItem categoryItem, int i);

        void onDetached();

        void onSeeAllClick(SeeAllArticlesItem seeAllArticlesItem);

        void setContentPresenter(SupportMvp.Presenter presenter);
    }

    public interface View {
        void addItem(int i, HelpItem helpItem);

        void removeItem(int i);

        void showItems(List<HelpItem> list);
    }
}
