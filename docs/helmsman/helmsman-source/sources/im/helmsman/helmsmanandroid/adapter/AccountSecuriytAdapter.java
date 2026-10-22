package im.helmsman.helmsmanandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class AccountSecuriytAdapter extends BaseAdapter {
    private boolean emailIsVerifiled;
    private boolean fbIsNull;
    private List<Map<String, Integer>> listDatas = new ArrayList();
    private boolean wxIsNull;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public AccountSecuriytAdapter(boolean z, boolean z2, boolean z3) {
        this.wxIsNull = z;
        this.fbIsNull = z2;
        this.emailIsVerifiled = z3;
        HashMap map = new HashMap();
        map.put("textid", Integer.valueOf(R.string.email));
        map.put("imageid", Integer.valueOf(R.drawable.icon_email));
        HashMap map2 = new HashMap();
        map2.put("textid", Integer.valueOf(R.string.password));
        map2.put("imageid", Integer.valueOf(R.drawable.icon_lock));
        HashMap map3 = new HashMap();
        map3.put("textid", Integer.valueOf(R.string.face_book));
        map3.put("imageid", Integer.valueOf(R.drawable.icon_fb_rect));
        HashMap map4 = new HashMap();
        map4.put("textid", Integer.valueOf(R.string.weixin));
        map4.put("imageid", Integer.valueOf(R.drawable.icon_wx_rect));
        this.listDatas.add(map);
        this.listDatas.add(map2);
        this.listDatas.add(map3);
        this.listDatas.add(map4);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.listDatas.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.listDatas.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate = LayoutInflater.from(Base2Activity.getTopActivity()).inflate(R.layout.item_account_securiyt_list, viewGroup, false);
        Map map = (Map) getItem(i);
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.iv_account_securiyt_icon);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_account_securiyt_text);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.tv_account_securiyt_bind_state);
        int iIntValue = ((Integer) map.get("textid")).intValue();
        int iIntValue2 = ((Integer) map.get("imageid")).intValue();
        textView.setText(iIntValue);
        imageView.setImageResource(iIntValue2);
        int i2 = R.string.Bound;
        if (i == 0) {
            if (!this.emailIsVerifiled) {
                i2 = R.string.not_bound;
            }
            textView2.setText(i2);
        } else if (i == 1) {
            textView2.setVisibility(8);
        } else if (i == 2) {
            if (this.fbIsNull) {
                i2 = R.string.not_bound;
            }
            textView2.setText(i2);
        } else if (i == 3) {
            if (this.wxIsNull) {
                i2 = R.string.not_bound;
            }
            textView2.setText(i2);
        }
        return viewInflate;
    }
}
