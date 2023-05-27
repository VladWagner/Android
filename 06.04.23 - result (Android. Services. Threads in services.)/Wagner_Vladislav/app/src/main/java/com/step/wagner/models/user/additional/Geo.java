package com.step.wagner.models.user.additional;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Geo implements Parcelable {
    public String lat;
    public String lng;

    protected Geo(Parcel in) {
        lat = in.readString();
        lng = in.readString();
    }

    public static final Creator<Geo> CREATOR = new Creator<Geo>() {
        @Override
        public Geo createFromParcel(Parcel in) {
            return new Geo(in);
        }

        @Override
        public Geo[] newArray(int size) {
            return new Geo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(lat);
        parcel.writeString(lng);
    }
}
