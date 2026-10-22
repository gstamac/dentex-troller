package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.model.CommodityModel;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CommodityAdapter extends BaseAdapter {
    private List<CommodityModel> datas;
    private Context mContext;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder_ViewBinding implements Unbinder {
        private ViewHolder target;

        public ViewHolder_ViewBinding(ViewHolder viewHolder, View view) {
            this.target = viewHolder;
            viewHolder.ivCommodityImg = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_commodity_img, "field 'ivCommodityImg'", ImageView.class);
            viewHolder.tvCommodityName = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_commodity_name, "field 'tvCommodityName'", TextView.class);
            viewHolder.tvCommodityPrice = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_commodity_price, "field 'tvCommodityPrice'", TextView.class);
        }

        @Override // butterknife.Unbinder
        public void unbind() {
            ViewHolder viewHolder = this.target;
            if (viewHolder == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            viewHolder.ivCommodityImg = null;
            viewHolder.tvCommodityName = null;
            viewHolder.tvCommodityPrice = null;
        }
    }

    public CommodityAdapter(Context context, List<CommodityModel> list) {
        this.datas = list;
        this.mContext = context;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.datas.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.datas.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_commodity_list, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CommodityModel commodityModel = (CommodityModel) getItem(i);
        int i2 = commodityModel.getImgRes()[0];
        String name = commodityModel.getName();
        int price = commodityModel.getPrice();
        viewHolder.ivCommodityImg.setImageResource(i2);
        viewHolder.tvCommodityName.setText(name);
        viewHolder.tvCommodityPrice.setText("$" + price);
        return view;
    }

    static class ViewHolder {

        @BindView(R.id.iv_commodity_img)
        ImageView ivCommodityImg;

        @BindView(R.id.tv_commodity_name)
        TextView tvCommodityName;

        @BindView(R.id.tv_commodity_price)
        TextView tvCommodityPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
