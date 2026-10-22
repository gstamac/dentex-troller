package com.zendesk.sdk.support.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zendesk.sdk.R;
import com.zendesk.sdk.model.helpcenter.SearchArticle;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class HelpSearchFragment extends Fragment {
    static final String ARG_ADD_LIST_PADDING_BOTTOM = "arg_add_list_padding_bottom";
    private HelpSearchRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<SearchArticle> searchArticles = Collections.emptyList();
    private String query = "";

    public static HelpSearchFragment newInstance(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_ADD_LIST_PADDING_BOTTOM, z);
        HelpSearchFragment helpSearchFragment = new HelpSearchFragment();
        helpSearchFragment.setArguments(bundle);
        return helpSearchFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_help, viewGroup, false);
        this.recyclerView = (RecyclerView) viewInflate.findViewById(R.id.recyclerView);
        setupRecyclerView();
        return viewInflate;
    }

    public void updateResults(List<SearchArticle> list, String str) {
        RecyclerView recyclerView;
        this.searchArticles = list;
        this.query = str;
        if (this.adapter == null || (recyclerView = this.recyclerView) == null) {
            return;
        }
        recyclerView.setVisibility(0);
        this.adapter.update(list, str);
    }

    public void clearResults() {
        HelpSearchRecyclerViewAdapter helpSearchRecyclerViewAdapter = this.adapter;
        if (helpSearchRecyclerViewAdapter != null) {
            helpSearchRecyclerViewAdapter.clearResults();
        }
    }

    private void setupRecyclerView() {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        HelpSearchRecyclerViewAdapter helpSearchRecyclerViewAdapter = new HelpSearchRecyclerViewAdapter(this.searchArticles, this.query, getArguments().getBoolean(ARG_ADD_LIST_PADDING_BOTTOM));
        this.adapter = helpSearchRecyclerViewAdapter;
        this.recyclerView.setAdapter(helpSearchRecyclerViewAdapter);
    }
}
