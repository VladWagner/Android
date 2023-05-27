package com.step.wagner.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

// Класс Пост
public class Post implements Parcelable {

    private int id;

    //Заголовок
    private String title;

    //Содержание
    private String body;


    public Post(int id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }


    protected Post(Parcel in) {
        id = in.readInt();
        title = in.readString();
        body = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(body);
    }

    //region Acessors
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    //endregion
}
