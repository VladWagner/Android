package com.step.wagner.sensors.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSettings {


    //Длительность опроса
    public static int queryDuration ;

    //Интервал запуска задачи
    public static long queryInterval = 1000;

    //Опрашивать акселерометр
    public static Boolean listenAccelerometer;

    //Опрашивать датчик освещённости
    public static Boolean listenLightSensor;

    //Опрашивать датчик приближения
    public static Boolean listenApproximation;

    //Запускать ли медиа плеер
    public static Boolean launchMediaPlayer;

    public static void getSetting(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


        String durationStr = sharedPreferences.getString("duration","120");

        queryDuration = Utils.tryParseInt(durationStr);

        String intervalStr = sharedPreferences.getString("interval","2");

        Long tempLong = Utils.tryParseLong(intervalStr);
        queryInterval = tempLong != null ? tempLong * 1000 : 2;

        listenAccelerometer = sharedPreferences.getBoolean("accelerometer",true);
        listenLightSensor = sharedPreferences.getBoolean("light_sensor",true);
        listenApproximation = sharedPreferences.getBoolean("approximation_sensor",true);
        launchMediaPlayer = sharedPreferences.getBoolean("launch_media_player",false);

    }
}
