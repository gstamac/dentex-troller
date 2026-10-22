package com.zendesk.sdk.support.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zendesk.sdk.R;
import com.zendesk.sdk.support.SupportMvp;
import com.zendesk.sdk.support.SupportUiConfig;

/* JADX INFO: loaded from: classes2.dex */
public class SupportHelpFragment extends Fragment {
    private static final String ARG_SDK_UI_CONFIG = "arg_sdk_ui_config";
    public static final String LOG_TAG = "SupportHelpFragment";
    private HelpRecyclerViewAdapter adapter;
    private SupportMvp.Presenter presenter;
    private RecyclerView recyclerView;

    public void setPresenter(SupportMvp.Presenter presenter) {
        this.presenter = presenter;
        HelpRecyclerViewAdapter helpRecyclerViewAdapter = this.adapter;
        if (helpRecyclerViewAdapter != null) {
            helpRecyclerViewAdapter.setContentUpdateListener(presenter);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        HelpRecyclerViewAdapter helpRecyclerViewAdapter = new HelpRecyclerViewAdapter((SupportUiConfig) getArguments().getParcelable(ARG_SDK_UI_CONFIG));
        this.adapter = helpRecyclerViewAdapter;
        SupportMvp.Presenter presenter = this.presenter;
        if (presenter != null) {
            helpRecyclerViewAdapter.setContentUpdateListener(presenter);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_help, viewGroup, false);
        this.recyclerView = (RecyclerView) viewInflate.findViewById(R.id.recyclerView);
        setupRecyclerView();
        return viewInflate;
    }

    private void setupRecyclerView() {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        this.recyclerView.addItemDecoration(new SeparatorDecoration(ContextCompat.getDrawable(getContext(), R.drawable.help_separator)));
        this.recyclerView.setAdapter(this.adapter);
    }

    public static SupportHelpFragment newInstance(SupportUiConfig supportUiConfig) {
        SupportHelpFragment supportHelpFragment = new SupportHelpFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_SDK_UI_CONFIG, supportUiConfig);
        supportHelpFragment.setArguments(bundle);
        return supportHelpFragment;
    }
}
