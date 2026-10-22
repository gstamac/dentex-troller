package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.model.DrawerMenuModel;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class DrawerMenuRecyclerAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private OnItemClickListener listener;
    private Context mContext;
    private List<DrawerMenuModel> mDatas;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public DrawerMenuRecyclerAdapter(Context context, List<DrawerMenuModel> list) {
        this.mContext = context;
        this.mDatas = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ListViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_drawer_menu, (ViewGroup) null));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ListViewHolder listViewHolder, int i) {
        listViewHolder.setData(this.mDatas.get(i), i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDatas.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv;
        private int position;
        private TextView tv;

        public ListViewHolder(View view) {
            super(view);
            this.tv = (TextView) view.findViewById(R.id.tv_drawermenu_menustr);
            this.iv = (ImageView) view.findViewById(R.id.iv_drawermenu_icon);
            view.setOnClickListener(this);
        }

        public void setData(DrawerMenuModel drawerMenuModel, int i) {
            this.tv.setText(DrawerMenuRecyclerAdapter.this.mContext.getString(drawerMenuModel.getMenuString()));
            this.iv.setImageResource(drawerMenuModel.getImageRec());
            this.position = i;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (DrawerMenuRecyclerAdapter.this.listener != null) {
                DrawerMenuRecyclerAdapter.this.listener.onItemClick(this.itemView, this.position);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }
}
