package org.itstep.pd011.serviceintropart8;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

// синхронный вызов методов сервиса из активности
public class MyService extends Service {

    final String LOG_TAG = "service_08";

    // объект для привязки сервиса к активности - дает
    // возможность синхронного вызова методов сервиса
    // из активности
    MyBinder binder = new MyBinder();

    Timer timer;             // таймер для периодического запука сервиса
    TimerTask tTask;         // задача, запускаяемая по таймеру
    long interval = 1_000;   // начальный интервал запуска задач

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyService onCreate");

        // таймер и планировщик обеспечивают периодический запуск полезной
        // работы сервиса
        timer = new Timer();
        schedule();
    } // onCreate

    // планировщик периодического запуска задачи - полезной работы сервиса
    void schedule() {
        if (tTask != null) tTask.cancel();

        if (interval > 0) {
            // создание задачи таймера - то, что будет запускаться
            tTask = new TimerTask() {
                public void run() {
                    Log.d(LOG_TAG, "run - полезное действие задачи TimerTask, период " +
                     interval + " мс.");
                }
            };

            // запуск задачи с задержкой 1000 мс и периодом interval мс
            timer.schedule(tTask, 1_000, interval);
        } // if
    } // schedule

    // используется для прямого синхронного обращения к сервису
    long upInterval(long gap) {
        interval += gap;
        if (interval > 10_000) interval = 10_000;

        // планировщик - перезапуск задачи с новым интервалом
        schedule();
        return interval;
    } // upInterval

    // используется для прямого синхронного обращения к сервису
    long downInterval(long gap) {
        interval -= gap;
        if (interval <= 0) interval = 1_000;

        // планировщик - перезапуск задачи с новым интервалом
        schedule();
        return interval;
    } // downInterval

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tTask != null) tTask.cancel();
    } // onDestroy

    // первый раз в этой серии пример использования onBind
    public IBinder onBind(Intent arg0) {
        Log.d(LOG_TAG, "MyService onBind");
        return binder;
    } // onBind

    // внутренний класс - нужен для получения ссылки на сервис
    // в активности - для прямого обращения к сервису при
    // использовании привязки
    class MyBinder extends Binder {
        // только ради этого метода и был создан MyBinder
        MyService getService() {
            return MyService.this;
        } // getService
    } // class MyBinder
} // class MyService
