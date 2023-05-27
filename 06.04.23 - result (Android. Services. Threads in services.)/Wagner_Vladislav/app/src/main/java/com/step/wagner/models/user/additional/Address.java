package com.step.wagner.models.user.additional;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

//Класс адреса
public class Address implements Parcelable {

    public String street;

    public String suite;

    public String city;

    public String zipcode;

    public Geo geo;

    public Address() {
    }

    //region Parcelable
    protected Address(Parcel in) {
        street = in.readString();
        suite = in.readString();
        city = in.readString();
        zipcode = in.readString();
        geo = in.readParcelable(Geo.class.getClassLoader());
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(street);
        parcel.writeString(suite);
        parcel.writeString(city);
        parcel.writeString(zipcode);
        parcel.writeParcelable(geo, i);
    }
    //endregion
}
