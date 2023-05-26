package org.itstep.pd011.asynctask2;

import java.util.concurrent.TimeUnit;

public class Utils {
    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepMs(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
