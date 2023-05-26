package org.itstep.pd011.jsonparserexample.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

// http://www.javenue.info/post/gson-json-api
public class ProductConverter implements JsonSerializer<Product>, JsonDeserializer<Product> {
    public JsonElement serialize(Product src, Type type,
                                 JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("name", src.getName());
        object.addProperty("price", src.getPrice());
        object.addProperty("amount", src.getAmount());
        return object;
    } // serialize

    public Product deserialize(JsonElement json, Type type,
                              JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String name = object.get("name").getAsString();
        int price = object.get("price").getAsInt();
        int amount = object.get("amount").getAsInt();

        return new Product(name, price, amount);
    } // deserialize
} // class ProductConverter
