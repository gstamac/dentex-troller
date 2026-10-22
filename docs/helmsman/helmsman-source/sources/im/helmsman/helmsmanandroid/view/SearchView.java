package im.helmsman.helmsmanandroid.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class SearchView extends LinearLayout implements TextWatcher {
    private EditText etSearchCont;
    private OnSearchViewTextChangeListener listener;
    private String searchHint;

    public interface OnSearchViewTextChangeListener {
        void onTextChange(String str);
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public SearchView(Context context) {
        super(context);
        initView(context);
    }

    public SearchView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.searchHint = context.obtainStyledAttributes(attributeSet, R.styleable.SearchView).getString(8);
        initView(context);
    }

    public SearchView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_searchview_layout, (ViewGroup) this, true);
        EditText editText = (EditText) findViewById(R.id.et_searchview_content);
        this.etSearchCont = editText;
        editText.setSingleLine();
        Drawable drawable = getResources().getDrawable(R.drawable.icon_search);
        drawable.setBounds(0, 0, 70, 70);
        this.etSearchCont.setCompoundDrawables(drawable, null, null, null);
        this.etSearchCont.addTextChangedListener(this);
        this.etSearchCont.setHint(this.searchHint);
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        OnSearchViewTextChangeListener onSearchViewTextChangeListener;
        if (editable == null || (onSearchViewTextChangeListener = this.listener) == null) {
            return;
        }
        onSearchViewTextChangeListener.onTextChange(editable.toString());
    }

    public void setOnSearchViewTextChangeListener(OnSearchViewTextChangeListener onSearchViewTextChangeListener) {
        this.listener = onSearchViewTextChangeListener;
    }

    public void clear() {
        EditText editText = this.etSearchCont;
        if (editText != null) {
            editText.setText("");
        }
    }

    public EditText getEditText() {
        return this.etSearchCont;
    }

    public void setText(String str) {
        this.etSearchCont.setText(str);
    }

    public String getText() {
        return this.etSearchCont.getText().toString();
    }
}
