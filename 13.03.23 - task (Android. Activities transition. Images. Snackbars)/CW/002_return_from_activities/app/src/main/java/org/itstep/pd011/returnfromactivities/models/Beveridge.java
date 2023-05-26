package org.itstep.pd011.returnfromactivities.models;

import android.os.Parcel;
import android.os.Parcelable;

// еще один пример класса для передачи в активность
public class Beveridge implements Parcelable {
    private String name;
    private int volume;
    private int cost;

    public Beveridge() { this("чай черный", 200, 50); }

    public Beveridge(String name, int volume, int cost) {
        this.name = name;
        this.volume = volume;
        this.cost = cost;
    }


    protected Beveridge(Parcel in) {
        name = in.readString();
        volume = in.readInt();
        cost = in.readInt();
    }

    public static final Creator<Beveridge> CREATOR = new Creator<Beveridge>() {
        @Override
        public Beveridge createFromParcel(Parcel in) {
            return new Beveridge(in);
        }

        @Override
        public Beveridge[] newArray(int size) {
            return new Beveridge[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Beveridge{" +
                "name='" + name + '\'' +
                ",\nvolume=" + volume +
                ",\ncost=" + cost +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(volume);
        parcel.writeInt(cost);
    }
}
