package org.itstep.pd011.androiddialogpart4;

import android.os.Parcel;
import android.os.Parcelable;

// Демонстрация передачи в диалог сложного типа данных
public class Customer implements Parcelable {
    private String name;     // имя
    private String surname;  // фамилия
    private int    amount;   // располагаемая сумма денег

    public Customer(String name, String surname, int amount) {
        this.name = name;
        this.surname = surname;
        this.amount = amount;
    }

    public Customer() {
        this("Галина", "Регузова", 78_400);
    }
	
    protected Customer(Parcel in) {
        name = in.readString();
        surname = in.readString();
        amount = in.readInt();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return surname + " " + name + ", располагает суммой " + amount + " руб.";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeInt(amount);
    }
}
