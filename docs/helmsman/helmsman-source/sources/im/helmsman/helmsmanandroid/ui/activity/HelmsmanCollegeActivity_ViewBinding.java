package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class HelmsmanCollegeActivity_ViewBinding implements Unbinder {
    private HelmsmanCollegeActivity target;
    private View view7f0801aa;

    public HelmsmanCollegeActivity_ViewBinding(HelmsmanCollegeActivity helmsmanCollegeActivity) {
        this(helmsmanCollegeActivity, helmsmanCollegeActivity.getWindow().getDecorView());
    }

    public HelmsmanCollegeActivity_ViewBinding(final HelmsmanCollegeActivity helmsmanCollegeActivity, View view) {
        this.target = helmsmanCollegeActivity;
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.iv_helmsman_college_search, "field 'ivHelmsmanCollegeSearch' and method 'onClick'");
        helmsmanCollegeActivity.ivHelmsmanCollegeSearch = (ImageView) Utils.castView(viewFindRequiredView, R.id.iv_helmsman_college_search, "field 'ivHelmsmanCollegeSearch'", ImageView.class);
        this.view7f0801aa = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.HelmsmanCollegeActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                helmsmanCollegeActivity.onClick(view2);
            }
        });
        helmsmanCollegeActivity.lvHelmsmanCollegeList = (ListView) Utils.findRequiredViewAsType(view, R.id.lv_helmsman_college_list, "field 'lvHelmsmanCollegeList'", ListView.class);
        helmsmanCollegeActivity.swipeHelmsmancollege = (SwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.swipe_helmsmancollege, "field 'swipeHelmsmancollege'", SwipeRefreshLayout.class);
        helmsmanCollegeActivity.ivNocontentHere = Utils.findRequiredView(view, R.id.linear_nocontent_here, "field 'ivNocontentHere'");
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        HelmsmanCollegeActivity helmsmanCollegeActivity = this.target;
        if (helmsmanCollegeActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        helmsmanCollegeActivity.ivHelmsmanCollegeSearch = null;
        helmsmanCollegeActivity.lvHelmsmanCollegeList = null;
        helmsmanCollegeActivity.swipeHelmsmancollege = null;
        helmsmanCollegeActivity.ivNocontentHere = null;
        this.view7f0801aa.setOnClickListener(null);
        this.view7f0801aa = null;
    }
}
