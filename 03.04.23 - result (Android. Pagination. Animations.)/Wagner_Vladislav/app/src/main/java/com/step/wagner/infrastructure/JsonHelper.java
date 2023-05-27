package com.step.wagner.infrastructure;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.step.wagner.converters.BaseConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//Класс для сериализации/десериализации объектов из JSON
public class JsonHelper {

    //Запись значений в файл
    public static <T> boolean writeToJson(Context context, List<T> collection, BaseConverter<T> converter, String fileName) {

        //Получить путь к файлу во внутреннем хранилище
        //File file = Utils.getExternalPath(fileName, context);
        File file = Utils.getInternalPath(fileName, context);

        try (FileOutputStream os = new FileOutputStream(file)) {

            GsonBuilder builder = new GsonBuilder();

            builder.registerTypeAdapter(collection.get(0).getClass(), converter);
            builder.setPrettyPrinting();
            Gson gson = builder.create();

            String jsonStr = gson.toJson(collection);

            os.write(jsonStr.getBytes());

        } catch (Exception e) {

            Utils.showSnackBar(((Activity) context).findViewById(android.R.id.content),
                    String.format("При записи файла %s упало исключение!", fileName));

            return false;
        }//catch

        return true;
    }//writeToJson

    //Чтение из файла
    public static <T> List<T> readFromJson(Context context, BaseConverter<T> converter, Type scalarType, Type arrayType, String fileName) {

        //Получить путь к файлу во внутреннем хранилище
        //File file = Utils.getExternalPath(fileName, context);
        File file = Utils.getInternalPath(fileName, context);

        try (InputStreamReader isr = new InputStreamReader(file.toURL().openStream())) {

            GsonBuilder builder = new GsonBuilder();

            builder.registerTypeAdapter(scalarType, converter);
            Gson gson = builder.create();


            return new ArrayList<>(List.of(gson.fromJson(isr,arrayType)));

        } catch (Exception e) {


            Log.d("JsonFileException",e.getMessage());
            e.printStackTrace();

            return null;
        }

    }//readFromJson

}
