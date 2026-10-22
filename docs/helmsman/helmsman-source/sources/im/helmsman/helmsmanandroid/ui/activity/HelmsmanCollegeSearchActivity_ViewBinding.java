package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.view.SearchView;

/* JADX INFO: loaded from: classes2.dex */
public class HelmsmanCollegeSearchActivity_ViewBinding implements Unbinder {
    private HelmsmanCollegeSearchActivity target;
    private View view7f0801ab;

    public HelmsmanCollegeSearchActivity_ViewBinding(HelmsmanCollegeSearchActivity helmsmanCollegeSearchActivity) {
        this(helmsmanCollegeSearchActivity, helmsmanCollegeSearchActivity.getWindow().getDecorView());
    }

    public HelmsmanCollegeSearchActivity_ViewBinding(final HelmsmanCollegeSearchActivity helmsmanCollegeSearchActivity, View view) {
        this.target = helmsmanCollegeSearchActivity;
        helmsmanCollegeSearchActivity.tvHelmsmanCollegeSearchTitle = (SearchView) Utils.findRequiredViewAsType(view, R.id.tv_helmsman_college_search_title, "field 'tvHelmsmanCollegeSearchTitle'", SearchView.class);
        helmsmanCollegeSearchActivity.lvHelmsmanCollegeSearchList = (ListView) Utils.findRequiredViewAsType(view, R.id.lv_helmsman_college_search_list, "field 'lvHelmsmanCollegeSearchList'", ListView.class);
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.iv_helmsman_college_seasrch_back, "field 'ivHelmsmanCollegeSeasrchBack' and method 'onClick'");
        helmsmanCollegeSearchActivity.ivHelmsmanCollegeSeasrchBack = (ImageView) Utils.castView(viewFindRequiredView, R.id.iv_helmsman_college_seasrch_back, "field 'ivHelmsmanCollegeSeasrchBack'", ImageView.class);
        this.view7f0801ab = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.HelmsmanCollegeSearchActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                helmsmanCollegeSearchActivity.onClick();
            }
        });
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        HelmsmanCollegeSearchActivity helmsmanCollegeSearchActivity = this.target;
        if (helmsmanCollegeSearchActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        helmsmanCollegeSearchActivity.tvHelmsmanCollegeSearchTitle = null;
        helmsmanCollegeSearchActivity.lvHelmsmanCollegeSearchList = null;
        helmsmanCollegeSearchActivity.ivHelmsmanCollegeSeasrchBack = null;
        this.view7f0801ab.setOnClickListener(null);
        this.view7f0801ab = null;
    }
}
