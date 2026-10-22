package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import im.helmsman.helmsmanandroid.R;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class WiFiListAdapter extends BaseAdapter {
    private Context mContext;
    private List<ScanResult> mScanResults;
    private String mSsid;
    private WifiManager mWifimanger;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public WiFiListAdapter(Context context, List<ScanResult> list) {
        this.mScanResults = list;
        this.mContext = context;
    }

    public WiFiListAdapter(Context context, List<ScanResult> list, WifiManager wifiManager) {
        this.mScanResults = list;
        this.mContext = context;
        this.mWifimanger = wifiManager;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mScanResults.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.mScanResults.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate = LayoutInflater.from(this.mContext).inflate(R.layout.item_bluebooth_list, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_item_bluebooth_name);
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.iv_item_bluetooth_correct);
        ScanResult scanResult = (ScanResult) getItem(i);
        textView.setText(scanResult.SSID);
        String ssid = this.mWifimanger.getConnectionInfo().getSSID();
        this.mSsid = ssid;
        if (ssid.subSequence(1, ssid.length() - 1).equals(scanResult.SSID)) {
            imageView.setVisibility(0);
        }
        return viewInflate;
    }
}
