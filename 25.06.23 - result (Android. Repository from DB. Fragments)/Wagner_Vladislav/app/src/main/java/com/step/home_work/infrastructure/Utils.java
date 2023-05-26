package com.step.home_work.infrastructure;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.step.home_work.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
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
    public static SimpleDateFormat DBdateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

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
    }

}//Utils
