package com.step.wagner.models.queries;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.wagner.infrastructure.Utils;

import java.text.ParseException;
import java.util.Date;

public class Query6 implements Parcelable {

    private Date appointmentDate;
    private int amount;
    private int maxPrice;

    public Query6(Date appointmentDate, int amount, int maxPrice) {
        this.appointmentDate = appointmentDate;
        this.amount = amount;
        this.maxPrice = maxPrice;
    }

    //region Parcelable
    protected Query6(Parcel in) {
        //Получение даты приема
        try {
            appointmentDate = Utils.DBdateFormat.parse(in.readString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        amount = in.readInt();
        maxPrice = in.readInt();
    }

    public static final Creator<Query6> CREATOR = new Creator<Query6>() {
        @Override
        public Query6 createFromParcel(Parcel in) {
            return new Query6(in);
        }

        @Override
        public Query6[] newArray(int size) {
            return new Query6[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        //Записать дату приема
        parcel.writeString(Utils.DBdateFormat.format(appointmentDate));
        parcel.writeInt(amount);
        parcel.writeInt(maxPrice);
    }
    //endregion


    //region Accessors
    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }
    //endregion
}
