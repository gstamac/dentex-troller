package im.helmsman.helmsmanandroid.dao;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class BleBlueToothConnected extends DataSupport {
    private String address;
    private String alais;
    private String name;

    public BleBlueToothConnected(String str, String str2, String str3) {
        this.name = str;
        this.address = str2;
        this.alais = str3;
    }

    public BleBlueToothConnected(String str) {
        this.address = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public String getAlais() {
        return this.alais;
    }

    public void setAlais(String str) {
        this.alais = str;
    }

    public BluetoothDevice toBlueToothDevice() {
        return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(this.address);
    }

    public static BluetoothDevice toBlueToothDevice(BleBlueToothConnected bleBlueToothConnected) {
        return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(bleBlueToothConnected.address);
    }

    public static List<BluetoothDevice> toBlueToothDevices(List<BleBlueToothConnected> list) {
        ArrayList arrayList = new ArrayList();
        Iterator<BleBlueToothConnected> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(toBlueToothDevice(it.next()));
        }
        return arrayList;
    }
}
