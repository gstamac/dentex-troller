package im.helmsman.helmsmanandroid.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.view.CycleBanner;

/* JADX INFO: loaded from: classes2.dex */
public class PCommodityDetailFragment_ViewBinding implements Unbinder {
    private PCommodityDetailFragment target;
    private View view7f080095;
    private View view7f080096;
    private View view7f080193;

    public PCommodityDetailFragment_ViewBinding(final PCommodityDetailFragment pCommodityDetailFragment, View view) {
        this.target = pCommodityDetailFragment;
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.btn_commodity_buy, "field 'btnCommodityBuy' and method 'onClick'");
        pCommodityDetailFragment.btnCommodityBuy = (Button) Utils.castView(viewFindRequiredView, R.id.btn_commodity_buy, "field 'btnCommodityBuy'", Button.class);
        this.view7f080095 = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PCommodityDetailFragment_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                pCommodityDetailFragment.onClick(view2);
            }
        });
        View viewFindRequiredView2 = Utils.findRequiredView(view, R.id.btn_commodity_share, "field 'btnCommodityShare' and method 'onClick'");
        pCommodityDetailFragment.btnCommodityShare = (Button) Utils.castView(viewFindRequiredView2, R.id.btn_commodity_share, "field 'btnCommodityShare'", Button.class);
        this.view7f080096 = viewFindRequiredView2;
        viewFindRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PCommodityDetailFragment_ViewBinding.2
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                pCommodityDetailFragment.onClick(view2);
            }
        });
        View viewFindRequiredView3 = Utils.findRequiredView(view, R.id.iv_commodity_detail_back, "field 'ivCommodityDetailBack' and method 'onClick'");
        pCommodityDetailFragment.ivCommodityDetailBack = (ImageView) Utils.castView(viewFindRequiredView3, R.id.iv_commodity_detail_back, "field 'ivCommodityDetailBack'", ImageView.class);
        this.view7f080193 = viewFindRequiredView3;
        viewFindRequiredView3.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PCommodityDetailFragment_ViewBinding.3
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                pCommodityDetailFragment.onClick(view2);
            }
        });
        pCommodityDetailFragment.cyclebanner = (CycleBanner) Utils.findRequiredViewAsType(view, R.id.cyclebanner, "field 'cyclebanner'", CycleBanner.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        PCommodityDetailFragment pCommodityDetailFragment = this.target;
        if (pCommodityDetailFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        pCommodityDetailFragment.btnCommodityBuy = null;
        pCommodityDetailFragment.btnCommodityShare = null;
        pCommodityDetailFragment.ivCommodityDetailBack = null;
        pCommodityDetailFragment.cyclebanner = null;
        this.view7f080095.setOnClickListener(null);
        this.view7f080095 = null;
        this.view7f080096.setOnClickListener(null);
        this.view7f080096 = null;
        this.view7f080193.setOnClickListener(null);
        this.view7f080193 = null;
    }
}
