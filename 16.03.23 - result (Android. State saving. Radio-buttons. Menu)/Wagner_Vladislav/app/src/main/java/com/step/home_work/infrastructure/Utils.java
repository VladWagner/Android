package com.step.home_work.infrastructure;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.step.home_work.models.FileName;
import com.step.home_work.models.ship.CargoType;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.*;

public class Utils {

    //Генерация значений
    private static Random rand = new Random();

    //Формат вывода чисел
    public static DecimalFormat numbersFormatter = new DecimalFormat("###,###,###");

    //Формат вывода даты
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy",Locale.UK);

    public Utils() {
    }

    //Получение случайных значений
    public static double getRandom(double lo, double hi) {
        return lo + rand.nextDouble() * (hi - lo);
    }

    public static int getRandom(int lo, int hi) {
        return lo + rand.nextInt(hi - lo);
    }
    public static long getRandom(long lo, long hi) {
        return lo + rand.nextLong()*(hi - lo);
    }

    //ФИО владельцев
    private static final String[] owners = new String[]{
            "Юрковский П.С.",
            "Якубовская В.А.",
            "Шапиро В.П.",
            "Вожжаев В.Б.",
            "Хроменко Е.М.",
    };

    public static String getOwnerSnp(){
        return owners[getRandom(0, owners.length)];
    }


    //Породы животных
    private static final FileName[] breeds = new FileName[]{
            new FileName("Мейн-кун","cat.jpg"),
            new FileName("скоттиш-фолд","cat.jpg"),
            new FileName("бенгальская","cat.jpg"),
            new FileName("абиссинская","cat.jpg"),
    };

    public static FileName getBreed(){
        return breeds[getRandom(0, breeds.length)];
    }

    //Клички
    private static final String[] animalsNames = new String[]{
            "Фрида",
            "Шерри",
            "Вальдо",
            "Данте",
            "Зефир",
            "Клеон",
    };

    public static String getAnimalName(){
        return animalsNames[getRandom(0, animalsNames.length)];
    }

    //Типы кораблей
    private static final FileName[] shipTypes = new FileName[]{
            new FileName("Балкер","bulk.jpg"),
            new FileName("Контейнеровоз","container.jpg"),
    };

    public static FileName getShipType(){
        return shipTypes[getRandom(0, shipTypes.length)];
    }

    //Пункты назначений
    private static final String[] destinations = new String[]{
           "порт Шанхая",
           "порт Роттердама",
           "порт Пусан",
           "порт Генуя",
           "порт Балтимор"
    };

    public static String getDestination(){
        return destinations[getRandom(0, destinations.length)];
    }

    //Типы груозов
    private static final CargoType[] cargoTypes = new CargoType[]{
           new CargoType("уголь",8000),
           new CargoType("железная руда",27000),
           new CargoType("нефтепродукты",60000),
           new CargoType("зерно",5000)
    };

    public static CargoType getCargoType(){
        return cargoTypes[getRandom(0, cargoTypes.length)];
    }

    //Записать изображение в imageView
    public static void setImageView(String fileName, ImageView imgView, Context appContext) throws Exception {

        //Окрыть поток чтения из assets
        try(InputStream is = appContext.getAssets().open(fileName)){

            Drawable drawable = Drawable.createFromStream(is,null);
            imgView.setImageDrawable(drawable);


        }catch (IOException e){
            throw new Exception(e);
        }
    }

}//Utils
