package org.itstep.pd011.serviceintropart4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

// Применение Intent для передачи параметров в сервис при запуске сервиса
// Применение PendingIntent для передачи результатов из сервиса в активность
public class MyService extends Service {
    final String LOG_TAG = "service_04";
    ExecutorService es;

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyService onCreate");

        // пул потоков для работы задач (сервиса)
        es = Executors.newFixedThreadPool(2);
    } // onCreate

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MyService onDestroy");
    } // onDestroy


    // получение параметров при запуске сервиса
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService onStartCommand");

        int time = intent.getIntExtra(MainActivity.PARAM_TIME, 1);
        PendingIntent pi = intent.getParcelableExtra(MainActivity.PARAM_PINTENT);

        // создать и запустить полезную часть сервиса (в пуле потоков)
        MyRun mr = new MyRun(time, startId, pi);
        es.execute(mr);

        return super.onStartCommand(intent, flags, startId);
    } // onStartCommand

    public IBinder onBind(Intent arg0) {
        return null;
    }


    // реализация полезной работы сервиса
    class MyRun implements Runnable {

        private int time;           // данные для обработки - время
        private int startId;        // идентификатор
        private PendingIntent pi;   // для связи с активностью

        public MyRun(int time, int startId, PendingIntent pi) {
            this.time = time;
            this.startId = startId;
            this.pi = pi;
            Log.d(LOG_TAG, String.format("MyRun#%d: создан", startId));
        } // MyRun

        // реализуем полезную работу сервиса
        public void run() {
            Log.d(LOG_TAG, String.format("MyRun#%d: запущен, time = %d", startId, time));
            try {
                // сообщаем о старте задачи - отправляем интент pi в активность
                pi.send(MainActivity.STATUS_START);

                // начинаем выполнение задачи
                Utils.sleep(time); // имитация длительного выполнения

                // полезная работа сервиса :)
                int result = time * 100;

                // сообщаем об окончании задачи и взвращаем результат
                // а) сформировать возвращаемый пакет данных в интенте
                Intent intent = new Intent()
                        .putExtra(MainActivity.PARAM_RESULT, result);
                // б) отправить интент с данным в активность - при помощи PendingIntent
                pi.send(MyService.this, MainActivity.STATUS_FINISH, intent);

            } catch (CanceledException e) {
                e.printStackTrace();
            } // try-catch
            stop();
        } // run

        // завершение сервиса оформим в отдельный метод
        void stop() {
            boolean result = stopSelfResult(startId);
            Log.d(LOG_TAG, String.format("MyRun#%1$d завершен, stopSelfResult(%1$d) = %2$b", startId, result));
        } // stop
    } // class MyRun
} // class MyService
