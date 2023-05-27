package com.step.wagner.converters;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

//Используется для передачи конвертера в JsonHelper (Infrastructure → JsonHelper)
public abstract class BaseConverter<T> implements JsonSerializer<T>, JsonDeserializer<T>  {
}
