package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.model.ArticlesResultModel;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class HelmsmanCollegeListAdapter extends BaseAdapter {
    private List<ArticlesResultModel.ArticlesBean> datas;
    private Context mContext;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder_ViewBinding implements Unbinder {
        private ViewHolder target;

        public ViewHolder_ViewBinding(ViewHolder viewHolder, View view) {
            this.target = viewHolder;
            viewHolder.tvHelmsmancollegeItemTitle = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_helmsmancollege_item_title, "field 'tvHelmsmancollegeItemTitle'", TextView.class);
        }

        @Override // butterknife.Unbinder
        public void unbind() {
            ViewHolder viewHolder = this.target;
            if (viewHolder == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            viewHolder.tvHelmsmancollegeItemTitle = null;
        }
    }

    public HelmsmanCollegeListAdapter(Context context, List<ArticlesResultModel.ArticlesBean> list) {
        this.mContext = context;
        this.datas = list;
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
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_helmsmancollege_list, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvHelmsmancollegeItemTitle.setText(((ArticlesResultModel.ArticlesBean) getItem(i)).getTitle());
        return view;
    }

    static class ViewHolder {

        @BindView(R.id.tv_helmsmancollege_item_title)
        TextView tvHelmsmancollegeItemTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
