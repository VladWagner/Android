package org.itstep.pd011.serviceintropart3;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


/*
 * Метод onStartCommand должен возвращать int. В прошлых примерах мы не использовали ответ,
 * а возвращали ответ метода супер-класса. В этом примере разберемся, что мы можем возвращать,
 * и чем нам это грозит.
 * Также посмотрим, что за флаги (второй параметр) идут на вход этому методу.
 *
 * Система может убить сервис, если ей будет не хватать памяти.
 * Но в наших силах сделать так, чтобы наш сервис ожил, когда проблема с памятью будет устранена.
 * И более того, не просто ожил, а еще и снова начал выполнять незавершенные вызовы startService.
 * */

public class MyService extends Service {

    final String LOG_TAG = "service_03";

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyService - onCreate");

    } // onCreate

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MyService - onDestroy");
    } // onDestroy

    // intent  -  параметры запуска
    // flags   -  флаги запуска (передаются в сервис из системы)
    //            START_FLAG_REDELIVERY - значит, что предыдущий вызов не завершен через stopSelf()
    //            START_FLAG_RETRY      - повторный запуск сервиса
    //            0                     - никогда не получается...
    // startId - идентификатор сервиса, фактически - счетчик запущенных экземпляров сервисов
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService - onStartCommand");
        logFlags(flags);

        // получить параметры запуска сервиса
        Bundle bundle = intent.getExtras();
        String param = bundle.getString(MainActivity.SERVICE_DATA, "");

        MyRun mr = new MyRun(startId, param);
        new Thread(mr).start();

        // return START_NOT_STICKY;   // сервис не будет перезапущен после убийства сервиса системой
        return START_STICKY;          // сервис будет перезапущен после убийства сервиса системой
        // return START_REDELIVER_INTENT; // сервис будет перезапущен после убийства сервиса системой,
        // и к тому же получит все вызовы startService(), которые не были завершены stopSelf()
    } // onStartCommand

    public IBinder onBind(Intent arg0) { return null; }

    // вывод флагов запуска в LogCat
    void logFlags(int flags) {
        String str = "flags = " + flags + " ";
        if ((flags & START_FLAG_REDELIVERY) == START_FLAG_REDELIVERY)
            str = " START_FLAG_REDELIVERY";
        if ((flags & START_FLAG_RETRY) == START_FLAG_RETRY)
            str += " | START_FLAG_RETRY";
        Log.d(LOG_TAG, str);
    } // logFlags

    // класс - реализация сервиса
    class MyRun implements Runnable {
        private static final int N_REPEAT = 15;  // количество повторов внутреннего цикла сервиса
        int startId;
        String param;

        public MyRun(int startId, String param) {
            this.startId = startId;
            this.param = param;

            Log.d(LOG_TAG, String.format("MyRun#%d: создан", startId));
        } // MyRun

		// полезные действия сервиса
        public void run() {
            Log.d(LOG_TAG, String.format("MyRun#%d: запущен, параметр - %s", startId, param));
            for (int i = 0; i < N_REPEAT; i++) {
                Utils.sleep(1);
                Log.d(LOG_TAG, String.format("MyRun#%d: i = %d", startId, i));
            } // for i

			// вызов метода - оболочки для завершения сервиса 
            stop();
        } // run

        // метод для завершения сервиса
        void stop() {
            boolean result = stopSelfResult(startId);

            Log.d(LOG_TAG, String.format("MyRun#%1$d завершен, stopSelfResult(%1$d) = %2$b", startId, result));
        } // stop
    } // class MyRun
} // class MyService