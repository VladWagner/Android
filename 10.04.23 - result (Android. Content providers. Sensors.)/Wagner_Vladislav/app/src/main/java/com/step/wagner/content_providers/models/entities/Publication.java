package com.step.wagner.content_providers.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Publication implements Parcelable {

    private final int id;

    //Вид издания
    private String publicationType;
    private int publicationTypeId;

    //Индекс издания
    private String publicationIndex;

    //Наименование издания
    private String publicationName;

    //Стоимость единицы
    private int unitPrice;

    public Publication(int id,
                       String publicationType,
                       int typeId,
                       String publicationIndex,
                       String publicationName,
                       int unitPrice) {
        this.id = id;
        this.publicationType = publicationType;
        this.publicationTypeId = typeId;
        this.publicationIndex = publicationIndex;
        this.publicationName = publicationName;
        this.unitPrice = unitPrice;
    }


    //region Parcelable
    protected Publication(Parcel in) {
        id = in.readInt();
        publicationType = in.readString();
        publicationTypeId = in.readInt();
        publicationIndex = in.readString();
        publicationName = in.readString();
        unitPrice = in.readInt();
    }

    public static final Creator<Publication> CREATOR = new Creator<Publication>() {
        @Override
        public Publication createFromParcel(Parcel in) {
            return new Publication(in);
        }

        @Override
        public Publication[] newArray(int size) {
            return new Publication[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(publicationType);
        parcel.writeInt(publicationTypeId);
        parcel.writeString(publicationIndex);
        parcel.writeString(publicationName);
        parcel.writeInt(unitPrice);
    }
    //endregion


    //region Accessors

    public int getId() {
        return id;
    }

    //Вид издания
    public String getPublicationType() {
        return publicationType;
    }

    public void setPublicationType(String publicationType) {
        this.publicationType = publicationType;
    }

    //Id вида издания

    public int getPublicationTypeId() {
        return publicationTypeId;
    }

    public void setPublicationTypeId(int publicationTypeId) {
        this.publicationTypeId = publicationTypeId;
    }

    //Индекс издания
    public String getPublicationIndex() {
        return publicationIndex;
    }

    public void setPublicationIndex(String publicationIndex) {
        this.publicationIndex = publicationIndex;
    }

    //Наименование издания
    public String getPublicationName() {
        return publicationName;
    }

    public void setPublicationName(String publicationName) {
        this.publicationName = publicationName;
    }

    //Стоимость единицы
    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }
    //endregion
}
