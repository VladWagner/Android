package com.step.wagner.content_providers.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.wagner.content_providers.infrastructure.Utils;

import java.util.Date;

public class Subscription implements Parcelable {

    private final int id;

    //Id издания
    private int publicationId;

    //Издание
    private Publication publication;

    //Дата начала подписки
    private Date dateStart;

    //Длительность подписки
    private int duration;

    public Subscription(int id, Publication publication, Date dateStart, int duration) {
        this.id = id;
        this.publication = publication;
        this.publicationId = publication.getId();
        this.dateStart = dateStart;
        this.duration = duration;
    }


    public Subscription(int id,
                        int publicationId,
                        String pubType,
                        String pubIndex,
                        String pubName,
                        int unitPrice,
                        Date dateStart,
                        int duration) {
        this.id = id;
        this.publicationId = publicationId;

        this.publication = new Publication(publicationId, pubType,0,pubIndex,pubName,unitPrice);

        this.dateStart = dateStart;
        this.duration = duration;
    }

    //region Parcelable
    protected Subscription(Parcel in) {
        id = in.readInt();
        publicationId = in.readInt();
        publication = in.readParcelable(Publication.class.getClassLoader());
        duration = in.readInt();

        dateStart = Utils.tryParseDBDate(in.readString());
    }

    public static final Creator<Subscription> CREATOR = new Creator<Subscription>() {
        @Override
        public Subscription createFromParcel(Parcel in) {
            return new Subscription(in);
        }

        @Override
        public Subscription[] newArray(int size) {
            return new Subscription[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(publicationId);
        parcel.writeParcelable(publication, i);
        parcel.writeInt(duration);

        parcel.writeString(Utils.DBdateFormat.format(dateStart));
    }
    //endregion


    //region Accessors
    public int getId() {
        return id;
    }

    public int getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    //endregion
}
