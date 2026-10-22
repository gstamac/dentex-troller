package im.helmsman.helmsmanandroid.adapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.dao.BleBlueToothConnected;
import java.util.ArrayList;
import java.util.List;
import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class BleBlueToothListAdapter extends BaseAdapter {
    private int connectedSize;
    private boolean isCheck;
    private List<BluetoothDevice> mCheckLists;
    private Context mContext;
    private List<BluetoothDevice> mDatas;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public BleBlueToothListAdapter(Context context, List<BluetoothDevice> list, List<BluetoothDevice> list2) {
        new ArrayList();
        this.isCheck = false;
        this.connectedSize = 0;
        this.mContext = context;
        this.mDatas = list;
        this.mCheckLists = list2;
        if (MyApplication.getInstance().bluetoothDevice == null || this.mDatas.contains(MyApplication.getInstance().bluetoothDevice)) {
            return;
        }
        this.mDatas.add(MyApplication.getInstance().bluetoothDevice);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        if (Build.VERSION.SDK_INT >= 31 && (ContextCompat.checkSelfPermission(this.mContext, "android.permission.BLUETOOTH_SCAN") != 0 || ContextCompat.checkSelfPermission(this.mContext, "android.permission.BLUETOOTH_ADVERTISE") != 0 || ContextCompat.checkSelfPermission(this.mContext, "android.permission.BLUETOOTH_CONNECT") != 0)) {
            this.mDatas.clear();
            this.mDatas.add(0, null);
            this.mDatas.add(1, null);
            return 2;
        }
        return this.mDatas.size();
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        List listFindAll = DataSupport.findAll(BleBlueToothConnected.class, new long[0]);
        this.connectedSize = listFindAll.size();
        if (!this.mDatas.contains(null)) {
            this.mDatas.add(0, null);
            List<BluetoothDevice> blueToothDevices = BleBlueToothConnected.toBlueToothDevices(listFindAll);
            if (blueToothDevices != null) {
                sortConnectedDevices(blueToothDevices);
            }
            this.mDatas.add(blueToothDevices.size() + 1, null);
        }
        if (MyApplication.getInstance().bluetoothDevice != null) {
            if (this.mDatas.contains(MyApplication.getInstance().bluetoothDevice)) {
                this.mDatas.remove(MyApplication.getInstance().bluetoothDevice);
            }
            this.mDatas.add(1, MyApplication.getInstance().bluetoothDevice);
        }
        super.notifyDataSetChanged();
    }

    private void sortConnectedDevices(List<BluetoothDevice> list) {
        for (int i = 0; i < list.size(); i++) {
            if (this.mDatas.contains(list.get(i))) {
                this.mDatas.remove(list.get(i));
            }
            this.mDatas.add(1, list.get(i));
        }
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.mDatas.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate = LayoutInflater.from(this.mContext).inflate(R.layout.item_bluebooth_list, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_item_bluebooth_name);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.tv_item_bluebooth_address);
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.iv_item_bluetooth_correct);
        TextView textView3 = (TextView) viewInflate.findViewById(R.id.tv_item_mydevices);
        TextView textView4 = (TextView) viewInflate.findViewById(R.id.tv_item_state);
        CheckBox checkBox = (CheckBox) viewInflate.findViewById(R.id.cb_ble_connect_delete);
        BluetoothDevice bluetoothDevice = (BluetoothDevice) getItem(i);
        if (bluetoothDevice == null) {
            checkBox.setVisibility(8);
            if (i == 0) {
                textView3.setText(this.mContext.getString(R.string.blebluetooth_connect_mydevice));
            } else {
                textView3.setText(this.mContext.getString(R.string.blebluetooth_connect_device));
            }
            textView.setVisibility(8);
            textView3.setVisibility(0);
            textView4.setVisibility(8);
            viewInflate.setClickable(true);
            return viewInflate;
        }
        if (i < this.connectedSize + 1 && this.isCheck) {
            checkBox.setVisibility(0);
            checkBox.setChecked(this.mCheckLists.contains(bluetoothDevice));
        } else {
            checkBox.setVisibility(8);
        }
        BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(bluetoothDevice.getAddress());
        viewInflate.setClickable(false);
        textView.setVisibility(0);
        textView4.setVisibility(0);
        textView3.setVisibility(8);
        textView.setText(remoteDevice.getName());
        List listFind = DataSupport.where("address=?", remoteDevice.getAddress()).find(BleBlueToothConnected.class);
        if (listFind.size() > 0) {
            String name = ((BleBlueToothConnected) listFind.get(0)).getName();
            String alais = ((BleBlueToothConnected) listFind.get(0)).getAlais();
            if (!TextUtils.isEmpty(alais)) {
                name = alais;
            }
            textView.setText(name);
        } else if (remoteDevice.getName() == null || remoteDevice.getName().equals("")) {
            textView.setText(R.string.unknow_device);
        } else {
            textView.setText(remoteDevice.getName());
        }
        textView2.setText(remoteDevice.getAddress());
        if (MyApplication.getInstance().bluetoothDevice != null && MyApplication.getInstance().bluetoothDevice.equals(remoteDevice)) {
            imageView.setVisibility(0);
            textView.setTextColor(this.mContext.getResources().getColor(R.color.bluetool_blue));
            textView4.setText(this.mContext.getString(R.string.bluetooth_connected));
        }
        return viewInflate;
    }

    public void setIsCheck(boolean z) {
        this.isCheck = z;
        notifyDataSetChanged();
    }

    public boolean getIsCheck() {
        return this.isCheck;
    }
}
