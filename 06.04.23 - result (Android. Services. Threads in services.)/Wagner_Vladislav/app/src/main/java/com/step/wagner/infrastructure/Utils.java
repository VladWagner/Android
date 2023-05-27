package com.step.wagner.infrastructure;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.step.wagner.R;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

    public Utils() {
    }

    public static Integer tryParseInt(String strValue) {

        Integer parsed = null;
        try {
            parsed = Integer.parseInt(strValue);
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

    public static Date tryParseDate(String strValue) {

        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(strValue);
        } catch (ParseException e) {
            return null;
        }

        return parsedDate;
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

    //Получаемые параметры в формате dd.MM.yyyy
    public static Date getRandomDate(/*String dateMin, String dateMax*/){

        Date dateLo = tryParseDate("01.01.2022");
        Date dateHi = tryParseDate("31.12.2023");

        if (dateLo == null || dateHi == null) return null;

        long randTime = getRandom(dateLo.getTime(),dateHi.getTime());

        Date resultDate = new Date(randTime);

        resultDate.setYear(getRandom(dateLo.getYear(),dateHi.getYear()+1));

        return resultDate;
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

    //Заголовки страниц
    public static String[] pagesHeaders = new String[]{
            "Альбомы",
            "Комментарии",
            "Посты",
            "Пользователи",
    };

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



}//Utils
