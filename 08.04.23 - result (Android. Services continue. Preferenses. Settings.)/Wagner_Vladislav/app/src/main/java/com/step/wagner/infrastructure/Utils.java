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
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.step.wagner.R;
import com.step.wagner.models.Message;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    //Отправители сообщений
    private static final List<String> senders = new ArrayList<>(List.of(
            "Юрковский М.О",
            "Якубовская Д.П",
            "Шапиро Ф.Ф",
            "Вожжаев С.Д",
            "Хроменко И.В"
    ));

    public static String getSender(){
        return senders.get(Utils.getRandom(0,senders.size()));
    }

    //Получатели сообщений
    private static final List<String> receivers = new ArrayList<>(List.of(
            "Пелых М.У",
            "Лапотникова Т.О",
            "Огородников С.И",
            "Яйло Е.Н",
            "Бутусова И.А"
    ));

    public static String getReceiver(){
        return receivers.get(Utils.getRandom(0,receivers.size()));
    }


    //Темы писем
    private static final List<String> subjects = new ArrayList<>(List.of(
            "provident id voluptas",
            "et omnis dolorem",
            "provident id voluptas",
            "modi ut eos dolores",
            "perferendis",
            "aut et tenetur ducimus",
            "aliquid rerum",
            ""
    ));

    public static String getSubject(){
        return subjects.get(Utils.getRandom(0,subjects.size()));
    }

    //Текста сообщений
    private static final List<String> texts = new ArrayList<>(List.of(
            "quia molestiae reprehenderit quasi aspernatur cimus et vero voluptates excepturi deleniti ratione",
            "non et atque noccaecati deserunt quas accusantium unde odit nobis qui",
            "doloribus at sed quis culpa deserunt consectetur qui praesentium naccusamus fugiat",
            "rerum ut voluptate autem nvoluptatem repellendus aspernatur dolorem in",
            "maiores sed dolores similique labore et inventore et nquasi temporibus esse sunt id et neos",
            "voluptatem aliquam naliquid ratione corporis molestiae mollitia quia et magnam",
            "sapiente assumenda molestiae atque nadipisci laborum distinctio aperiam et ab ut omnis",
            "voluptate iusto quis nobis reprehenderit ipsum amet nulla nquia quas dolores"
    ));

    public static String getText(){
        return texts.get(Utils.getRandom(0,texts.size()));
    }

    public static List<Message> generateMessagesList(){
        return Stream.generate(Message::factory).limit(10).collect(Collectors.toList());
    }

}//Utils
