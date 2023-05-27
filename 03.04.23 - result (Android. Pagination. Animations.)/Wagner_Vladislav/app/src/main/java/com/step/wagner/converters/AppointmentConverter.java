package com.step.wagner.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.entities.Appointment;

import java.lang.reflect.Type;
import java.util.Date;

public class AppointmentConverter extends BaseConverter<Appointment>  {

    @Override
    public JsonElement serialize(Appointment appointment, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObj = new JsonObject();

        jsonObj.addProperty("id",appointment.getId());
        jsonObj.addProperty("appointment_date", Utils.dateFormat.format(appointment.getAppointmentDate()));
        jsonObj.addProperty("doctor_id",appointment.getDoctor().getId());
        jsonObj.addProperty("patient_id",appointment.getPatient().getId());


        return jsonObj;
    }

    @Override
    public Appointment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObj = json.getAsJsonObject();

        int id = jsonObj.get("id").getAsInt();
        Date appointmentDate = Utils.tryParseDate(jsonObj.get("appointment_date").getAsString());
        int doctorId = jsonObj.get("doctor_id").getAsInt();
        int patientId = jsonObj.get("patient_id").getAsInt();

        return new Appointment(id,appointmentDate,patientId,doctorId);
    }

}
