package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.QucikStartListAdapter;
import im.helmsman.helmsmanandroid.inet.model.ArticlesResultModel;
import im.helmsman.helmsmanandroid.inet.model.SectionResultModel;
import im.helmsman.helmsmanandroid.presenter.QuickStartPresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.QuickStartView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.helmsmanandroid.view.TitleBar;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class QuickStartActivity extends Mvp2BaseActivity<QuickStartView, QuickStartPresenter> implements QuickStartView, ExpandableListView.OnChildClickListener, SwipeRefreshLayout.OnRefreshListener {
    private Map<Integer, List<ArticlesResultModel.ArticlesBean>> aModel;

    @BindView(R.id.elv_instructions)
    ExpandableListView elvInstructions;
    private List<SectionResultModel.SectionsBean> sModel;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.titleBar)
    TitleBar titleBar;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_quick_start);
        ButterKnife.bind(this);
        getData();
        new Handler().postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.QuickStartActivity.1
            @Override // java.lang.Runnable
            public void run() {
                QuickStartActivity.this.swipeRefresh.setRefreshing(true);
            }
        }, 200L);
        this.swipeRefresh.setOnRefreshListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public QuickStartPresenter initPresenter() {
        return new QuickStartPresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.QuickStartView
    public void initListViewData(List<SectionResultModel.SectionsBean> list, Map<Integer, List<ArticlesResultModel.ArticlesBean>> map) {
        Log.e("initListViewData", "initListViewData");
        this.sModel = list;
        this.aModel = map;
        this.elvInstructions.setAdapter(new QucikStartListAdapter(this, list, map));
        this.elvInstructions.setGroupIndicator(null);
        this.elvInstructions.setOnChildClickListener(this);
        this.elvInstructions.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.QuickStartActivity.2
            @Override // android.widget.ExpandableListView.OnGroupClickListener
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long j) {
                return true;
            }
        });
        for (int i = 0; i < list.size(); i++) {
            this.elvInstructions.expandGroup(i);
        }
        RefreshFinish();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.QuickStartView
    public void loadListViewDataError(String str) {
        this.swipeRefresh.setRefreshing(false);
        ViewUtils.ShowToast(getString(R.string.loaddatafailed) + str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.QuickStartView
    public void RefreshFinish() {
        this.swipeRefresh.setRefreshing(false);
    }

    private void getData() {
        ((QuickStartPresenter) this.presenter).getSectionListData(204136948);
    }

    @Override // android.widget.ExpandableListView.OnChildClickListener
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
        String html_url = this.aModel.get(Integer.valueOf(this.sModel.get(i).getId())).get(i2).getHtml_url();
        Intent intent = new Intent(this, (Class<?>) WebViewActivity.class);
        intent.putExtra("weburl", html_url);
        startActivity(intent);
        return false;
    }

    @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
    public void onRefresh() {
        getData();
    }
}
