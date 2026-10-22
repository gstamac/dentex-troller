package im.helmsman.helmsmanandroid.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class TMainFragment_ViewBinding implements Unbinder {
    private TMainFragment target;
    private View view7f080093;
    private View view7f0800a1;
    private View view7f0800a2;
    private View view7f0800a6;
    private View view7f0803a1;

    public TMainFragment_ViewBinding(final TMainFragment tMainFragment, View view) {
        this.target = tMainFragment;
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.btn_left, "field 'btnLeft' and method 'onClick'");
        tMainFragment.btnLeft = (Button) Utils.castView(viewFindRequiredView, R.id.btn_left, "field 'btnLeft'", Button.class);
        this.view7f0800a2 = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.TMainFragment_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                tMainFragment.onClick(view2);
            }
        });
        View viewFindRequiredView2 = Utils.findRequiredView(view, R.id.btn_right, "field 'btnRight' and method 'onClick'");
        tMainFragment.btnRight = (Button) Utils.castView(viewFindRequiredView2, R.id.btn_right, "field 'btnRight'", Button.class);
        this.view7f0800a6 = viewFindRequiredView2;
        viewFindRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.TMainFragment_ViewBinding.2
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                tMainFragment.onClick(view2);
            }
        });
        View viewFindRequiredView3 = Utils.findRequiredView(view, R.id.btn_add, "field 'btnAdd' and method 'onClick'");
        tMainFragment.btnAdd = (Button) Utils.castView(viewFindRequiredView3, R.id.btn_add, "field 'btnAdd'", Button.class);
        this.view7f080093 = viewFindRequiredView3;
        viewFindRequiredView3.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.TMainFragment_ViewBinding.3
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                tMainFragment.onClick(view2);
            }
        });
        View viewFindRequiredView4 = Utils.findRequiredView(view, R.id.btn_jian, "field 'btnJian' and method 'onClick'");
        tMainFragment.btnJian = (Button) Utils.castView(viewFindRequiredView4, R.id.btn_jian, "field 'btnJian'", Button.class);
        this.view7f0800a1 = viewFindRequiredView4;
        viewFindRequiredView4.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.TMainFragment_ViewBinding.4
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                tMainFragment.onClick(view2);
            }
        });
        View viewFindRequiredView5 = Utils.findRequiredView(view, R.id.tv_main_title, "field 'tvMainTitle' and method 'onClick'");
        tMainFragment.tvMainTitle = (TextView) Utils.castView(viewFindRequiredView5, R.id.tv_main_title, "field 'tvMainTitle'", TextView.class);
        this.view7f0803a1 = viewFindRequiredView5;
        viewFindRequiredView5.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.TMainFragment_ViewBinding.5
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                tMainFragment.onClick(view2);
            }
        });
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        TMainFragment tMainFragment = this.target;
        if (tMainFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        tMainFragment.btnLeft = null;
        tMainFragment.btnRight = null;
        tMainFragment.btnAdd = null;
        tMainFragment.btnJian = null;
        tMainFragment.tvMainTitle = null;
        this.view7f0800a2.setOnClickListener(null);
        this.view7f0800a2 = null;
        this.view7f0800a6.setOnClickListener(null);
        this.view7f0800a6 = null;
        this.view7f080093.setOnClickListener(null);
        this.view7f080093 = null;
        this.view7f0800a1.setOnClickListener(null);
        this.view7f0800a1 = null;
        this.view7f0803a1.setOnClickListener(null);
        this.view7f0803a1 = null;
    }
}
