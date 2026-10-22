package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.dao.Boat;
import im.helmsman.lib.ABaseProduct;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class BoatListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Boat> mDatas;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public BoatListAdapter(Context context, List<Boat> list) {
        this.mContext = context;
        this.mDatas = list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mDatas.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.mDatas.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_boatlist, (ViewGroup) null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvSsid = (TextView) view.findViewById(R.id.tv_itemboatlist_ssid);
            viewHolder.tvState = (TextView) view.findViewById(R.id.tv_itemboatlist_connectstate);
            view.setTag(viewHolder);
        }
        ViewHolder viewHolder2 = (ViewHolder) view.getTag();
        Boat boat = (Boat) getItem(i);
        if (boat.getSsid().equals(MyApplication.getInstance().ssid) && ABaseProduct.getInstance() != null && ABaseProduct.getInstance().boatControlComponent.getConnectState() == 0) {
            viewHolder2.tvSsid.setText(boat.getSsid());
            viewHolder2.tvState.setText(this.mContext.getString(R.string.mydevice_connected));
        } else {
            viewHolder2.tvSsid.setText(boat.getSsid());
        }
        return view;
    }

    class ViewHolder {
        TextView tvSsid;
        TextView tvState;

        ViewHolder() {
        }
    }
}
