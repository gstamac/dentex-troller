package com.juanpabloprado.countrypicker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class CountryPicker extends DialogFragment {
    private static final String DIALOG_TITLE_KEY = "dialogTitle";
    static List<Country> countries = getAllCountries();
    private CountryAdapter mAdapter;
    private CountryPickerListener mListener;
    private List<String> userCountryCodes;

    public static CountryPicker getInstance(String str, CountryPickerListener countryPickerListener) {
        CountryPicker countryPicker = getInstance(countryPickerListener);
        Bundle bundle = new Bundle();
        bundle.putString(DIALOG_TITLE_KEY, str);
        countryPicker.setArguments(bundle);
        return countryPicker;
    }

    public static CountryPicker getInstance(CountryPickerListener countryPickerListener) {
        CountryPicker countryPicker = new CountryPicker();
        countryPicker.mListener = countryPickerListener;
        return countryPicker;
    }

    public static CountryPicker getInstance(CountryPickerListener countryPickerListener, List<String> list) {
        CountryPicker countryPicker = getInstance(countryPickerListener);
        countryPicker.userCountryCodes = list;
        return countryPicker;
    }

    private static List<Country> getAllCountries() {
        ArrayList arrayList = new ArrayList();
        for (String str : Locale.getISOCountries()) {
            Country country = new Country();
            country.code = str;
            country.name = new Locale("", str).getDisplayCountry();
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
                country.name = new Locale("", str).getDisplayCountry();
                arrayList.add(country);
            }
        }
        return arrayList;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.country_picker, viewGroup, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            getDialog().setTitle(arguments.getString(DIALOG_TITLE_KEY));
            getDialog().getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.cp_dialog_width), getResources().getDimensionPixelSize(R.dimen.cp_dialog_height));
        }
        EditText editText = (EditText) viewInflate.findViewById(R.id.country_picker_search);
        RecyclerView recyclerView = (RecyclerView) viewInflate.findViewById(R.id.country_picker_recycler_view);
        List<String> list = this.userCountryCodes;
        if (list == null) {
            countries = getAllCountries();
        } else {
            countries = getAllCountries(list);
        }
        Collections.sort(countries);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, 1));
        CountryAdapter countryAdapter = new CountryAdapter(this, this.mListener);
        this.mAdapter = countryAdapter;
        recyclerView.setAdapter(countryAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.juanpabloprado.countrypicker.CountryPicker.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView2, int i) {
                super.onScrollStateChanged(recyclerView2, i);
                CountryPicker.this.hideKeyboard();
            }
        });
        editText.addTextChangedListener(new TextWatcher() { // from class: com.juanpabloprado.countrypicker.CountryPicker.2
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                CountryPicker.this.mAdapter.getFilter().filter(editable);
            }
        });
        return viewInflate;
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager;
        View currentFocus;
        try {
            FragmentActivity activity = getActivity();
            if (activity != null && (inputMethodManager = (InputMethodManager) activity.getSystemService("input_method")) != null && (currentFocus = activity.getCurrentFocus()) != null && currentFocus.getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        } catch (Exception unused) {
        }
    }
}
