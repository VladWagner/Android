package org.itstep.pd011.serviceintropart5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    final String LOG_TAG = "service_05";
    ExecutorService es;

    // обработка создания сервиса
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyService onCreate");

        // создать пул исполнения на 2 потока
        es = Executors.newFixedThreadPool(2);
    } // onCreate

    // обработка уничтожения сервиса
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MyService onDestroy");
    } // onDestroy

    // запуск сервиса
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService onStartCommand");

        // получить параметры запуска сервиса из активности/фрагмента/диалога
        int time = intent.getIntExtra(MainActivity.PARAM_TIME, 1);
        int task = intent.getIntExtra(MainActivity.PARAM_TASK, 0);

        // полезная работа сервиса
        MyRun mr = new MyRun(startId, time, task);
        es.execute(mr);

        return super.onStartCommand(intent, flags, startId);
    } // onStartCommand

    public IBinder onBind(Intent arg0) { return null; }

    // реализация полезной работы сервиса
    class MyRun implements Runnable {

        private int startId;  // идентификатор сервиса
        private int time;     // время работы - параметр сервиса
        private int task;     // код задачи - код запроса к сервису - параметр сервиса

        public MyRun(int startId, int time, int task) {
            this.time = time;
            this.startId = startId;
            this.task = task;
            Log.d(LOG_TAG, String.format("MyRun#%d: создан", startId));
        } // MyRun

        // исполняемая часть потока - тут
        public void run() {
            // передаем в активность сообщение о старте
            // создать интент для поиска по Action (имени для фильтрации)
            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
            Log.d(LOG_TAG, String.format("MyRun#%d: запущен, time = %d", startId, time));

            // сообщаем о старте задачи, отправим intent на поиски BroadcastReceiver'а
            intent.putExtra(MainActivity.PARAM_TASK, task);
            intent.putExtra(MainActivity.PARAM_STATUS, MainActivity.STATUS_START);
            sendBroadcast(intent);

            // начинаем выполнение задачи, полезная работа
            Utils.sleep(time);
            int result = time * 100;

            // сообщаем об окончании задачи, формируем результат работы задачи 100*time
            // отправим intent на поиски BroadcastReceiver'а
            intent
                 .putExtra(MainActivity.PARAM_TASK, task)  // повторная запись, для надежности
                 .putExtra(MainActivity.PARAM_STATUS, MainActivity.STATUS_FINISH)
                 .putExtra(MainActivity.PARAM_RESULT, result);
            sendBroadcast(intent);

			// вызываем метод-оболочку для завершения сервиса
            stop();
        } // run

        // завершение сервиса оформим в отдельный метод
        void stop() {
            boolean result = stopSelfResult(startId);
            Log.d(LOG_TAG, String.format("MyRun#%1$d завершен, stopSelfResult(%1$d) = %2$b", startId, result));
        } // stop
    } // class MyRun
} // class MyService

