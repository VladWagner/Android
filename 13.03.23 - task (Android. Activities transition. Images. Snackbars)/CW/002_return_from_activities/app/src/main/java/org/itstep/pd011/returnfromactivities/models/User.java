package org.itstep.pd011.returnfromactivities.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String fullName;
    private int age;
    private int salary;

    public User() { this("Иванова И.И.", 42, 34000); }

    public User(String fullName, int age, int salary) {
        this.fullName = fullName;
        this.age = age;
        this.salary = salary;
    }

    protected User(Parcel in) {
        fullName = in.readString();
        age = in.readInt();
        salary = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeInt(age);
        dest.writeInt(salary);
    }

    @Override
    public String toString() {
        return "User{" +
                "surnameNp='" + fullName + '\'' +
                ",\nage=" + age +
                ",\nsalary=" + salary +
                '}';
    }
}
