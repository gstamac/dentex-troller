package im.helmsman.helmsmanandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.model.ArticlesResultModel;
import im.helmsman.helmsmanandroid.inet.model.SectionResultModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class QucikStartListAdapter extends BaseExpandableListAdapter {
    private Map<Integer, List<ArticlesResultModel.ArticlesBean>> child;
    private Context mContext;
    private List<SectionResultModel.SectionsBean> parent;

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int i, int i2) {
        return i * i2;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int i) {
        return i;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        return true;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    public QucikStartListAdapter(Context context, List<SectionResultModel.SectionsBean> list, Map<Integer, List<ArticlesResultModel.ArticlesBean>> map) {
        this.parent = new ArrayList();
        new HashMap();
        this.mContext = context;
        this.parent = list;
        this.child = map;
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        return this.parent.size();
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int i) {
        return this.child.get(Integer.valueOf(((SectionResultModel.SectionsBean) getGroup(i)).getId())).size();
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int i) {
        return this.parent.get(i);
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int i, int i2) {
        return this.child.get(Integer.valueOf(((SectionResultModel.SectionsBean) getGroup(i)).getId())).get(i2);
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_quickstart_parent, (ViewGroup) null);
        }
        ((TextView) view.findViewById(R.id.tv)).setText(((SectionResultModel.SectionsBean) getGroup(i)).getName());
        return view;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_quickstart_child, (ViewGroup) null);
        }
        ((TextView) view.findViewById(R.id.tv)).setText(((ArticlesResultModel.ArticlesBean) getChild(i, i2)).getName());
        return view;
    }
}
