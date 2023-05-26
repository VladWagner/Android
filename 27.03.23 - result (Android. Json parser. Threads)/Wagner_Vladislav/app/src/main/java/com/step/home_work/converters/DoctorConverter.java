package com.step.home_work.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.entities.Appointment;
import com.step.home_work.models.entities.Doctor;

import java.lang.reflect.Type;
import java.util.Date;

public class DoctorConverter extends BaseConverter<Doctor>  {

    @Override
    public JsonElement serialize(Doctor doctor, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObj = new JsonObject();

        jsonObj.addProperty("id",doctor.getId());
        jsonObj.addProperty("surname",doctor.getSurname());
        jsonObj.addProperty("name",doctor.getName());
        jsonObj.addProperty("patronymic",doctor.getPatronymic());
        jsonObj.addProperty("speciality",doctor.getSpeciality());
        jsonObj.addProperty("percent",doctor.getPercent());
        jsonObj.addProperty("payment",doctor.getPayment());


        return jsonObj;
    }

    @Override
    public Doctor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObj = json.getAsJsonObject();

        int id = jsonObj.get("id").getAsInt();
        String surname =    jsonObj.get("surname").getAsString();
        String name =       jsonObj.get("name").getAsString();
        String patronymic = jsonObj.get("patronymic").getAsString();
        String speciality = jsonObj.get("speciality").getAsString();
        double percent =    jsonObj.get("percent").getAsDouble();
        int payment =       jsonObj.get("payment").getAsInt();

        return new Doctor(id,surname,name,patronymic,speciality,percent,payment);
    }

}
