package im.helmsman.helmsmanandroid.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.common.BaseFragment;
import im.helmsman.helmsmanandroid.view.CycleBanner;

/* JADX INFO: loaded from: classes2.dex */
public class PCommodityDetailFragment extends BaseFragment {

    @BindView(R.id.btn_commodity_buy)
    Button btnCommodityBuy;

    @BindView(R.id.btn_commodity_share)
    Button btnCommodityShare;

    @BindView(R.id.cyclebanner)
    CycleBanner cyclebanner;

    @BindView(R.id.iv_commodity_detail_back)
    ImageView ivCommodityDetailBack;
    private String link;
    private String name;
    private int price;
    private int[] res;
    private View view;

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseFragment
    public BasePresenter initPresenter() {
        return null;
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.pfragment_commoditydetail, viewGroup, false);
        this.view = viewInflate;
        ButterKnife.bind(this, viewInflate);
        this.cyclebanner.setCycleDatas(this.res);
        return this.view;
    }

    @OnClick({R.id.btn_commodity_buy, R.id.btn_commodity_share, R.id.iv_commodity_detail_back})
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btn_commodity_buy) {
            if (id != R.id.iv_commodity_detail_back) {
                return;
            }
            getActivity().finish();
        } else {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.baidu.com"));
            intent.addCategory("android.intent.category.BROWSABLE");
            startActivity(intent);
        }
    }
}
