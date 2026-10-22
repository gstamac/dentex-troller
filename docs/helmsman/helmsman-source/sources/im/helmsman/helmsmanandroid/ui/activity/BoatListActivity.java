package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.BoatListAdapter;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.DaoUtil;

/* JADX INFO: loaded from: classes2.dex */
public class BoatListActivity extends Base2Activity implements AdapterView.OnItemClickListener {
    private ListView mLvBoatList;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_boat_list);
        initView();
        initEvent();
    }

    private void initView() {
        this.mLvBoatList = (ListView) findViewById(R.id.lv_boatlist);
        this.mLvBoatList.setAdapter((ListAdapter) new BoatListAdapter(this, DaoUtil.getInstance().queryAllBoatList()));
    }

    private void initEvent() {
        this.mLvBoatList.setOnItemClickListener(this);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        String string = ((TextView) view.findViewById(R.id.tv_itemboatlist_ssid)).getText().toString();
        Intent intent = new Intent(this, (Class<?>) MyDevicesActivity.class);
        intent.putExtra("ssid", string);
        startActivity(intent);
    }
}
