package org.itstep.pd011.serviceintropart2;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

// пример передаваемого сервису объекта
public class SomeClass implements Parcelable {
    private int value;
    private String name;

    // блок инициализации
    {
        value = -1;
        name = "no name";
    }

    public SomeClass() { }

    public SomeClass(int value, String name) {
        this.value = value;
        this.name = name;
    } // SomeClass

    protected SomeClass(Parcel in) {
        value = in.readInt();
        name = in.readString();
    }

    public static final Creator<SomeClass> CREATOR = new Creator<SomeClass>() {
        @Override
        public SomeClass createFromParcel(Parcel in) {
            return new SomeClass(in);
        }

        @Override
        public SomeClass[] newArray(int size) {
            return new SomeClass[size];
        }
    };

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return String.format("{value=%d, name='%s'", value, name);
    }
} // class SomeClass
