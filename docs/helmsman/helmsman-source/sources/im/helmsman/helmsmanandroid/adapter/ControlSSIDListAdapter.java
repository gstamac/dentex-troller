package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import im.helmsman.helmsmanandroid.R;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ControlSSIDListAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mDatas;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public ControlSSIDListAdapter(Context context, List<String> list) {
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
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_control_ssid, (ViewGroup) null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvSSid = (TextView) view.findViewById(R.id.tv_controlssid_ssid);
            view.setTag(viewHolder);
        }
        ((ViewHolder) view.getTag()).tvSSid.setText((String) getItem(i));
        return view;
    }

    class ViewHolder {
        private TextView tvSSid;

        ViewHolder() {
        }
    }
}
