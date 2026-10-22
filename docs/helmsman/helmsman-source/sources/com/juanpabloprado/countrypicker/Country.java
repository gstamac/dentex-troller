package com.juanpabloprado.countrypicker;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class Country implements Parcelable, Comparable<Country> {
    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() { // from class: com.juanpabloprado.countrypicker.Country.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Country createFromParcel(Parcel parcel) {
            Country country = new Country();
            country.code = parcel.readString();
            country.name = parcel.readString();
            return country;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Country[] newArray(int i) {
            return new Country[i];
        }
    };
    public String code;
    public String name;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.code);
        parcel.writeString(this.name);
    }

    @Override // java.lang.Comparable
    public int compareTo(Country country) {
        return this.name.compareTo(country.name);
    }
}
