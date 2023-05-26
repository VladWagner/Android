package com.step.home_work.models.ship;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.home_work.infrastructure.Utils;

public class ShipType implements Parcelable {

    private String type;

    private int typeId;

    public ShipType(String type, int typeId) {
        this.type = type;
        this.typeId = typeId;
    }

    protected ShipType(Parcel in) {
        type = in.readString();
        typeId = in.readInt();
    }

    public static final Creator<ShipType> CREATOR = new Creator<ShipType>() {
        @Override
        public ShipType createFromParcel(Parcel in) {
            return new ShipType(in);
        }

        @Override
        public ShipType[] newArray(int size) {
            return new ShipType[size];
        }
    };

    //Фабричный метод
    public static ShipType factory(){
        return Utils.getShipType();
    }


    //region Accessors
    public String getType() {
        return type;
    }

    public void setType(String type) throws Exception {
        if (type.isBlank())
            throw new Exception("Тип корабля задан некоррктно!");
        this.type = type;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) throws Exception {

        if (typeId < 0)
            throw new Exception("Id типа корабля не должен быть <= 0!");

        this.typeId = typeId;
    }
    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeInt(typeId);
    }
}
