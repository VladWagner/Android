package com.step.wagner.infrastructure;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.step.wagner.R;
import com.step.wagner.repositories.AccelerometersRepository;
import com.step.wagner.repositories.AccelerometersStatisticsRepository;
import com.step.wagner.repositories.CollectingRepository;
import com.step.wagner.repositories.StatisticsRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.*;
import java.util.function.Predicate;

public class Utils {

    //Генерация значений
    private static Random rand = new Random();

    //Формат вывода чисел
    public static DecimalFormat numbersFormatter = new DecimalFormat("###,###,###");

    //Формат вывода даты
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
    public static SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
    public static SimpleDateFormat dbDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.UK);

    public static TimeZone timeZone = TimeZone.getTimeZone("Europe/Moscow");


    public Utils() {
    }


    //region Репозитории
    public static AccelerometersRepository accelerometersRepository;

    public static CollectingRepository collectingRepository;
    public static StatisticsRepository statisticsRepository;
    public static AccelerometersStatisticsRepository accelerometersStatisticsRepository;
    //endregion

    //Анимация для прозрачной кнопки
    public static AlphaAnimation btnClickAnimation = new AlphaAnimation(1, 0.1F);

    public static Integer tryParseInt(String strValue) {

        Integer parsed = null;
        try {
            parsed = Integer.parseInt(strValue);
        } catch (NumberFormatException e) {
            return null;
        }

        return parsed;
    }

    public static Long tryParseLong(String strValue) {

        Long parsed = null;
        try {
            parsed = Long.parseLong(strValue);
        } catch (NumberFormatException e) {
            return null;
        }

        return parsed;
    }

    public static Double tryParseDouble(String strValue) {

        Double parsed = null;
        try {
            parsed = Double.parseDouble(strValue);
        } catch (NumberFormatException e) {
            return null;
        }

        return parsed;
    }

    //Получить дату из строки
    public static Date tryParseDate(String strValue) {

        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(strValue);
        } catch (ParseException e) {
            return null;
        }

        return parsedDate;
    }

    //Получение даты в формате, который требует SQLListe
    public static Date tryParseDbDate(String strValue) {

        Date parsedDate = null;
        try {
            parsedDate = dbDateFormat.parse(strValue);
        } catch (ParseException e) {
            return null;
        }

        return parsedDate;
    }

    //Получение даты и времени из строки
    public static Date tryParseDbDateTime(String strValue) {

        Date parsedDate = null;
        try {
            parsedDate = dbDateTimeFormat.parse(strValue);
        } catch (ParseException e) {
            return null;
        }

        return parsedDate;
    }

    //Времени из строки
    public static Date tryParseTime(String strValue) {

        Date parsedDate = null;
        try {
            parsedDate = timeFormat.parse(strValue);
        } catch (ParseException e) {
            return null;
        }

        return parsedDate;
    }

    public static long millisecondsToSeconds(long milliseconds){
        return milliseconds > 1000 ? milliseconds/1000 : 0;
    }

    //Получение случайных значений
    public static double getRandom(double lo, double hi) {
        return lo + rand.nextDouble() * (hi - lo);
    }

    public static int getRandom(int lo, int hi) {
        return lo + rand.nextInt(hi - lo);
    }

    public static long getRandom(long lo, long hi) {
        return lo + rand.nextLong() * (hi - lo);
    }

    //Получить путь к файлу во внешнем хранилище
    public static File getExternalPath(String fileName,Context context) {
        return new File(context.getExternalFilesDir(null), fileName);
    } // getExternalPath

    public static File getInternalPath(String fileName,Context context) {
        return new File(context.getFilesDir(), fileName);
    } // getExternalPath

    //Записать изображение в imageView
    public static void setImageView(String fileName, ImageView imgView, Context appContext) throws Exception {


        Drawable drawable = null;
        //Окрыть поток чтения из assets
        try (InputStream is = appContext.getAssets().open(fileName)) {
            drawable = Drawable.createFromStream(is, null);
        } catch (IOException e) {

            //Попытка открыть стандартную картинку
            try (InputStream is = appContext.getAssets().open("none.jpg")) {
                drawable = Drawable.createFromStream(is, null);
            } catch (Exception ex) {

                throw new Exception(ex);
            }
        }//try catch


        imgView.setImageDrawable(drawable);
    }

    //Вывести сообщение под полем ввода
    public static void showErrorMessage(EditText editText, Context context, String message) {
        editText.setError(message);
        editText.getBackground()
                .setColorFilter(context.getResources().getColor(R.color.error),
                        PorterDuff.Mode.SRC_ATOP);
    }

    //Метод валидациии
    public static boolean isValidEditText(EditText editText, Context context, String message, Predicate<String> predicate) {
        if (!predicate.test(editText.getText().toString())) {
            showErrorMessage(editText, context, message);
            return false;
        }
        editText.setError(null);
        editText.getBackground()
                .setColorFilter(context.getResources().getColor(R.color.default_field),
                        PorterDuff.Mode.SRC_ATOP);
        return true;
    }


    //Вывести snackBar
    public static void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Закрыть", (v) -> {
        });

        snackbar.show();
    }//showSnackBar

    //Вывести toast
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }//showToast

    //Заголовки страниц
    public static String[] pagesHeaders = new String[]{
            "Текущая обработка",
            "История обработок"
    };

}//Utils
