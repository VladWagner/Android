package org.itstep.pd011.jsonparserexample.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.itstep.pd011.jsonparserexample.models.Product;
import org.itstep.pd011.jsonparserexample.models.ProductConverter;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// http://www.javenue.info/post/gson-json-api
public class JsonHelper {
    // поле имени файла
    private static final String FILE_NAME = "data.json";

    // экспортирование данных в JSON-файл
    public static boolean exportToJSON(Context context, List<Product> dataList) {
        boolean result = false;

        // http://www.javenue.info/post/gson-json-api
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Product.class, new ProductConverter());
        Gson gson = builder.create();

        String jsonString = gson.toJson(dataList);

        try (FileOutputStream fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } // try-catch

        return result;
    } // exportToJSON


    // импортирование данных из JSON-файла
    public static List<Product> importFromJSON(Context context) {

        try (InputStreamReader streamReader = new InputStreamReader(context.openFileInput(FILE_NAME))){
            // http://www.javenue.info/post/gson-json-api
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Product.class, new ProductConverter());

            Gson gson = builder.create();
            return new ArrayList<>(Arrays.asList(gson.fromJson(streamReader, Product[].class)));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        } // try-catch
    } // importFromJSON
} // class JsonHelper
