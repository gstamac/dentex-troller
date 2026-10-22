package im.helmsman.helmsmanandroid.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.CommodityAdapter;
import im.helmsman.helmsmanandroid.inet.model.CommodityModel;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.activity.CommodityDetailActivity;
import im.helmsman.helmsmanandroid.ui.common.BaseFragment;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class PshopFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private List<CommodityModel> datas;

    @BindView(R.id.lv_shop_commodity)
    ListView lvShopCommodity;
    private View view;

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseFragment
    public BasePresenter initPresenter() {
        return null;
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.pfragment_shop, viewGroup, false);
        this.view = viewInflate;
        ButterKnife.bind(this, viewInflate);
        initListData();
        this.lvShopCommodity.setOnItemClickListener(this);
        return this.view;
    }

    private void initListData() {
        this.datas = new ArrayList();
        CommodityModel commodityModel = new CommodityModel(new int[]{R.drawable.testimage, R.drawable.testimage, R.drawable.testimage, R.drawable.testimage}, "www.baidu.com", "呵呵", 10);
        this.datas.add(commodityModel);
        this.datas.add(commodityModel);
        this.datas.add(commodityModel);
        this.datas.add(commodityModel);
        this.lvShopCommodity.setAdapter((ListAdapter) new CommodityAdapter(getActivity(), this.datas));
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        CommodityModel commodityModel = this.datas.get(i);
        Intent intent = new Intent(getActivity(), (Class<?>) CommodityDetailActivity.class);
        intent.putExtra("name", commodityModel.getName());
        intent.putExtra("price", commodityModel.getPrice());
        intent.putExtra("imageres", commodityModel.getImgRes());
        intent.putExtra("link", commodityModel.getLink());
        startActivity(intent);
    }
}
