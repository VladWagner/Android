package com.step.wagner.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.step.wagner.models.Message;

import java.lang.reflect.Type;

public class MessageConverter extends BaseConverter<Message>  {

    @Override
    public JsonElement serialize(Message message, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObj = new JsonObject();

        jsonObj.addProperty("id",message.getId());
        jsonObj.addProperty("has_attachment",message.getAttachment());
        jsonObj.addProperty("sender",message.getSender());
        jsonObj.addProperty("receiver",message.getReceiver());
        jsonObj.addProperty("subject",message.getSubject());
        jsonObj.addProperty("text",message.getText());


        return jsonObj;
    }

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObj = json.getAsJsonObject();

        int id = jsonObj.get("id").getAsInt();
        Boolean hasAttachments = jsonObj.get("has_attachment").getAsBoolean();
        String sender =   jsonObj.get("sender").getAsString();
        String receiver = jsonObj.get("receiver").getAsString();
        String subject =  jsonObj.get("subject").getAsString();
        String text =     jsonObj.get("text").getAsString();

        return new Message(id,sender,receiver,hasAttachments,subject,text);
    }

}
