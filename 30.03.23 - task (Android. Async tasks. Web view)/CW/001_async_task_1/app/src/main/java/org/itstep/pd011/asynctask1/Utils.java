package org.itstep.pd011.asynctask1;

import java.util.concurrent.TimeUnit;

public class Utils {
    public static void sleep(int seconds) {
        sleepMs(seconds * 1000);
    } // sleep

    // пауза в мс
    public static void sleepMs(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } // try-catch
    } // sleepMs
} // class Utils
