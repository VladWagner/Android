package org.itstep.pd011.serviceintropart1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

// простейшая реализация сервиса (службы)
// !! сервис имеет возможность работать в том же потоке, что и MainActivity
public class MyService extends Service {

    final String LOG_TAG = "service_01";
    private int startId;

    // при создании сервиса
    public void onCreate() {
        super.onCreate();

        Log.d(LOG_TAG, "Service: onCreate");
    } // onCreate


    // запускается по startService
    // intent - параметры, передаваемые службе
    // flags - флаги запуска службы
    // startId - фактически это счетчик вызовов
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, String.format("onStartCommand, startId %d", startId));
        this.startId = startId;

        // в отдельный метод вынесем реализацию полезной работы службы
        someTask();

        return super.onStartCommand(intent, flags, startId);
    } // onStartCommand


    // при уничтожении сервиса
    public void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TAG, "Service " + startId + ": onDestroy");
    } // onDestroy


    // должен обязательно переопределяться, будем использовать позже
    // вызывается при закреплении клиента за сервисом с помощью метода
    // bindService()
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    } // onBind


    // полезная работа службы кодируется в этом методе - имя не обязательно,
    // выбрано таким просто для примера
    void someTask() {
        // работа в потоке UI
//        final int NUM_ITERS = 7;
//        for (int i = 1; i <= NUM_ITERS; i++) {
//            Log.d(LOG_TAG, "i = " + i);
//            Utils.sleep(3); // имитация длительной обработки
//        } // for i
//
//        // служба сама себя останавливает по окончании работы
//        stopSelf();

        // полезная работа службы выполняется в отдельном потоке исполнения
        new Thread(new Runnable() {
            private final static int NUM_ITERS = 15;

            // в этом методе и выполняется полезная работа службы
            public void run() {

                for (int i = 1; i <= NUM_ITERS; i++) {
                    Log.d(LOG_TAG, String.format("Service %d: %d", startId, i));
                    Utils.sleep(1);  // имитация длительной обработки
                } // for i

                // служба сама себя останавливает по окончании работы
                // приложение может завершаться, сервис продолжает работу,
                // сам себя останавливает, что привощит к событию onDestroy
                stopSelf();
            } // run
        }).start();  // запуск созданного экземпляра анонимного класса - запуск потока
    } // someTask
} // class MyService