package org.itstep.pd011.saveresorestate.models;

import android.os.Parcel;
import android.os.Parcelable;

// еще один пример класса для передачи в активность
public class Beveridge implements Parcelable {
    private String name;
    private int volume;
    private int cost;
    private boolean useTermoIsolation;    // использовать термоизоляцию

    public Beveridge() { this("чай черный", 200, 50, false); }

    public Beveridge(String name, int volume, int cost, boolean useTermoIsolation) {
        this.name = name;
        this.volume = volume;
        this.cost = cost;
        this.useTermoIsolation = useTermoIsolation;
    }

    protected Beveridge(Parcel in) {
        name   = in.readString();
        volume = in.readInt();
        cost   = in.readInt();

        // обходим требование на ограничение версии Android при передаче
        // boolean
        useTermoIsolation = in.readByte()!=0;
    } // Beveridge

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

    public boolean isUseTermoIsolation() {
        return useTermoIsolation;
    }
    public void setUseTermoIsolation(boolean value) {
        this.useTermoIsolation = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(volume);
        dest.writeInt(cost);
        dest.writeByte((byte) (useTermoIsolation?1:0));
    }

    @Override
    public String toString() {
        return "Beveridge{" +
                "name='" + name + '\'' +
                ", volume=" + volume +
                ", cost=" + cost +
                ", useTermoIsolation=" + useTermoIsolation +
                '}';
    } // toString
} // class Beveridge
