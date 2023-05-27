package com.step.wagner.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.Utils;

public class Message implements Parcelable {

    private int id;

    private String sender;

    private String receiver;

    private Boolean isAttachment;

    private String subject;

    private String text;

    public Message(String sender, String receiver, Boolean isAttachment, String subject, String text) {
        this.id = ++Parameters.messageId;
        this.sender = sender;
        this.receiver = receiver;
        this.isAttachment = isAttachment;
        this.subject = subject;
        this.text = text;
    }

    public Message(int id, String sender, String receiver, Boolean isAttachment, String subject, String text) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.isAttachment = isAttachment;
        this.subject = subject;
        this.text = text;
    }

    //Фабричный метод
    public static Message factory(){
        return new Message(
                Utils.getSender(),
                Utils.getReceiver(),
                Utils.getRandom(0,2) > 0,
                Utils.getRandom(0,2) > 0 ? Utils.getSubject() : "",
                Utils.getText()
        );
    }

    //region Parcelable
    protected Message(Parcel in) {
        id = in.readInt();
        sender = in.readString();
        receiver = in.readString();
        byte tmpIsAttachment = in.readByte();
        isAttachment = tmpIsAttachment == 0 ? null : tmpIsAttachment == 1;
        subject = in.readString();
        text = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(sender);
        parcel.writeString(receiver);
        parcel.writeByte((byte) (isAttachment == null ? 0 : isAttachment ? 1 : 2));
        parcel.writeString(subject);
        parcel.writeString(text);
    }
    //endregion

    //region Accessors

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Boolean getAttachment() {
        return isAttachment;
    }

    public void setAttachment(Boolean attachment) {
        isAttachment = attachment;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    //endregion
}
