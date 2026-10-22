package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.HelmsmanCollegeListAdapter;
import im.helmsman.helmsmanandroid.inet.model.ArticlesResultModel;
import im.helmsman.helmsmanandroid.presenter.HelmsmanCollegePresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.HelmsmanCollegeView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class HelmsmanCollegeActivity extends Mvp2BaseActivity<HelmsmanCollegeView, HelmsmanCollegePresenter> implements HelmsmanCollegeView, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private List<ArticlesResultModel.ArticlesBean> datas;

    @BindView(R.id.iv_helmsman_college_search)
    ImageView ivHelmsmanCollegeSearch;

    @BindView(R.id.linear_nocontent_here)
    View ivNocontentHere;

    @BindView(R.id.lv_helmsman_college_list)
    ListView lvHelmsmanCollegeList;

    @BindView(R.id.swipe_helmsmancollege)
    SwipeRefreshLayout swipeHelmsmancollege;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_helmsman_college);
        ButterKnife.bind(this);
        ((HelmsmanCollegePresenter) this.presenter).getHelmsmanCollegeList();
        initEvent();
        new Handler().postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.HelmsmanCollegeActivity.1
            @Override // java.lang.Runnable
            public void run() {
                HelmsmanCollegeActivity.this.swipeHelmsmancollege.setRefreshing(true);
            }
        }, 200L);
    }

    private void initEvent() {
        this.swipeHelmsmancollege.setOnRefreshListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public HelmsmanCollegePresenter initPresenter() {
        return new HelmsmanCollegePresenter();
    }

    @OnClick({R.id.iv_helmsman_college_search})
    public void onClick(View view) {
        if (view.getId() != R.id.iv_helmsman_college_search) {
            return;
        }
        startActivity(new Intent(this, (Class<?>) HelmsmanCollegeSearchActivity.class));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.HelmsmanCollegeView
    public void initHelmsmanCollege(List<ArticlesResultModel.ArticlesBean> list) {
        this.swipeHelmsmancollege.setRefreshing(false);
        this.lvHelmsmanCollegeList.setAdapter((ListAdapter) new HelmsmanCollegeListAdapter(this, list));
        this.lvHelmsmanCollegeList.setOnItemClickListener(this);
        this.datas = list;
        noContentHere();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.HelmsmanCollegeView
    public void showGetListError(String str) {
        this.swipeHelmsmancollege.setRefreshing(false);
        ViewUtils.ShowToast(str);
        noContentHere();
    }

    private void noContentHere() {
        List<ArticlesResultModel.ArticlesBean> list = this.datas;
        if (list == null || list.size() == 0) {
            this.ivNocontentHere.setVisibility(0);
            this.lvHelmsmanCollegeList.setVisibility(8);
        } else {
            this.ivNocontentHere.setVisibility(8);
            this.lvHelmsmanCollegeList.setVisibility(0);
        }
    }

    @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
    public void onRefresh() {
        ((HelmsmanCollegePresenter) this.presenter).getHelmsmanCollegeList();
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        String html_url = this.datas.get(i).getHtml_url();
        Intent intent = new Intent(this, (Class<?>) WebViewActivity.class);
        intent.putExtra("weburl", html_url);
        startActivity(intent);
    }
}
