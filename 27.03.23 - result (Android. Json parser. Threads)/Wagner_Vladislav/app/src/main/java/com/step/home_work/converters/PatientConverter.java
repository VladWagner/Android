package com.step.home_work.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.entities.Doctor;
import com.step.home_work.models.entities.Patient;

import java.lang.reflect.Type;
import java.util.Date;

public class PatientConverter extends BaseConverter<Patient>  {

    @Override
    public JsonElement serialize(Patient patient, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObj = new JsonObject();

        jsonObj.addProperty("id",patient.getId());
        jsonObj.addProperty("surname",patient.getSurname());
        jsonObj.addProperty("name",patient.getName());
        jsonObj.addProperty("patronymic",patient.getPatronymic());
        jsonObj.addProperty("birth_date", Utils.dateFormat.format(patient.getBirthDate()));
        jsonObj.addProperty("address",patient.getAddress());
        jsonObj.addProperty("passport",patient.getPassport());


        return jsonObj;
    }

    @Override
    public Patient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObj = json.getAsJsonObject();

        int id = jsonObj.get("id").getAsInt();
        String surname =    jsonObj.get("surname").getAsString();
        String name =       jsonObj.get("name").getAsString();
        String patronymic = jsonObj.get("patronymic").getAsString();
        Date birthDate =    Utils.tryParseDate(jsonObj.get("birth_date").getAsString());
        String address =    jsonObj.get("address").getAsString();
        String passport =   jsonObj.get("passport").getAsString();

        return new Patient(id,surname,name,patronymic,birthDate,address,passport);
    }

}
