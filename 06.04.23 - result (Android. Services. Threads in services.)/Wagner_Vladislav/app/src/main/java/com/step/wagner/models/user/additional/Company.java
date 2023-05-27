package com.step.wagner.models.user.additional;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

//Класс компании
public class Company implements Parcelable {
    public String name;
    public String catchPhrase;
    public String bs;


    protected Company(Parcel in) {
        name = in.readString();
        catchPhrase = in.readString();
        bs = in.readString();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(catchPhrase);
        parcel.writeString(bs);
    }
}
