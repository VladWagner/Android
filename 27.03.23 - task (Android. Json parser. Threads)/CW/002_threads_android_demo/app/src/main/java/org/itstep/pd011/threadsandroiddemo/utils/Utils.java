package org.itstep.pd011.threadsandroiddemo.utils;

import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

// вспомогательные методы
public class Utils {
    public static Random random = new Random();

    // вернуть текущее время в строковом формате
    public static String currentTimeToString() {
        // получаем текущее время
        Calendar c = Calendar.getInstance();

        return String.format(Locale.UK,
            "Текущее время:\n%02d:%02d:%02d",
            c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
            c.get(Calendar.SECOND));
    } // toString

    // формирование задержки по времени
    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Log.d("Utls", e.getMessage());
        } // try-catch
    } // sleep

    public static double getRand(double from, double to) {
        return  from + (to - from)*random.nextDouble();
    }
}
