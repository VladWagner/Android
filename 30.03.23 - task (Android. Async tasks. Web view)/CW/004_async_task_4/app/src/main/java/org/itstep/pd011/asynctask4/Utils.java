package org.itstep.pd011.asynctask4;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Utils {
    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    } // sleep

    public static void sleepMs(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    } // sleepMs
	
	private static Random random = new Random();

    // формирование случайных чисел типа int
    public static int getRandom(int lo, int hi) {
        return lo + random.nextInt(hi - lo);
    }  // getRandom
}
