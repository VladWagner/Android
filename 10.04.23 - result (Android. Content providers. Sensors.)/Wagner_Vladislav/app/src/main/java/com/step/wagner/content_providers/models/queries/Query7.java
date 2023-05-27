package com.step.wagner.content_providers.models.queries;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Query7 implements Parcelable {

    private String speciality;
    private int amount;
    private double avgPercent;

    public Query7(String speciality,
                  int amount,
                  double avgPercent) {
        this.speciality = speciality;
        this.amount = amount;
        this.avgPercent = avgPercent;
    }


    //region Parcelable

    protected Query7(Parcel in) {
        speciality = in.readString();
        amount = in.readInt();
        avgPercent = in.readDouble();
    }

    public static final Creator<Query7> CREATOR = new Creator<Query7>() {
        @Override
        public Query7 createFromParcel(Parcel in) {
            return new Query7(in);
        }

        @Override
        public Query7[] newArray(int size) {
            return new Query7[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(speciality);
        parcel.writeInt(amount);
        parcel.writeDouble(avgPercent);
    }

    //endregion


    //region Accessors

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getAvgPercent() {
        return avgPercent;
    }

    public void setAvgPercent(double avgPercent) {
        this.avgPercent = avgPercent;
    }

    //endregion
}
