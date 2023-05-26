package com.step.wagner.converters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.entities.Appointment;
import com.step.wagner.models.entities.Doctor;
import com.step.wagner.models.entities.Patient;

import java.lang.reflect.Type;
import java.util.Date;

//Конвертер для записи/чтения сущности приёма вместе с ссылочными типами
public class AppointmentComplexConverter extends BaseConverter<Appointment>  {

    private final Gson gson;

    //Запись объекта вместе со ссылочными типами
    public AppointmentComplexConverter() {

        gson = Utils.getAppointmentGson();
    }

    @Override
    public JsonElement serialize(Appointment appointment, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObj = new JsonObject();

        jsonObj.addProperty("id",appointment.getId());
        jsonObj.addProperty("appointment_date", Utils.dateFormat.format(appointment.getAppointmentDate()));
        jsonObj.add("doctor",gson.toJsonTree(appointment.getDoctor()));
        jsonObj.add("patient",gson.toJsonTree(appointment.getPatient()));


        return jsonObj;
    }

    @Override
    public Appointment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObj = json.getAsJsonObject();

        int id = jsonObj.get("id").getAsInt();
        Date appointmentDate = Utils.tryParseDate(jsonObj.get("appointment_date").getAsString());
        Doctor doctor = gson.fromJson(jsonObj.get("doctor"),Doctor.class);
        Patient patient = gson.fromJson(jsonObj.get("patient"),Patient.class);

        return new Appointment(id,appointmentDate,patient,doctor);
    }

}
