package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.HelmsmanCollegeSearchListAdapter;
import im.helmsman.helmsmanandroid.inet.model.ZendeskManualsListModelResult;
import im.helmsman.helmsmanandroid.presenter.HelmsmanCollegeSearchPresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.HelmsmanCollegeSearchView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.helmsmanandroid.view.SearchView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class HelmsmanCollegeSearchActivity extends Mvp2BaseActivity<HelmsmanCollegeSearchView, HelmsmanCollegeSearchPresenter> implements HelmsmanCollegeSearchView, AdapterView.OnItemClickListener, SearchView.OnSearchViewTextChangeListener {
    private List<ZendeskManualsListModelResult.ResultsBean> datas;

    @BindView(R.id.iv_helmsman_college_seasrch_back)
    ImageView ivHelmsmanCollegeSeasrchBack;

    @BindView(R.id.lv_helmsman_college_search_list)
    ListView lvHelmsmanCollegeSearchList;

    @BindView(R.id.tv_helmsman_college_search_title)
    SearchView tvHelmsmanCollegeSearchTitle;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_helmsman_college_search);
        ButterKnife.bind(this);
        initEvent();
    }

    private void initEvent() {
        this.tvHelmsmanCollegeSearchTitle.setOnSearchViewTextChangeListener(this);
        this.lvHelmsmanCollegeSearchList.setOnItemClickListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public HelmsmanCollegeSearchPresenter initPresenter() {
        return new HelmsmanCollegeSearchPresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.HelmsmanCollegeSearchView
    public void initHelmsmanCollege(List<ZendeskManualsListModelResult.ResultsBean> list) {
        this.datas = list;
        this.lvHelmsmanCollegeSearchList.setAdapter((ListAdapter) new HelmsmanCollegeSearchListAdapter(this, list));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.HelmsmanCollegeSearchView
    public void showGetListError(String str) {
        ViewUtils.ShowToast(str);
    }

    @Override // im.helmsman.helmsmanandroid.view.SearchView.OnSearchViewTextChangeListener
    public void onTextChange(String str) {
        ((HelmsmanCollegeSearchPresenter) this.presenter).getHelmsmanCollegeList(str);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        String html_url = this.datas.get(i).getHtml_url();
        Intent intent = new Intent(this, (Class<?>) WebViewActivity.class);
        intent.putExtra("weburl", html_url);
        startActivity(intent);
    }

    @OnClick({R.id.iv_helmsman_college_seasrch_back})
    public void onClick() {
        finish();
    }
}
