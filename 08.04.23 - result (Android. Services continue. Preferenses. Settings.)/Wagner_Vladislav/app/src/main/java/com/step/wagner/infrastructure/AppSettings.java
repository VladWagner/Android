package com.step.wagner.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSettings {


    //Интервал запуска задачи
    public static long interval = 1000;

    //Задержка запуска
    public  static  long delay = 500;

    //Имя файла для записив JSON
    public static String jsonFileName = "messages.json";

    //Нужно ли сохранять значения в полях диалога
    public static Boolean saveFields;

    public static void getSetting(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String intervalStr = sharedPreferences.getString("interval","1");
        interval = Utils.tryParseLong(intervalStr)*1000;

        String delayStr = sharedPreferences.getString("delay","1");
        delay = Utils.tryParseLong(delayStr)*1000;

        jsonFileName = sharedPreferences.getString("json_file_name","messages.json");
        saveFields = sharedPreferences.getBoolean("save_dialog_fields",true);

    }
}
