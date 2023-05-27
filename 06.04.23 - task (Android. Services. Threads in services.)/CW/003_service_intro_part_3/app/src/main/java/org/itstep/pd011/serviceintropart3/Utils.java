package org.itstep.pd011.serviceintropart3;

import android.util.Log;

import java.util.concurrent.TimeUnit;

public class Utils {
    final static String LOG_TAG = "Utils";
    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Log.d(LOG_TAG, "Отсчет времени прерван досрочно!");
        } // try-catch
    } // sleep
} // class Utils
