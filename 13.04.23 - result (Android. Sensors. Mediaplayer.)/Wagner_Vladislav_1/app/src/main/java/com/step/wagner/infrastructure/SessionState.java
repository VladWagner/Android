package com.step.wagner.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//Контроль состояния сессии -
public class SessionState {

    //Флаг обработки сеанса чтения датчиков
    public static boolean isSessionHandled;

    //Флаг появления новых значений в таблице сбора
    public static boolean isNewCollecting;

    private static boolean isFirstLaunch = true;

    private static SharedPreferences.Editor editor;

    public static void readValues(Context context){

        //Если в работающем приложении запуск не первый, тогда не читаем значения
        if (!isFirstLaunch)
            return;

        SharedPreferences sharedPreferences = context.getSharedPreferences(Parameters.SESSION_STATE_FILE, Context.MODE_PRIVATE);

        //Задаём состояние по умолчанию, если данных нет - обработка произошла, а нового сбора ещё не бло
        isSessionHandled = sharedPreferences.getBoolean("is_session_handled",true);
        isNewCollecting = sharedPreferences.getBoolean("is_new_collecting",false);

        isFirstLaunch = false;

    }
    public static void writeValues(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(Parameters.SESSION_STATE_FILE, Context.MODE_PRIVATE);

        if (editor == null)
            editor = sharedPreferences.edit();

        editor.putBoolean("is_session_handled",isSessionHandled);
        editor.putBoolean("is_new_collecting",isNewCollecting);

        editor.commit();

    }
}
