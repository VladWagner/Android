package com.step.home_work.converters;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.step.home_work.models.entities.Appointment;

public abstract class BaseConverter<T> implements JsonSerializer<T>, JsonDeserializer<T>  {
}
