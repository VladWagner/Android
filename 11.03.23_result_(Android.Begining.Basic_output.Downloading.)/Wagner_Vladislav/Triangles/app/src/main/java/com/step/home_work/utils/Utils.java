package com.step.home_work.utils;


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

}//Utils
