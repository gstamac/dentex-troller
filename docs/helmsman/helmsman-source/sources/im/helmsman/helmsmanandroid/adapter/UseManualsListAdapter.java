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
import im.helmsman.helmsmanandroid.dao.UserManualsDao;
import im.helmsman.helmsmanandroid.inet.model.ManualsListResultModel;
import im.helmsman.helmsmanandroid.presenter.UseManualsPresenter;
import java.io.File;
import java.util.List;
import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class UseManualsListAdapter extends BaseAdapter {
    private List<ManualsListResultModel.UserManualsBean> datas;
    private Context mContext;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder_ViewBinding implements Unbinder {
        private ViewHolder target;

        public ViewHolder_ViewBinding(ViewHolder viewHolder, View view) {
            this.target = viewHolder;
            viewHolder.ivManualsDownload = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_manuals_download, "field 'ivManualsDownload'", ImageView.class);
            viewHolder.tvManualName = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_manual_name, "field 'tvManualName'", TextView.class);
        }

        @Override // butterknife.Unbinder
        public void unbind() {
            ViewHolder viewHolder = this.target;
            if (viewHolder == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            viewHolder.ivManualsDownload = null;
            viewHolder.tvManualName = null;
        }
    }

    public UseManualsListAdapter(Context context, List<ManualsListResultModel.UserManualsBean> list) {
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
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_manuals, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ManualsListResultModel.UserManualsBean userManualsBean = (ManualsListResultModel.UserManualsBean) getItem(i);
        List listFind = DataSupport.where("manualsId=?", userManualsBean.getId() + "").find(UserManualsDao.class);
        File file = new File(UseManualsPresenter.MANUAL_PATH, userManualsBean.getId() + ".pdf");
        if (listFind.size() > 0) {
            UserManualsDao userManualsDao = (UserManualsDao) listFind.get(0);
            if (userManualsBean.getDownload_url().equals(userManualsDao.getDownloadUrl())) {
                viewHolder.ivManualsDownload.setVisibility(8);
            } else {
                userManualsDao.delete();
                file.delete();
                viewHolder.ivManualsDownload.setVisibility(0);
            }
        } else {
            viewHolder.ivManualsDownload.setVisibility(0);
            if (file.exists()) {
                file.delete();
            }
        }
        viewHolder.tvManualName.setText(userManualsBean.getTitle());
        return view;
    }

    static class ViewHolder {

        @BindView(R.id.iv_manuals_download)
        ImageView ivManualsDownload;

        @BindView(R.id.tv_manual_name)
        TextView tvManualName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
