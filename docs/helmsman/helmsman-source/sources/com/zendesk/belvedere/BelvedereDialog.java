package com.zendesk.belvedere;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class BelvedereDialog extends AppCompatDialogFragment {
    private static final String EXTRA_INTENT = "extra_intent";
    private static final String FRAGMENT_TAG = "BelvedereDialog";
    private static final String LOG_TAG = "BelvedereDialog";
    private ListView listView;

    private interface StartActivity {
        Context getContext();

        void startActivity(BelvedereIntent belvedereIntent);
    }

    public static void showDialog(FragmentManager fragmentManager, List<BelvedereIntent> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        BelvedereDialog belvedereDialog = new BelvedereDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_INTENT, new ArrayList<>(list));
        belvedereDialog.setArguments(bundle);
        belvedereDialog.show(fragmentManager.beginTransaction(), "BelvedereDialog");
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.belvedere_dialog, viewGroup, false);
        this.listView = (ListView) viewInflate.findViewById(R.id.belvedere_dialog_listview);
        return viewInflate;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(1, getTheme());
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ArrayList parcelableArrayList = getArguments().getParcelableArrayList(EXTRA_INTENT);
        if (getParentFragment() != null) {
            final Fragment parentFragment = getParentFragment();
            fillListView(new StartActivity() { // from class: com.zendesk.belvedere.BelvedereDialog.1
                @Override // com.zendesk.belvedere.BelvedereDialog.StartActivity
                public void startActivity(BelvedereIntent belvedereIntent) {
                    belvedereIntent.open(parentFragment);
                }

                @Override // com.zendesk.belvedere.BelvedereDialog.StartActivity
                public Context getContext() {
                    return parentFragment.getContext();
                }
            }, parcelableArrayList);
        } else if (getActivity() != null) {
            final FragmentActivity activity = getActivity();
            fillListView(new StartActivity() { // from class: com.zendesk.belvedere.BelvedereDialog.2
                @Override // com.zendesk.belvedere.BelvedereDialog.StartActivity
                public void startActivity(BelvedereIntent belvedereIntent) {
                    belvedereIntent.open(activity);
                }

                @Override // com.zendesk.belvedere.BelvedereDialog.StartActivity
                public Context getContext() {
                    return activity;
                }
            }, parcelableArrayList);
        } else {
            Log.w("BelvedereDialog", "Not able to find a valid context for starting an BelvedereIntent");
            dismiss();
        }
    }

    private void fillListView(final StartActivity startActivity, List<BelvedereIntent> list) {
        this.listView.setAdapter((ListAdapter) new Adapter(startActivity.getContext(), R.layout.belvedere_dialog_row, list));
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.zendesk.belvedere.BelvedereDialog.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (view.getTag() instanceof BelvedereIntent) {
                    startActivity.startActivity((BelvedereIntent) view.getTag());
                    BelvedereDialog.this.dismiss();
                }
            }
        });
    }

    private class Adapter extends ArrayAdapter<BelvedereIntent> {
        private Context context;

        Adapter(Context context, int i, List<BelvedereIntent> list) {
            super(context, i, list);
            this.context = context;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(this.context).inflate(R.layout.belvedere_dialog_row, viewGroup, false);
            }
            BelvedereIntent item = getItem(i);
            AttachmentSource attachmentSourceFrom = AttachmentSource.from(item, this.context);
            ((ImageView) view.findViewById(R.id.belvedere_dialog_row_image)).setImageDrawable(ContextCompat.getDrawable(this.context, attachmentSourceFrom.getDrawable()));
            ((TextView) view.findViewById(R.id.belvedere_dialog_row_text)).setText(attachmentSourceFrom.getText());
            view.setTag(item);
            return view;
        }
    }

    /* JADX INFO: renamed from: com.zendesk.belvedere.BelvedereDialog$4, reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$zendesk$belvedere$BelvedereSource;

        static {
            int[] iArr = new int[BelvedereSource.values().length];
            $SwitchMap$com$zendesk$belvedere$BelvedereSource = iArr;
            try {
                iArr[BelvedereSource.Camera.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$zendesk$belvedere$BelvedereSource[BelvedereSource.Gallery.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    private static class AttachmentSource {
        private final int drawable;
        private final String text;

        public static AttachmentSource from(BelvedereIntent belvedereIntent, Context context) {
            int i = AnonymousClass4.$SwitchMap$com$zendesk$belvedere$BelvedereSource[belvedereIntent.getSource().ordinal()];
            if (i == 1) {
                return new AttachmentSource(R.drawable.ic_camera, context.getString(R.string.belvedere_dialog_camera));
            }
            if (i == 2) {
                return new AttachmentSource(R.drawable.ic_image, context.getString(R.string.belvedere_dialog_gallery));
            }
            return new AttachmentSource(-1, context.getString(R.string.belvedere_dialog_unknown));
        }

        private AttachmentSource(int i, String str) {
            this.drawable = i;
            this.text = str;
        }

        public int getDrawable() {
            return this.drawable;
        }

        public String getText() {
            return this.text;
        }
    }
}
