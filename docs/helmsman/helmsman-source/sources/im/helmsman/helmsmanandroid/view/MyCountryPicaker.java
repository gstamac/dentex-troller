package im.helmsman.helmsmanandroid.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.juanpabloprado.countrypicker.Country;
import com.juanpabloprado.countrypicker.CountryAdapter;
import com.juanpabloprado.countrypicker.CountryPicker;
import com.juanpabloprado.countrypicker.CountryPickerListener;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class MyCountryPicaker extends CountryPicker {
    private static final String DIALOG_TITLE_KEY = "dialogTitle";
    private CountryAdapter mAdapter;
    private CountryPickerListener mListener;
    private List<String> userCountryCodes;

    @Override // com.juanpabloprado.countrypicker.CountryPicker, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        List<Country> allCountries;
        View viewInflate = layoutInflater.inflate(R.layout.country_picker, viewGroup, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            getDialog().setTitle(arguments.getString(DIALOG_TITLE_KEY));
            getDialog().getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.cp_dialog_width), getResources().getDimensionPixelSize(R.dimen.cp_dialog_height));
        }
        EditText editText = (EditText) viewInflate.findViewById(R.id.country_picker_search);
        editText.setSingleLine();
        RecyclerView recyclerView = (RecyclerView) viewInflate.findViewById(R.id.country_picker_recycler_view);
        List<String> list = this.userCountryCodes;
        if (list == null) {
            allCountries = getAllCountries();
        } else {
            allCountries = getAllCountries(list);
        }
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, 1));
        this.mAdapter = new CountryAdapter(this, this.mListener);
        try {
            Field declaredField = CountryAdapter.class.getDeclaredField("mFilteredCountries");
            declaredField.setAccessible(true);
            declaredField.set(this.mAdapter, allCountries);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
        recyclerView.setAdapter(this.mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: im.helmsman.helmsmanandroid.view.MyCountryPicaker.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView2, int i) {
                super.onScrollStateChanged(recyclerView2, i);
                MyCountryPicaker.this.hideKeyboard();
            }
        });
        editText.addTextChangedListener(new TextWatcher() { // from class: im.helmsman.helmsmanandroid.view.MyCountryPicaker.2
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                MyCountryPicaker.this.mAdapter.getFilter().filter(editable);
            }
        });
        return viewInflate;
    }

    public static MyCountryPicaker getInstance(String str, CountryPickerListener countryPickerListener) {
        MyCountryPicaker myCountryPicaker = getInstance(countryPickerListener);
        Bundle bundle = new Bundle();
        bundle.putString(DIALOG_TITLE_KEY, str);
        myCountryPicaker.setArguments(bundle);
        return myCountryPicaker;
    }

    public static MyCountryPicaker getInstance(CountryPickerListener countryPickerListener) {
        MyCountryPicaker myCountryPicaker = new MyCountryPicaker();
        myCountryPicaker.mListener = countryPickerListener;
        return myCountryPicaker;
    }

    public static MyCountryPicaker getInstance(CountryPickerListener countryPickerListener, List<String> list) {
        MyCountryPicaker myCountryPicaker = getInstance(countryPickerListener);
        myCountryPicaker.userCountryCodes = list;
        return myCountryPicaker;
    }

    private static List<Country> getAllCountries() {
        ArrayList arrayList = new ArrayList();
        for (String str : Locale.getISOCountries()) {
            Country country = new Country();
            country.code = str;
            country.name = StringUtils.getCountryNameByCode(str);
            arrayList.add(country);
        }
        return arrayList;
    }

    private static List<Country> getAllCountries(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (String str : Locale.getISOCountries()) {
            if (list.contains(str)) {
                Country country = new Country();
                country.code = str;
                country.name = StringUtils.getCountryNameByCode(str);
                arrayList.add(country);
            }
        }
        return arrayList;
    }
}
