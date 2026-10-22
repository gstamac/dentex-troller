package com.juanpabloprado.countrypicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes2.dex */
public class CountryAdapter extends RecyclerView.Adapter<CountryHolder> implements Filterable {
    private static final Pattern ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    private CountryPicker mCountryPicker;
    private List<Country> mFilteredCountries = new ArrayList(CountryPicker.countries);
    private LayoutInflater mInflater;
    private CountryPickerListener mListener;

    public CountryAdapter(CountryPicker countryPicker, CountryPickerListener countryPickerListener) {
        this.mCountryPicker = countryPicker;
        this.mInflater = LayoutInflater.from(countryPicker.getActivity());
        this.mListener = countryPickerListener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public CountryHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CountryHolder(this.mInflater.inflate(R.layout.row, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(CountryHolder countryHolder, int i) {
        final Country country = this.mFilteredCountries.get(i);
        countryHolder.textView.setText(country.name);
        int identifier = this.mCountryPicker.getResources().getIdentifier("flag_" + country.code.toLowerCase(Locale.ENGLISH), "drawable", this.mCountryPicker.getActivity().getPackageName());
        if (identifier != 0) {
            countryHolder.imageView.setImageDrawable(ContextCompat.getDrawable(this.mCountryPicker.getActivity(), identifier));
        }
        countryHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.juanpabloprado.countrypicker.CountryAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CountryAdapter.this.mListener.onSelectCountry(country.name, country.code);
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mFilteredCountries.size();
    }

    public class CountryHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public CountryHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.row_title);
            this.imageView = (ImageView) view.findViewById(R.id.row_icon);
        }
    }

    public void refill(List<Country> list) {
        this.mFilteredCountries.clear();
        this.mFilteredCountries.addAll(list);
        notifyDataSetChanged();
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        return new Filter() { // from class: com.juanpabloprado.countrypicker.CountryAdapter.2
            @Override // android.widget.Filter
            protected Filter.FilterResults performFiltering(CharSequence charSequence) {
                Filter.FilterResults filterResults = new Filter.FilterResults();
                if (charSequence != null) {
                    ArrayList arrayList = new ArrayList();
                    for (Country country : CountryPicker.countries) {
                        if (CountryAdapter.this.containsIgnoreCaseAndAccents(country.name, (String) charSequence)) {
                            arrayList.add(country);
                        }
                    }
                    filterResults.values = arrayList;
                    filterResults.count = arrayList.size();
                }
                return filterResults;
            }

            @Override // android.widget.Filter
            protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                if (filterResults != null) {
                    CountryAdapter.this.refill((List) filterResults.values);
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean containsIgnoreCaseAndAccents(String str, String str2) {
        return removeAccents(str).toLowerCase().contains(removeAccents(str2).toLowerCase());
    }

    private String removeAccents(String str) {
        return ACCENTS_PATTERN.matcher(Normalizer.normalize(str, Normalizer.Form.NFD)).replaceAll("");
    }
}
